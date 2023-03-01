package ru.skillbox.blog.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.skillbox.blog.service.FileStorageService;

@Controller
public class DefaultController implements ErrorController {

    private final FileStorageService fileStorageService;

    public DefaultController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

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

    @GetMapping({"/upload/**", "/avatars/**"})
    public ResponseEntity<?> getImage(HttpServletRequest request) {
        Resource file = fileStorageService.getImage(request.getRequestURI());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=\"%s\"", file.getFilename()))
                .body(file);
    }
}
