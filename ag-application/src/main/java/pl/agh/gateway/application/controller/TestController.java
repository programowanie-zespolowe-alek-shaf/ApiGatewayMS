package pl.agh.gateway.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class TestController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<String> currentUser(Principal principal) {
        return ResponseEntity.ok("Logged: " + principal.getName());
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Integer> healthCheck() {
        return ResponseEntity.ok(1);
    }
}
