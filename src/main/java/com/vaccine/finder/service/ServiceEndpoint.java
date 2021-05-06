package com.vaccine.finder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * endpoint to test service on demand
 */
@RestController
public class ServiceEndpoint {

    @Autowired
    VaccineFinderService vaccineFinderService;

    @Autowired
    MailServiceImpl mailService;

    @GetMapping(path = "/find")
    public String find() {
       return vaccineFinderService.findVaccinationCenters();
    }
}
