package bank.mvp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {
    @Id
    private String accountNumber; // 16-char generated
    private String accountType;

    @OneToOne
    private AppUser user;
    private BigDecimal balance;
//    private Long numberOfDecimals;

    @OneToOne
    private Currency currency;
    @OneToOne
    private Bank bank;
}