package com.vaccine.finder.service;

import com.vaccine.finder.domain.Center;

import java.util.List;
import java.util.Map;

public interface MailService {
    void sendMail(Map<String, List<Center>> districtGroup);
}
