package com.hrytsik.crmbot.entity.dto;

import lombok.Data;

import java.sql.Date;
import java.time.LocalTime;

@Data
public class AppointmentToDoctorDTO {

    private Date date;
    private String time;
    private String doctorSpeciality;
    private String doctorFirstName;
    private String doctorLastName;

}