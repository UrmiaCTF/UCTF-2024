package ir.alirezaalijani.ctf.payment.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "the_order")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    // Define the desired format
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String uniqueId;
    private boolean payed;
    private String paymentId;
    private int creditId;
    private String clientId;
    private Integer identityId;
    private String paymentUrl;
    private Instant orderedAt;
    private Instant payAt;

    public String getDateString(Instant instant){
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        return zdt.format(formatter);
    }
}
