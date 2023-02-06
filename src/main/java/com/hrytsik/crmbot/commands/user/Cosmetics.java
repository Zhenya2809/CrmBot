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
public class Cosmetics implements Command {
    @Override
    public void doCommand(ExecutionContext executionContext) {


        List<ReplyButton> replyButtonList = List.of(new ReplyButton("текст1"), new ReplyButton("Главное меню"));
        executionContext.buildReplyKeyboard("TextToKeyboard", replyButtonList);
        executionContext.setLocalState(null);
        executionContext.setGlobalState(null);


    }

    @Override
    public boolean shouldRunOnText(String text) {
        return text.equals("Косметика");
    }

    @Override
    public TelegramUser.botstate getGlobalState() {
        return TelegramUser.botstate.COSMETICS;
    }
}
