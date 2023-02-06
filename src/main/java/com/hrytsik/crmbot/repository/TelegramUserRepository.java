package com.hrytsik.crmbot.repository;


import com.hrytsik.crmbot.entity.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    TelegramUser findDataUserByChatId(Long id);

    Optional<TelegramUser> findDataUserTgByChatId(Long id);

    List<TelegramUser> findAllByRole(String role);

}
