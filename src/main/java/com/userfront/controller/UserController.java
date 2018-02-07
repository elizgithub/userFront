package com.userfront.controller;

import com.userfront.model.User;
import com.userfront.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String UserProfile(Model model, Principal principal){
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user",user);//get the user model from the principal AKA session and pass it the method below
        return "profile";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String UserProfilePost(@ModelAttribute ("user")User newuser,Model model){

        User user = userService.findByUsername(newuser.getUsername());
        user.setFirstName(newuser.getFirstName());
        user.setLastName(newuser.getLastName());
        user.setEmail(newuser.getEmail());
        user.setUsername(newuser.getUsername());
        user.setPhone(newuser.getPhone());

        model.addAttribute("user",user);
        userService.saveUser(user);
        return "profile";
    }
}
