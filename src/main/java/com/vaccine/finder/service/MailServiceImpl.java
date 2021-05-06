package com.vaccine.finder.service;

import com.vaccine.finder.config.SearchCriteria;
import com.vaccine.finder.domain.Center;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService {

    Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    SearchCriteria searchCriteria;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendMail(Map<String, List<Center>> districtGroup) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(this.fromEmail);
            message.setSubject("Vaccination Slots Available");
            districtGroup.forEach((district, centers) -> {
                List<String> mailinglist = this.searchCriteria.getDistricts().get(district).getMailinglist();
                message.setTo(mailinglist.toArray(new String[mailinglist.size()]));
                List<String> cclist = this.searchCriteria.getDistricts().get(district).getCclist();
                message.setCc(cclist.toArray(new String[cclist.size()]));
                message.setText(this.getBody(centers));
            });
            this.javaMailSender.send(message);
            this.logger.info("Mail sent successfully");
        } catch (MailException e) {
            this.logger.error("Failed to send mail. ", e);
        }
    }

    private String getBody(List<Center> centers) {
        StringBuilder mailBody = new StringBuilder("Hurry!!! Slots are available at below vaccination centers.\n");
        centers.forEach(center -> {
            mailBody.append("\nCenter Name: ").append(center.getName());
            mailBody.append("\nAddress: ").append(center.getAddress());
            mailBody.append("\nPincode: ").append(center.getPincode());
            mailBody.append("\nSlots:");
            mailBody.append("\nDate");
            mailBody.append("\t\tAvailableCapacity");
            mailBody.append("\tAgeLimit");
            mailBody.append("\tVaccine");
            center.getSessions().forEach(session -> {
                mailBody.append("\n").append(session.getDate());
                mailBody.append("\t").append(session.getAvailable_capacity());
                mailBody.append("\t\t\t\t").append(session.getMin_age_limit());
                mailBody.append("\t\t").append(session.getVaccine());
            });
            mailBody.append("\n");
        });
        return mailBody.toString();
    }
}
