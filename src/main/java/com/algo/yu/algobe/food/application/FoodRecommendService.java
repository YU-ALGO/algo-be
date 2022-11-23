package com.algo.yu.algobe.food.application;

import com.algo.yu.algobe.food.domain.FoodRecommendIdListDto;
import com.algo.yu.algobe.food.domain.AllergyInfoDto;
import com.algo.yu.algobe.food.domain.FoodListResponseDto;
import com.algo.yu.algobe.food.domain.FoodRepository;
import com.algo.yu.algobe.security.config.Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class FoodRecommendService {
    private final FoodRepository foodRepository;

    public List<Long> getRecommendedFoodList(List<Long> likeList, List<Long> viewList, AllergyInfoDto allergyInfoDto) {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(5000);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        HttpEntity entity = new HttpEntity(httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(Config.PY_BASE_URL).append(Config.PY_METHOD);
        uriBuilder.append("?favorites=");
        uriBuilder.append(likeList.toString());
        uriBuilder.append("&recently_viewed=");
        uriBuilder.append(viewList.toString());
        uriBuilder.append("&allergy=");

        Map<String, Boolean> map = allergyInfoDto.toMap();
        log.info(map.keySet());
        for (String key : map.keySet()) {
            if(map.get(key))
                uriBuilder.append(1);
            else
                uriBuilder.append(0);
        }
        log.info("uri : " + uriBuilder.toString());
        log.info("likeList : " + likeList.toString());
        log.info("recently : " + viewList.toString());

        ResponseEntity<FoodRecommendIdListDto> responseEntity = restTemplate.exchange(uriBuilder.toString(), HttpMethod.GET, entity, FoodRecommendIdListDto.class);

       return responseEntity.getBody().getRecommendList();
    }

    public List<FoodListResponseDto> getRecommendedFoodListDto(List<Long> recommendedFoodList){
        return foodRepository.findViewFoodsByFoodId(recommendedFoodList);
    }

}
