package com.hrytsik.crmbot.commands.usercommands;



import com.hrytsik.crmbot.ExecutionContext;
import com.hrytsik.crmbot.commands.Command;
import com.hrytsik.crmbot.entity.ReplyButton;
import com.hrytsik.crmbot.entity.TelegramUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class Contact implements Command {
    @Override
    public void doCommand(ExecutionContext executionContext) {

        executionContext.replyMessage("Найти нас очень просто!\n" +
                "Киев, ул. Вацлава Гавела 9а \n" +
                "или позвонить нам " +
                "\uD83D\uDCDE +38(077)-777-77-77");
        executionContext.sendAddress(30.425590655889714, 50.44593677173056);
        List<ReplyButton> replyButtonList = List.of(new ReplyButton("Заказать обратный звонок"), new ReplyButton("Главное меню"));
        executionContext.buildReplyKeyboard("Наши номера:", replyButtonList);
        executionContext.replyMessage("+38077-777-77-77 - Администратор\n" +
                "+38066-666-66-66- Приемная" +
                "E-mail\n" +
                "info@clinicName.com.ua\n" +
                "Режим работы\n" +
                "Пн - Пт: с 8:00 до 20:00\n" +
                "Сб - Вс: с 9:00 до 17:00");
        executionContext.setLocalState(null);
        executionContext.setGlobalState(null);


    }

    @Override
    public boolean shouldRunOnText(String text) {
        return text.equals("Контакты");
    }

    @Override
    public TelegramUser.botstate getGlobalState() {
        return TelegramUser.botstate.CONTACT;
    }
}
