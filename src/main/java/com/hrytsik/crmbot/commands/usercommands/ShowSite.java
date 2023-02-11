package com.hrytsik.crmbot.commands.usercommands;



import com.hrytsik.crmbot.Constants;
import com.hrytsik.crmbot.ExecutionContext;
import com.hrytsik.crmbot.commands.Command;
import com.hrytsik.crmbot.entity.InlineButton;
import com.hrytsik.crmbot.entity.TelegramUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ShowSite implements Command {

    @Override
    public void doCommand(ExecutionContext executionContext) {


        List<InlineButton> inlineButtons = List.of(new InlineButton("Наш сайт", Constants.site));
        executionContext.buildInlineKeyboard("Перейдите на наш сайт", inlineButtons);
        executionContext.setLocalState(null);
        executionContext.setGlobalState(null);
    }

    @Override
    public boolean shouldRunOnText(String text) {
        return text.equals("Покажи свой сайт \uD83C\uDF10");
    }

    @Override
    public TelegramUser.botstate getGlobalState() {
        return TelegramUser.botstate.SHOW_SITE;
    }
}