package bank.mvp.controller;

import bank.mvp.dto.GetInternationalTransferDetailResponse;
import bank.mvp.dto.JwtResponse;
import bank.mvp.dto.LoginRequestDto;
import bank.mvp.entity.AppUser;
import bank.mvp.entity.BankAccount;
import bank.mvp.security.jwt.JwtUtils;
import bank.mvp.security.service.UserDetailsImpl;
import bank.mvp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;


import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService service;
    @Autowired
    PasswordEncoder encoder;


    @PostMapping("/signIn")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDto loginRequest) {
        return service.authenticateUserWithToken(loginRequest);
    }

    @PostMapping("/open")
    public ResponseEntity<BankAccount> open(@RequestBody AppUser user) {
        return ResponseEntity.ok(service.openAccount(user));
    }

    @PostMapping("/toWallet")
    public ResponseEntity<String> bankToWallet(@RequestParam String account, @RequestParam BigDecimal amount) {
        service.transferToWallet(account, amount);
        return ResponseEntity.ok("Transferred to wallet");
    }

    @PostMapping("/toBank")
    public ResponseEntity<String> walletToBank(@RequestParam String account, @RequestParam BigDecimal amount) {
        service.transferToBank(account, amount);
        return ResponseEntity.ok("Transferred to bank");
    }

    @GetMapping("/internationalTransfer/details")
    public ResponseEntity<GetInternationalTransferDetailResponse> getTransferDetails(@RequestParam Long referenceId) {
        return ResponseEntity.ok( service.getTransferDetails(referenceId));
    }
}