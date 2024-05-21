package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.*;
import novi.backend.opdracht.backendservice.dto.input.ProductInputDto;
import novi.backend.opdracht.backendservice.dto.input.ProductUpdateDto;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false, unique = true)
    private String productName;

    @Column(nullable = false)
    private String productType;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int inventoryCount;

    @Column
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String productDescription;

    @Column
    private String material;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designer_id", referencedColumnName = "designerId")
    private Designer designer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getInventoryCount() {
        return inventoryCount;
    }

    public void setInventoryCount(int inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Designer getDesigner() {
        return designer;
    }

    public void setDesigner(Designer designer) {
        this.designer = designer;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public void insertCommonFields(ProductInputDto productInputDTO) {
        this.productName = productInputDTO.getProductName();
        this.productType = productInputDTO.getProductType();
        this.price = productInputDTO.getPrice();
        this.inventoryCount = productInputDTO.getInventoryCount();
        this.imageUrl = productInputDTO.getImageUrl();
        this.productDescription = productInputDTO.getProductDescription();
        this.material = productInputDTO.getMaterial();
    }

    public void updateCommonFields(ProductUpdateDto productUpdateDTO) {
        this.productName = productUpdateDTO.getProductName();
        this.price = productUpdateDTO.getPrice();
        this.inventoryCount = productUpdateDTO.getInventoryCount();
        this.imageUrl = productUpdateDTO.getImageUrl();
        this.productDescription = productUpdateDTO.getProductDescription();
        this.material = productUpdateDTO.getMaterial();
    }

}