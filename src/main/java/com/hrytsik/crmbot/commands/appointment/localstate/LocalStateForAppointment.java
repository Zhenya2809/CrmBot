package com.hrytsik.crmbot.commands.appointment.localstate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalStateForAppointment {
    private Long doctorId;
    private String step;
    private LocalDate date;
}
