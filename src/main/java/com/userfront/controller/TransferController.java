package com.userfront.controller;

import com.userfront.model.PrimaryAccount;
import com.userfront.model.SavingsAccount;
import com.userfront.model.User;
import com.userfront.service.TransactionService;
import com.userfront.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.jnlp.UnavailableServiceException;
import java.awt.event.MouseEvent;
import java.rmi.MarshalledObject;
import java.security.Principal;

@Controller
@RequestMapping("/transfer")
public class TransferController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/betweenAccounts",method = RequestMethod.GET)
    public String betweenAccounts(Model model){

        model.addAttribute("transferFrom","");
        model.addAttribute("transferTo","");
        model.addAttribute("amount","");
        return "betweenAccounts";
    }

    @RequestMapping(value = "/betweenAccounts",method = RequestMethod.POST)
    public String betweenAccountsPost(
            @ModelAttribute("transferFrom") String transferFrom,//from the html to the attributes
            @ModelAttribute("tansferTo") String transferTo,
            @ModelAttribute("amount") String amount,
            Principal principal
            )throws Exception{

        User user = userService.findByUsername(principal.getName());
        PrimaryAccount primaryAccount = user.getPrimaryAccount();
        SavingsAccount savingsAccount = user.getSavingsAccount();

        transactionService.betweenAccountsTransfer(transferFrom,transferTo,amount,primaryAccount,savingsAccount);

        return "redirect:/userFront";
    }
}
