package ir.alirezaalijani.ctf.payment.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamCreditCard {
    @Id
    private int id;
    private String cardNumber;
    private String cvv2;
    private String expireDate;
    private String password;
    private long balance;
}
