package bank.mvp.repository;

import bank.mvp.entity.BankAccount;
import bank.mvp.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByBankAccount_AccountNumber(String accountNumber);
    Optional<Wallet> findByBankAccount(BankAccount bank);
}
