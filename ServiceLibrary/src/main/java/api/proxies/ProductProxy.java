package api.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import api.dtos.ProductDto;

/* LOCAL
@FeignClient(name = "product-service", url = "http://localhost:8300") */
@FeignClient(name = "product-service", url = "http://product-service:8300")
public interface ProductProxy {

	@GetMapping("/products/email")
	ResponseEntity<?> getProductsByEmail(@RequestParam String email);
	
	@GetMapping("/products/id")
	ResponseEntity<ProductDto> getProductsById(@RequestParam int id);
	
	@PutMapping("/internal/products/update")
	ProductDto updateProductAuction(@RequestBody ProductDto dto);
	
}
