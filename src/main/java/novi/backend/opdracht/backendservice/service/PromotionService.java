package novi.backend.opdracht.backendservice.service;

import novi.backend.opdracht.backendservice.dto.input.PromotionInputDto;
import novi.backend.opdracht.backendservice.dto.output.PromotionResponseDto;
import novi.backend.opdracht.backendservice.model.AbstractProduct;
import novi.backend.opdracht.backendservice.model.Designer;
import novi.backend.opdracht.backendservice.model.Promotion;
import novi.backend.opdracht.backendservice.model.User;
import novi.backend.opdracht.backendservice.repository.DesignerRepository;
import novi.backend.opdracht.backendservice.repository.ProductRepository;
import novi.backend.opdracht.backendservice.repository.PromotionRepository;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromotionService {

    private final DesignerRepository designerRepository;
    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;
    private final AuthenticationService authenticationService;

    public PromotionService(DesignerRepository designerRepository, PromotionRepository promotionRepository, ProductRepository productRepository, AuthenticationService authenticationService) {
        this.designerRepository = designerRepository;
        this.promotionRepository = promotionRepository;
        this.productRepository = productRepository;
        this.authenticationService = authenticationService;
    }

    public List<PromotionResponseDto> getDesignerPromotions(Long designerId) {
        User currentUser = authenticationService.getCurrentUser();
        Long jwtDesignerId = getDesignerIdFromUser(currentUser);
        if (!jwtDesignerId.equals(designerId)) {
            throw new AuthorizationServiceException("User is not authorized for this action");
        }

        List<Promotion> promotions = promotionRepository.findByDesignerDesignerId(designerId);
        return promotions.stream()
                .map(this::mapToPromotionResponseDto)
                .collect(Collectors.toList());
    }

    private Long getDesignerIdFromUser(User user) {
        if (user.getDesigner() != null) {
            return user.getDesigner().getDesignerId();
        } else {
            throw new UsernameNotFoundException("User not found or not associated with a designer");
        }
    }

    public void createPromotion(Long designerId, PromotionInputDto promotionInputDto) {
        User currentUser = authenticationService.getCurrentUser();
        if (!isUserAuthorizedForDesigner(currentUser, designerId)) {
            throw new AuthorizationServiceException("User is not authorized for this action");
        }

        if (promotionRepository.existsByPromotionName(promotionInputDto.getPromotionName())) {
            throw new IllegalArgumentException("A promotion with the same name already exists");
        }

        Designer designer = designerRepository.findById(designerId)
                .orElseThrow(() -> new RuntimeException("Designer not found"));

        Promotion promotion = new Promotion();
        promotion.setPromotionName(promotionInputDto.getPromotionName());
        promotion.setPromotionDescription(promotionInputDto.getPromotionDescription());
        promotion.setPromotionPercentage(promotionInputDto.getPromotionPercentage());
        promotion.setPromotionStartDateTime(promotionInputDto.getPromotionStartDateTime());
        promotion.setPromotionEndDateTime(promotionInputDto.getPromotionEndDateTime());
        promotion.setDesigner(designer);
        promotionRepository.save(promotion);
    }

    public void applyPromotionToProduct(Long productId, Long designerId, Long promotionId) {
        User currentUser = authenticationService.getCurrentUser();
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new IllegalArgumentException("Promotion not found"));
        Designer designer = promotion.getDesigner();

        if (designer == null) {
            throw new IllegalArgumentException("Designer not found for promotion");
        }
        if (!isUserAuthorizedForDesigner(currentUser, designerId)) {
            throw new AuthorizationServiceException("User is not authorized for this action");
        }
        if (!designer.getDesignerId().equals(designerId)) {
            throw new AuthorizationServiceException("Designer making the request is not authorized for this action");
        }
        if (!isProductOwnedByDesigner(productId, designer)) {
            throw new AuthorizationServiceException("Product is not owned by the designer");
        }

        AbstractProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        product.setPromotion(promotion);

        productRepository.save(product);
    }

    private boolean isProductOwnedByDesigner(Long productId, Designer designer) {
        AbstractProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        return product.getDesigner().equals(designer);
    }

    private boolean isUserAuthorizedForDesigner(User user, Long designerId) {
        return user.getDesigner() != null && user.getDesigner().getDesignerId().equals(designerId);
    }

    private PromotionResponseDto mapToPromotionResponseDto(Promotion promotion) {
        PromotionResponseDto dto = new PromotionResponseDto();
        dto.setPromotionId(promotion.getPromotionId());
        dto.setPromotionName(promotion.getPromotionName());
        dto.setPromotionDescription(promotion.getPromotionDescription());
        dto.setPromotionPercentage(promotion.getPromotionPercentage());
        dto.setPromotionStartDateTime(promotion.getPromotionStartDateTime());
        dto.setPromotionEndDateTime(promotion.getPromotionEndDateTime());
        return dto;
    }
}
