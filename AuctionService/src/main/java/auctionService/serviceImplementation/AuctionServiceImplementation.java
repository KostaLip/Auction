package auctionService.serviceImplementation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.AuctionCreateUpdateDto;
import api.dtos.AuctionDto;
import api.dtos.ProductDto;
import api.enums.Currency;
import api.proxies.ProductProxy;
import api.proxies.UserProxy;
import api.services.AuctionService;
import auctionService.entity.AuctionModel;
import auctionService.repository.AuctionRepository;
import util.exceptions.AuctionNotFoundException;
import util.exceptions.AuctionOwnerException;
import util.exceptions.CurrencyNameException;
import util.exceptions.UserEmailAuctionException;
import util.exceptions.UserNotFoundException;

@RestController
public class AuctionServiceImplementation implements AuctionService{
	
	@Autowired
	private ProductProxy productProxy;
	
	@Autowired
	private AuctionRepository repo;
	
	@Autowired
	private UserProxy userProxy;

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

}
