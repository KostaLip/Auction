package userService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.UserDto;
import api.enums.Role;
import api.proxies.BankAccountProxy;
import api.services.UserService;
import util.exceptions.AdminUserDeletedException;
import util.exceptions.DeletingOtherUserException;
import util.exceptions.RoleChangeException;
import util.exceptions.UserAlreadyExistsException;
import util.exceptions.UserEditAnotherUserException;
import util.exceptions.UserNotFoundException;
import util.exceptions.UserProfileDetailsException;

@RestController
public class UserServiceImplementation implements UserService{

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private BankAccountProxy bankAccountProxy;
	
	@Override
	public List<UserDto> getUsers() {
		List<UserModel> models = repo.findAll();
		List<UserDto> dtos = new ArrayList<UserDto>();
		
		for(UserModel model: models) {
			dtos.add(convertUserModelToDto(model));
		}
		
		return dtos;
	}

	@Override
	public ResponseEntity<?> getUserByEmailAuth(String email) {
		UserModel user = repo.findByEmail(email);
		
		if(user == null) {
			throw new UserNotFoundException("User with provided email does not exist");
		}
		
		UserDto dto = convertUserModelToDto(user);
		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}
	
	@Override
	public ResponseEntity<?> getUserByEmail(String email, Role role, String currentEmail) {
		UserModel user = repo.findByEmail(email);
		
		if(user == null) {
			throw new UserNotFoundException("User with provided email does not exist");
		}
		
		if(role.equals(Role.USER) && !email.equals(currentEmail)) {
			throw new UserProfileDetailsException("You can only get details of your profile");
		}
		
		UserDto dto = convertUserModelToDto(user);
		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}

	@Override
	public ResponseEntity<?> createAdmin(UserDto dto) {
		if (repo.findByEmail(dto.getEmail()) != null) {
			throw new UserAlreadyExistsException(
	        		"User with email '" + dto.getEmail() + "' already exists");
	    }
	    
	    dto.setRole(Role.ADMIN);
	    dto.setPassword(encoder.encode(dto.getPassword()));
	    UserModel model = convertUserDtoToModel(dto);
	    UserModel saved = repo.save(model);
	    
	    return ResponseEntity.status(HttpStatus.CREATED)
	        .body(convertUserModelToDto(saved));
	}

	@Override
	public ResponseEntity<?> createUser(UserDto dto) {
		if (repo.findByEmail(dto.getEmail()) != null) {
	        throw new UserAlreadyExistsException(
	        		"User with email '" + dto.getEmail() + "' already exists");
	    }
	    
	    dto.setRole(Role.USER);
	    dto.setPassword(encoder.encode(dto.getPassword()));
	    UserModel model = convertUserDtoToModel(dto);
	    UserModel saved = repo.save(model);
	    
	    bankAccountProxy.createBankAccount(dto.getEmail());
	    
	    return ResponseEntity.status(HttpStatus.CREATED)
	        .body(convertUserModelToDto(saved));
	}

	@Override
	public ResponseEntity<?> updateUser(UserDto dto, Role role, String currentEmail) {
		UserModel user = repo.findByEmail(dto.getEmail());
		
		if(user == null) {
			throw new UserNotFoundException("User with provided email does not exist");
		}
		
		if(role.equals(Role.USER) && !dto.getEmail().equals(currentEmail)) {
			throw new UserEditAnotherUserException("You can only change your profile details");
		}
		
		if(!dto.getRole().equals(user.getRole())) {
			throw new RoleChangeException("You can not change existing role");
		}
		
		repo.updateUser(dto.getEmail(), encoder.encode(dto.getPassword()), dto.getName(), dto.getRole());
		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}

	@Override
	public ResponseEntity<?> deleteUser(String email, Role role, String currentEmail) {
		UserModel user = repo.findByEmail(email);
		UserModel currentUser = repo.findByEmail(currentEmail);
		
		if(user != null) {
			if(user.getRole().equals(Role.ADMIN)) {
				throw new AdminUserDeletedException("ADMIN users can not be deleted");
			} else if(role.equals(Role.USER) && !currentUser.getEmail().equals(email)) {
				throw new DeletingOtherUserException("You can not delete other users accounts");
			}
			
			repo.delete(user);
			bankAccountProxy.deleteBankAccount(email);
			
			return ResponseEntity.status(HttpStatus.OK).body(String.format(
					"User with email: %s, has been successfully deleted", email));
		} else {
			throw new UserNotFoundException("User with provided email does not exist");		}
	}
	
	public UserDto convertUserModelToDto(UserModel model) {
		return new UserDto(model.getEmail(), model.getPassword(), model.getName(), model.getRole());
	}
	
	public UserModel convertUserDtoToModel(UserDto dto) {
		return new UserModel(dto.getEmail(), dto.getPassword(), dto.getName(), dto.getRole());
	}

}
