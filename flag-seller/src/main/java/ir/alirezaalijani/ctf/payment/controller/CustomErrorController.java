package ir.alirezaalijani.ctf.payment.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;

@ControllerAdvice
@Controller
public class CustomErrorController implements ErrorController {

    private static final String PATH = "/error";

    @GetMapping(PATH)
    public String handleError(HttpServletResponse response) {
        int statusCode = response.getStatus();
        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            return "error/error-404";
        } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
            return "error/error-403";
        } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
            return "error/error-401";
        } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
            return "error/error-400";
        } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return "error/error-500";
        } else if (statusCode == HttpStatus.TOO_MANY_REQUESTS.value()) {
            return "error/error-429";
        }
        return "error/error";
    }
}
