package auctionService.serviceImplementation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.AuctionCreateUpdateDto;
import api.dtos.AuctionDto;
import api.dtos.AuctionParticipantDto;
import api.dtos.BankAccountDto;
import api.dtos.ProductDto;
import api.proxies.BankAccountProxy;
import api.proxies.ProductProxy;
import api.proxies.UserProxy;
import api.services.AuctionService;
import auctionService.entity.AuctionModel;
import auctionService.entity.AuctionParticipantModel;
import auctionService.repository.AuctionParticipantRepository;
import auctionService.repository.AuctionRepository;
import util.exceptions.AuctionNotFoundException;
import util.exceptions.AuctionOwnerException;
import util.exceptions.CurrencyDepositException;
import util.exceptions.ProductOnAuctionException;
import util.exceptions.UserEmailAuctionException;
import util.exceptions.UserNotFoundException;

@RestController
public class AuctionServiceImplementation implements AuctionService{
	
	@Autowired
	private ProductProxy productProxy;
	
	@Autowired
	private AuctionRepository repo;
	
	@Autowired
	private AuctionParticipantRepository participantRepo;
	
	@Autowired
	private UserProxy userProxy;
	
	@Autowired
	private BankAccountProxy bankAccountProxy;
	
	BigDecimal percentage = new BigDecimal("0.10");

	@Override
	public ResponseEntity<?> createAuction(AuctionCreateUpdateDto dto, String currentEmail) {
		if(!currentEmail.equals(dto.getOwnerEmail())) {
			throw new UserEmailAuctionException(
					"You can not create auctions for other users");
		}
		
		ProductDto product;
		
		try {
			product = productProxy.getProductsById(dto.getProductId()).getBody();
		} catch(feign.FeignException.NotFound e) {
			throw new AuctionOwnerException("Product with given id does not exist in product service");
		}
		
		if(!currentEmail.equals(product.getOwnerEmail())) {
			throw new AuctionOwnerException(
					"You can not create auctions for products that do not belong to you");
		}
		
		List<AuctionDto> auctions = (List<AuctionDto>) getAuctions().getBody();
		
		for(AuctionDto auction : auctions) {
			if(auction.getProductId() == dto.getProductId()) {
				throw new ProductOnAuctionException("This product is already on auction");
			}
		}
		
		AuctionModel model = new AuctionModel(dto.getProductId(), dto.getOwnerEmail(), 
				dto.getStartPrice(), dto.getCurrency(), BigDecimal.ZERO, "");
		
		return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(model));
		
	}

	@Override
	public ResponseEntity<?> getAuctions() {
		List<AuctionModel> models = repo.findAll();
		List<AuctionDto> dtos = new ArrayList<AuctionDto>();
		
		for(AuctionModel model : models) {
			dtos.add(convertFromModelToDto(model));
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(dtos);
	}

	@Override
	public ResponseEntity<?> getAuctionById(int id) {
		Optional<AuctionModel> auction = repo.findById(id);
		
		if(auction.isEmpty()) {
			throw new AuctionNotFoundException("Auction with given id does not exist");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(convertFromModelToDto(auction.get()));
	}

	@Override
	public ResponseEntity<?> getAuctionByEmail(String email) {
		if(!userProxy.checkUserExists(email).getBody()) {
			throw new UserNotFoundException("User with given email does not exist");
		}
		
		List<AuctionModel> models = repo.findByOwnerEmail(email);
		List<AuctionDto> dtos = new ArrayList<AuctionDto>();
		
		for(AuctionModel model : models) {
			dtos.add(convertFromModelToDto(model));
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(dtos);
	}
	
	private AuctionDto convertFromModelToDto(AuctionModel model) {
		return new AuctionDto(model.getId(), model.getProductId(), model.getOwnerEmail(), 
				model.getStartPrice(), model.getCurrency(), model.getCurrentHighestBid(), 
				model.getCurrentWinnerEmail(), model.getCreatedAt(), model.getClosedAt());
	}

	@Override
	public ResponseEntity<?> joinAuction(int id, String currentEmail) {
		Optional<AuctionModel> auction = repo.findById(id);
		
		if(auction.isEmpty()) {
			throw new AuctionNotFoundException("Auction with given id does not exist");
		}
		
		if(auction.get().getOwnerEmail().equals(currentEmail)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					"You are owner of this auction");
		}
		
		List<AuctionParticipantModel> auctionParticipants = participantRepo.findAll();
		for(AuctionParticipantModel participant : auctionParticipants) {
			if(participant.getAuctionId() == id && currentEmail.equals(participant.getParticipantEmail())) {
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
						"You have already joined to this auction");
			}
		}
		
		BigDecimal auctionStartPrice = auction.get().getStartPrice();
		BigDecimal deposit = auctionStartPrice.multiply(percentage);
		
		BankAccountDto bankAccount = bankAccountProxy.getBankAccount(currentEmail);
		BankAccountDto responseAccount = new BankAccountDto(bankAccount.getEmail(), 
				bankAccount.getUsdAmount(), bankAccount.getEurAmount(), bankAccount.getRsdAmount());
		
		switch(auction.get().getCurrency()) {
		case EUR -> {
			if(deposit.compareTo(bankAccount.getEurAmount()) > 0) {
				throw new CurrencyDepositException("You do not have enough currency amount");
			}
			bankAccount.setEurAmount(bankAccount.getEurAmount().subtract(deposit));
		}
		case USD -> {
			if(deposit.compareTo(bankAccount.getUsdAmount()) > 0) {
				throw new CurrencyDepositException("You do not have enough currency amount");
			}
			bankAccount.setUsdAmount(bankAccount.getUsdAmount().subtract(deposit));
		}
		case RSD -> {
			if(deposit.compareTo(bankAccount.getRsdAmount()) > 0) {
				throw new CurrencyDepositException("You do not have enough currency amount");
			}
			bankAccount.setRsdAmount(bankAccount.getRsdAmount().subtract(deposit));
		}
		}
		
		AuctionParticipantModel auctionParticipant = new AuctionParticipantModel(id, currentEmail, deposit);
		AuctionParticipantModel saved = participantRepo.save(auctionParticipant);
		AuctionParticipantDto dto = convertFromModelToDto(saved);
		
		Map<String, Object> response = new HashMap<>();
		
		bankAccountProxy.updateBankAccount(bankAccount);
		
		response.put("Bank Account before deposit", responseAccount);
		response.put("Bank Account after deposit", bankAccount);
		response.put("Auction Participant", dto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}

	private AuctionParticipantDto convertFromModelToDto(AuctionParticipantModel model) {
		return new AuctionParticipantDto(model.getAuctionId(), model.getParticipantEmail(), 
				model.getDeposit(), model.getJoinedAt());
	}
	
}
