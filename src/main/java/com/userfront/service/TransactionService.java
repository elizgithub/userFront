package com.userfront.service;

import com.userfront.model.PrimaryAccount;
import com.userfront.model.PrimaryTransaction;
import com.userfront.model.SavingsAccount;
import com.userfront.model.SavingsTransaction;

import java.util.List;

public interface TransactionService {

    List<PrimaryTransaction> findPrimaryTransactionList(String username);
    List<SavingsTransaction> findSavingsTransactionList(String username);
    void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction);
    void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction);
    void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction);
    void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction);
    void betweenAccountsTransfer(String from, String to, String amount, PrimaryAccount primaryAccount, SavingsAccount savingsAccount) throws Exception;
}
