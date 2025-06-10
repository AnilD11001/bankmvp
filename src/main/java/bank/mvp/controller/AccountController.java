package bank.mvp.controller;

import bank.mvp.entity.AppUser;
import bank.mvp.entity.BankAccount;
import bank.mvp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService service;

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
}