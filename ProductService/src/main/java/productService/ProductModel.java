package productService;

import java.io.Serializable;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;

@Entity
public class ProductModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "Product_SEQ_GENERATOR", sequenceName = "product_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Product_SEQ_GENERATOR")
	private int id;
	
	@Column(nullable = false)
	private String ownerEmail;
	
	@Column(nullable = false)
	private String name;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@Column(updatable = false)
	private Instant createdAt;
	
	@PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }
	
	public ProductModel() {
		
	}
	
	public ProductModel(String ownerEmail, String name, String description) {
		this.ownerEmail = ownerEmail;
        this.name = name;
        this.description = description;
	}
	
	public ProductModel(int id, String ownerEmail, String name, String description) {
		this.id = id;
		this.ownerEmail = ownerEmail;
        this.name = name;
        this.description = description;
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
