package ir.alirezaalijani.ctf.payment.service;

import ir.alirezaalijani.ctf.payment.models.TeamCreditCard;
import ir.alirezaalijani.ctf.payment.repository.TeamCreditCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamCreditCardServiceImpl implements TeamCreditCardService{
    private final TeamCreditCardRepository creditCardRepository;
    private static final Random rand = new Random();
    private final Environment env;

    @Override
    public TeamCreditCard getById(int identityId){
        var opCredit = creditCardRepository.findById(identityId);
        if (opCredit.isPresent()){
            return opCredit.get();
        }else {
            log.debug("generate credit for team with id {}",identityId);
            TeamCreditCard newCredit=generateRandomCard(identityId);
            if (env.acceptsProfiles(Profiles.of("dev"))) {
//                newCredit.setBalance(5_000_000_000_000L);
            }
            return creditCardRepository.save(newCredit);
        }
    }

    private TeamCreditCard generateRandomCard(int id){
        return TeamCreditCard.builder()
                .cardNumber(generateRandomCreditCardNumber())
                .cvv2(generateRandomCVV2())
                .expireDate(generateRandomExpirationDate())
                .password(generateRandomPassword())
                .id(id)
                .balance(50_000).build();
    }

    public static String generateRandomCreditCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        // Generate the first digit of the credit card number (should be 4 for Visa)
        cardNumber.append("4");
        // Generate the next 15 digits randomly
        for (int i = 0; i < 15; i++) {
            cardNumber.append(rand.nextInt(10)); // Append a random digit (0-9)
        }
        return cardNumber.toString();
    }
    public static String generateRandomCVV2() {
        int cvv2 = rand.nextInt(900) + 100; // Ensure a 3-digit number
        return String.valueOf(cvv2);
    }

    public static String generateRandomExpirationDate() {
        int month = rand.nextInt(12) + 1; // Random month (1-12)
        int year = rand.nextInt(10) + 22;  // Random year (current year + 1 to current year + 10)
        return String.format("%02d/%02d", month, year);
    }
    public static String generateRandomPassword() {
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            password.append(rand.nextInt(10)); // Append a random digit (0-9)
        }
        return password.toString();
    }
}
