package bank.mvp.service;

import bank.mvp.dto.*;
import bank.mvp.dto.Currency;
import bank.mvp.entity.*;
import bank.mvp.repository.*;
import bank.mvp.security.jwt.JwtUtils;
import bank.mvp.security.service.UserDetailsImpl;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
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
import java.util.*;
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
    private InternationalTransferDetailRepository transferDetailRepository;
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
//        String accountNumber = generateAccountNumber(user);
//        BankAccount bank = new BankAccount(accountNumber, user, BigDecimal.ZERO);
//        Wallet wallet = new Wallet(null, bank, BigDecimal.ZERO);
//        bankRepo.save(bank);
//        walletRepo.save(wallet);
        return null;
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

    @Transactional
    public  List<GetInternationalTransferDetailResponse> getTransferDetails(String referenceType) {
       List<InternationalTransferDetail> transferDetailList = transferDetailRepository.findByReferenceType(referenceType);
       List<GetInternationalTransferDetailResponse> internationalTransferDetailResponseList = new ArrayList<>();
       transferDetailList.stream().forEach(transferDetail->{
           GetInternationalTransferDetailResponse response= new GetInternationalTransferDetailResponse();
           BeanUtils.copyProperties(transferDetail,response);
           response.getTransferType().put("type", transferDetail.getTransferType().getType());
           response.setStatus(transferDetail.getStatus().toString());

           BankAccount orderingBank = transferDetail.getOrderingBank();
           InternalAccountFormat internalAccountFormat =new InternalAccountFormat();
           internalAccountFormat.getBranch().put("code",orderingBank.getBank().getBranchCode());
           internalAccountFormat.getBranch().put("designation",orderingBank.getBank().getDesignation());
           Currency currency = new Currency();
           BeanUtils.copyProperties(transferDetail.getCurrency(),currency);
           internalAccountFormat.setCurrency(currency);
           internalAccountFormat.setSuffix("");
           internalAccountFormat.setAccount(orderingBank.getAccountNumber());

           OrderingDto orderingDto=new OrderingDto();
           orderingDto.getAccountClass().put("value", "222111");
           orderingDto.getAccountClass().put("comparisonOperator", "EQUALS");
           orderingDto.getAccountNumber().put("internalFormatAccountOurBranch",internalAccountFormat);

           Customer customer = new Customer();
           customer.setCustomerType("1");
           customer.setNationalIdentifier("52522578828");
           customer.getCustomer().put("customerNumber", orderingBank.getAccountId());
           customer.getCustomer().put("displayedName", orderingBank.getUser().getName());
           customer.getCustomerOfficer().put("code", "056");
           customer.getCustomerOfficer().put("name", "PIERRE DAMIEN");
           orderingDto.setCustomer(customer);
           response.setOrdering(orderingDto);

           AccountNumber accountNumber = new AccountNumber();
           accountNumber.getExternalFormatAccount().put("value","DE98 1278 7096 7006 1774");
           response.getCorrespondentBanking().put("accountNumber",accountNumber);
           BankAccount beneficiaryBankAccount = transferDetail.getBeneficiaryBank();

           BeneficiaryAddressBank beneficiaryAddressBank=new BeneficiaryAddressBank();
           beneficiaryAddressBank.setAddressFormat(beneficiaryBankAccount.getBank().getAddressFormat());
           beneficiaryAddressBank.setAddressLine1(beneficiaryBankAccount.getBank().getAddressLine1());
           beneficiaryAddressBank.setAddressLine2(beneficiaryBankAccount.getBank().getAddressLine2());
           response.setBeneficiaryAddressBank(beneficiaryAddressBank);
           Amount amount=new Amount();
           amount.setAmount(transferDetail.getAmount());
           CurrencyDto currencyDto = new CurrencyDto();
           currencyDto.setCurrency(currency);
           currencyDto.setNumberOfDecimals(2);
           amount.setCurrency(currencyDto);
           response.setAmount(amount);
           internationalTransferDetailResponseList.add(response);
       });

        return internationalTransferDetailResponseList;
    }

    public AppUser registerUser(AppUser user) {
        user.setPassword(encoder.encode(user.getPassword()));
       userRepo.save(user);
        return  user;
    }
}
