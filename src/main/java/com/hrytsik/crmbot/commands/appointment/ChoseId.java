package com.hrytsik.crmbot.commands.appointment;




import com.hrytsik.crmbot.ExecutionContext;
import com.hrytsik.crmbot.commands.appointment.localstate.LocalStateForAppointment;
import com.hrytsik.crmbot.entity.ReplyButton;
import com.hrytsik.crmbot.entity.dto.DoctorDto;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ChoseId implements Appointment {


    @Override
    public void execute(ExecutionContext executionContext, LocalStateForAppointment localStateForAppointment) throws IOException {


        LocalDate today = LocalDate.now();
        String inputMessage = executionContext.getUpdate().getMessage().getText();
        String[] split = inputMessage.split(" ");
        String firstName = split[0];
        String lastName = split[1];
        ;
        localStateForAppointment.setDoctorId(executionContext.sendMethod(DoctorDto.class, HttpMethod.GET,"http://localhost:8085/api/v1/telegram//finddoctorBy/"+firstName+"/"+lastName).getBody().getId());
        List<ReplyButton> replyButtons = List.of(new ReplyButton(today.toString()),
                new ReplyButton(today.plusDays(1).toString()),
                new ReplyButton(today.plusDays(2).toString()),
                new ReplyButton(today.plusDays(3).toString()),
                new ReplyButton(today.plusDays(4).toString()));
        executionContext.buildReplyKeyboard("На какое число вы хотите записаться?", replyButtons);
        localStateForAppointment.setStep("chose_data_to_appointment");


    }
}
