package api.dtos;

import java.time.Instant;

public class ProductDto {
	
	private int id;
	private String ownerEmail;
	private String name;
	private String description;
	private Instant createdAt;
	
	public ProductDto() {
		
	}
	
	public ProductDto(int id, String ownerEmail, String name, String description, 
			Instant createdAt) {
		this.id = id;
        this.ownerEmail = ownerEmail;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getOwnerEmail() {
		return ownerEmail;
	}
	
	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

}
