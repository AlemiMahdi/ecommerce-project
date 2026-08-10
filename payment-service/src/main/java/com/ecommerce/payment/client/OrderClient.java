package com.ecommerce.payment.client;

import com.ecommerce.payment.dto.OrderDetailsResponse;
import com.ecommerce.payment.exception.OrderNotPayableException;
import com.ecommerce.payment.exception.OrderServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import com.ecommerce.payment.dto.OrderPaymentResultRequest;
import com.ecommerce.payment.entity.PaymentStatus;
import org.springframework.http.MediaType;

//Http-client som används av payment-service för att hämta order-information
@Component
public class OrderClient {

    private final RestClient orderResClient;
    private final String internalServiceKey;

    public OrderClient(
        @Value("${order-service.base-url}") 
        String orderServiceBaseUrl,

        @Value("${internal.service-key}")
        String internalServiceKey
    ){
        this.orderResClient = RestClient.builder()
                .baseUrl(orderServiceBaseUrl)
                .build();
        this.internalServiceKey = internalServiceKey;
    }

    /**
     * Hämtar en specifik order som tillhör användaren.
     *
     * X-User-Id skickas vidare eftersom order-service
     * använder headern för att kontrollera orderns ägare.
     */
    public OrderDetailsResponse getOrderById(
        Long orderId,
        Long userId
    ){
        try {
            OrderDetailsResponse response = orderResClient
                .get()
                .uri("/internal/orders/{orderId}", orderId)
                .header("X-Internal-Service-Key", internalServiceKey)
                .header("X-User-Id", userId.toString())
                .retrieve()
                .body(OrderDetailsResponse.class);
            
            if (response == null) {
                throw new OrderServiceException(
                    "Order-service returned an empty response"
                );
            }
            return response;
        } catch (RestClientResponseException exception) {
            int status = exception.getStatusCode().value();

            /*
             * Order-service returnerar normalt 404 både när:
             * - ordern inte finns
             * - ordern inte tillhör användaren
             */
            if (status == 404) {
                throw new OrderNotPayableException(
                    "Ordet not found or does not belong to the current user"
                );
            }
            throw new OrderServiceException(
                "Order-service returned status: " + status
            );
        } catch(ResourceAccessException exception){
            throw new OrderServiceException(
                "Order-service is not available"
            );
        }
    }

    //Skicakr betalningsresultatet till order-service
    public void sendPaymentResult(
        Long orderId,
        Long userId,
        PaymentStatus paymentStatus
    ){
        try {
            orderResClient.patch()
                    .uri("/internal/orders/{orderId}/payment-result", orderId)
                    .header("X-Internal-Service-Key", internalServiceKey)
                    .header("X-User-Id", userId.toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new OrderPaymentResultRequest(paymentStatus))
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException exception) {
            int status = exception.getStatusCode().value();

            if (status == 400 || status == 404 || status == 409) {
                throw new OrderNotPayableException(
                    "Order could not process the payment result"
                );
            }
            throw new OrderServiceException(
                "Order-service returned status: " + status
            );
        } catch (ResourceAccessException exception){
            throw new OrderServiceException(
                "Order-servie is not available"
            );
        }
    }
}
