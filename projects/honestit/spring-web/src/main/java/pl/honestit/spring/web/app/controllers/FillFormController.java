package pl.honestit.spring.web.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class FillFormController {

    @PostMapping("/fill-form")
    public String post() {
        return "FillFormController.post";
    }
}
