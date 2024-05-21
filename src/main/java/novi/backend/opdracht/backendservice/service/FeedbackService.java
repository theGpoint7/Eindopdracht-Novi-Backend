package novi.backend.opdracht.backendservice.service;

import novi.backend.opdracht.backendservice.dto.input.FeedbackInputDto;
import novi.backend.opdracht.backendservice.dto.output.FeedbackOutputDto;
import novi.backend.opdracht.backendservice.exception.BadRequestException;
import novi.backend.opdracht.backendservice.model.*;
import novi.backend.opdracht.backendservice.repository.DesignerRepository;
import novi.backend.opdracht.backendservice.repository.FeedbackRepository;
import novi.backend.opdracht.backendservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ProductRepository productRepository;
    private final DesignerRepository designerRepository;
    private final AuthenticationService authenticationService;

    public FeedbackService(FeedbackRepository feedbackRepository, ProductRepository productRepository, DesignerRepository designerRepository, AuthenticationService authenticationService) {
        this.feedbackRepository = feedbackRepository;
        this.productRepository = productRepository;
        this.designerRepository = designerRepository;
        this.authenticationService = authenticationService;
    }

    public FeedbackOutputDto postFeedback(FeedbackInputDto feedbackInputDTO) {
        User user = authenticationService.getCurrentUser();
        Feedback feedback = new Feedback();
        feedback.validateFeedbackInput(feedbackInputDTO, productRepository, designerRepository);

        feedback.setUser(user);

        if (feedbackInputDTO.getProductId() != null) {
            AbstractProduct product = findProductById(feedbackInputDTO.getProductId());
            feedback.setProduct(product);
        } else if (feedbackInputDTO.getDesignerId() != null) {
            Designer designer = findDesignerById(feedbackInputDTO.getDesignerId());
            feedback.setDesigner(designer);
        }

        feedback.setContent(feedbackInputDTO.getContent());
        feedback.setFeedbackDateTime(LocalDateTime.now());

        Feedback savedFeedback = feedbackRepository.save(feedback);
        return mapToFeedbackOutputDTO(savedFeedback);
    }

    public FeedbackOutputDto getFeedbackById(Long feedbackId) {
        Feedback feedback = findFeedbackById(feedbackId);
        return mapToFeedbackOutputDTO(feedback);
    }

    public List<FeedbackOutputDto> getAllFeedback() {
        List<Feedback> feedbackList = feedbackRepository.findAll();
        return feedbackList.stream()
                .map(this::mapToFeedbackOutputDTO)
                .collect(Collectors.toList());
    }

    private AbstractProduct findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException("Product niet gevonden"));
    }

    private Designer findDesignerById(Long designerId) {
        return designerRepository.findById(designerId)
                .orElseThrow(() -> new BadRequestException("Ontwerper niet gevonden"));
    }

    private Feedback findFeedbackById(Long feedbackId) {
        return feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new BadRequestException("Feedback niet gevonden"));
    }

    private FeedbackOutputDto mapToFeedbackOutputDTO(Feedback feedback) {
        FeedbackOutputDto outputDTO = new FeedbackOutputDto();
        outputDTO.setFeedbackId(feedback.getFeedbackId());
        outputDTO.setUsername(feedback.getUser().getUsername());

        if (feedback.getProduct() != null) {
            outputDTO.setProductId(feedback.getProduct().getProductId());
        } else if (feedback.getDesigner() != null) {
            outputDTO.setDesignerId(feedback.getDesigner().getDesignerId());
        }

        outputDTO.setContent(feedback.getContent());
        outputDTO.setFeedbackDateTime(feedback.getFeedbackDateTime());
        return outputDTO;
    }

    public List<FeedbackOutputDto> getFeedbacksByProductId(Long productId) {
        List<Feedback> feedbacks = feedbackRepository.findByProductProductId(productId);
        return feedbacks.stream()
                .map(this::mapToFeedbackOutputDTO)
                .collect(Collectors.toList());
    }

    public List<FeedbackOutputDto> getFeedbacksByDesignerId(Long designerId) {
        Designer designer = findDesignerById(designerId);
        List<Feedback> feedbacks = designer.getFeedbacks();
        return feedbacks.stream()
                .map(this::mapToFeedbackOutputDTO)
                .collect(Collectors.toList());
    }
}
