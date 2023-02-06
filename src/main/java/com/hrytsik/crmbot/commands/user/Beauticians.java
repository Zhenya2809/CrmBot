package com.hrytsik.crmbot.commands.user;



import com.hrytsik.crmbot.ExecutionContext;
import com.hrytsik.crmbot.commands.Command;
import com.hrytsik.crmbot.entity.ReplyButton;
import com.hrytsik.crmbot.entity.TelegramUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class Beauticians implements Command {

    @Override
    public void doCommand(ExecutionContext executionContext) {


        List<ReplyButton> replyButtonList = List.of(
                new ReplyButton("Записаться к косметологу"),
                new ReplyButton("Специалисты"),
                new ReplyButton("Услуги"),
                new ReplyButton("Наш адрес"),
                new ReplyButton("Главное меню"));
        executionContext.buildReplyKeyboard("Вы можете задать все вопросы нашему администратору", replyButtonList);
        executionContext.setLocalState(null);
        executionContext.setGlobalState(null);


    }

    @Override
    public boolean shouldRunOnText(String text) {
        return text.equals("Косметологи");
    }

    @Override
    public TelegramUser.botstate getGlobalState() {
        return TelegramUser.botstate.BEAUTICIANS;
    }
}