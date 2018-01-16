package com.userfront.service.UserServiceImpl;

import com.userfront.dao.AppointmentDao;
import com.userfront.model.Appointment;
import com.userfront.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService{

    @Autowired
    private AppointmentDao appointmentDao;
    @Override
    public Appointment createAppointment(Appointment appointment) {

        return appointmentDao.save(appointment);
    }

    public List<Appointment> appointmentList(){
        return appointmentDao.findAll();
    }

    public Appointment findAppointment(Long id){
        return appointmentDao.findOne(id);
    }

    public void confirmAppointment(Long id){
        Appointment appointment = appointmentDao.findOne(id);
        appointment.setConfirmed(true);
        appointmentDao.save(appointment);
    }
}
