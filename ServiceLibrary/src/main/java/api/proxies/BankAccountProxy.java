package api.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import api.dtos.BankAccountDto;

/* LOCAL
@FeignClient(name = "bank-account", url = "http://localhost:8200") */
@FeignClient(name = "bank-account", url = "http://bank-account:8200")
public interface BankAccountProxy {

	@PostMapping("/bankAccounts")
	ResponseEntity<?> createBankAccount(@RequestParam String email);
	
	@DeleteMapping("/bankAccounts")
	ResponseEntity<?> deleteBankAccount(@RequestParam String email);
	
	@GetMapping("/internal/bankAccount/email")
    BankAccountDto getBankAccount(@RequestParam String email);
	
	@PutMapping("/bankAccounts")
	ResponseEntity<?> updateBankAccount(@RequestBody BankAccountDto dto);
	
}
