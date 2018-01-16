package com.userfront.service;

import com.userfront.model.*;

import java.util.List;

public interface AppointmentService {
    Appointment createAppointment(Appointment appointment);
    List<Appointment> appointmentList();
    Appointment findAppointment(Long id);
    void confirmAppointment(Long id);

}
