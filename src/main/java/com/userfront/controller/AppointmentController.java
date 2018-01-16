package com.userfront.controller;


import com.userfront.model.Appointment;
import com.userfront.model.User;
import com.userfront.service.AppointmentService;
import com.userfront.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class AppointmentController {


    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;

    @RequestMapping(value = "/appointment/create" , method = RequestMethod.GET)
    public String appointment(Model model){
        Appointment appointment = new Appointment();

        model.addAttribute("appointment",appointment);
        model.addAttribute("dateString","");
        return "appointment";
    }

    @RequestMapping(value = "/appointment/create" , method = RequestMethod.POST)
    public String appointmentPost(@ModelAttribute("appointment") Appointment appointment,
                                  @ModelAttribute("dateString") String dateString,
                                  @ModelAttribute("location") String location,
                                    Principal principal) throws ParseException {

        System.out.println("the date "+dateString);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date = format.parse(dateString);
        appointment.setDate(date);

        User user = userService.findByUsername(principal.getName());
        appointment.setUser(user);

        appointmentService.createAppointment(appointment);
        return "redirect:/userFront";

    }
}
