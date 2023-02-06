package com.hrytsik.crmbot.commands;

import com.hrytsik.crmbot.ExecutionContext;
import com.hrytsik.crmbot.entity.TelegramUser;
import com.hrytsik.crmbot.entity.ReplyButton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class Start implements Command {


    @Override
    public void doCommand(ExecutionContext executionContext) throws Exception {

        List<ReplyButton> replyButtonList = List.of(
                new ReplyButton("Начнем \uD83D\uDE09"),
                new ReplyButton("Покажи свой сайт \uD83C\uDF10"),
                new ReplyButton("О нас"));

        executionContext.buildReplyKeyboard("Привіт " + executionContext.getFirstName() + "\n" +
                "Я віртуальний помічник сучасного медичного центру краси і здоров'я  CLINIC_NAME\n" +
                "Чем могу вам помочь?", replyButtonList);
        log.info("command START");
        executionContext.setLocalState(null);
        executionContext.setGlobalState(null);
    }

    @Override
    public boolean shouldRunOnText(String text) {
        return text.equals("/start");
    }

    @Override
    public TelegramUser.botstate getGlobalState() {
        return TelegramUser.botstate.START;
    }
}