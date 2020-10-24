package pl.honestit.spring.web.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@ResponseBody
@RequestMapping("/paths/user")
public class PathParamsController {

    @GetMapping("/raw/30/delete}")
    public String raw(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String[] segments = requestURI.substring(requestURI.indexOf("/paths/user")).split("/");

        String id = null;
        String operation = null;

        if (segments.length == 5) {
            id = segments[4];
            operation = null;
        } else if (segments.length == 6) {
            id = segments[4];
            operation = segments[5];
        }

        if (id == null && operation == null) {
            return "Brak wartości w ścieżce";
        } else if (operation == null) {
            return "Id=" + id;
        } else if (id == null) {
            return "Operation=" + operation;
        } else {
            return "Id=" + id + " i Operation=" + operation;
        }
    }

    @GetMapping("/spring/{id}/{operation}")
    public String spring(@PathVariable("id") Long id,
                         @PathVariable String operation) {
        return "Id=" + id + " i Operation=" + operation;
    }
}
