package ir.alirezaalijani.ctf.payment.repository;

import ir.alirezaalijani.ctf.payment.models.TeamCreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamCreditCardRepository extends JpaRepository<TeamCreditCard,Integer> {
    Optional<TeamCreditCard> findByCardNumberAndCvv2AndPassword(String curdNumber,String cvv2,String password);
}
