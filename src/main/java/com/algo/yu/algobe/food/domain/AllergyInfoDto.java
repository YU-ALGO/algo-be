package com.algo.yu.algobe.food.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

@Getter
@Setter
@RequiredArgsConstructor
public class AllergyInfoDto {

    private Boolean squid = false;
    private Boolean eggs = false;
    private Boolean chicken = false;
    private Boolean wheat = false;
    private Boolean nuts = false;
    private Boolean milk = false;
    private Boolean pork = false;
    private Boolean beef = false;
    private Boolean clams = false;
    private Boolean sulphite = false;
    private Boolean buckwheat = false;
    private Boolean crab = false;
    private Boolean shrimp = false;
    private Boolean soybean = false;
    private Boolean tomato = false;
    private Boolean fish = false;
    private Boolean sesame = false;
    private Boolean fruit = false;
    private Boolean garlic = false;
    private Boolean vegetable = false;

    public AllergyInfoDto(AllergyInfo allergyInfo) {
        this.beef = allergyInfo.getBeef();
        this.buckwheat = allergyInfo.getBuckwheat();
        this.chicken = allergyInfo.getChicken();
        this.clams = allergyInfo.getClams();
        this.crab = allergyInfo.getCrab();
        this.eggs = allergyInfo.getEggs();
        this.fish = allergyInfo.getFish();
        this.fruit = allergyInfo.getFruit();
        this.garlic = allergyInfo.getGarlic();
        this.milk = allergyInfo.getMilk();
        this.nuts = allergyInfo.getNuts();
        this.pork = allergyInfo.getPork();
        this.sesame = allergyInfo.getSesame();
        this.shrimp = allergyInfo.getShrimp();
        this.soybean = allergyInfo.getSoybean();
        this.squid = allergyInfo.getSquid();
        this.sulphite = allergyInfo.getSulphite();
        this.tomato = allergyInfo.getTomato();
        this.vegetable = allergyInfo.getVegetable();
        this.wheat = allergyInfo.getWheat();
    }

    public AllergyInfo toEntity() {
        return AllergyInfo.builder()
                .beef(this.beef)
                .buckwheat(this.buckwheat)
                .chicken(this.chicken)
                .clams(this.clams)
                .crab(this.crab)
                .eggs(this.eggs)
                .fish(this.fish)
                .fruit(this.fruit)
                .garlic(this.garlic)
                .milk(this.milk)
                .nuts(this.nuts)
                .pork(this.pork)
                .sesame(this.sesame)
                .shrimp(this.shrimp)
                .soybean(this.soybean)
                .squid(this.squid)
                .sulphite(this.sulphite)
                .tomato(this.tomato)
                .vegetable(this.vegetable)
                .wheat(this.wheat)
                .build();
    }

    public Map<String, Boolean> toTrueMap() {
        Field[] fields = this.getClass().getDeclaredFields();
        TreeMap<String, Boolean> allergyInfoMap = new TreeMap<>();
        for(Field field : fields) {
            try {
                boolean value = (Boolean) field.get(this);
                if(value)
                    allergyInfoMap.put(field.getName(), true);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return allergyInfoMap;
    }

    public Map<String, Boolean> toMap() {
        Field[] fields = this.getClass().getDeclaredFields();
        TreeMap<String, Boolean> allergyInfoMap = new TreeMap<>();
        for(Field field : fields) {
            try {
                allergyInfoMap.put(field.getName(), (Boolean) field.get(this));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return allergyInfoMap;
    }
}
