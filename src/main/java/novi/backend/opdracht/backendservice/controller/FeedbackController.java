package novi.backend.opdracht.backendservice.controller;

import novi.backend.opdracht.backendservice.dto.input.FeedbackInputDto;
import novi.backend.opdracht.backendservice.dto.output.FeedbackOutputDto;
import novi.backend.opdracht.backendservice.service.FeedbackService;
import novi.backend.opdracht.backendservice.service.ValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;
import java.net.URI;

@CrossOrigin
@RestController
@RequestMapping(value = "/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final ValidationService validationService;

    public FeedbackController(FeedbackService feedbackService, ValidationService validationService) {
        this.feedbackService = feedbackService;
        this.validationService = validationService;
    }

    @PostMapping
    public ResponseEntity<?> postFeedback(@Valid @RequestBody FeedbackInputDto feedbackInputDTO, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = validationService.formatFieldErrors(result);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        FeedbackOutputDto createdFeedback = feedbackService.postFeedback(feedbackInputDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{feedbackId}")
                .buildAndExpand(createdFeedback.getFeedbackId())
                .toUri();
        return ResponseEntity.created(location).body(createdFeedback);
    }
}
