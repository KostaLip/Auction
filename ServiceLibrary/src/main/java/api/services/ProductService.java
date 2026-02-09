package api.services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import api.dtos.ProductCreateUpdateDto;
import api.dtos.ProductDto;

@Service
public interface ProductService {

	@GetMapping("/products")
	List<ProductDto> getAllProducts();
	
	@GetMapping("/products/email")
	ResponseEntity<?> getProductsByEmail(@RequestParam String email);
	
	@GetMapping("/products/id")
	ResponseEntity<?> getProductsById(@RequestParam int id);
	
	@PostMapping("/products")
	ResponseEntity<?> createProduct(@RequestBody ProductCreateUpdateDto dto, 
			@RequestHeader(value = "X-Auth-Email") String currentEmail);
	
	@PutMapping("/products/id")
	ResponseEntity<?> updateProduct(@RequestBody ProductCreateUpdateDto dto, 
			@RequestParam int id, 
			@RequestHeader(value = "X-Auth-Email") String currentEmail);
	
	@DeleteMapping("/products/id")
	ResponseEntity<?> deleteProduct(@RequestParam int id, 
			@RequestHeader(value = "X-Auth-Email") String currentEmail);
	
}
