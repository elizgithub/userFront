package com.userfront.service.UserServiceImpl;

import com.userfront.dao.*;
import com.userfront.model.*;
import com.userfront.service.TransactionService;
import com.userfront.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private UserService userService;

    @Autowired
    private PrimaryTransactionDao primaryTransactionDao;

    @Autowired
    private SavingsTransactionDao savingsTransactionDao;

    @Autowired
    private PrimaryAccountDao primaryAccountDao;

    @Autowired
    private SavingsAccountDao savingsAccountDao;

    @Autowired
    private RecipientDao recipientDao;
    public List<PrimaryTransaction> findPrimaryTransactionList(String username){

        User user = userService.findByUsername(username);

        List<PrimaryTransaction> primaryTransactionList = user.getPrimaryAccount().getPrimaryTransactionList();

        return primaryTransactionList;
    }

    public List<SavingsTransaction> findSavingsTransactionList(String username){

        User user = userService.findByUsername(username);

        List<SavingsTransaction> savingsTransactionList = user.getSavingsAccount().getSavingsTransactionList();

        return savingsTransactionList;
    }


    public void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction){

        primaryTransactionDao.save(primaryTransaction);
    }

    public void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction){
        savingsTransactionDao.save(savingsTransaction);
    }

    @Override
    public void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction) {
        primaryTransactionDao.save(primaryTransaction);
    }

    @Override
    public void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction) {
        savingsTransactionDao.save(savingsTransaction);
    }

    public void betweenAccountsTransfer(String from, String to, double amount, PrimaryAccount primaryAccount, SavingsAccount savingsAccount){

        if (from.equalsIgnoreCase("Primary") && to.equalsIgnoreCase("Savings")){
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);
            savingsAccountDao.save(savingsAccount);

            Date date = new Date();
            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date,"Between Accounts Transferd from "+from+" To "+to, "Transfer","Completed",amount,primaryAccount.getAccountBalance(),primaryAccount);

            primaryTransactionDao.save(primaryTransaction);
        }
        else if (from.equalsIgnoreCase("Savings") && to.equalsIgnoreCase("Primary")){

            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
            savingsAccountDao.save(savingsAccount);
            primaryAccountDao.save(primaryAccount);

            Date date = new Date();

            SavingsTransaction savingsTransaction = new SavingsTransaction(date,"Between Accounts Transferd from "+from+" To "+to, "Transfer","Completed",amount,primaryAccount.getAccountBalance(),savingsAccount);

            savingsTransactionDao.save(savingsTransaction);
        }
        else {
            try {
                throw new Exception("error");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    public List<Recipient> findAllRecipientList(Principal principal){
        String Username = principal.getName();
        List<Recipient> recipientList = recipientDao.findAll().stream() //convert list to stream
                .filter(recipient -> Username.equals(recipient.getUser().getUsername())) //then only filters out recipients from DB which are binded to the princpial user
                    .collect(Collectors.toList());//then adds the filtered recipient to list;
        return recipientList;
    }

    public Recipient saveRecipient(Recipient recipient){
        return recipientDao.save(recipient);
    }

    @Override
    public Recipient findRecipientByName(String name) {
        return recipientDao.findByName(name);
    }

    public void deleteRecipientByName(String recipientName){
        recipientDao.deleteByName(recipientName);
    }


    public void toSomeoneElseTransfer(Recipient recipient,String accountType,String amount,PrimaryAccount primaryAccount,SavingsAccount savingsAccount){

        if(accountType.equalsIgnoreCase("Primary")) {
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);
            Date date = new Date();
            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date,"Account Transfered to Recipient "+recipient.getName(), " Transfer","Completed",Double.parseDouble(amount),primaryAccount.getAccountBalance(),primaryAccount);
            primaryTransactionDao.save(primaryTransaction);
        }
        else if(accountType.equalsIgnoreCase("Savings")){
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccountDao.save(savingsAccount);
            Date date = new Date();
            SavingsTransaction savingsTransaction = new SavingsTransaction(date,"Account Transfered to Recipient "+recipient.getName(), " Transfer","Completed",Double.parseDouble(amount),savingsAccount.getAccountBalance(),savingsAccount);
            savingsTransactionDao.save(savingsTransaction);
        }

    }
}

