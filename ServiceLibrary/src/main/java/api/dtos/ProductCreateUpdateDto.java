package api.dtos;

public class ProductCreateUpdateDto {

	private String ownerEmail;
	private String name;
	private String description;
	
	private ProductCreateUpdateDto() {
		
	}
	
	private ProductCreateUpdateDto(String ownerEmail, String name, String description) {
		this.ownerEmail = ownerEmail;
		this.name = name;
		this.description = description;
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
	
}
