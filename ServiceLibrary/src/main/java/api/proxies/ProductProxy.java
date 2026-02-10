package api.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import api.dtos.ProductDto;

@FeignClient(name = "product-service", url = "http://localhost:8300")
public interface ProductProxy {

	@GetMapping("/products/email")
	ResponseEntity<?> getProductsByEmail(@RequestParam String email);
	
	@GetMapping("/products/id")
	ResponseEntity<ProductDto> getProductsById(@RequestParam int id);
	
}
