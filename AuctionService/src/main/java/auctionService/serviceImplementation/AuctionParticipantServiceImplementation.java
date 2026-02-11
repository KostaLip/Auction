package auctionService.serviceImplementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.AuctionParticipantDto;
import api.proxies.UserProxy;
import api.services.AuctionParticipantService;
import auctionService.entity.AuctionParticipantModel;
import auctionService.repository.AuctionParticipantRepository;
import util.exceptions.UserNotFoundException;

@RestController
public class AuctionParticipantServiceImplementation implements AuctionParticipantService{

	@Autowired
	private AuctionParticipantRepository repo;
	
	@Autowired
	private UserProxy userProxy;
	
	@Override
	public ResponseEntity<?> getAuctionsParticipantByEmail(String email) {
		if(!userProxy.checkUserExists(email).getBody()) {
			throw new UserNotFoundException("User with given email does not exist");
		}
		
		List<AuctionParticipantModel> models = repo.findByParticipantEmail(email);
		List<AuctionParticipantDto> dtos = new ArrayList<AuctionParticipantDto>();
		
		for(AuctionParticipantModel model : models) {
			dtos.add(convertFromModelToDto(model));
		}
		
		if(dtos.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(
					"You are not participant of any auction");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(dtos);
	}

	@Override
	public List<AuctionParticipantDto> getAuctionsParticipants() {
		List<AuctionParticipantModel> models = repo.findAll();
		List<AuctionParticipantDto> dtos = new ArrayList<AuctionParticipantDto>();
		
		for(AuctionParticipantModel model : models) {
			dtos.add(convertFromModelToDto(model));
		}
		
		return dtos;
	}
	
	private AuctionParticipantDto convertFromModelToDto(AuctionParticipantModel model) {
		return new AuctionParticipantDto(model.getAuctionId(), model.getParticipantEmail(), 
				model.getDeposit(), model.getJoinedAt());
	}

}
