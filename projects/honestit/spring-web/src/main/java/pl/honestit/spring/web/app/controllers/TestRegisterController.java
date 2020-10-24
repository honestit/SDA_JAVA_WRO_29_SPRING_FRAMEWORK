package pl.honestit.spring.web.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/test-register")
public class TestRegisterController {

    @GetMapping
    public String get() {
        return "TestRegisterController.get";
    }

    @PostMapping
    public String post() {
        return "TestRegisterController.post";
    }
}
