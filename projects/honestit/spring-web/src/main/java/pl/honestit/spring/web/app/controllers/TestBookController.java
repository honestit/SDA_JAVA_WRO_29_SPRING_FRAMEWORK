package pl.honestit.spring.web.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/test-book")
public class TestBookController {

    @GetMapping
    public String get() {
        return "TestUserController.get";
    }

    @PostMapping("/add")
    public String put() {
        return "TestUserController.put";
    }

    @PostMapping("/edit")
    public String post() {
        return "TestUserController.post";
    }

    @PostMapping("/delete")
    public String delete() {
        return "TestUserController.delete";
    }
}
