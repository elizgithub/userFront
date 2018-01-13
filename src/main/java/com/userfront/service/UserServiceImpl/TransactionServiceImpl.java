package com.userfront.service.UserServiceImpl;

import com.userfront.dao.PrimaryAccountDao;
import com.userfront.dao.PrimaryTransactionDao;
import com.userfront.dao.SavingsAccountDao;
import com.userfront.dao.SavingsTransactionDao;
import com.userfront.model.*;
import com.userfront.service.TransactionService;
import com.userfront.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

}

