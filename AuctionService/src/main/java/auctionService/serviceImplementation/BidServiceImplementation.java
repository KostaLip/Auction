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

import api.dtos.BankAccountDto;
import api.dtos.BidDto;
import api.proxies.BankAccountProxy;
import api.services.BidService;
import auctionService.entity.AuctionModel;
import auctionService.entity.AuctionParticipantModel;
import auctionService.entity.BidModel;
import auctionService.repository.AuctionParticipantRepository;
import auctionService.repository.AuctionRepository;
import auctionService.repository.BidRepository;
import util.exceptions.AuctionNotFoundException;
import util.exceptions.HighestBidException;
import util.exceptions.NotEnoughCurrencyAmountException;
import util.exceptions.UserNotPartOfAuctionException;

@RestController
public class BidServiceImplementation implements BidService{

	@Autowired
	private BidRepository repo;
	
	@Autowired
	private AuctionRepository auctionRepository;
	
	@Autowired
	private AuctionParticipantRepository auctionParticipantRepository;
	
	@Autowired
	private BankAccountProxy bankAccountProxy;
	
	@Override
	public ResponseEntity<?> createBid(int id, BigDecimal amount, String currentEmail) {
		Optional<AuctionModel> auction = auctionRepository.findById(id);
		Optional<AuctionParticipantModel> auctionParticipant = 
				auctionParticipantRepository.findByParticipantEmailAndAuctionId(currentEmail, id);
		
		BankAccountDto bankAccount = bankAccountProxy.getBankAccount(currentEmail);
		BankAccountDto responseAccount = new BankAccountDto(bankAccount.getEmail(), bankAccount.getUsdAmount(), 
				bankAccount.getEurAmount(), bankAccount.getRsdAmount());
		
		if(auction.isEmpty()) {
			throw new AuctionNotFoundException("Auction with given id does not exist");
		}
		if(auctionParticipant.isEmpty()) {
			throw new UserNotPartOfAuctionException("You did not join to this auction");
		}
		
		BigDecimal currentHighestBid = auction.get().getCurrentHighestBid();
		
		if(currentHighestBid.compareTo(amount) > 0) {
			throw new HighestBidException("Provided amount must be higher then current highest bid. Provided amount: " + 
		amount + ". Current highest bid: " + currentHighestBid);
		}
		
		switch(auction.get().getCurrency()) {
		case USD -> {
			if(amount.compareTo(bankAccount.getUsdAmount()) > 0) {
				throw new NotEnoughCurrencyAmountException("You do not have enough USD currency. Current USD amount: " +
			bankAccount.getUsdAmount());
			}
			bankAccount.setUsdAmount(bankAccount.getUsdAmount().subtract(amount));
		}
		case RSD -> {
			if(amount.compareTo(bankAccount.getRsdAmount()) > 0) {
				throw new NotEnoughCurrencyAmountException("You do not have enough RSD currency. Current RSD amount: " +
			bankAccount.getRsdAmount());
			}
			bankAccount.setRsdAmount(bankAccount.getRsdAmount().subtract(amount));
		}
		case EUR -> {
			if(amount.compareTo(bankAccount.getEurAmount()) > 0) {
				throw new NotEnoughCurrencyAmountException("You do not have enough EUR currency. Current EUR amount: " +
			bankAccount.getEurAmount());
			}
			bankAccount.setEurAmount(bankAccount.getEurAmount().subtract(amount));
		}
		}
		
		BidModel bidModel = new BidModel(id, currentEmail, amount, auction.get().getCurrency());
		BidModel saved = repo.save(bidModel);
		BidDto dto = convertFromModelToDto(saved);
		
		Map<String, Object> response = new HashMap<>();
		
		bankAccountProxy.updateBankAccount(bankAccount);
		
		response.put("Bank Account before bid", responseAccount);
		response.put("Bank Account after bid", bankAccount);
		response.put("Your BID", dto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Override
	public ResponseEntity<?> getAuctionBids(int id) {
		List<BidModel> models = repo.findByAuctionId(id);
		List<BidDto> dtos = new ArrayList<BidDto>();
		
		for(BidModel model : models) {
			dtos.add(convertFromModelToDto(model));
		}
			
		return ResponseEntity.status(HttpStatus.OK).body(dtos);
	}

	@Override
	public ResponseEntity<?> getCurrentUserBids(String currentEmail) {
		List<BidModel> models = repo.findByUserEmail(currentEmail);
		List<BidDto> dtos = new ArrayList<BidDto>();
		
		for(BidModel model : models) {
			dtos.add(convertFromModelToDto(model));
		}
			
		return ResponseEntity.status(HttpStatus.OK).body(dtos);
	}
	
	private BidDto convertFromModelToDto(BidModel model) {
		return new BidDto(model.getAuctionId(), model.getUserEmail(), model.getAmount(), 
				model.getCurrency(), model.getCreatedAt());
	}

}
