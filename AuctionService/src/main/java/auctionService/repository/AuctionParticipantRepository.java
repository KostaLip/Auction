package auctionService.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import auctionService.entity.AuctionParticipantModel;

@Repository
public interface AuctionParticipantRepository extends JpaRepository<AuctionParticipantModel, Integer>{

	List<AuctionParticipantModel> findByParticipantEmail(String email);
	Optional<AuctionParticipantModel> findByParticipantEmailAndAuctionId(String email, int id);
	List<AuctionParticipantModel> findByAuctionId(int id);
	
}
