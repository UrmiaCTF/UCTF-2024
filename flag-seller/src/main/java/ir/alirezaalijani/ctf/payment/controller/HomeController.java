package ir.alirezaalijani.ctf.payment.controller;

import ir.alirezaalijani.ctf.payment.models.Order;
import ir.alirezaalijani.ctf.payment.repository.OrderPaymentRepository;
import ir.alirezaalijani.ctf.payment.service.TeamCreditCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final TeamCreditCardService teamCreditCardService;
    private final OrderPaymentRepository orderPaymentRepository;

    @GetMapping("/home")
    public String showHomePage(Model model, Principal principal){
        log.debug("generate home page for team {}",principal.getName());
        var teamCredit= teamCreditCardService.getById(Integer.parseInt(principal.getName()));
        teamCredit.setCardNumber(formatCardNumber(teamCredit.getCardNumber()));
        model.addAttribute("credit",teamCredit);
        List<Order> orders=orderPaymentRepository.findByIdentityId(Integer.parseInt(principal.getName()));
        model.addAttribute("orders",orders);
        return "home";
    }

    @GetMapping("pay/docs")
    public String payDoc(){
        return "pay-blu-doc";
    }

    public static String formatCardNumber(String cardNumber) {
        StringBuilder formattedNumber = new StringBuilder();
        for (int i = 0; i < cardNumber.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                formattedNumber.append("  ");
            }
            formattedNumber.append(cardNumber.charAt(i));
        }
        return formattedNumber.toString();
    }
}
