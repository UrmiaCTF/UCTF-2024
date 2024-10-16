package ir.alirezaalijani.ctf.payment.repository;

import ir.alirezaalijani.ctf.payment.models.PaymentClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentClientRepository extends JpaRepository<PaymentClient,Integer> {
    Optional<PaymentClient> findByUniqueId(String uniqueId);
    List<PaymentClient> findAllByIdentityId(Integer identityId);
    int countAllByIdentityId(Integer identityId);
}
