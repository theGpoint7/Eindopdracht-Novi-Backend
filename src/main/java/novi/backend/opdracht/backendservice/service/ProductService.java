package novi.backend.opdracht.backendservice.service;

import novi.backend.opdracht.backendservice.dto.input.ProductInputDTO;
import novi.backend.opdracht.backendservice.dto.output.ProductOutputDTO;
import novi.backend.opdracht.backendservice.exception.BadRequestException;
import novi.backend.opdracht.backendservice.exception.ProductNameTooLongException;
import novi.backend.opdracht.backendservice.model.*;
import novi.backend.opdracht.backendservice.repository.DesignerRepository;
import novi.backend.opdracht.backendservice.repository.ProductRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final DesignerRepository designerRepository;

    public ProductService(ProductRepository productRepository, DesignerRepository designerRepository) {
        this.productRepository = productRepository;
        this.designerRepository = designerRepository;
    }

    public ProductOutputDTO getProductById(Long productId) {
        AbstractProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException("Product niet gevonden"));
        return toProductOutputDTO(product);
    }

    public ProductOutputDTO createProduct(ProductInputDTO productInputDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Designer> designerOptional = designerRepository.findByUserUsername(username);
        Designer designer = designerOptional.orElseThrow(() -> new RuntimeException("Ontwerper niet gevonden voor gebruikersnaam: " + username));
        if (productRepository.existsByProductName(productInputDTO.getProductName())) {
            throw new BadRequestException("U kunt dit product niet maken, de naam bestaat al.");
        }

        AbstractProduct product;
        switch (productInputDTO.getProductType()) {
            case "Clothing":
                Clothing clothing = new Clothing();
                clothing.setClothingSize(productInputDTO.getClothingSize());
                clothing.setColor(productInputDTO.getColor());
                clothing.setFit(productInputDTO.getFit());
                product = clothing;
                break;
            case "Accessory":
                Accessory accessory = new Accessory();
                accessory.setAccessoryType(AccessoryType.valueOf(productInputDTO.getAccessoryType()));
                product = accessory;
                break;
            case "Footwear":
                Footwear footwear = new Footwear();
                footwear.setFootwearSize(productInputDTO.getFootwearSize());
                footwear.setGender(productInputDTO.getGender());
                product = footwear;
                break;
            default:
                throw new BadRequestException("Ongeldig producttype: " + productInputDTO.getProductType());
        }

        product.setProductName(productInputDTO.getProductName());
        product.setProductType(productInputDTO.getProductType());
        product.setPrice(productInputDTO.getPrice());
        product.setInventoryCount(productInputDTO.getInventoryCount());
        product.setImageUrl(productInputDTO.getImageUrl());
        product.setProductDescription(productInputDTO.getProductDescription());
        product.setMaterial(productInputDTO.getMaterial());

        designer = designerRepository.save(designer);
        product.setDesigner(designer);

        product = productRepository.save(product);

        return toProductOutputDTO(product);
    }

    public ProductOutputDTO updateProduct(Long productId, ProductInputDTO productInputDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Designer designer = designerRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Ontwerper niet gevonden voor gebruikersnaam: " + username));

        AbstractProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException("Product niet gevonden"));

        if (!product.getDesigner().equals(designer)) {
            throw new BadRequestException("U bent niet gemachtigd om dit product bij te werken");
        }

        if (!product.getProductName().equals(productInputDTO.getProductName()) && productNameExists(productInputDTO.getProductName())) {
            throw new ProductNameTooLongException("Productnaam bestaat al");
        }

        product.setProductName(productInputDTO.getProductName());
        product.setProductType(productInputDTO.getProductType());
        product.setPrice(productInputDTO.getPrice());
        product.setInventoryCount(productInputDTO.getInventoryCount());
        product.setImageUrl(productInputDTO.getImageUrl());
        product.setProductDescription(productInputDTO.getProductDescription());
        product.setMaterial(productInputDTO.getMaterial());

        if (product instanceof Clothing clothing) {
            clothing.setClothingSize(productInputDTO.getClothingSize());
            clothing.setColor(productInputDTO.getColor());
            clothing.setFit(productInputDTO.getFit());
        } else if (product instanceof Accessory accessory) {
            accessory.setAccessoryType(AccessoryType.valueOf(productInputDTO.getAccessoryType()));
        } else if (product instanceof Footwear footwear) {
            footwear.setFootwearSize(productInputDTO.getFootwearSize());
            footwear.setGender(productInputDTO.getGender());
        }
        product = productRepository.save(product);
        return toProductOutputDTO(product);
    }

    public List<ProductOutputDTO> findAllProducts(Integer footwearSize, String clothingSize, String color, String storeName) {
        List<AbstractProduct> products;

        if (storeName != null) {
            products = productRepository.findByDesignerStoreName(storeName);
        } else {
            products = productRepository.findAll();
        }

        if (footwearSize != null) {
            products = products.stream()
                    .filter(product -> {
                        if (product instanceof Footwear) {
                            return ((Footwear) product).getFootwearSize() != null &&
                                    ((Footwear) product).getFootwearSize() == footwearSize.intValue();
                        } else {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());
        }

        if (clothingSize != null) {
            products = products.stream()
                    .filter(product -> {
                        if (product instanceof Clothing) {
                            return ((Clothing) product).getClothingSize() != null &&
                                    ((Clothing) product).getClothingSize().equals(clothingSize);
                        } else {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());
        }

        if (color != null) {
            products = products.stream()
                    .filter(product -> {
                        if (product instanceof Clothing) {
                            return ((Clothing) product).getColor() != null &&
                                    ((Clothing) product).getColor().equals(color);
                        } else {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());
        }

        return products.stream()
                .map(this::toProductOutputDTOWithDesigner)
                .collect(Collectors.toList());
    }

    private ProductOutputDTO toProductOutputDTO(AbstractProduct product) {
        ProductOutputDTO dto = new ProductOutputDTO();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setProductType(product.getProductType());
        dto.setPrice(product.getPrice());
        dto.setInventoryCount(product.getInventoryCount());
        dto.setImageUrl(product.getImageUrl());
        dto.setProductDescription(product.getProductDescription());
        dto.setMaterial(product.getMaterial());

        if (product instanceof Clothing clothingProduct) {
            dto.setClothingSize(clothingProduct.getClothingSize());
            dto.setColor(clothingProduct.getColor());
            dto.setFit(clothingProduct.getFit());
        } else if (product instanceof Accessory accessoryProduct) {
            dto.setAccessoryType(accessoryProduct.getAccessoryType().name());
        } else if (product instanceof Footwear footwearProduct) {
            dto.setFootwearSize(footwearProduct.getFootwearSize());
            dto.setGender(footwearProduct.getGender());
        }

        if (product.getDesigner() != null) {
            ProductOutputDTO.DesignerInfo designerDTO = new ProductOutputDTO.DesignerInfo();
            designerDTO.setDesignerId(product.getDesigner().getDesignerId());
            designerDTO.setStoreName(product.getDesigner().getStoreName());
            dto.setDesigner(designerDTO);
        }

        if (product.getPromotion() != null) {
            ProductOutputDTO.PromotionInfo promotionDTO = new ProductOutputDTO.PromotionInfo();
            promotionDTO.setPromotionId(product.getPromotion().getPromotionId());
            promotionDTO.setPromotionDetails(product.getPromotion().getPromotionName());
            promotionDTO.setPromotionDescription(product.getPromotion().getPromotionDescription());
            promotionDTO.setPromotionPercentage(product.getPromotion().getPromotionPercentage());
            promotionDTO.setPromotionEndDateTime(product.getPromotion().getPromotionEndDateTime());
            dto.setPromotion(promotionDTO);
        }
        return dto;
    }

    public boolean productNameExists(String productName) {
        return productRepository.existsByProductName(productName);
    }

    private ProductOutputDTO toProductOutputDTOWithDesigner(AbstractProduct product) {
        ProductOutputDTO dto = toProductOutputDTO(product);

        if (product.getDesigner() != null) {
            ProductOutputDTO.DesignerInfo designerDTO = new ProductOutputDTO.DesignerInfo();
            designerDTO.setDesignerId(product.getDesigner().getDesignerId());
            designerDTO.setStoreName(product.getDesigner().getStoreName());
            dto.setDesigner(designerDTO);
        }

        if (product.getPromotion() != null) {
            ProductOutputDTO.PromotionInfo promotionDTO = new ProductOutputDTO.PromotionInfo();
            promotionDTO.setPromotionId(product.getPromotion().getPromotionId());
            promotionDTO.setPromotionDetails(product.getPromotion().getPromotionDescription());
            dto.setPromotion(promotionDTO);
        }

        return dto;
    }
}
