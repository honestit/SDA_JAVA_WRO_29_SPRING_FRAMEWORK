package pl.honestit.spring.web.app.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/dupa")
public class DupaController {

    @Value("#{systemProperties['user.name']}")
    private String username;

    @GetMapping
    @ResponseBody
    public String sayHello() {
        return username + " jesteś dupa";
    }
}
