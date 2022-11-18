package com.stock.yu.downbitbe.food.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

public class AllergyUtils {
        public final static Map<String, String> allergyDict= new HashMap<String, String>() {
                {
                        put("squid", "오징어");
                        put("eggs", "난류");
                        put("chicken", "닭");
                        put("wheat", "밀");
                        put("nuts", "견과류");
                        put("milk", "우유");
                        put("pork", "돼지고기");
                        put("beef", "소고기");
                        put("clams", "조개류");
                        put("sulphite", "아황산류");
                        put("buckwheat", "메밀");
                        put("crab", "게");
                        put("shrimp", "새우");
                        put("soybean", "대두");
                        put("tomato", "토마토");
                        put("fish", "생선");
                        put("sesame", "참깨");
                        put("fruit", "과일");
                        put("garlic", "마늘");
                        put("vegetable", "채소");
                }
        };

        public static String mapToString(Map<String, Boolean> allergyInfo) {
                StringBuilder stringBuilder = new StringBuilder();
                allergyInfo.keySet().forEach((key) -> {
                        stringBuilder.append(allergyDict.get(key));
                        stringBuilder.append(" ");
                });
                return stringBuilder.toString().trim().replaceAll(" ", ", ");
        }
}
