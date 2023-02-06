package com.hrytsik.crmbot.commands.appointment;


import com.hrytsik.crmbot.ExecutionContext;
import com.hrytsik.crmbot.commands.appointment.localstate.LocalStateForAppointment;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class ChoseDataToAppointment implements Appointment {

    @Override
    public void execute(ExecutionContext executionContext, LocalStateForAppointment localStateForAppointment) throws IOException {

        Long docId = localStateForAppointment.getDoctorId();
        String inputMessage = executionContext.getUpdate().getMessage().getText();
        localStateForAppointment.setDate(LocalDate.parse(inputMessage));

        String url= "http://localhost:8085/api/v1/telegram/findavailabletime/"+docId;
        List<String> stringList = executionContext.sendListMEthod(HttpMethod.GET, url, new ParameterizedTypeReference<List<String>>(){});
        executionContext.buildReplyKeyboardWithStringList("Выберите время", stringList);
        localStateForAppointment.setStep("chose_time");


    }
}
