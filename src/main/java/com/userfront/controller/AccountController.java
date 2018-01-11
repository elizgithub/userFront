package com.userfront.controller;


import com.userfront.model.PrimaryAccount;
import com.userfront.model.SavingsAccount;
import com.userfront.model.User;
import com.userfront.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;

    @RequestMapping("/primaryAccount")
    private String PrimaryAccount(Model model, Principal principal)
    {
        User user = userService.findByUsername(principal.getName());//finds the user loged in from dbtabase by using the user loged in
        PrimaryAccount primaryAccount = user.getPrimaryAccount();//primary account binded to user;
        model.addAttribute("primaryAccount",primaryAccount);//mapped to the view
        return "primaryAccount";
    }

    @RequestMapping("/savingsAccount")
    private String SavingsAccount(Model model, Principal principal)
    {
        User user = userService.findByUsername(principal.getName());
        SavingsAccount savingsAccount = user.getSavingsAccount();
        model.addAttribute("savingsAccount",savingsAccount);

        return "savingsAccount";
    }
}
