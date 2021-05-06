package com.vaccine.finder.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "searchcriteria")
public class SearchCriteria {

    private final Map<String, District> districts;

    public SearchCriteria(Map<String, District> districts) {
        this.districts = districts;
    }

    public Map<String, District> getDistricts() {
        return districts;
    }

    public static class District {
        private int agelimit;
        private List<String> pincodes;
        private List<String> mailinglist;
        private List<String> cclist;

        public int getAgelimit() {
            return agelimit;
        }

        public void setAgelimit(int agelimit) {
            this.agelimit = agelimit;
        }

        public List<String> getPincodes() {
            return pincodes;
        }

        public void setPincodes(List<String> pincodes) {
            this.pincodes = pincodes;
        }

        public List<String> getMailinglist() {
            return mailinglist;
        }

        public void setMailinglist(List<String> mailinglist) {
            this.mailinglist = mailinglist;
        }

        public List<String> getCclist() {
            return cclist;
        }

        public void setCclist(List<String> cclist) {
            this.cclist = cclist;
        }
    }
}
