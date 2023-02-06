package com.hrytsik.crmbot.commands.appointment;




import com.hrytsik.crmbot.ExecutionContext;
import com.hrytsik.crmbot.commands.appointment.localstate.LocalStateForAppointment;
import com.hrytsik.crmbot.commands.authorization.rolemenu.Doctor;
import com.hrytsik.crmbot.entity.ReplyButton;
import com.hrytsik.crmbot.entity.dto.DoctorDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.List;

public class ChoseDoctor implements Appointment {

    @Override
    public void execute(ExecutionContext executionContext, LocalStateForAppointment localStateForAppointment) throws IOException {

        String inputMessage = executionContext.getUpdate().getMessage().getText();
        String url="";
        List<DoctorDto> doctorsBySpeciality = executionContext.sendListMEthod(HttpMethod.GET, url, new ParameterizedTypeReference<List<DoctorDto>>() {
        });
                doctorsBySpeciality.forEach(e -> {
            String speciality = e.getSpeciality();
            String fio = e.getDoctorFirstName()+" "+ e.getDoctorLastName();
            executionContext.replyImage(e.getLinkPhoto());
            executionContext.replyMessage(e.getAbout());
            executionContext.replyMessage(speciality + " " + fio);
        });
        List<ReplyButton> doctorsFIOListForButton = doctorsBySpeciality.stream().map(e -> new ReplyButton(e.getDoctorFirstName()+" "+e.getDoctorLastName())).toList();
        executionContext.buildReplyKeyboard("Выберите доктора", doctorsFIOListForButton);
        localStateForAppointment.setStep("chose_id");


    }
}
