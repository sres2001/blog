package ru.skillbox.blog.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class DefaultController implements ErrorController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("error")
    public String error(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            if (Integer.parseInt(status.toString()) == HttpStatus.NOT_FOUND.value()) {
                return "index";
            }
        }
        return "error";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
