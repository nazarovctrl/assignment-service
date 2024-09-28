package uz.ccrew.assignmentservice.payment;

import uz.ccrew.assignmentservice.dto.Response;
import uz.ccrew.assignmentservice.dto.ResponseMaker;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
@Tag(name = "Payment Controller", description = "Payment API")
public class PaymentController {

    @GetMapping("/types")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Get available payment types")
    public ResponseEntity<Response<List<String>>> getPaymentTypes() {
        return ResponseMaker.ok(List.of("account", "card"));
    }
}
