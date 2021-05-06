package com.vaccine.finder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {

    List<Center> centers;

    public List<Center> getCenters() {
        return centers;
    }

    public void setCenters(List<Center> centers) {
        this.centers = centers;
    }

    @Override
    public String toString() {
        return "Response{" +
                "centers=" + centers +
                '}';
    }
}
