package com.hrytsik.crmbot.entity;

import lombok.Data;

@Data
public class DataToAppointment {
    String email;
    String date;
    String time;
    String doctorID;
    TelegramUser user;
}
