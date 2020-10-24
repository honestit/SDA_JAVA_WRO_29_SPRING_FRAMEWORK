package pl.honestit.spring.web.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@ResponseBody
@RequestMapping("/parameters")
public class ParametersController {

    @GetMapping("/raw")
    public String serveRawParameters(HttpServletRequest request) {
        String first = request.getParameter("first");
        String second = request.getParameter("second");

        if (first == null && second == null) {
            return "Brak parametrów";
        } else if (first == null) {
            return "Second=" + second;
        } else if (second == null) {
            return "First=" + first;
        } else {
            return "First=" + first + " i Second=" + second;
        }
    }

    @GetMapping("/spring")
    public String serveSpringParameters(@RequestParam String first, @RequestParam String second) {
        return "First=" + first + " i Second=" + second;
    }
}
