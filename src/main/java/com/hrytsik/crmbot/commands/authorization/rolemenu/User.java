package com.hrytsik.crmbot.commands.authorization.rolemenu;


import com.hrytsik.crmbot.ExecutionContext;
import com.hrytsik.crmbot.entity.ReplyButton;
import com.hrytsik.crmbot.entity.TelegramUser;
import com.hrytsik.crmbot.service.TelegramUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class User implements ChoseRole {

    @Override
    public void execute(ExecutionContext executionContext) {
        log.info("SEND ROLE MENU - USER");
        List<ReplyButton> replyButtonList = List.of(new ReplyButton("О нас"),
                new ReplyButton("Специалисты"),
                new ReplyButton("Услуги"),
                new ReplyButton("Наш адрес"),
                new ReplyButton("Мои записи"));

        executionContext.buildReplyKeyboard("О, как так? Тогда нам есть о чем поговорить! \n" +
                "так хочеться рассказать тебе о нас", replyButtonList);


        executionContext.setLocalState(null);
        executionContext.setGlobalState(null);


    }
}
