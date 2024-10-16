package ir.alirezaalijani.ctf.payment.controller;

import ir.alirezaalijani.ctf.payment.controller.vm.PaymentClientVM;
import ir.alirezaalijani.ctf.payment.models.PaymentClient;
import ir.alirezaalijani.ctf.payment.repository.PaymentClientRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PaymentClientController {

    private final PaymentClientRepository paymentClientRepository;


    @GetMapping("blu-pay/client")
    public String bluPayClients(Model model, Principal principal){
        List<PaymentClient> paymentClients = paymentClientRepository.findAllByIdentityId(Integer.parseInt(principal.getName()));
        model.addAttribute("clients",paymentClients);
        model.addAttribute("new_client",new PaymentClientVM());
        return "pay-blu-client";
    }

    @GetMapping("blu-pay/client/delete")
    public String bluPayClientDelete(@RequestParam String id,Model model,Principal principal){
        PaymentClient paymentClient = paymentClientRepository.findByUniqueId(id).orElse(null);
        if (paymentClient==null){
            model.addAttribute("message", "Client Not found");
            return "error/error-message";
        }
        if (!paymentClient.getIdentityId().equals(Integer.parseInt(principal.getName()))){
            model.addAttribute("message", "403");
            return "error/error-message";
        }
        paymentClientRepository.delete(paymentClient);
        return "redirect:/blu-pay/client";
    }

    @PostMapping("blu-pay/client")
    public String bluPayCreate(@Valid @ModelAttribute(name = "new_client") PaymentClientVM paymentClientVM,Model model,Principal principal){
        log.debug("create new payment client");
        if (!validToAddClient(Integer.parseInt(principal.getName()))){
            model.addAttribute("message", "You cannot add more client");
            return "error/error-message";
        }
        paymentClientRepository.save(PaymentClient.builder()
                        .identityId(Integer.parseInt(principal.getName()))
                        .clientUrl(paymentClientVM.getClientUrl())
                        .uniqueId(RandomStringGenerator.generateRandomString())
                        .requestSecret(RandomHexGenerator.generateRandomHexString())
                        .responseSecret(RandomHexGenerator.generateRandomHexString())
                        .balance(0)
                .build());
        return "redirect:/blu-pay/client";
    }

    private boolean validToAddClient(int id){
        int count = paymentClientRepository.countAllByIdentityId(id);
        if (count<=3){
            return true;
        }
        return false;
    }

    public static class RandomHexGenerator {

        private static final SecureRandom random = new SecureRandom();

        public static String generateRandomHexString() {
            // Generate a random number and convert it to a hexadecimal string
            // 64 characters in hex = 32 bytes = 256 bits
            return new BigInteger(256, random).toString(16);
        }
    }
    public static class RandomStringGenerator {

        private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        private static final int STRING_LENGTH = 32;

        public static String generateRandomString() {
            SecureRandom random = new SecureRandom();
            StringBuilder sb = new StringBuilder(STRING_LENGTH);

            for (int i = 0; i < STRING_LENGTH; i++) {
                int index = random.nextInt(CHARACTERS.length());
                sb.append(CHARACTERS.charAt(index));
            }

            return sb.toString();
        }
    }
}
