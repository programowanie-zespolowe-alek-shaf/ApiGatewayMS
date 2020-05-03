package pl.agh.gateway.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@RestController
public class LoginController {

    @RequestMapping(value = "/api/login", method = RequestMethod.GET)
    public ResponseEntity<String> login(Principal principal, HttpSession session) {
        if (!session.isNew()) {
            session.invalidate();
        }
        return ResponseEntity.ok("Logged: " + principal.getName());
    }
}
