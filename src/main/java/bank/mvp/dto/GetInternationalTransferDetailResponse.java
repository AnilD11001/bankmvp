package bank.mvp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class GetInternationalTransferDetailResponse {

    private OrderingDto ordering;
    private Map<String, AccountNumber> correspondentBanking = new HashMap<>(); //key: 'accountNumber'
    private BeneficiaryAddressBank beneficiaryAddressBank;
    private Amount amount;
    private String refdos;
    private Map<String, String> transferType = new HashMap<>(); //key:'type'
    private String nature;
    private String executionDate;
    private String inputDate;
    private String bankValueDate;
    private String customerValueDate;
    private String chargesBears;
    private String reason;
    private String status;

}
