package auctionService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import auctionService.entity.AuctionParticipantModel;

@Repository
public interface AuctionParticipantRepository extends JpaRepository<AuctionParticipantModel, Integer>{

	List<AuctionParticipantModel> findByParticipantEmail(String email);
	
}
