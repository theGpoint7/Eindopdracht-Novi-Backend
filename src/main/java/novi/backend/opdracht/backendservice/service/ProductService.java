package novi.backend.opdracht.backendservice.service;

import novi.backend.opdracht.backendservice.dto.input.ProductInputDto;
import novi.backend.opdracht.backendservice.dto.input.ProductUpdateDto;
import novi.backend.opdracht.backendservice.dto.output.ProductOutputDto;
import novi.backend.opdracht.backendservice.exception.BadRequestException;
import novi.backend.opdracht.backendservice.exception.ProductNameTooLongException;
import novi.backend.opdracht.backendservice.exception.ResourceNotFoundException;
import novi.backend.opdracht.backendservice.model.*;
import novi.backend.opdracht.backendservice.repository.DesignerRepository;
import novi.backend.opdracht.backendservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final DesignerRepository designerRepository;
    private final AuthenticationService authenticationService;

    public ProductService(ProductRepository productRepository, DesignerRepository designerRepository, AuthenticationService authenticationService) {
        this.productRepository = productRepository;
        this.designerRepository = designerRepository;
        this.authenticationService = authenticationService;
    }

    public ProductOutputDto getProductById(Long productId) {
        AbstractProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product niet gevonden"));
        return toProductOutputDTO(product);
    }

    public ProductOutputDto createProduct(ProductInputDto productInputDTO) {
        User user = authenticationService.getCurrentUser();

        Designer designer = designerRepository.findByUserUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Ontwerper niet gevonden voor gebruikersnaam: " + user.getUsername()));
        if (productRepository.existsByProductName(productInputDTO.getProductName())) {
            throw new BadRequestException("U kunt dit product niet maken, de naam bestaat al.");
        }

        if (productInputDTO.getProductType() == null || productInputDTO.getProductType().isEmpty()) {
            throw new BadRequestException("Producttype mag niet leeg zijn.");
        }

        AbstractProduct product;
        switch (productInputDTO.getProductType()) {
            case "Clothing":
                Clothing clothing = new Clothing();
                clothing.insertSpecificFields(productInputDTO);
                product = clothing;
                break;
            case "Accessory":
                Accessory accessory = new Accessory();
                accessory.insertSpecificFields(productInputDTO);
                product = accessory;
                break;
            case "Footwear":
                Footwear footwear = new Footwear();
                footwear.insertSpecificFields(productInputDTO);
                product = footwear;
                break;
            default:
                throw new BadRequestException("Ongeldig producttype: " + productInputDTO.getProductType());
        }

        product.insertCommonFields(productInputDTO);
        product.setDesigner(designer);

        product = productRepository.save(product);

        return toProductOutputDTO(product);
    }

    public ProductOutputDto updateProduct(Long productId, ProductUpdateDto productUpdateDTO) {
        User user = authenticationService.getCurrentUser();

        Designer designer = designerRepository.findByUserUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Ontwerper niet gevonden voor gebruikersnaam: " + user.getUsername()));

        AbstractProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product niet gevonden"));

        if (!product.getDesigner().equals(designer)) {
            throw new BadRequestException("U bent niet gemachtigd om dit product bij te werken");
        }

        if (!product.getProductName().equals(productUpdateDTO.getProductName()) && productNameExists(productUpdateDTO.getProductName())) {
            throw new ProductNameTooLongException("Productnaam bestaat al");
        }

        product.updateCommonFields(productUpdateDTO);

        if (product instanceof Clothing) {
            ((Clothing) product).updateSpecificFields(productUpdateDTO);
        } else if (product instanceof Accessory) {
            ((Accessory) product).updateSpecificFields(productUpdateDTO);
        } else if (product instanceof Footwear) {
            ((Footwear) product).updateSpecificFields(productUpdateDTO);
        }

        product = productRepository.save(product);
        return toProductOutputDTO(product);
    }


    public List<ProductOutputDto> findAllProducts(Integer footwearSize, String clothingSize, String color, String storeName) {
        List<AbstractProduct> products;

        if (storeName != null) {
            products = productRepository.findByDesignerStoreName(storeName);
        } else {
            products = productRepository.findAll();
        }

        if (footwearSize != null) {
            products = products.stream()
                    .filter(product -> product instanceof Footwear && ((Footwear) product).getFootwearSize() != null && Objects.equals(((Footwear) product).getFootwearSize(), footwearSize))
                    .collect(Collectors.toList());
        }

        if (clothingSize != null) {
            products = products.stream()
                    .filter(product -> product instanceof Clothing && ((Clothing) product).getClothingSize() != null && ((Clothing) product).getClothingSize().equals(clothingSize))
                    .collect(Collectors.toList());
        }

        if (color != null) {
            products = products.stream()
                    .filter(product -> product instanceof Clothing && ((Clothing) product).getColor() != null && ((Clothing) product).getColor().equals(color))
                    .collect(Collectors.toList());
        }

        return products.stream()
                .map(this::toProductOutputDTO)
                .collect(Collectors.toList());
    }

    public boolean productNameExists(String productName) {
        return productRepository.existsByProductName(productName);
    }

    ProductOutputDto toProductOutputDTO(AbstractProduct product) {
        ProductOutputDto dto = new ProductOutputDto();
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
            ProductOutputDto.DesignerInfo designerDTO = new ProductOutputDto.DesignerInfo();
            designerDTO.setDesignerId(product.getDesigner().getDesignerId());
            designerDTO.setStoreName(product.getDesigner().getStoreName());
            dto.setDesigner(designerDTO);
        }

        if (product.getPromotion() != null) {
            ProductOutputDto.PromotionInfo promotionDTO = new ProductOutputDto.PromotionInfo();
            promotionDTO.setPromotionId(product.getPromotion().getPromotionId());
            promotionDTO.setPromotionDetails(product.getPromotion().getPromotionDescription());
            promotionDTO.setPromotionDescription(product.getPromotion().getPromotionDescription());
            promotionDTO.setPromotionPercentage(product.getPromotion().getPromotionPercentage());
            promotionDTO.setPromotionEndDateTime(product.getPromotion().getPromotionEndDateTime());
            dto.setPromotion(promotionDTO);
        }
        return dto;
    }
}
