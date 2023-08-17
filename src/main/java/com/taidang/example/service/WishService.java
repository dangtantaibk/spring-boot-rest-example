package com.taidang.example.service;

import com.taidang.example.dao.jpa.WishRepository;
import com.taidang.example.domain.Wish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/*
 * Sample service to demonstrate what the API would use to get things done
 */
@Service
public class WishService {

    private static final Logger log = LoggerFactory.getLogger(WishService.class);

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    CounterService counterService;

    @Autowired
    GaugeService gaugeService;

    public WishService() {
    }

    public Wish create(Wish wish) {
        return wishRepository.save(wish);
    }

    public Wish get(long id) {
        return wishRepository.findOne(id);
    }

    public void update(Wish wish) {
        wishRepository.save(wish);
    }

    public void delete(Long id) {
        wishRepository.delete(id);
    }

    public Page<Wish> getAll(Integer page, Integer size) {
        Page pages = wishRepository.findAll(new PageRequest(page, size));
        // example of adding to the /metrics
        if (size > 50) {
            counterService.increment("taidang.WishService.getAll.largePayload");
        }
        return pages;
    }
}
