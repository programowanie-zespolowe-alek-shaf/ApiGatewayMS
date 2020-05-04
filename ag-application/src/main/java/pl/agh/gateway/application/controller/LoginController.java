package pl.agh.gateway.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class LoginController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<String> login(Principal principal) {
        return ResponseEntity.ok("Logged: " + principal.getName());
    }
}
