package ir.alirezaalijani.ctf.payment.controller.vm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private int id;
    private boolean payed;
    private String paymentId;
    private Instant payAt;
}
