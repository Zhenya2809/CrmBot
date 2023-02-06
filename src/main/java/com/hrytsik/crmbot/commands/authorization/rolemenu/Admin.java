package com.hrytsik.crmbot.commands.authorization.rolemenu;



import com.hrytsik.crmbot.ExecutionContext;
import com.hrytsik.crmbot.entity.ReplyButton;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class Admin implements ChoseRole {
    @Override
    public void execute(ExecutionContext executionContext) {
        log.info("SEND ROLE MENU - ADMIN");
        List<ReplyButton> replyButtonList = List.of(
                new ReplyButton("Вход"),
                new ReplyButton("Отправить уведомления"));
        executionContext.buildReplyKeyboard("Привет " + executionContext.getLastName() + " \n" +
                "не забудь войти, выйти с акаунта", replyButtonList);

    }
}
