package com.userfront.service;

import com.userfront.model.*;

import java.security.Principal;
import java.util.List;

public interface TransactionService {

    List<PrimaryTransaction> findPrimaryTransactionList(String username);
    List<SavingsTransaction> findSavingsTransactionList(String username);
    void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction);
    void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction);
    void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction);
    void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction);
    void betweenAccountsTransfer(String from, String to, double amount, PrimaryAccount primaryAccount, SavingsAccount savingsAccount)throws Exception;

    List<Recipient> findAllRecipientList(Principal principal);
    Recipient saveRecipient(Recipient recipient);
    Recipient findRecipientByName(String name);
    void deleteRecipientByName(String recipientName);

}
