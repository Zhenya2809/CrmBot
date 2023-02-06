package com.hrytsik.crmbot.service;

import com.hrytsik.crmbot.entity.TelegramUser;

import java.util.List;
import java.util.Optional;

public interface TelegramUserService {
    Optional<TelegramUser> findTelegramUserByChatId(Long chatId);

    void createTelegramUser(Long chatId, String firstName, String lastName, String role);

    void save(TelegramUser user);


    List<Long> findAll();
    List<TelegramUser> findAllByRole(String role);
}
