package com.hrytsik.crmbot.commands.admincommands;


import com.hrytsik.crmbot.ExecutionContext;
import com.hrytsik.crmbot.commands.Command;
import com.hrytsik.crmbot.entity.TelegramUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Slf4j
public class SendAppointments implements Command {

    @Override
    public void doCommand(ExecutionContext executionContext) {

        Date date = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");

        
//        List<AppointmentToDoctors> all = appointmentService.findAll().stream().filter(e -> e.getDate().toString().equals(formatForDateNow.format(date))).toList();
//        all.forEach(e -> {
//
//
//            Long chatId = e.getPatient().getChatId();
//            executionContext.sendMessageToUserWithId("Напоминаем у вас запись к: \n"+e.getDoctor().getFirstName()+" "+e.getDoctor().getLastName() + "\n" + e.getDate() + "\n" + e.getTime(), String.valueOf(chatId));
//        });


    }

    @Override
    public boolean shouldRunOnText(String text) {
        return text.equals("Отправить уведомления");
    }

    @Override
    public TelegramUser.botstate getGlobalState() {
        return TelegramUser.botstate.SEND_APPOINTMENT_FOR_TODAY;
    }
}