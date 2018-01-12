package com.userfront.controller;


import com.userfront.model.*;
import com.userfront.service.AccountService;
import com.userfront.service.TransactionService;
import com.userfront.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @RequestMapping("/primaryAccount") //the primary account page
    private String PrimaryAccount(Model model, Principal principal)
    {

        List<PrimaryTransaction> primaryTransactionList = transactionService.findPrimaryTransactionList(principal.getName());
        User user = userService.findByUsername(principal.getName());//finds the user loged in from dbtabase by using the user loged in
        PrimaryAccount primaryAccount = user.getPrimaryAccount();//primary account binded to user;
        model.addAttribute("primaryAccount",primaryAccount);//mapped to the view
        model.addAttribute("primaryTransactionList",primaryTransactionList);//The List of transaction list mapped to the view for the table
        return "primaryAccount";
    }

    @RequestMapping("/savingsAccount") // the savings account page
    private String SavingsAccount(Model model, Principal principal)
    {
        List<SavingsTransaction> savingsTransactionList = transactionService.findSavingsTransactionList(principal.getName());
        User user = userService.findByUsername(principal.getName());
        SavingsAccount savingsAccount = user.getSavingsAccount();
        model.addAttribute("savingsAccount",savingsAccount);
        model.addAttribute("savingsTransactionList",savingsTransactionList);
        return "savingsAccount";
    }

    @RequestMapping(value = "/deposit", method= RequestMethod.GET)
    private String deposit(Model model)
    {
        //assigns empty value to the models because its just get method to display the form
        model.addAttribute("accountType","");
        model.addAttribute("amount","");
        return "deposit";
    }

    @RequestMapping(value = "/deposit", method= RequestMethod.POST)
    private String depositPost(@ModelAttribute("amount") String amount, @ModelAttribute("accountType") String accountType, Principal principal)
    {
        accountService.deposit(accountType,Double.parseDouble(amount),principal);
        return "redirect:/userFront";
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.GET)
    private String withdraw(Model model){
        model.addAttribute("accountType","");
        model.addAttribute("amount","");
        return "/withdraw";
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    private String withdrawPost(@ModelAttribute("amount") String amount,@ModelAttribute("accountType") String accountType,Principal principal){

        accountService.withdraw(accountType,Double.parseDouble(amount),principal);
        return "redirect:/userFront";
    }

}
