package userService;

import java.io.Serializable;

import org.hibernate.annotations.Check;

import api.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
@Check(constraints = "role IN ('ADMIN', 'USER')")
public class UserModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "User_SEQ_GENERATOR", sequenceName = "user_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "User_SEQ_GENERATOR")
	private int id;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String name;
	
	@Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
	private Role role;
	
	public UserModel() {
		
	}
	
	public UserModel(String email, String password, String name, Role role) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.role = role;
	}
	
	public UserModel(int id, String email, String password, String name, Role role) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
