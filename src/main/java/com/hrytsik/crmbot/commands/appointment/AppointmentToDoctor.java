package com.hrytsik.crmbot.commands.appointment;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrytsik.crmbot.ExecutionContext;
import com.hrytsik.crmbot.commands.Command;
import com.hrytsik.crmbot.commands.appointment.localstate.LocalStateForAppointment;
import com.hrytsik.crmbot.entity.TelegramUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AppointmentToDoctor implements Command {

    @Override
    public void doCommand(ExecutionContext executionContext) {

        try {

            String localState = executionContext.getLocalState();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();

            if (localState == null) {
                LocalStateForAppointment localStateForAppointment = new LocalStateForAppointment(null, "send_doctor_speciality", null);
                executionContext.setLocalState(objectMapper.writeValueAsString(localStateForAppointment));
            }
            LocalStateForAppointment localStateForAppointment = objectMapper.readValue(executionContext.getLocalState(), LocalStateForAppointment.class);
            String step = localStateForAppointment.getStep();

            Map<String, Appointment> appointmentMap = new HashMap<>();
            appointmentMap.put("send_doctor_speciality", new SendDoctorSpeciality());
            appointmentMap.put("chose_doctor", new ChoseDoctor());
            appointmentMap.put("chose_id", new ChoseId());
            appointmentMap.put("chose_data_to_appointment", new ChoseDataToAppointment());
            appointmentMap.put("chose_time", new ChoseTime());

            Appointment appointment = appointmentMap.get(step);
            if (appointment == null) {
                throw new RuntimeException("fail to find by step " + step);
            }
            appointment.execute(executionContext, localStateForAppointment);
            executionContext.setLocalState(objectMapper.writeValueAsString(localStateForAppointment));


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean shouldRunOnText(String text) {
        return text.equals("Записаться к доктору");
    }


    @Override
    public TelegramUser.botstate getGlobalState() {
        return TelegramUser.botstate.APPOINTMENT_TO_DOCTOR;
    }
}