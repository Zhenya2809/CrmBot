package com.hrytsik.crmbot.inlinequerry;


import com.hrytsik.crmbot.MyBot;
import com.hrytsik.crmbot.entity.TelegramUser;
import com.hrytsik.crmbot.entity.dto.TelegramUserDto;
import com.hrytsik.crmbot.repository.TelegramUserRepository;
import com.hrytsik.crmbot.service.TelegramUserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class InlineTelegramBot {
    @Autowired
    private TelegramUserService userService;
    private static final Integer CACHETIME = 1;

    public void handleIncomingInlineQuery(InlineQuery inlineQuery, MyBot myAppBot) {
        String query = inlineQuery.getQuery();
        log.info("Searching: " + query);
        try {
            myAppBot.execute(convertResultsToResponse(inlineQuery));
        } catch (Throwable e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    private AnswerInlineQuery convertResultsToResponse(InlineQuery inlineQuery) {
        AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
        answerInlineQuery.setInlineQueryId(inlineQuery.getId());
        log.info("answerID=" + inlineQuery.getId());
        answerInlineQuery.setCacheTime(CACHETIME);
        answerInlineQuery.setIsPersonal(true);
        answerInlineQuery.setResults(convertResults(inlineQuery.getQuery()));
        return answerInlineQuery;
    }

    private List<InlineQueryResult> convertResults(String query) {

        List<TelegramUser> allUsersForAdmin = userService.getAllUsersForAdmin(query.toLowerCase(Locale.ROOT));
        List<InlineQueryResult> results = new ArrayList<>();

        Random random = new Random();
        long number = random.nextInt(1000000000) + 1;
        for (TelegramUser user : allUsersForAdmin) {

            InlineQueryResultArticle article = new InlineQueryResultArticle();
            InputTextMessageContent messageContent = new InputTextMessageContent();

            messageContent.setDisableWebPagePreview(true);
            messageContent.setParseMode(ParseMode.MARKDOWN);
            messageContent.setMessageText(user.getRole());
            article.setInputMessageContent(messageContent);
//            article.setInputMessageContent(new InputTextMessageContent("Result: " + query));
            article.setId(Long.toString(number));
            article.setTitle(user.getPhone());
            article.setDescription(user.getFirstName() + " " + user.getLastName());
            article.setThumbUrl("https://i.ibb.co/nnbCS6w/thumb-img-5710add72d38b-resize-0-215.jpg");
            results.add(article);

        }

        return results;
    }
}
