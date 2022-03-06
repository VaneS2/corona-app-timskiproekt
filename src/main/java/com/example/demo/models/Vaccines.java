package com.example.demo.models;

import lombok.Data;

@Data
public class Vaccines {

    Integer updated;
    Integer cases;
    Integer todayCases;
    Integer deaths;
    Integer todayDeaths;
    Integer recovered;
    Integer todayRecovered;
    Integer active;
    Integer critical;
    Integer casesPerOneMillion;
    Integer deathsPerOneMillion;
    Integer tests;

    public Vaccines(Integer updated, Integer cases, Integer todayCases, Integer deaths, Integer todayDeaths, Integer recovered, Integer todayRecovered, Integer active, Integer critical, Integer casesPerOneMillion, Integer deathsPerOneMillion, Integer tests) {
        this.updated = updated;
        this.cases = cases;
        this.todayCases = todayCases;
        this.deaths = deaths;
        this.todayDeaths = todayDeaths;
        this.recovered = recovered;
        this.todayRecovered = todayRecovered;
        this.active = active;
        this.critical = critical;
        this.casesPerOneMillion = casesPerOneMillion;
        this.deathsPerOneMillion = deathsPerOneMillion;
        this.tests = tests;
    }

    public Vaccines() {
    }
}
