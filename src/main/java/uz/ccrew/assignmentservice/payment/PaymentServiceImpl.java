package uz.ccrew.assignmentservice.payment;

import uz.ccrew.assignmentservice.enums.PaymentType;
import uz.ccrew.assignmentservice.exp.BadRequestException;
import uz.ccrew.assignmentservice.entity.RequisiteAssignment;

import com.google.gson.Gson;
import org.springframework.http.*;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpClientErrorException;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    @Value("${payment.service.url}")
    private String paymentServiceUrl;
    @Value("${payment.username}")
    private String paymentServiceUsername;
    @Value("${payment.password}")
    private String paymentServicePassword;
    private final Gson gson = new Gson();
    private final RestTemplate restTemplate;

    @Override
    public String withdraw(RequisiteAssignment requisite) {
        if (requisite.getPaymentType().equals(PaymentType.CARD)) {
            return withdrawByCard(requisite.getCardNumber(), requisite.getPaymentAmount());
        } else {
            return withdrawByAccount(requisite.getAccountNumber(), requisite.getPaymentAmount());
        }
    }

    @Override
    public void reverse(String paymentId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBasicAuth(paymentServiceUsername, paymentServicePassword);

            String requestUrl = String.format("%s/api/v1/payment/reverse{%s}", paymentServiceUrl, paymentId);

            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.POST, request, String.class);
        } catch (HttpClientErrorException e) {
            throw new BadRequestException("Payment reverse exception");
        }
    }

    private String withdrawByCard(String cardNumber, Long paymentAmount) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("cardNumber", cardNumber);
            json.addProperty("amount", paymentAmount);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBasicAuth(paymentServiceUsername, paymentServicePassword);

            HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);

            ResponseEntity<String> response = restTemplate.exchange(paymentServiceUrl + "/api/v1/payment/withdraw/card", HttpMethod.POST, request, String.class);
            JsonObject jsonObject = gson.fromJson(response.getBody(), JsonObject.class);

            return jsonObject.get("data").getAsJsonObject().get("paymentId").getAsString();
        } catch (Exception e) {
            return null;
        }
    }

    private String withdrawByAccount(String accountNumber, Long paymentAmount) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("accountNumber", accountNumber);
            json.addProperty("amount", paymentAmount);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBasicAuth(paymentServiceUsername, paymentServicePassword);

            HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);

            ResponseEntity<String> response = restTemplate.exchange(paymentServiceUrl + "/api/v1/payment/withdraw/account", HttpMethod.POST, request, String.class);
            JsonObject jsonObject = gson.fromJson(response.getBody(), JsonObject.class);

            return jsonObject.get("data").getAsJsonObject().get("paymentId").getAsString();
        } catch (Exception e) {
            return null;
        }
    }
}
