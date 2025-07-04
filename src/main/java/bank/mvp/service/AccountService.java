package bank.mvp.service;

import bank.mvp.dto.JwtResponse;
import bank.mvp.dto.LoginRequestDto;
import bank.mvp.entity.AppUser;
import bank.mvp.entity.BankAccount;
import bank.mvp.entity.Transaction;
import bank.mvp.entity.Wallet;
import bank.mvp.repository.BankAccountRepository;
import bank.mvp.repository.TransactionRepository;
import bank.mvp.repository.UserRepository;
import bank.mvp.repository.WalletRepository;
import bank.mvp.security.jwt.JwtUtils;
import bank.mvp.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private BankAccountRepository bankRepo;
    @Autowired
    private WalletRepository walletRepo;
    @Autowired
    private TransactionRepository txnRepo;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    AuthenticationManager authenticationManager;

    public ResponseEntity<JwtResponse> authenticateUserWithToken(LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    public BankAccount openAccount(AppUser user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user = userRepo.save(user);
        String accountNumber = generateAccountNumber(user);
        BankAccount bank = new BankAccount(accountNumber, user, BigDecimal.ZERO);
        Wallet wallet = new Wallet(null, bank, BigDecimal.ZERO);
        bankRepo.save(bank);
        walletRepo.save(wallet);
        return bank;
    }

    private String generateAccountNumber(AppUser user) {
        return String.format("%04d%012d", user.getBank().getId(), user.getId());
    }

    public void transferToWallet(String accountNumber, BigDecimal amount) {
        BankAccount bank = bankRepo.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException("BankAccount not found"));
        if (bank.getBalance().compareTo(amount) < 0) throw new RuntimeException("Insufficient funds");
        bank.setBalance(bank.getBalance().subtract(amount));
        Wallet wallet = walletRepo.findByBankAccount(bank).orElseThrow(() -> new RuntimeException("Wallet not found"));
        wallet.setBalance(wallet.getBalance().add(amount));
        txnRepo.save(new Transaction(null, "DEBIT", "BANK", amount, LocalDateTime.now(), bank, null));
        txnRepo.save(new Transaction(null, "CREDIT", "WALLET", amount, LocalDateTime.now(), null, wallet));
    }


    public void transferToBank(String accountNumber, BigDecimal amount) {
        Wallet wallet = walletRepo.findByBankAccount_AccountNumber(accountNumber).orElseThrow(() -> new RuntimeException("Wallet not found"));
        if (wallet.getBalance().compareTo(amount) < 0) throw new RuntimeException("Insufficient wallet funds");
        wallet.setBalance(wallet.getBalance().subtract(amount));
        BankAccount bank = wallet.getBankAccount();
        bank.setBalance(bank.getBalance().add(amount));
        txnRepo.save(new Transaction(null, "DEBIT", "WALLET", amount, LocalDateTime.now(), null, wallet));
        txnRepo.save(new Transaction(null, "CREDIT", "BANK", amount, LocalDateTime.now(), bank, null));
    }
}
