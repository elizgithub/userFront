package com.userfront.service;

import com.userfront.model.PrimaryAccount;
import com.userfront.model.SavingsAccount;
import org.springframework.stereotype.Service;

public interface AccountService {

    PrimaryAccount createPrimaryAccount();
    SavingsAccount createSavingsAccount();
}
