package com.hrytsik.crmbot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "b_user")
public class TelegramUser {

    @Id
    @Column(name = "chatId", nullable = false)
    private Long chatId;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "localState")
    private String localeState;
    @Enumerated(EnumType.STRING)
    @Column(name = "globalState")
    public botstate globalState;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "role", columnDefinition = "varchar(20) default 'USER'")
    private String role;
    @Column(name = "authorizationToken")
    private String authorizationToken;

    public TelegramUser(Long chatId, String firstName, String lastName) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public enum botstate {
        START,
        ADMIN_MENU,
        SEND_ALL_MESSAGE,
        START_BOT_CHATTING,
        ABOUT,
        ADDRESS,
        BEAUTICIANS,
        CALL_BACK,
        CONTACT,
        COSMETICS,
        DOCTORS,
        SERVICES,
        SHOW_SITE,
        MY_APPOINTMENTS,
        SPECIALISTS,
        APPOINTMENT_TO_DOCTOR,
        SEND_APPOINTMENT_FOR_TODAY



    }

    @Override
    public String toString() {
        return String.format("chatID:: ,%s, firstName:: ,%s,  lastName: ,%s,", this.chatId, this.firstName, this.lastName);
    }
}

