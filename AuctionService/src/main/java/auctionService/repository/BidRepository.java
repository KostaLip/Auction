package auctionService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import auctionService.entity.BidModel;

@Repository
public interface BidRepository extends JpaRepository<BidModel, Integer>{

	List<BidModel> findByUserEmail(String email);
	List<BidModel> findByAuctionId(int id);
	
}
