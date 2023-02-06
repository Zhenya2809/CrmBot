package com.hrytsik.crmbot.commands.appointment;

import com.hrytsik.crmbot.Constants;
import com.hrytsik.crmbot.ExecutionContext;
import com.hrytsik.crmbot.commands.appointment.localstate.LocalStateForAppointment;
import com.hrytsik.crmbot.entity.InlineButton;
import com.hrytsik.crmbot.entity.ReplyButton;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import java.io.IOException;
import java.util.List;

public class SendDoctorSpeciality implements Appointment {

    @Override
    public void execute(ExecutionContext executionContext, LocalStateForAppointment localStateForAppointment) throws IOException {

        List<String> allSpeciality = executionContext.sendListMEthod(
                HttpMethod.GET,
                "http://localhost:8085/api/v1/telegram/getAllSpeciality",
                new ParameterizedTypeReference<List<String>>() {});

        List<InlineButton> inlineButtons = allSpeciality.stream().map(
                e -> new InlineButton(e,
                Constants.site + "choseDoctor/" + e)).toList();
        List<ReplyButton> replyButtons = allSpeciality.stream().map(ReplyButton::new).toList();
        executionContext.buildInlineKeyboard("перейти на сайт для записи", inlineButtons);
        executionContext.buildReplyKeyboard("Продолжить через телеграмм", replyButtons);
        localStateForAppointment.setStep("chose_doctor");
    }
}
