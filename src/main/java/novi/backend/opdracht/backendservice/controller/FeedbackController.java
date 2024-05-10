package novi.backend.opdracht.backendservice.controller;

import novi.backend.opdracht.backendservice.dto.input.FeedbackInputDTO;
import novi.backend.opdracht.backendservice.dto.output.FeedbackOutputDTO;
import novi.backend.opdracht.backendservice.service.FeedbackService;

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

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<?> postFeedback(@Valid @RequestBody FeedbackInputDTO feedbackInputDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validatiefouten zijn opgetreden.");
        }

        FeedbackOutputDTO createdFeedback = feedbackService.postFeedback(feedbackInputDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{feedbackId}")
                .buildAndExpand(createdFeedback.getFeedbackId())
                .toUri();
        return ResponseEntity.created(location).body(createdFeedback);
    }
}
