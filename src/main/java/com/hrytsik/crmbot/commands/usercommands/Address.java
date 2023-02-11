package com.hrytsik.crmbot.commands.usercommands;



import com.hrytsik.crmbot.ExecutionContext;
import com.hrytsik.crmbot.commands.Command;
import com.hrytsik.crmbot.commands.authorization.Authorization;
import com.hrytsik.crmbot.entity.TelegramUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class Address implements Command {
    @Autowired
    Authorization authorization;

    @Override
    public void doCommand(ExecutionContext executionContext) throws TelegramApiException {


        executionContext.replyMessage("Найти нас очень просто!\n" +
                "Киев, ул. Вацлава Гавела 9а \n" +
                "или позвонить нам " +
                "\uD83D\uDCDE +38(077)-777-77-77");
        executionContext.sendAddress(30.425590655889714, 50.44593677173056);

        authorization.doCommand(executionContext);
        executionContext.setLocalState(null);
        executionContext.setGlobalState(null);


    }

    @Override
    public boolean shouldRunOnText(String text) {
        return text.equals("Наш адрес");
    }

    @Override
    public TelegramUser.botstate getGlobalState() {
        return TelegramUser.botstate.ADDRESS;
    }
}
