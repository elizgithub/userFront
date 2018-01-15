package com.userfront.controller;

import com.userfront.model.PrimaryAccount;
import com.userfront.model.Recipient;
import com.userfront.model.SavingsAccount;
import com.userfront.model.User;
import com.userfront.service.AccountService;
import com.userfront.service.TransactionService;
import com.userfront.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.event.MouseEvent;
import java.rmi.MarshalledObject;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/transfer")
public class TransferController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

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
            @ModelAttribute("transferTo") String transferTo,
            @ModelAttribute("amount") String amount,
            Principal principal
            )throws Exception{

        User user = userService.findByUsername(principal.getName());
        PrimaryAccount primaryAccount = user.getPrimaryAccount();
        SavingsAccount savingsAccount = user.getSavingsAccount();

        System.out.println("errorfrom "+transferFrom+" "+transferTo+ " " +amount);
        transactionService.betweenAccountsTransfer(transferFrom,transferTo,Double.parseDouble(amount),primaryAccount,savingsAccount);

        return "redirect:/userFront";
    }

    @RequestMapping(value = "/recipient",method = RequestMethod.GET) //just display the page of recipients
    public String recipient(Model model , Principal principal){
        Recipient recipient = new Recipient();
        List<Recipient> recipientList = transactionService.findAllRecipientList(principal);
        model.addAttribute("recipientList",recipientList);
        model.addAttribute("recipient",recipient);

        return "recipient";
    }

    @RequestMapping(value = "/recipient/save", method = RequestMethod.POST) //to save the recipient list
    public String saveRecipientPost(@ModelAttribute("recipient") Recipient recipient,Principal principal){

        User user = userService.findByUsername(principal.getName());
        recipient.setUser(user);
        transactionService.saveRecipient(recipient);
        return "redirect:/transfer/recipient";
    }

    @RequestMapping(value = "/recipient/edit" , method = RequestMethod.GET)
    public String editRecipient(@RequestParam("recipientName") String recipientName, Model model, Principal principal){

        Recipient recipient = transactionService.findRecipientByName(recipientName);
        List<Recipient> recipientList = transactionService.findAllRecipientList(principal);
        model.addAttribute("recipientList",recipientList);
        model.addAttribute("recipient",recipient);
        return "recipient";
    }

    @RequestMapping(value = "/recipient/delete", method = RequestMethod.GET)
    @Transactional
    public String deleteRecipient(@RequestParam("recipientName") String recipientName, Model model, Principal principal){

        transactionService.deleteRecipientByName(recipientName);

        List<Recipient> recipientList = transactionService.findAllRecipientList(principal);
        Recipient recipient = new Recipient();
        model.addAttribute("recipientList",recipientList);
        model.addAttribute("recipient",recipient);

        return "redirect:/transfer/recipient";
    }

    @RequestMapping(value = "/toSomeoneElse",method = RequestMethod.GET)
    public String toSomeOneElse(Model model, Principal principal){
        Recipient recipient = new Recipient();
        List<Recipient> recipientList = transactionService.findAllRecipientList(principal);
        model.addAttribute("recipientList",recipientList);
        model.addAttribute("accountType","");
        return "toSomeoneElse";
    }

    @RequestMapping(value = "/toSomeoneElse",method = RequestMethod.POST)
    public String toSomeoneElsePost(@ModelAttribute("recipientName") String recipientName,
                                    @ModelAttribute("accountType") String accountType,
                                    @ModelAttribute("amount") String amount,
                                    Principal principal){
        User user = userService.findByUsername(principal.getName()); //get the sender object by the name
        Recipient recipient = transactionService.findRecipientByName(recipientName);//select form the dropdown and select recipient

        transactionService.toSomeoneElseTransfer(recipient,accountType,amount,user.getPrimaryAccount(),user.getSavingsAccount());

        return "toSomeOne";
    }
}
