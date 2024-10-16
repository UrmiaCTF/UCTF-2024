package ir.alirezaalijani.ctf.payment.controller.vm;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentVM {
    @Min(500)
    private Long amount;
    @Size(max = 4,min = 4)
    private String cardNoPart4;
    @Size(max = 4,min = 4)
    private String cardNoPart3;
    @Size(max = 4,min = 4)
    private String cardNoPart2;
    @Size(max = 4,min = 4)
    private String cardNoPart1;
    @Size(max = 3,min = 3)
    private String cvv2;
    @Min(1) @Max(12)
    private Integer month;
    @Min(22) @Max(50)
    private Integer year;
    @Size(max = 20,min = 5)
    private String pin;
    @Size(max = 100,min = 10)
    private String orderId;
    @Size(max = 64,min = 64)
    private String hash;
    @Size(max = 32,min = 32)
    private String client;
}
