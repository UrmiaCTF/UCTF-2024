package ir.alirezaalijani.ctf.payment.controller;

import ir.alirezaalijani.ctf.payment.controller.vm.OrderDTO;
import ir.alirezaalijani.ctf.payment.controller.vm.PaymentVM;
import ir.alirezaalijani.ctf.payment.models.Order;
import ir.alirezaalijani.ctf.payment.models.PaymentClient;
import ir.alirezaalijani.ctf.payment.models.TeamCreditCard;
import ir.alirezaalijani.ctf.payment.repository.OrderPaymentRepository;
import ir.alirezaalijani.ctf.payment.repository.PaymentClientRepository;
import ir.alirezaalijani.ctf.payment.repository.TeamCreditCardRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PaymentController {
    private final OrderPaymentRepository orderPaymentRepository;
    private final TeamCreditCardRepository teamCreditCardRepository;
    private final PaymentClientRepository paymentClientRepository;
    private final Environment env;
    private static final long baseAmount = 5_000_000;
    @Value("${application.pay.request-secret}")
    private String requestSecret;
    @Value("${application.pay.response-secret}")
    private String responseSecret;
    @Value("${application.flag-seller-id}")
    private String flagSellerClientId;
    @Value("${application.flag-seller-url}")
    private String flagSellerUrl;
    @Value("${application.blupay-url}")
    private String bluPayUrl;
    @Value("${ctfd.flag}")
    private String ctfFlag;
    private static PaymentClient paymentClientCached;

    @GetMapping("order")
    public RedirectView order(Principal principal) {
        int teamId = Integer.parseInt(principal.getName());

        PaymentClient paymentClient = paymentClientCached;
        if (paymentClient == null) {
            log.debug("generate flag seller payment client");
            paymentClient = paymentClientRepository
                    .findByUniqueId(flagSellerClientId).orElseGet(() -> paymentClientRepository.save(PaymentClient.builder()
                    .clientUrl(flagSellerUrl)
                    .requestSecret(requestSecret)
                    .responseSecret(responseSecret)
                    .uniqueId(flagSellerClientId)
                    .identityId(0)
                    .build()));
            paymentClientCached = paymentClient;
        }

        Order newOrder = Order.builder()
                .creditId(teamId)
                .payed(false)
                .uniqueId(UUID.randomUUID().toString())
                .identityId(teamId)
                .orderedAt(Instant.now())
                .build();

        // hash = SHA256(amount + "-" + orderid + "-" + clientid + "-" + requestSecret)
        String hash = (baseAmount + "-" + newOrder.getUniqueId() + "-" + paymentClient.getUniqueId() + "-" + paymentClient.getRequestSecret());
        String sha256hex = DigestUtils.sha256Hex(hash);

//        return "redirect:pay?orderid=" + newOrder.getId() + "&amount=" + baseAmount + "&client="+paymentClient.getUniqueId()+"&hash=" + sha256hex;
        String paymentUrl=bluPayUrl + "?orderId=" + newOrder.getUniqueId() + "&amount=" + baseAmount + "&clientId=" + paymentClient.getUniqueId() + "&hash=" + sha256hex;
        newOrder.setPaymentUrl(paymentUrl);
        orderPaymentRepository.save(newOrder);

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(paymentUrl);

        return redirectView;
    }

    @GetMapping("pay")
    public String paymentPage(@RequestParam("orderId") String order, @RequestParam("amount") Long amount, @RequestParam("clientId") String clintUniqueId,
                              @RequestParam("hash") String hash,
                              Model model) {

        PaymentClient paymentClient = paymentClientRepository.findByUniqueId(clintUniqueId).orElse(null);
        if (paymentClient == null) {
            model.addAttribute("message", "The client id is wrong");
            return "error/error-message";
        }
        String newHash = (amount + "-" + order + "-" + clintUniqueId + "-" + paymentClient.getRequestSecret());
        String sha256hex = DigestUtils.sha256Hex(newHash);
        if (!Objects.equals(hash, sha256hex)) {
            model.addAttribute("message", "The payment hash is wrong");
            return "error/error-message";
        } else {
            log.debug("loading payment page");
            PaymentVM paymentVM = new PaymentVM();
            paymentVM.setAmount(amount);
            paymentVM.setOrderId(order);
            paymentVM.setHash(hash);
            paymentVM.setClient(clintUniqueId);
            if (env.acceptsProfiles(Profiles.of("dev"))) {
                log.debug("setting dev credit card");
                teamCreditCardRepository.findById(1).ifPresent(creditCard -> {
                    paymentVM.setCvv2(creditCard.getCvv2());
                    String[] monthAndYear = creditCard.getExpireDate().split("/");
                    paymentVM.setMonth(Integer.parseInt(monthAndYear[0]));
                    paymentVM.setYear(Integer.parseInt(monthAndYear[1]));
                    paymentVM.setPin(creditCard.getPassword());
                    paymentVM.setCardNoPart1(creditCard.getCardNumber().substring(0, 4));
                    paymentVM.setCardNoPart2(creditCard.getCardNumber().substring(4, 8));
                    paymentVM.setCardNoPart3(creditCard.getCardNumber().substring(8, 12));
                    paymentVM.setCardNoPart4(creditCard.getCardNumber().substring(12, 16));
                });
            }
            model.addAttribute("payment", paymentVM);
            return "pay-blu";
        }
    }

    @PostMapping("/payment")
    public String processPayment(
            @Valid @ModelAttribute(name = "payment") PaymentVM paymentVM,
            Model model) {

        PaymentClient paymentClient = paymentClientRepository.findByUniqueId(paymentVM.getClient()).orElse(null);
        if (paymentClient == null) {
            model.addAttribute("message", "The client id is wrong");
            return "error/error-message";
        }

        String newHash = (paymentVM.getAmount() + "-" + paymentVM.getOrderId() + "-" + paymentVM.getClient() + "-" + paymentClient.getRequestSecret());
        String sha256hex = DigestUtils.sha256Hex(newHash);
        if (!Objects.equals(sha256hex, paymentVM.getHash())) {
            log.error("hash is not valid for: amount:{} ,order:{} ,sha256hex: {}", paymentVM.getAmount(), paymentVM.getOrderId(), sha256hex);
            return "error/error";
        }

        String cardNumber = paymentVM.getCardNoPart1() + paymentVM.getCardNoPart2() + paymentVM.getCardNoPart3() + paymentVM.getCardNoPart4();
        log.debug("verifying card: {}", cardNumber);
        Optional<TeamCreditCard> teamCreditCardOptional = teamCreditCardRepository
                .findByCardNumberAndCvv2AndPassword(cardNumber, paymentVM.getCvv2(), paymentVM.getPin());
        String expireDate = String.format("%02d/%02d", paymentVM.getMonth(), paymentVM.getYear());

        Optional<Order> orderOptional = orderPaymentRepository.findByUniqueId(paymentVM.getOrderId());
        // Validate credit card and order
        if (teamCreditCardOptional.isPresent() && orderOptional.isPresent()) {
            // validate expire data of credit card
            if (!expireDate.equals(teamCreditCardOptional.get().getExpireDate())) {
                model.addAttribute("message", "The card date is invalid.");
                return "error/error-message";
            }
            // validate account balance
            if (paymentVM.getAmount() > teamCreditCardOptional.get().getBalance()) {
                model.addAttribute("message", "The card balance is low.");
                return "error/error-message";
            }
            // validate order is payed
            if (orderOptional.get().isPayed()) {
                model.addAttribute("message", "Order with id " + orderOptional.get().getId() + " is payed already.");
                return "error/error-message";
            }

            log.debug("payment validate is success");
            var credit = teamCreditCardOptional.get();
            credit.setBalance(credit.getBalance() - paymentVM.getAmount());
            teamCreditCardRepository.save(credit);

            paymentClient.setBalance(paymentVM.getAmount() + paymentClient.getBalance());
            paymentClientRepository.save(paymentClient);

            var order = orderOptional.get();
            order.setPaymentId(UUID.randomUUID().toString());
            order.setCreditId(credit.getId());
            order.setClientId(paymentVM.getClient());
            order.setPayed(true);
            order.setPayAt(Instant.now());
            orderPaymentRepository.save(order);

            log.debug("credit {} pay amount {}", cardNumber, paymentVM.getAmount());
            //SHA256(orderId + "-" + responseSecret)
            String payHash = order.getPaymentId()+"-"+order.getId() + "-" + paymentClient.getResponseSecret();
            String payHashSha256hex = DigestUtils.sha256Hex(payHash);
            return "redirect:" + paymentClient.getClientUrl() + "?paymentId=" + order.getPaymentId() + "&orderId=" + paymentVM.getOrderId() + "&hash=" + payHashSha256hex;

        } else {
            log.debug("card or order is not present");
        }
        log.debug("payment failed.");
        model.addAttribute("message", "Payment failed credit card is invalid.");
        return "error/error-message";
    }

    @ResponseBody
    @GetMapping("api/payment")
    public ResponseEntity<?> paymentWithId(@RequestParam String paymentId, Principal principal){
        Order order = orderPaymentRepository.findByPaymentId(paymentId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!principal.getName().equals(String.valueOf(order.getIdentityId()))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(OrderDTO.builder()
                        .paymentId(order.getPaymentId())
                        .id(order.getId())
                        .payed(order.isPayed())
                        .payAt(order.getPayAt())
                .build());
    }

    @GetMapping("order/success")
    public String orderSuccess(@RequestParam("paymentId") String paymentId,
                               @RequestParam(value = "orderId", required = false) String orderId,
                               @RequestParam(value = "hash", required = false) String hash,
                               Model model) {

//        if (hash.isEmpty()) {
//            model.addAttribute("message", "The payment code is wrong");
//            return "error/error-message";
//        }

//        String payHash = orderId + "-" + responseSecret;
//        String payHashSha256hex = DigestUtils.sha256Hex(payHash);
//        if (!Objects.equals(payHashSha256hex, hash)) {
//            model.addAttribute("message", "The payment code is wrong");
//            return "error/error-message";
//        }

        var orderOptional = orderPaymentRepository.findByPaymentIdAndUniqueId(paymentId, orderId);
        if (orderOptional.isPresent()) {
            var order = orderOptional.get();
            if (order.isPayed()){
                model.addAttribute("flag",ctfFlag);
                return "flag";
            }else {
                model.addAttribute("message", "Payment Not fond or not payed.");
                return "error/error-message";
            }
        } else {
            model.addAttribute("message", "Payment Not fond in blu pay.");
            return "error/error-message";
        }
    }
}
