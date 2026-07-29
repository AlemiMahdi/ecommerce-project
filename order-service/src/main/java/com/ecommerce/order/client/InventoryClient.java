package com.ecommerce.order.client;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.beans.factory.annotation.Value;
import com.ecommerce.order.dto.InventoryResponse;
import com.ecommerce.order.dto.ReleaseInventoryRequest;
import com.ecommerce.order.dto.ReserveInventoryRequest;
import com.ecommerce.order.exception.InventoryOperationException;
import com.ecommerce.order.exception.InventoryServiceException;

//Ansvara för HTTP-kommunikation mellan order-service och inventory-service
@Component
public class InventoryClient {
    private final RestClient inventoryRestClient;

    /**
     * Skapar en RestClient med inventory-service som base URL.
     *
     * Vi skapar klienten här för att inte orsaka konflikt med den
     * RestClient-bean som redan används för product-service.
     */
    public InventoryClient(
            @Value("${inventory-service.base-url}") String inventoryServiceBaseUrl
    ) {
        this.inventoryRestClient = RestClient.builder()
                .baseUrl(inventoryServiceBaseUrl)
                .build();
    }

    //Reserverar lager för en produkt
    public InventoryResponse reseveInventory(
        Long productId,
        Integer quantity
    ){
        try {
            InventoryResponse response = inventoryRestClient
                    .post()
                    .uri("/api/v1/inventory/reserve")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ReserveInventoryRequest(productId, quantity))
                    .retrieve()
                    .body(InventoryResponse.class);
        if (response == null) {
            throw new InventoryServiceException(
                "Inventory-service returned an empty response"
            );
        }
        return response;
        } catch (RestClientResponseException exception) {
            int status = exception.getStatusCode().value();
            if (status == 400 || status == 404 || status == 409) {
                throw new InventoryOperationException(
                    "Could not resever quantity" + quantity
                    + " for product id" + productId
                );
            }

            throw new InventoryServiceException(
                "Inventory-service retunred status: " + status
            );

        } catch (ResourceAccessException exception){
            throw new InventoryServiceException(
                "Inventory-service is not avaiable"
            );
        }
    }

    //Släppger tillbaka tidigare reserverat lager
    public InventoryResponse releaseInventory(
        Long productId,
        Integer quantity
    ){
        try {
            InventoryResponse response = inventoryRestClient
                .post()
                .uri("/api/v1/inventory/release")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ReleaseInventoryRequest(productId, quantity))
                .retrieve()
                .body(InventoryResponse.class);
            if (response == null) {
                throw new InventoryServiceException(
                    "Inventory-servie returned an empty response"
                );
            }
            return response;
        } catch (RestClientResponseException exception) {
            int status = exception.getStatusCode().value();

            if (status == 400|| status == 404 || status == 409) {
                throw new InventoryOperationException(
                    "Could not release quantity " + quantity
                    + " for product id" + productId
                );
            }
            throw new InventoryServiceException(
                "Inventory-service returned status: " + status
            );
        } catch(ResourceAccessException exception){
            throw new InventoryServiceException(
                "Inventory-service is not avaialble "
            );
        }
    }
}
