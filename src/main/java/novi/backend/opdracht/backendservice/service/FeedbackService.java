package novi.backend.opdracht.backendservice.service;

import novi.backend.opdracht.backendservice.dto.input.FeedbackInputDTO;
import novi.backend.opdracht.backendservice.dto.output.FeedbackOutputDTO;
import novi.backend.opdracht.backendservice.exception.BadRequestException;
import novi.backend.opdracht.backendservice.model.*;
import novi.backend.opdracht.backendservice.repository.DesignerRepository;
import novi.backend.opdracht.backendservice.repository.FeedbackRepository;
import novi.backend.opdracht.backendservice.repository.ProductRepository;
import novi.backend.opdracht.backendservice.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final DesignerRepository designerRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, ProductRepository productRepository, UserRepository userRepository, DesignerRepository designerRepository) {
        this.feedbackRepository = feedbackRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.designerRepository = designerRepository;
    }

    public FeedbackOutputDTO postFeedback(FeedbackInputDTO feedbackInputDTO) {
        validateFeedbackInput(feedbackInputDTO);

        User user = getCurrentUser();
        Feedback feedback = createFeedback(feedbackInputDTO, user);
        Feedback savedFeedback = feedbackRepository.save(feedback);

        return mapToFeedbackOutputDTO(savedFeedback);
    }

    public FeedbackOutputDTO getFeedbackById(Long feedbackId) {
        Feedback feedback = findFeedbackById(feedbackId);
        return mapToFeedbackOutputDTO(feedback);
    }

    public List<FeedbackOutputDTO> getAllFeedback() {
        List<Feedback> feedbackList = feedbackRepository.findAll();
        return feedbackList.stream()
                .map(this::mapToFeedbackOutputDTO)
                .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User not found"));
    }

    private Feedback createFeedback(FeedbackInputDTO feedbackInputDTO, User user) {
        Feedback feedback = new Feedback();
        feedback.setUser(user);

        if (feedbackInputDTO.getProductId() != null) {
            AbstractProduct product = findProductById(feedbackInputDTO.getProductId());
            feedback.setProduct(product);
        } else if (feedbackInputDTO.getDesignerId() != null) {
            Designer designer = findDesignerById(feedbackInputDTO.getDesignerId());
            feedback.setDesigner(designer);
        } else {
            throw new BadRequestException("Either productId or designerId must be provided");
        }

        feedback.setContent(feedbackInputDTO.getContent());
        feedback.setFeedbackDateTime(LocalDateTime.now());
        return feedback;
    }

    private AbstractProduct findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException("Product not found"));
    }

    private Designer findDesignerById(Long designerId) {
        return designerRepository.findById(designerId)
                .orElseThrow(() -> new BadRequestException("Designer not found"));
    }

    private Feedback findFeedbackById(Long feedbackId) {
        return feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new BadRequestException("Feedback not found"));
    }

    private FeedbackOutputDTO mapToFeedbackOutputDTO(Feedback feedback) {
        FeedbackOutputDTO outputDTO = new FeedbackOutputDTO();
        outputDTO.setFeedbackId(feedback.getFeedbackId());
        outputDTO.setUsername(feedback.getUser().getUsername());

        if (feedback.getProduct() != null) {
            outputDTO.setProductId(feedback.getProduct().getProductId());
            outputDTO.setFeedbackDateTime(feedback.getFeedbackDateTime());
        } else if (feedback.getDesigner() != null) {
            outputDTO.setDesignerId(feedback.getDesigner().getDesignerId());
        }

        outputDTO.setContent(feedback.getContent());
        return outputDTO;
    }

    public List<FeedbackOutputDTO> getFeedbacksByProductId(Long productId) {
        List<Feedback> feedbacks = feedbackRepository.findByProductProductId(productId);
        return feedbacks.stream()
                .map(this::mapToFeedbackOutputDTO)
                .collect(Collectors.toList());
    }

    public List<FeedbackOutputDTO> getFeedbacksByDesignerId(Long designerId) {
        Designer designer = findDesignerById(designerId);
        List<Feedback> feedbacks = designer.getFeedbacks();
        return feedbacks.stream()
                .map(this::mapToFeedbackOutputDTO)
                .collect(Collectors.toList());
    }

    private void validateFeedbackInput(FeedbackInputDTO feedbackInputDTO) {
        if (feedbackInputDTO.getProductId() == null && feedbackInputDTO.getDesignerId() == null) {
            throw new BadRequestException("Either productId or designerId must be provided");
        }

        if (feedbackInputDTO.getProductId() != null) {
            if (!productRepository.existsById(feedbackInputDTO.getProductId())) {
                throw new BadRequestException("Product with ID " + feedbackInputDTO.getProductId() + " not found");
            }
        }

        if (feedbackInputDTO.getDesignerId() != null) {
            if (!designerRepository.existsById(feedbackInputDTO.getDesignerId())) {
                throw new BadRequestException("Designer with ID " + feedbackInputDTO.getDesignerId() + " not found");
            }
        }
    }
}
