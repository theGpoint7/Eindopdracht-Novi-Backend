package novi.backend.opdracht.backendservice.service;

import novi.backend.opdracht.backendservice.dto.output.SalesFiguresDto;
import novi.backend.opdracht.backendservice.dto.output.DesignerResponseDto;
import novi.backend.opdracht.backendservice.dto.output.FeedbackOutputDto;
import novi.backend.opdracht.backendservice.exception.AuthenticationException;
import novi.backend.opdracht.backendservice.exception.ResourceNotFoundException;
import novi.backend.opdracht.backendservice.model.*;
import novi.backend.opdracht.backendservice.repository.DesignerRepository;
import novi.backend.opdracht.backendservice.repository.OrderRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DesignerService {

    private final DesignerRepository designerRepository;
    private final OrderRepository orderRepository;

    public DesignerService(DesignerRepository designerRepository, OrderRepository orderRepository) {
        this.designerRepository = designerRepository;
        this.orderRepository = orderRepository;
    }

    public List<DesignerResponseDto> getAllDesigners() {
        List<Designer> designers = designerRepository.findAll();
        return designers.stream()
                .map(this::mapToDesignerResponseDto)
                .collect(Collectors.toList());
    }

    public DesignerResponseDto getDesignerProfile(Long designerId) {
        Designer designer = designerRepository.findById(designerId)
                .orElseThrow(() -> new ResourceNotFoundException("Designer niet gevonden."));
        return mapToDesignerResponseDto(designer);
    }

    public SalesFiguresDto getSalesFiguresByDesignerId(Long designerId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Designer designer = designerRepository.findById(designerId)
                .orElseThrow(() -> new ResourceNotFoundException("Designer niet gevonden."));
        if (!designer.getUser().getUsername().equals(username)) {
            throw new AuthenticationException("U bent niet gemachtigd om de verkoopcijfers van deze designer te bekijken.");
        }

        List<Order> orders = orderRepository.findAllByDesignerId(designerId);
        double netSales = designer.calculateTotalSales(orders);

        SalesFiguresDto salesFiguresDto = new SalesFiguresDto();
        salesFiguresDto.setTotalSales(netSales);
        salesFiguresDto.setTotalOrders(orders.size());

        return salesFiguresDto;
    }

    private DesignerResponseDto mapToDesignerResponseDto(Designer designer) {
        DesignerResponseDto dto = new DesignerResponseDto();
        dto.setDesignerId(designer.getDesignerId());
        dto.setStoreName(designer.getStoreName());
        dto.setBio(designer.getBio());
        List<FeedbackOutputDto> feedbacks = designer.getFeedbacks().stream()
                .map(this::mapFeedbackToFeedbackOutputDTO)
                .collect(Collectors.toList());
        dto.setFeedbacks(feedbacks);

        return dto;
    }

    private FeedbackOutputDto mapFeedbackToFeedbackOutputDTO(Feedback feedback) {
        FeedbackOutputDto feedbackOutputDTO = new FeedbackOutputDto();
        feedbackOutputDTO.setFeedbackId(feedback.getFeedbackId());
        feedbackOutputDTO.setUsername(feedback.getUser().getUsername());
        if (feedback.getProduct() != null) {
            feedbackOutputDTO.setProductId(feedback.getProduct().getProductId());
        }
        if (feedback.getDesigner() != null) {
            feedbackOutputDTO.setDesignerId(feedback.getDesigner().getDesignerId());
        }
        feedbackOutputDTO.setContent(feedback.getContent());
        feedbackOutputDTO.setFeedbackDateTime(feedback.getFeedbackDateTime());
        return feedbackOutputDTO;
    }
}
