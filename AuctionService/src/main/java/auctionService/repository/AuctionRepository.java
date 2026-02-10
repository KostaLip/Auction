package auctionService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import auctionService.entity.AuctionModel;

@Repository
public interface AuctionRepository extends JpaRepository<AuctionModel, Integer>{

	List<AuctionModel> findByOwnerEmail(String email);
	
}
