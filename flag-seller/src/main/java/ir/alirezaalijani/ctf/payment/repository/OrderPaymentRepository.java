package ir.alirezaalijani.ctf.payment.repository;

import ir.alirezaalijani.ctf.payment.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderPaymentRepository extends JpaRepository<Order,Integer> {

    Optional<Order> findByPaymentIdAndUniqueId(String paymentId,String uniqueId);
    Optional<Order> findByPaymentId(String paymentId);
    Optional<Order> findByUniqueId(String uniqueId);
    List<Order> findByIdentityId(Integer identityId);
}
