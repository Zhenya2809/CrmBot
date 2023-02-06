package com.hrytsik.crmbot;


import com.google.gson.Gson;
import com.hrytsik.crmbot.entity.*;
import com.hrytsik.crmbot.service.TelegramUserService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Getter
@Setter
@Slf4j
@Component
public class ExecutionContext {
    private RestTemplate restTemplate = new RestTemplate();

    private MyBot myBot;
    public TelegramUser.botstate GlobalState;
    private Long chatId;
    private String firstName;
    private String lastName;
    private String inputText;
    private Update update;
    private TelegramUserService telegramUserService;
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public void setGlobalState(TelegramUser.botstate newState) {
        Optional<TelegramUser> dataUserByChatId = telegramUserService.findTelegramUserByChatId(chatId);
        if (dataUserByChatId.isPresent()) {
            TelegramUser user = dataUserByChatId.get();
            user.setGlobalState(newState);
            telegramUserService.save(user);
        }
    }

    public String getLocalState() {
        Optional<TelegramUser> dataUserByChatId = telegramUserService.findTelegramUserByChatId(chatId);
        if (dataUserByChatId.isEmpty()) {
            return "пользователь не найден";
        }
        return dataUserByChatId.get().getLocaleState();
    }

    public String setLocalState(String newState) {
        Optional<TelegramUser> dataUserByChatId = telegramUserService.findTelegramUserByChatId(chatId);
        if (dataUserByChatId.isPresent()) {
            TelegramUser telegramUser = dataUserByChatId.get();
            telegramUser.setLocaleState(newState);
            telegramUserService.save(telegramUser);
        }
        return newState;
    }

    public void buildReplyKeyboard(String responseMessage, List<ReplyButton> buttonNames) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(responseMessage);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        buttonNames.forEach(e -> {
            KeyboardRow row = new KeyboardRow();

            row.add(new KeyboardButton(e.getReplyMesasge()));
            keyboardRowList.add(row);

        });

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        // Add it to the message
        message.setReplyMarkup(replyKeyboardMarkup);
        execute(message);
    }

    public String printDateAndState() {
        Date date = new Date();
        // Вывод текущей даты и cостояний
        return date + ":      GlobalState:  " + getGlobalState() + "   LocalState:  " + getLocalState();
    }

    public TelegramUser getAuthorizationUser() {
        Optional<TelegramUser> telegramUserByChatId = telegramUserService.findTelegramUserByChatId(chatId);
        if (telegramUserByChatId.isPresent()) {
            return telegramUserByChatId.get();
        }
        throw new RuntimeException("user not found");

    }

    public void getContactKeyboard() {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Поделись своим номером телефона:");

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();


        KeyboardRow row = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText("поделиться номером телефона");
        keyboardButton.setRequestContact(true);
//            keyboardButton.setRequestLocation(true);
        row.add(keyboardButton);
        keyboardRowList.add(row);


        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        // Add it to the message
        message.setReplyMarkup(replyKeyboardMarkup);
        execute(message);

    }

    public void sendMessageToUserWithId(String messageText, String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);
        execute(message);
    }

    public TelegramUser getUser() {
        Optional<TelegramUser> dataUserByChatId = telegramUserService.findTelegramUserByChatId(chatId);
        if (dataUserByChatId.isPresent()) {
            return dataUserByChatId.get();
        }
        throw new RuntimeException("user not found");
    }

    public void replyMessage(String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(messageText);
        execute(message);
    }

    public void buildInlineKeyboard(String replyMessage, List<InlineButton> inlineButtons) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(getChatId()));
        message.setText(replyMessage);
        // Create InlineKeyboardMarkup object
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        //Создаём клавиатуру (list of InlineKeyboardButton list)
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        // Создаём лист для кнопок
        List<InlineKeyboardButton> buttons = new ArrayList<InlineKeyboardButton>();

        inlineButtons.forEach((e) -> {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setUrl(e.getUrl());
            inlineKeyboardButton.setText(e.getText());
            buttons.add(inlineKeyboardButton);
        });

        keyboard.add(buttons);
        inlineKeyboardMarkup.setKeyboard(keyboard);
        // Add it to the message
        message.setReplyMarkup(inlineKeyboardMarkup);
        execute(message);

    }

    public void sendAddress(double longitude, double latitude) {
        SendLocation sendLocation = new SendLocation();
        sendLocation.setChatId(String.valueOf(chatId));
        sendLocation.setLongitude(longitude);
        sendLocation.setLatitude(latitude);
        execute(sendLocation);
    }

    public void execute(SendMessage message) {
        try {
            myBot.execute(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void execute(SendLocation location) {
        try {
            myBot.execute(location);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void execute(SendPhoto sendPhoto) {
        try {
            myBot.execute(sendPhoto);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void execute(SendPoll sendPool) {
        try {
            myBot.execute(sendPool);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public AuthorizationData sendPOST(String POST_URL) throws IOException {
        String USER_AGENT = "Chrome/109.0.0.0";
        String POST_PARAMS = "userName=TelegramBot";
        URL obj = new URL(POST_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);

// For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
// For POST only - END

        int responseCode = con.getResponseCode();
        log.info("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

// print result
            String jsonString = response.toString();

            Gson g = new Gson();
            AuthorizationData data = g.fromJson(jsonString, AuthorizationData.class);
            log.info(data.toString());
            return data;
        } else {
            throw new RuntimeException("POST request did not work.");
        }
    }


    public HttpResponse sendGet(String URL) throws Exception {
        updateToken();
        Optional<TelegramUser> telegramUser = telegramUserService.findTelegramUserByChatId(chatId);
        if (telegramUser.isPresent()) {
            TelegramUser user = telegramUser.get();
            String authorizationToken = user.getAuthorizationToken();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(URL))
                    .setHeader("Authorization", authorizationToken)
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());


            log.info("Response status code: " + response.statusCode());
            log.info("Response: " + response.body());
            return response;
        }
        throw new RuntimeException();
    }

    public void getPatient() {
        Optional<TelegramUser> user = telegramUserService.findTelegramUserByChatId(chatId);
        if (user.isPresent()) {
            TelegramUser telegramUser = user.get();
            String firstName1 = telegramUser.getFirstName();
            String lastName1 = telegramUser.getLastName();
            String phone = telegramUser.getPhone();
            String email = telegramUser.getEmail();
            String role = telegramUser.getRole();

        }

    }

    public <T> ResponseEntity<T> sendMethod(Class<T> tClass, HttpMethod methods, String url) throws IOException {
        String bearerToken = updateToken();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", bearerToken);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        return restTemplate.exchange(url, methods, httpEntity, tClass);

    }

    public <T> ResponseEntity<T> sendRequestWithJson(Class<T> tClass, Object o, HttpMethod httpMethod, String url) throws IOException {
        String bearerToken = updateToken();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", bearerToken);

        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        Gson g = new Gson();
        String s1 = g.toJson(o);


        HttpEntity<String> httpEntity = new HttpEntity<>(s1, headers);

        return restTemplate.exchange(url, httpMethod, httpEntity, tClass);

    }
    public <T> ResponseEntity<T> sendRequestWithJsonWithoutHeader(Class<T> tClass, Object o, HttpMethod httpMethod, String url){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        Gson g = new Gson();
        String s1 = g.toJson(o);
        HttpEntity<String> httpEntity = new HttpEntity<>(s1);
        return restTemplate.exchange(url, httpMethod, httpEntity, tClass);

    }
    public <T> T sendListMEthod(HttpMethod methods, String url, ParameterizedTypeReference<T> type) throws IOException {
        String bearerToken = updateToken();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", bearerToken);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<T> exchange = restTemplate.exchange(url, methods, httpEntity, type);


        return exchange.getBody();
    }

    public String updateToken() throws IOException {

        Optional<TelegramUser> telegramUserByChatId = telegramUserService.findTelegramUserByChatId(chatId);
        if (telegramUserByChatId.isPresent()) {
            TelegramUser me = telegramUserByChatId.get();

            String role = me.getRole();
            String phone = me.getPhone();
            AuthorizationData authorizationData = sendPOST("http://localhost:8085/api/v1/auth/loginTelegram/" + phone + "/ROLE_" + role);

            me.setAuthorizationToken("Bearer_" + authorizationData.getToken());
            telegramUserService.save(me);
            log.info("Auth token updated");
            return "Bearer_" + authorizationData.getToken();
        }
        log.error("Token error updated");
        throw new RuntimeException("Token error updated");

    }

    public void replyImage(String photoLink) {

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId.toString());
        InputFile inputFile = new InputFile();
        inputFile.setMedia(photoLink);
        sendPhoto.setPhoto(inputFile);
        sendPhoto.setParseMode("*bold \\*text*");
        execute(sendPhoto);
    }

    public void buildReplyKeyboardWithStringList(String responseMessage, List<String> buttonNames) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(responseMessage);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        buttonNames.forEach(e -> {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(e));
            keyboardRowList.add(row);

        });

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        // Add it to the message
        message.setReplyMarkup(replyKeyboardMarkup);
        execute(message);
    }

}
