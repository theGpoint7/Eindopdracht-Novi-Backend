package novi.backend.opdracht.backendservice.service;

import novi.backend.opdracht.backendservice.dto.input.SalesFiguresDto;
import novi.backend.opdracht.backendservice.dto.output.DesignerResponseDto;
import novi.backend.opdracht.backendservice.dto.output.FeedbackOutputDTO;
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
    private final OrderService orderService;


    public DesignerService(DesignerRepository designerRepository, OrderRepository orderRepository, OrderService orderService) {
        this.designerRepository = designerRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    public List<DesignerResponseDto> getAllDesigners() {
        List<Designer> designers = designerRepository.findAll();
        return designers.stream()
                .map(this::mapToDesignerResponseDto)
                .collect(Collectors.toList());
    }

    public DesignerResponseDto getDesignerProfile(Long designerId) {
        Designer designer = designerRepository.findById(designerId)
                .orElseThrow(() -> new RuntimeException("Designer not found"));
        return mapToDesignerResponseDto(designer);
    }

    public SalesFiguresDto getSalesFiguresByDesignerId(Long designerId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Designer designer = designerRepository.findById(designerId)
                .orElseThrow(() -> new ResourceNotFoundException("Designer not found"));
        if (!designer.getUser().getUsername().equals(username)) {
            throw new AuthenticationException("You are not authorized to view sales figures for this designer");
        }
        double totalSales = getTotalSalesByDesignerId(designerId);
        double totalPromotions = getTotalPromotionsByDesignerId(designerId);
        double netSales = totalSales - totalPromotions;

        SalesFiguresDto salesFiguresDto = new SalesFiguresDto();
        salesFiguresDto.setTotalSales(netSales);

        return salesFiguresDto;
    }



    public SalesFiguresDto getSalesFigures(Long designerId) {
        double totalSales = getTotalSalesByDesignerId(designerId);
        double totalPromotions = getTotalPromotionsByDesignerId(designerId);
        double netSales = totalSales - totalPromotions;

        SalesFiguresDto salesFiguresDto = new SalesFiguresDto();
        salesFiguresDto.setTotalSales(netSales);

        return salesFiguresDto;
    }


    public double getTotalSalesByDesignerId(Long designerId) {
        return orderService.getTotalSalesByDesignerId(designerId);
    }

    private double getTotalPromotionsByDesignerId(Long designerId) {
        List<Order> orders = orderRepository.findAllByUserDesignerDesignerId(designerId);
        if (orders.isEmpty()) {
            System.out.println("No orders found for designer ID: " + designerId);
            return 0.0;
        }
        return orders.stream()
                .peek(order -> System.out.println("Order ID: " + order.getOrderId() + " has " + order.getOrderLines().size() + " lines."))
                .mapToDouble(this::calculatePromotions)
                .peek(totalDiscount -> System.out.println("Total discount calculated: " + totalDiscount))
                .sum();
    }

    private double calculatePromotions(Order order) {
        double totalDiscount = 0.0;
        for (OrderLine orderLine : order.getOrderLines()) {
            AbstractProduct product = orderLine.getProduct();
            if (product != null && product.getPromotion() != null) {
                double promotionPercentage = product.getPromotion().getPromotionPercentage();
                double discountAmount = (orderLine.getPrice() * orderLine.getQuantity() * promotionPercentage) / 100.0;
                totalDiscount += discountAmount;
                System.out.println("Calculated discount for order line: " + discountAmount);
            }
        }
        return totalDiscount;
    }



    private DesignerResponseDto mapToDesignerResponseDto(Designer designer) {
        DesignerResponseDto dto = new DesignerResponseDto();
        dto.setDesignerId(designer.getDesignerId());
        dto.setStoreName(designer.getStoreName());
        dto.setBio(designer.getBio());
        List<FeedbackOutputDTO> feedbacks = designer.getFeedbacks().stream()
                .map(this::mapFeedbackToFeedbackOutputDTO)
                .collect(Collectors.toList());
        dto.setFeedbacks(feedbacks);

        return dto;
    }

    private FeedbackOutputDTO mapFeedbackToFeedbackOutputDTO(Feedback feedback) {
        FeedbackOutputDTO feedbackOutputDTO = new FeedbackOutputDTO();
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