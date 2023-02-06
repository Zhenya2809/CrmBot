package com.hrytsik.crmbot.commands.user;



import com.hrytsik.crmbot.ExecutionContext;
import com.hrytsik.crmbot.commands.Command;
import com.hrytsik.crmbot.entity.InlineButton;
import com.hrytsik.crmbot.entity.ReplyButton;
import com.hrytsik.crmbot.entity.TelegramUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class AboutUs implements Command {
    @Override
    public void doCommand(ExecutionContext executionContext) {


        List<InlineButton> inlineButtons = List.of(new InlineButton("Instagram", "https://instagram.com"), new InlineButton("Facebook", "https://facebook.com"));
        executionContext.buildInlineKeyboard("Возможно тебя заинтересует одна из наших соц. сетей?", inlineButtons);

        List<ReplyButton> replyButtonList = List.of(
                new ReplyButton("Наш адрес"),
                new ReplyButton("Главное меню"));

        executionContext.buildReplyKeyboard("CLINIC_NAME — это частная клиника в Киеве для всей семьи.", replyButtonList);
        executionContext.replyMessage("""
                Мы позаботимся как о новорожденном ребенке, так и о людях почтенного возраста.
                Мы предоставляем медицинские услуги с выездом на дом, в клинике и онлайн,\s
                чтобы всегда держать под контролем ваше хорошее самочувствие.""");

    }

    @Override
    public boolean shouldRunOnText(String text) {
        return text.equals("О нас");
    }

    @Override
    public TelegramUser.botstate getGlobalState() {
        return TelegramUser.botstate.ABOUT;
    }
}