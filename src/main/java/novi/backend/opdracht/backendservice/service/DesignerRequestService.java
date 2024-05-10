package novi.backend.opdracht.backendservice.service;

import novi.backend.opdracht.backendservice.dto.input.DesignerInfoInputDto;
import novi.backend.opdracht.backendservice.dto.input.DesignerRequestDto;
import novi.backend.opdracht.backendservice.dto.output.DesignerInfoOutputDto;
import novi.backend.opdracht.backendservice.dto.output.DesignerRequestResponseDto;
import novi.backend.opdracht.backendservice.exception.BadRequestException;
import novi.backend.opdracht.backendservice.model.*;
import novi.backend.opdracht.backendservice.repository.DesignerRepository;
import novi.backend.opdracht.backendservice.repository.DesignerRequestRepository;
import novi.backend.opdracht.backendservice.repository.UserRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DesignerRequestService {
    private final DesignerRequestRepository designerRequestRepository;
    private final DesignerRepository designerRepository;
    private final UserRepository userRepository;

    public DesignerRequestService(DesignerRequestRepository designerRequestRepository,
                                  UserRepository userRepository,
                                  DesignerRepository designerRepository) {
        this.designerRequestRepository = designerRequestRepository;
        this.userRepository = userRepository;
        this.designerRepository = designerRepository;
    }

    public Long submitDesignerRequest(DesignerRequestDto designerRequestDTO) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        DesignerRequest newRequest = new DesignerRequest();
        newRequest.setUser(user);
        newRequest.setKvkNumber(designerRequestDTO.getKvkNumber());
        newRequest.setRequestDateTime(java.time.LocalDateTime.now());
        newRequest.setStatus(DesignerRequestStatus.PENDING);
        designerRequestRepository.save(newRequest);
        return newRequest.getRequestId();
    }

    public List<DesignerRequestResponseDto> getDesignerRequestsByUsername(String username) {
        List<DesignerRequest> requests = designerRequestRepository.findByUserUsername(username);
        return requests.stream()
                .map(this::fromDesignerRequest)
                .collect(Collectors.toList());
    }

    public void updateDesignerInfo(String username, DesignerInfoInputDto designerInfo) {
        Designer designer = designerRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Designer not found"));

        designer.setStoreName(designerInfo.getStoreName());
        designer.setBio(designerInfo.getBio());
        designerRepository.save(designer);
    }

    public DesignerInfoOutputDto getDesignerInfo(String username) {
        Designer designer = designerRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Designer not found"));

        DesignerInfoOutputDto outputDto = new DesignerInfoOutputDto();
        outputDto.setStoreName(designer.getStoreName());
        outputDto.setBio(designer.getBio());
        return outputDto;
    }

    public Long findDesignerIdByUsername(String username) {
        Designer designer = designerRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Designer not found for username: " + username));
        return designer.getDesignerId();
    }

    public List<DesignerRequestResponseDto> getAllDesignerRequests() {
        return designerRequestRepository.findAll().stream()
                .map(this::fromDesignerRequest)
                .collect(Collectors.toList());
    }

    public void processDesignerRequest(Long requestId, boolean approve, String rejectionReason) {
        DesignerRequest request = designerRequestRepository.findById(requestId)
                .orElseThrow(() -> new UsernameNotFoundException("Designer request not found"));

        if (approve) {
            request.setStatus(DesignerRequestStatus.APPROVED);
            request.setApprovalDateTime(java.time.LocalDateTime.now());
            addAuthority(request.getUser().getUsername(), Role.ROLE_DESIGNER);

            Designer existingDesigner = designerRepository.findByUserUsername(request.getUser().getUsername()).orElse(null);
            if (existingDesigner == null) {
                Designer newDesigner = new Designer();
                newDesigner.setUser(request.getUser());
                newDesigner.setStoreName("Default Store Name");
                newDesigner.setBio("Default bio");
                designerRepository.save(newDesigner);
            }
        } else {
            request.setStatus(DesignerRequestStatus.DENIED);
            request.setRejectionDateTime(java.time.LocalDateTime.now());
            request.setRejectionReason(rejectionReason);
            removeAuthority(request.getUser().getUsername(), Role.ROLE_DESIGNER);
        }
        designerRequestRepository.save(request);
    }

    public void addAuthority(String username, Role role) {
        if (!userRepository.existsById(username)) {
            throw new UsernameNotFoundException(username);
        }

        try {
            User user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));
            user.addAuthority(new Authority(username, role));
            userRepository.save(user);
        } catch (Exception ex) {
            throw new BadRequestException("Error adding authority for user: " + username);
        }
    }

    public void removeAuthority(String username, Role role) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        Optional<Authority> authorityToRemove = user.getAuthorities()
                .stream()
                .filter(a -> a.getAuthority() == role)
                .findAny();
        authorityToRemove.ifPresent(user::removeAuthority);
        userRepository.save(user);
    }

    private DesignerRequestResponseDto fromDesignerRequest(DesignerRequest request) {
        DesignerRequestResponseDto response = new DesignerRequestResponseDto();
        response.setRequestId(request.getRequestId());
        response.setUsername(request.getUser().getUsername());
        response.setKvkNumber(request.getKvkNumber());
        response.setStatus(request.getStatus().toString());
        response.setRequestDateTime(request.getRequestDateTime());
        response.setApprovalDateTime(request.getApprovalDateTime());
        response.setRejectionDateTime(request.getRejectionDateTime());
        response.setRejectionReason(request.getRejectionReason());
        return response;
    }
}
