package com.vaccine.finder.service;

import com.vaccine.finder.config.SearchCriteria;
import com.vaccine.finder.domain.Center;
import com.vaccine.finder.domain.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VaccineFinderService {

    Logger logger = LoggerFactory.getLogger(VaccineFinderService.class);

    private static final String COWIN_URI = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode={pincode}&date={date}";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    SearchCriteria searchCriteria;

    @Autowired
    MailService mailService;

    @Scheduled(cron = "${cron.expression}")
    public String findVaccinationCenters() {
        try {
            List<Center> centers = new ArrayList<>();
            Map<String, String> uriVariables = new HashMap<>();
            Map<String, Center> commonCenters = new HashMap<>();
            Map<String, List<Center>> districtGroup = new HashMap<>();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36");
            HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
            this.searchCriteria.getDistricts().forEach((name, district) -> {
                district.getPincodes().forEach(pincode -> {
                    //check for 7 days
                    Calendar calendar = Calendar.getInstance();
                    uriVariables.put("pincode", pincode);
                    uriVariables.put("date", simpleDateFormat.format(calendar.getTime()));
                    ResponseEntity<Response> responseEntity = this.restTemplate.exchange(COWIN_URI, HttpMethod.GET, httpEntity, Response.class, uriVariables);
                    Response response = responseEntity.getBody();
                    if (response != null) {
                        // remove slots with 0 capacity and not in age limit
                        response.getCenters().forEach(center -> {
                            center.getSessions().removeIf(next -> next.getAvailable_capacity() == 0 || !district.getAgelimit().contains(next.getMin_age_limit()));
                        });
                        //remove center with 0 slots
                        response.getCenters().removeIf(center -> center.getSessions().isEmpty());
                        centers.addAll(response.getCenters());
                    }
                    //check for 7+7 days
                    calendar.add(Calendar.DATE, 7);
                    uriVariables.put("date", simpleDateFormat.format(calendar.getTime()));
                    responseEntity = this.restTemplate.exchange(COWIN_URI, HttpMethod.GET, httpEntity, Response.class, uriVariables);
                    response = responseEntity.getBody();
                    if (response != null) {
                        // remove slots with 0 capacity and not in age limit
                        response.getCenters().forEach(center -> {
                            center.getSessions().removeIf(next -> next.getAvailable_capacity() == 0 || !district.getAgelimit().contains(next.getMin_age_limit()));
                        });
                        //remove center with 0 slots
                        response.getCenters().removeIf(center -> center.getSessions().isEmpty());
                        centers.addAll(response.getCenters());
                    }
                });
            });

            // merge all common centers
            if (!centers.isEmpty()) {
                centers.forEach(center -> {
                    String key = center.getName();
                    if (!commonCenters.containsKey(key)) {
                        commonCenters.put(key, center);
                    } else {
                        commonCenters.get(key).getSessions().addAll(center.getSessions());
                    }
                });
            }
            // group centers by district
            commonCenters.forEach((name, center) -> {
                String key = center.getDistrict_name();
                if (!districtGroup.containsKey(key)) {
                    districtGroup.put(key, new ArrayList<>());
                }
                districtGroup.get(key).add(center);
            });
            if (!districtGroup.isEmpty()) {
                this.logger.info("Vaccinations available. Sending mail.....");
                this.mailService.sendMail(districtGroup);
                return "Vaccinations available. Check mail for more details";
            } else {
                String stmt = "No vaccination slots available";
                this.logger.info(stmt);
                return stmt;
            }
        } catch (Exception e) {
            String stmt = "Exception occurred while finding vaccination centers: ";
            this.logger.error(stmt, e);
            return stmt + e.getMessage();
        }
    }
}
