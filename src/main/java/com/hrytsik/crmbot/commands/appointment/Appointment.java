package com.hrytsik.crmbot.commands.appointment;


import com.hrytsik.crmbot.ExecutionContext;
import com.hrytsik.crmbot.commands.appointment.localstate.LocalStateForAppointment;

public interface Appointment {
   void execute(ExecutionContext executionContext, LocalStateForAppointment localStateForAppointment ) throws Exception;
}
