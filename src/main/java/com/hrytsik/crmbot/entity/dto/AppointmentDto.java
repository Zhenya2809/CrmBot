package com.hrytsik.crmbot.entity.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AppointmentDto {
    private Long docId;
    private Long chatId;
    private String date;
    private String time;

}
