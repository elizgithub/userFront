package com.userfront.service.UserServiceImpl;

import com.userfront.dao.PrimaryAccountDao;
import com.userfront.dao.PrimaryTransactionDao;
import com.userfront.dao.SavingsAccountDao;
import com.userfront.dao.SavingsTransactionDao;
import com.userfront.model.*;
import com.userfront.service.AccountService;
import com.userfront.service.TransactionService;
import com.userfront.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;

@Service

public class AccountServiceImpl implements AccountService{

    private static int nextAccountNumber = 1000000;

    @Autowired
    private PrimaryAccountDao primaryAccountDao;

    @Autowired
    private SavingsAccountDao savingsAccountDao;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;


    @Override
    public PrimaryAccount createPrimaryAccount() {

        PrimaryAccount primaryAccount = new PrimaryAccount();
        primaryAccount.setAccountBalance(new BigDecimal(0.0));
        primaryAccount.setAccountNumber(accountGen());

        primaryAccountDao.save(primaryAccount);
        return primaryAccountDao.findByAccountNumber(primaryAccount.getAccountNumber());

    }

    @Override
    public SavingsAccount createSavingsAccount() {

        SavingsAccount savingsAccount = new SavingsAccount();
        savingsAccount.setAccountBalance(new BigDecimal(0.0));
        savingsAccount.setAccountNumber(accountGen());

        savingsAccountDao.save(savingsAccount);
        return savingsAccountDao.findByAccountNumber(savingsAccount.getAccountNumber());

    }

    private int accountGen()
    {
        return nextAccountNumber;
    }

    public void deposit(String accountType, double amount, Principal principal)
    {
        //we know who is going to deposit by using the principal object and bind it to user object
        User user = userService.findByUsername(principal.getName());
        if(accountType.equalsIgnoreCase("Primary")){
            PrimaryAccount primaryAccount = user.getPrimaryAccount();
            //we "add" the new amount to the current getAccountBalance
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
            //then save the primary account
            primaryAccountDao.save(primaryAccount);

            Date date = new Date();

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date,"Deposited to Primary Account","Primary Account","Completed",amount,primaryAccount.getAccountBalance(),primaryAccount);
            transactionService.savePrimaryDepositTransaction(primaryTransaction);
        }
        else if(accountType.equalsIgnoreCase("Savings")){
            SavingsAccount savingsAccount = user.getSavingsAccount();
            //we "add" the new amount to the current getAccountBalance
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
            //then save the primary account persists to database
            savingsAccountDao.save(savingsAccount);

            //date created
            Date date = new Date();
            //deposit transaction object instantiated
            SavingsTransaction savingsTransaction = new SavingsTransaction(date,"Deposited to Savings Account","Savings Account","Completed",amount,savingsAccount.getAccountBalance(),savingsAccount);
            transactionService.saveSavingsDepositTransaction(savingsTransaction);
        }
    }

    @Override
    public void withdraw(String accountType, double amount, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        if(accountType.equalsIgnoreCase("Primary")){
            PrimaryAccount primaryAccount =user.getPrimaryAccount();
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);

            Date date = new Date();

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date,"Withdraw From Primary Account","PrimaryAccount","Completed",amount,primaryAccount.getAccountBalance(),primaryAccount);
            transactionService.savePrimaryWithdrawTransaction(primaryTransaction);
        }
        else if(accountType.equalsIgnoreCase("Savings")){
            SavingsAccount savingsAccount = user.getSavingsAccount();
            //we "add" the new amount to the current getAccountBalance
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            //then save the primary account
            savingsAccountDao.save(savingsAccount);

            //date created
            Date date = new Date();
            //deposit transaction object instantiated
            SavingsTransaction savingsTransaction = new SavingsTransaction(date,"Withdraw From Savings Account","Savings Account","Completed",amount,savingsAccount.getAccountBalance(),savingsAccount);
            transactionService.saveSavingsWithdrawTransaction(savingsTransaction);
        }
    }
}
