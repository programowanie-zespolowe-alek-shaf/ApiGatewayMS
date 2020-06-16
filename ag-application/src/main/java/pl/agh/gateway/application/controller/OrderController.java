package pl.agh.gateway.application.controller;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.agh.gateway.application.dto.RequestDTO;
import pl.agh.gateway.application.rest.MicroService;
import pl.agh.gateway.application.rest.RestClient;
import pl.agh.gateway.application.service.OrderService;
import pl.agh.gateway.application.util.ListResponse;

import javax.validation.Valid;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequiredArgsConstructor
@RequestMapping("/finalizeOrder")
public class OrderController {

    private final RestClient restClient;
    private final OrderService orderService;


    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> completeOrder(@RequestBody @Valid RequestDTO requestDTO) {
        Map<String, Object> currentCard = restClient.get(MicroService.CART_MS, "/shoppingCards/" + requestDTO.getShoppingCardID(), Map.class);
        String username = (String) currentCard.getOrDefault("username", "AnonymousUser");
        if ("AnonymousUser".equals(username)) {
            return ResponseEntity.notFound().build();
        }

        //get total value from currentCard and create Transaction
        Map itemList = (LinkedHashMap) currentCard.getOrDefault("items", null);
        double totalValue = (double) (itemList.getOrDefault("totalValue", -1.));
        orderService.createTransaction(requestDTO.getShoppingCardID(),
                (float) totalValue,
                requestDTO.getCouponCode()
        );
        //Create a new Order
        orderService.createOrder(requestDTO.getShoppingCardID(),
                requestDTO.getShipDate(),
                requestDTO.getAddress()
        );
        //create new shopping cart
        Map<String, Object> newShoppingCard = orderService.createShoppingCard(username);

        //get user information based on username to update user record
        Map<String, Object> user = restClient.get(MicroService.CUSTOMER_MS, "/users/" + username, Map.class);

        //update user record with new shopping card id
        JSONObject jsonObject = new JSONObject()
                .appendField("firstName", user.getOrDefault("firstName", ""))
                .appendField("lastName", user.getOrDefault("lastName", ""))
                .appendField("email", user.getOrDefault("email", ""))
                .appendField("phone", user.getOrDefault("phone", ""))
                .appendField("address", user.getOrDefault("address", ""))
                .appendField("enabled", user.getOrDefault("enabled", ""))
                .appendField("lastShoppingCardId", newShoppingCard.getOrDefault("id", ""));
        restClient.put(MicroService.CUSTOMER_MS, "/users/", jsonObject,  username);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path(MicroService.CART_MS + "/shopping-card-ms/{id}")
                .buildAndExpand(newShoppingCard.getOrDefault("id", ""))
                .toUri();
        return ResponseEntity.created(uri).build();
    }
}
