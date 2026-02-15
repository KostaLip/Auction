package auctionService.serviceImplementation;

import java.math.BigDecimal;
import java.time.Instant;
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
import api.enums.Status;
import api.proxies.BankAccountProxy;
import api.proxies.ProductProxy;
import api.proxies.UserProxy;
import api.services.AuctionService;
import auctionService.entity.AuctionModel;
import auctionService.entity.AuctionParticipantModel;
import auctionService.entity.BidModel;
import auctionService.repository.AuctionParticipantRepository;
import auctionService.repository.AuctionRepository;
import auctionService.repository.BidRepository;
import util.exceptions.AuctionNotActiveException;
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
	
	@Autowired
	private BidRepository bidRepo;
	
	@Autowired
	private AuctionLoggerService auctionLogger;
	
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
			if(auction.getProductId() == dto.getProductId() && auction.getStatus().equals(Status.ACTIVE)) {
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
				model.getCurrentWinnerEmail(), model.getCreatedAt(), model.getClosedAt(), 
				model.getStatus());
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
		
		if(!auction.get().getStatus().equals(Status.ACTIVE)) {
			throw new AuctionNotActiveException("This auction is not active");
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

	@Override
	public ResponseEntity<?> getAuctionsByStatus(Status status) {
		List<AuctionModel> models = repo.findByStatus(status);
		List<AuctionDto> dtos = new ArrayList<AuctionDto>();
		
		for(AuctionModel model : models) {
			dtos.add(convertFromModelToDto(model));
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(dtos);
	}

	@Override
	public ResponseEntity<?> cancelAuction(int id, String currentEmail) {
		Optional<AuctionModel> auction = repo.findById(id);
		
		if(auction.isEmpty()) {
			throw new AuctionNotFoundException("Auction with given id does not exist");
		}
		if(!auction.get().getStatus().equals(Status.ACTIVE)) {
			throw new AuctionNotActiveException("This auction is not active");
		}
		if(!currentEmail.equals(auction.get().getOwnerEmail())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
					"You are not owner of this auction, you must be owner of auction to cancel it.");
		}
		
		List<AuctionParticipantModel> auctionParticipants = participantRepo.findByAuctionId(id);
		returnDeposit(auctionParticipants, auction);
		
		auction.get().setStatus(Status.CANCELLED);
		auction.get().setClosedAt(Instant.now());
	    AuctionModel savedAuction = repo.save(auction.get());
	    
	    auctionLogger.logAuctionCancelled(savedAuction, auctionParticipants, "Cancelled by owner");
		
		Map<String, Object> response = new HashMap<>();

		response.put("message", "Auction has been successfully canceled. All deposits are returned to users");
		response.put("AUCTION", savedAuction);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
		
	}

	@Override
	public ResponseEntity<?> finishAuction(int id, String currentEmail) {
		Optional<AuctionModel> auction = repo.findById(id);
		Map<String, Object> response = new HashMap<>();
		
		if(auction.isEmpty()) {
			throw new AuctionNotFoundException("Auction with given id does not exist");
		}
		if(!auction.get().getStatus().equals(Status.ACTIVE)) {
			throw new AuctionNotActiveException("This auction is not active");
		}
		if(!currentEmail.equals(auction.get().getOwnerEmail())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
					"You are not owner of this auction, you must be owner of auction to finish it.");
		}
		
		if(auction.get().getCurrentWinnerEmail().equals("")) {
			auction.get().setStatus(Status.NO_BIDS);
			auction.get().setClosedAt(Instant.now());
			response.put("WINNER", "Auction has been successfully finished, but with no BIDs");
			response.put("PRODUCT", productProxy.getProductsById(auction.get().getProductId()));
			response.put("AUCTION", repo.save(auction.get()));
			
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
		BankAccountDto winnersBankAccount = bankAccountProxy.getBankAccount(auction.get().getCurrentWinnerEmail());
		BigDecimal winnerCurrencyAmount = auction.get().getCurrentHighestBid();
		List<AuctionParticipantModel> auctionParticipants = participantRepo.findByAuctionId(id);
		
		Optional<AuctionParticipantModel> winnersParticipant = participantRepo.findByParticipantEmailAndAuctionId(
				auction.get().getCurrentWinnerEmail(), id);
		
		switch(auction.get().getCurrency()) {
		case EUR -> {
			if(winnerCurrencyAmount.compareTo(winnersBankAccount.getEurAmount()) > 0) {
				auctionParticipants.remove(winnersParticipant.get());
				response.put("WINNER", 
						"Winner did not have enough currency amount to pay this product, and deposit is not returned to him");
				returnDeposit(auctionParticipants, auction);
				response.put("PRODUCT", productProxy.getProductsById(auction.get().getProductId()));
				auction.get().setStatus(Status.COMPLETED);
				auction.get().setClosedAt(Instant.now());
				response.put("AUCTION", repo.save(auction.get()));
				return ResponseEntity.status(HttpStatus.OK).body(response);
			} else {
				winnersBankAccount.setEurAmount(winnersBankAccount.getEurAmount().subtract(winnerCurrencyAmount));
			}
		}
		case USD -> {
			if(winnerCurrencyAmount.compareTo(winnersBankAccount.getUsdAmount()) > 0) {
				auctionParticipants.remove(winnersParticipant.get());
				response.put("WINNER", 
						"Winner did not have enough currency amount to pay this product, and deposit is not returned to him");
				returnDeposit(auctionParticipants, auction);
				response.put("PRODUCT", productProxy.getProductsById(auction.get().getProductId()));
				auction.get().setStatus(Status.COMPLETED);
				auction.get().setClosedAt(Instant.now());
				response.put("AUCTION", repo.save(auction.get()));
				return ResponseEntity.status(HttpStatus.OK).body(response);
			} else {
				winnersBankAccount.setUsdAmount(winnersBankAccount.getUsdAmount().subtract(winnerCurrencyAmount));
			}
		}
		case RSD -> {
			if(winnerCurrencyAmount.compareTo(winnersBankAccount.getRsdAmount()) > 0) {
				auctionParticipants.remove(winnersParticipant.get());
				response.put("WINNER", 
						"Winner did not have enough currency amount to pay this product, and deposit is not returned to him");
				returnDeposit(auctionParticipants, auction);
				response.put("PRODUCT", productProxy.getProductsById(auction.get().getProductId()));
				auction.get().setStatus(Status.COMPLETED);
				auction.get().setClosedAt(Instant.now());
				response.put("AUCTION", repo.save(auction.get()));
				return ResponseEntity.status(HttpStatus.OK).body(response);
			} else {
				winnersBankAccount.setRsdAmount(winnersBankAccount.getRsdAmount().subtract(winnerCurrencyAmount));
			}
		}
		}	
		
		returnDeposit(auctionParticipants, auction);
	    
		response.put("WINNER", auction.get().getCurrentWinnerEmail());
		bankAccountProxy.updateBankAccount(winnersBankAccount);
		
		ProductDto product = productProxy.getProductsById(auction.get().getProductId()).getBody();
		product.setOwnerEmail(auction.get().getCurrentWinnerEmail());
		ProductDto updatedProduct = productProxy.updateProductAuction(product);
		response.put("PRODUCT", updatedProduct);
		
		auction.get().setStatus(Status.COMPLETED);
		auction.get().setClosedAt(Instant.now());
	    AuctionModel savedAuction = repo.save(auction.get());
		
		List<BidModel> allBids = bidRepo.findByAuctionId(id);
	    List<AuctionParticipantModel> participants = participantRepo.findByAuctionId(id);
	    
	    auctionLogger.logAuctionCompleted(auction.get(), participants, allBids);
		response.put("AUCTION", savedAuction);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
		
	}
	
	private AuctionParticipantDto convertFromModelToDto(AuctionParticipantModel model) {
		return new AuctionParticipantDto(model.getAuctionId(), model.getParticipantEmail(), 
				model.getDeposit(), model.getJoinedAt());
	}
	
	private void returnDeposit(List<AuctionParticipantModel> auctionParticipants, Optional<AuctionModel> auction) {
		for(AuctionParticipantModel auctionParticipant : auctionParticipants) {
			String participantEmail = auctionParticipant.getParticipantEmail();
			BankAccountDto bankAccount = bankAccountProxy.getBankAccount(participantEmail);
			BigDecimal deposit = auctionParticipant.getDeposit();
			
			switch(auction.get().getCurrency()) {
			case USD -> {
				bankAccount.setUsdAmount(bankAccount.getUsdAmount().add(deposit));
				bankAccountProxy.updateBankAccount(bankAccount);
			}
			case RSD -> {
				bankAccount.setRsdAmount(bankAccount.getRsdAmount().add(deposit));
				bankAccountProxy.updateBankAccount(bankAccount);
			}
			case EUR -> {
				bankAccount.setEurAmount(bankAccount.getEurAmount().add(deposit));
				bankAccountProxy.updateBankAccount(bankAccount);
			}
			}
			
		}
	}
	
}
