package com.userfront.service;

import com.userfront.model.PrimaryAccount;
import com.userfront.model.SavingsAccount;
import org.springframework.stereotype.Service;

import java.security.Principal;

public interface AccountService {

    PrimaryAccount createPrimaryAccount();
    SavingsAccount createSavingsAccount();
    void deposit(String accountType, double amount, Principal principal);
    void withdraw(String accountType, double amount, Principal principal);
}
