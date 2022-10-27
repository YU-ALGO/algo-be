package com.stock.yu.downbitbe.food.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.Map;

public class FoodRepositoryImpl implements FoodRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public FoodRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<FoodListResponseDto> findAll(Map<String, Boolean> allergyFilter, Pageable pageable, String keyword) {
        return null;
    }
}
