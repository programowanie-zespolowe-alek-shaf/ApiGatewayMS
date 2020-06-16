package pl.agh.gateway.application.service;

import lombok.AllArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;
import pl.agh.gateway.application.rest.MicroService;
import pl.agh.gateway.application.rest.RestClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@AllArgsConstructor
public class OrderService {


    private final RestClient restClient;


    public Map<String, Object> createOrder(Long shoppingCardId, LocalDate shipDate, String address) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("address", address);
        jsonObject.put("shoppingCardId", shoppingCardId);
        jsonObject.put("shipDate", shipDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return restClient.post(MicroService.ORDER_MS, "/orders/", jsonObject, Map.class);
    }

    public Map<String, Object> createTransaction(Long shoppingCardID, Float amount, String couponCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("shoppingCardID", shoppingCardID);
        jsonObject.put("amount", amount);
        jsonObject.put("couponCode", couponCode);
        return restClient.post(MicroService.PAYMENT_MS, "/transaction/", jsonObject, Map.class);

    }

    public Map<String, Object> createShoppingCard(String username) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        return restClient.post(MicroService.CART_MS, "/shoppingCards/", jsonObject, Map.class);
    }
}
