package bank.mvp.entity;

import bank.mvp.dto.AccountNumber;
import bank.mvp.dto.Amount;
import bank.mvp.dto.BeneficiaryAddressBank;
import bank.mvp.dto.OrderingDto;
import bank.mvp.enums.TransferType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternationalTransferDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    private OrderingDto ordering;
//    private Map<String, AccountNumber> correspondentBanking; //key: 'accountNumber'
//    private BeneficiaryAddressBank beneficiaryAddressBank;
//    private Amount amount;
    private String referenceId;
    private String refdos;
    @Enumerated(EnumType.STRING)
    private TransferType transferType; //key:'type'
    private String nature;
    private String executionDate;
    private String inputDate;
    private String bankValueDate;
    private String customerValueDate;
    private String chargesBears;
    private String reason;
    private String status;
}
