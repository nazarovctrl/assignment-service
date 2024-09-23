package uz.ccrew.assignmentservice.payment;

import uz.ccrew.assignmentservice.enums.PaymentType;
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
    public void withdraw(RequisiteAssignment requisite) {
        String paymentId;
        if (requisite.getPaymentType().equals(PaymentType.CARD)) {
            paymentId = withdrawByCard(requisite.getCardNumber(), requisite.getPaymentAmount());
        } else {
            paymentId = withdrawByAccount(requisite.getAccountNumber(), requisite.getPaymentAmount());
        }

        if (paymentId == null) {
            //send notification
            return;
        }

        System.out.println(paymentId);
        //update requisite
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
        } catch (HttpClientErrorException e) {
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
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

}
