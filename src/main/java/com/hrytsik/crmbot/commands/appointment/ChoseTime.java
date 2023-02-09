package com.hrytsik.crmbot.commands.appointment;


import com.hrytsik.crmbot.ExecutionContext;
import com.hrytsik.crmbot.commands.appointment.localstate.LocalStateForAppointment;
import com.hrytsik.crmbot.entity.ReplyButton;
import com.hrytsik.crmbot.entity.dto.AppointmentDto;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ChoseTime implements Appointment {


    @Override
    public void execute(ExecutionContext executionContext, LocalStateForAppointment localStateForAppointment) throws IOException {
        Long docId = localStateForAppointment.getDoctorId();
        String time = executionContext.getInputText() + ":00";
        String date = localStateForAppointment.getDate().toString();
        Long chatId = executionContext.getChatId();

        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setDate(date);
        appointmentDto.setChatId(chatId);
        appointmentDto.setDocId(docId);
        appointmentDto.setTime(time);

        String url = "http://localhost:8085/api/v1/telegram/createAppointment";

        executionContext.sendRequestWithJson(AppointmentDto.class, appointmentDto, HttpMethod.POST, url);

        executionContext.setLocalState(null);
        executionContext.setGlobalState(null);
        List<ReplyButton> replyButtonList = List.of(new ReplyButton("О нас"),
                new ReplyButton("Специалисты"),
                new ReplyButton("Услуги"),
                new ReplyButton("Наш адрес"),
                new ReplyButton("Мои записи"));

        executionContext.buildReplyKeyboard("О, как так? Тогда нам есть о чем поговорить! \n" +
                "так хочеться рассказать тебе о нас", replyButtonList);


    }
}
