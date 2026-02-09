package productService;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Integer>{

	List<ProductModel> findByOwnerEmail(String ownerEmail);
	
	@Modifying
	@Transactional
	@Query("update ProductModel u set u.ownerEmail = ?2, u.name = ?3, "
			+ "u.description = ?4 where u.id = ?1")
	void updateProduct(int id, String ownerEmail, String name, String description);
	
}
