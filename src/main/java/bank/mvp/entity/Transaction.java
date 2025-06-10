package bank.mvp.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type; // "DEBIT" or "CREDIT"
    private String source; // "WALLET" or "BANK"
    private BigDecimal amount;
    private LocalDateTime timestamp;

    @ManyToOne
    private BankAccount bankAccount;

    @ManyToOne
    private Wallet wallet;
}