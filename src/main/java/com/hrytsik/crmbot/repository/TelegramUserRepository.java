package com.hrytsik.crmbot.repository;


import com.hrytsik.crmbot.entity.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    TelegramUser findDataUserByChatId(Long id);

    Optional<TelegramUser> findDataUserTgByChatId(Long id);

    List<TelegramUser> findAllByRole(String role);

    @Query(value = "select * from b_user where last_name like :lastName% limit 50", nativeQuery = true)
    Collection<TelegramUser> searchUser(@Param("lastName") String lastName);
}
//"SELECT * FROM b_user WHERE first_name LIKE ':firstName%' AND last_name LIKE ':lastName%';"