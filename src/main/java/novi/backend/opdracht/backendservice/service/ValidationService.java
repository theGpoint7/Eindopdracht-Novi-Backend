package novi.backend.opdracht.backendservice.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class ValidationService {

    public String formatFieldErrors(BindingResult result) {
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fe : result.getFieldErrors()) {
            stringBuilder.append(fe.getField()).append(": ");
            stringBuilder.append(fe.getDefaultMessage());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
