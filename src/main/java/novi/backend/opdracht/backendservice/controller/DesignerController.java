package novi.backend.opdracht.backendservice.controller;

import novi.backend.opdracht.backendservice.dto.input.ApplyPromotionRequest;
import novi.backend.opdracht.backendservice.dto.input.PromotionInputDto;
import novi.backend.opdracht.backendservice.dto.output.SalesFiguresDto;
import novi.backend.opdracht.backendservice.dto.output.DesignerResponseDto;
import novi.backend.opdracht.backendservice.dto.output.PromotionResponseDto;
import novi.backend.opdracht.backendservice.service.DesignerService;
import novi.backend.opdracht.backendservice.service.PromotionService;
import novi.backend.opdracht.backendservice.service.ValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/designers")
public class DesignerController {

    private final DesignerService designerService;
    private final PromotionService promotionService;
    private final ValidationService validationService;

    public DesignerController(DesignerService designerService, PromotionService promotionService, ValidationService validationService) {
        this.designerService = designerService;
        this.promotionService = promotionService;
        this.validationService = validationService;
    }

    @GetMapping
    public ResponseEntity<List<DesignerResponseDto>> getAllDesigners() {
        List<DesignerResponseDto> designers = designerService.getAllDesigners();
        return ResponseEntity.ok(designers);
    }

    @GetMapping("/{designerId}")
    public ResponseEntity<DesignerResponseDto> getDesignerProfile(@PathVariable Long designerId) {
        DesignerResponseDto designerProfile = designerService.getDesignerProfile(designerId);
        if (designerProfile == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(designerProfile);
    }

    @PostMapping("/{designerId}/promotion")
    public ResponseEntity<Object> createPromotion(
            @PathVariable Long designerId,
            @Valid @RequestBody PromotionInputDto promotionInputDto) {
        try {
            promotionService.createPromotion(designerId, promotionInputDto);
            return ResponseEntity.ok("Promotie succesvol aangemaakt.");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Kon promotie niet aanmaken: " + ex.getMessage());
        }
    }

    @GetMapping("/{designerId}/promotions")
    public ResponseEntity<?> getDesignerPromotions(@PathVariable Long designerId) {
        try {
            List<PromotionResponseDto> promotions = promotionService.getDesignerPromotions(designerId);
            return ResponseEntity.ok(promotions);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Kon promoties niet ophalen: " + ex.getMessage());
        }
    }

    @PutMapping("/{designerId}/promotion/apply")
    public ResponseEntity<Object> applyPromotionToProduct(
            @PathVariable Long designerId,
            @Valid @RequestBody ApplyPromotionRequest requestDto) {
        try {
            Long productId = requestDto.getProductId();
            Long promotionId = requestDto.getPromotionId();
            promotionService.applyPromotionToProduct(productId, designerId, promotionId);
            return ResponseEntity.ok("Promotie succesvol toegepast op product.");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Kon promotie niet toepassen op product: " + ex.getMessage());
        }
    }

    @GetMapping("/{designerId}/sales")
    public ResponseEntity<SalesFiguresDto> getSalesFiguresByDesignerId(@PathVariable Long designerId) {
        SalesFiguresDto salesFigures = designerService.getSalesFiguresByDesignerId(designerId);
        return ResponseEntity.ok(salesFigures);
    }
}
