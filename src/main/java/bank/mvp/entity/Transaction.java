package bank.mvp.entity;

import bank.mvp.enums.TransferStatus;
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
    @Column(nullable = false, unique = true, length = 20)
    private String referenceId;  //  prefix-dateTime-random

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferStatus status;
    private BigDecimal amount;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "source_bank_account_id")
    private BankAccount sourceBankAccount;

    @ManyToOne
    @JoinColumn(name = "source_wallet_id")
    private Wallet sourceWallet;

    // Destination: Either a BankAccount or Wallet
    @ManyToOne
    @JoinColumn(name = "destination_bank_account_id")
    private BankAccount destinationBankAccount;

    @ManyToOne
    @JoinColumn(name = "destination_wallet_id")
    private Wallet destinationWallet;

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }
}