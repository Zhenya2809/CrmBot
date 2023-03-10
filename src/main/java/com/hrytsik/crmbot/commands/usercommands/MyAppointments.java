package com.hrytsik.crmbot.commands.usercommands;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrytsik.crmbot.ExecutionContext;
import com.hrytsik.crmbot.commands.Command;
import com.hrytsik.crmbot.entity.TelegramUser;
import com.hrytsik.crmbot.entity.dto.AppointmentToDoctorDTO;
import com.hrytsik.crmbot.service.TelegramUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class MyAppointments implements Command {
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
    @Autowired
    TelegramUserService telegramUserService;

    @Override
    public void doCommand(ExecutionContext executionContext) {


        try {

            executionContext.updateToken();
            Optional<TelegramUser> telegramUser = telegramUserService.findTelegramUserByChatId(executionContext.getChatId());
            if (telegramUser.isPresent()) {
                TelegramUser user = telegramUser.get();
                String authorizationToken = user.getAuthorizationToken();
                HttpRequest request = HttpRequest.newBuilder()
                        .GET()
                        .uri(URI.create("http://localhost:8085/api/v1/telegram/getAppointmentsToDoctor/"+executionContext.getChatId()))
                        .setHeader("Authorization", authorizationToken)
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());


                ObjectMapper mapper = new ObjectMapper();
                List<AppointmentToDoctorDTO> appointmentToDoctorDTOS = mapper.readValue(response.body(), new TypeReference<>() {
                });
                appointmentToDoctorDTOS.forEach(e ->
                        {
                            String doctorSpeciality = e.getDoctorSpeciality();
                            String doctorFirstName = e.getDoctorFirstName();
                            String doctorLastName = e.getDoctorLastName();
                            Date date = e.getDate();
                            String time = e.getTime();
                            java.util.Date today = new java.util.Date();
                            if (date.after(today) || (date.equals(today))) {
                                executionContext.replyMessage(
                                        "?????????????? ??????:                       \n" + doctorSpeciality + "\n" +
                                                doctorFirstName + " " + doctorLastName + "\n" +
                                                "????????: " + date + "\n" +
                                                "??????: " + time
                                );
                            }
                        }
                );

                log.info("Response status code: " + response.statusCode());
                log.info("Response: " + response.body());


            }

            executionContext.setLocalState(null);
            executionContext.setGlobalState(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public boolean shouldRunOnText(String text) {
        return text.equals("?????? ????????????");
    }

    @Override
    public TelegramUser.botstate getGlobalState() {
        return TelegramUser.botstate.MY_APPOINTMENTS;
    }
}