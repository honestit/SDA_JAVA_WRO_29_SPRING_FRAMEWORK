package pl.honestit.spring.web.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@ResponseBody
@RequestMapping("/users/test/{id}")
public class TestUserController {

    @GetMapping
    public String get() {
        return "TestUserController.get";
    }

    @PostMapping
    public String post() {
        return "TestUserController.post";
    }

    @PutMapping
    public String put() {
        return "TestUserController.put";
    }

    @DeleteMapping
    public String delete() {
        return "TestUserController.delete";
    }
}

