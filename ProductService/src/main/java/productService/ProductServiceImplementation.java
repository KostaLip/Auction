package productService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.ProductCreateUpdateDto;
import api.dtos.ProductDto;
import api.proxies.UserProxy;
import api.services.ProductService;
import util.exceptions.CreateProductUserException;
import util.exceptions.ProductNotFoundException;
import util.exceptions.UserNotFoundException;

@RestController
public class ProductServiceImplementation implements ProductService{

	@Autowired
	private ProductRepository repo;
	
	@Autowired
	private UserProxy userProxy;
	
	@Override
	public List<ProductDto> getAllProducts() {
		List<ProductModel> models = repo.findAll();
		List<ProductDto> dtos = new ArrayList<ProductDto>();
		
		for(ProductModel model : models) {
			dtos.add(convertModelToDto(model));
		}
		
		return dtos;
	}

	@Override
	public ResponseEntity<?> getProductsByEmail(String email) {
		if(!userProxy.checkUserExists(email).getBody()) {
			throw new UserNotFoundException("User with given email does not exist");
		}
		
		List<ProductModel> products = repo.findByOwnerEmail(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(products);
	}

	@Override
	public ResponseEntity<?> getProductsById(int id) {
		Optional<ProductModel> product = repo.findById(id);
		
		if(product.isEmpty()) {
			throw new ProductNotFoundException("Product with given id does not exist");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(product);
	}

	@Override
	public ResponseEntity<?> createProduct(ProductCreateUpdateDto dto, 
			String currentEmail) {
		if(!currentEmail.equals(dto.getOwnerEmail())) {
			throw new CreateProductUserException("You can not create products for other users");
		}
		
		ProductModel product = new ProductModel(dto.getOwnerEmail(), dto.getName(), 
				dto.getDescription());
		return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(product));
	}

	@Override
	public ResponseEntity<?> updateProduct(ProductCreateUpdateDto dto, int id, String currentEmail) {
		Optional<ProductModel> product = repo.findById(id);
		
		if(product.isEmpty()) {
			throw new ProductNotFoundException("Product with given id does not exist");
		}
		if(!currentEmail.equals(dto.getOwnerEmail())) {
			throw new CreateProductUserException("You can not update products for other users");
		}
		
		repo.updateProduct(id, dto.getOwnerEmail(), dto.getName(), dto.getDescription());
		return ResponseEntity.status(HttpStatus.OK).body(dto);
		
	}

	@Override
	public ResponseEntity<?> deleteProduct(int id, String currentEmail) {
		Optional<ProductModel> product = repo.findById(id);
		
		if(product.isEmpty()) {
			throw new ProductNotFoundException("Product with given id does not exist");
		}
		if(!currentEmail.equals(product.get().getOwnerEmail())) {
			throw new CreateProductUserException("You can not delete products for other users");
		}
		
		repo.delete(product.get());
		return ResponseEntity.status(HttpStatus.OK)
		        .body(Map.of("message", String.format(
		            "Product with id: %s, has been successfully deleted", id)));
	}
	
	private ProductDto convertModelToDto(ProductModel model) {
		return new ProductDto(
				model.getId(), model.getOwnerEmail(), model.getName(), model.getDescription(), 
				model.getCreatedAt());
	}

}
