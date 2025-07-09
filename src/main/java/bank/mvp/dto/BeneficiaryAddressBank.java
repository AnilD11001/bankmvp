package bank.mvp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BeneficiaryAddressBank {
    private String addressFormat;
    private String addressLine1;
    private String addressLine2;
}
