package bank.mvp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferDetailsDTO {
    private  String type;
    private BigDecimal transferAmount;
    private BigDecimal remainingBalance;
    private String accountNumber;
    private String referenceId;

    private String mail;
}
