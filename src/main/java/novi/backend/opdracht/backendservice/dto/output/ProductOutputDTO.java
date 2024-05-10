package novi.backend.opdracht.backendservice.dto.output;

import java.time.LocalDateTime;
import java.util.List;

public class ProductOutputDTO {
    private Long productId;
    private String productName;
    private String productType;
    private double price;
    private int inventoryCount;
    private String imageUrl;
    private String productDescription;
    private String material;
    private String clothingSize;
    private Integer footwearSize;
    private String color;
    private String fit;
    private String gender;
    private String accessoryType;
    private DesignerInfo designer;
    private PromotionInfo promotion;
    private List<FeedbackOutputDTO> feedbacks;


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

    public String getClothingSize() {
        return clothingSize;
    }

    public void setClothingSize(String clothingSize) {
        this.clothingSize = clothingSize;
    }

    public Integer getFootwearSize() {
        return footwearSize;
    }

    public void setFootwearSize(Integer footwearSize) {
        this.footwearSize = footwearSize;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFit() {
        return fit;
    }

    public void setFit(String fit) {
        this.fit = fit;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAccessoryType() {
        return accessoryType;
    }

    public void setAccessoryType(String accessoryType) {
        this.accessoryType = accessoryType;
    }

    public DesignerInfo getDesigner() {
        return designer;
    }

    public void setDesigner(DesignerInfo designer) {
        this.designer = designer;
    }

    public PromotionInfo getPromotion() {
        return promotion;
    }

    public void setPromotion(PromotionInfo promotion) {
        this.promotion = promotion;
    }

    public static class DesignerInfo {
        private Long designerId;
        private String storeName;

        public Long getDesignerId() {
            return designerId;
        }

        public void setDesignerId(Long designerId) {
            this.designerId = designerId;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }
    }

    public static class PromotionInfo {
        private Long promotionId;
        private String promotionDetails;
        private String promotionDescription;
        private double promotionPercentage;
        private LocalDateTime promotionEndDateTime;

        public Long getPromotionId() {
            return promotionId;
        }
        public void setPromotionId(Long promotionId) {
            this.promotionId = promotionId;
        }
        public String getPromotionDetails() {
            return promotionDetails;
        }
        public void setPromotionDetails(String promotionDetails) {
            this.promotionDetails = promotionDetails;
        }

        public String getPromotionDescription() {
            return promotionDescription;
        }

        public void setPromotionDescription(String promotionDescription) {
            this.promotionDescription = promotionDescription;
        }

        public double getPromotionPercentage() {
            return promotionPercentage;
        }

        public void setPromotionPercentage(double promotionPercentage) {
            this.promotionPercentage = promotionPercentage;
        }

        public LocalDateTime getPromotionEndDateTime() {
            return promotionEndDateTime;
        }

        public void setPromotionEndDateTime(LocalDateTime promotionEndDateTime) {
            this.promotionEndDateTime = promotionEndDateTime;
        }
    }
    public List<FeedbackOutputDTO> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<FeedbackOutputDTO> feedbacks) {
        this.feedbacks = feedbacks;
    }
}
