package com.hrytsik.crmbot.commands.appointment;


import com.hrytsik.crmbot.ExecutionContext;
import com.hrytsik.crmbot.commands.appointment.localstate.LocalStateForAppointment;
import com.hrytsik.crmbot.entity.dto.AppointmentDto;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.time.LocalDate;

public class ChoseTime implements Appointment {


    @Override
    public void execute(ExecutionContext executionContext, LocalStateForAppointment localStateForAppointment) throws IOException {
        Long docId = localStateForAppointment.getDoctorId();
        String time = executionContext.getInputText()+":00";
        String date = localStateForAppointment.getDate().toString();
        Long chatId = executionContext.getChatId();

        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setDate(date);
        appointmentDto.setChatId(chatId);
        appointmentDto.setDocId(docId);
        appointmentDto.setTime(time);

        String url="http://localhost:8085/api/v1/telegram/createAppointment";

        executionContext.sendRequestWithJson(AppointmentDto.class,appointmentDto, HttpMethod.POST,url);

        executionContext.setLocalState(null);
        executionContext.setGlobalState(null);

    }
}
