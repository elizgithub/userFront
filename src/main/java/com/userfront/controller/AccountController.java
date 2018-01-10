package com.userfront.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {

    @RequestMapping("/primaryaccount")
    private String PrimaryAccount()
    {
        return "primaryaccount";
    }

    @RequestMapping("/savingsaccount")
    private String SavingsAccount()
    {
        return "savingsaccount";
    }
}
