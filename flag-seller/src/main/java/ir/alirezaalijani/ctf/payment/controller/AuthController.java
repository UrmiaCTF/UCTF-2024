package ir.alirezaalijani.ctf.payment.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("login")
    public String loginPage(){
        return "login/login";
    }

    @GetMapping("")
    public String index(){
        return "redirect:/home";
    }

}
