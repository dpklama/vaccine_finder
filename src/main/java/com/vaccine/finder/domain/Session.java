package com.vaccine.finder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Session {

    String date;
    int available_capacity;
    int min_age_limit;
    String vaccine;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAvailable_capacity() {
        return available_capacity;
    }

    public void setAvailable_capacity(int available_capacity) {
        this.available_capacity = available_capacity;
    }

    public int getMin_age_limit() {
        return min_age_limit;
    }

    public void setMin_age_limit(int min_age_limit) {
        this.min_age_limit = min_age_limit;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    @Override
    public String toString() {
        return "Session{" +
                "date='" + date + '\'' +
                ", available_capacity='" + available_capacity + '\'' +
                ", min_age_limit='" + min_age_limit + '\'' +
                ", vaccine='" + vaccine + '\'' +
                '}';
    }
}
