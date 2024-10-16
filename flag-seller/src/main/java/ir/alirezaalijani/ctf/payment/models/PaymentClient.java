package ir.alirezaalijani.ctf.payment.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_client")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String uniqueId;
    private String requestSecret;
    private String responseSecret;
    private String clientUrl;
    private Integer identityId;
    @Builder.Default
    private long balance=0;
}
