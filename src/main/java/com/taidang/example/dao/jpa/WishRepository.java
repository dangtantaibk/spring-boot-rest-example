package com.taidang.example.dao.jpa;

import com.taidang.example.domain.Hotel;
import com.taidang.example.domain.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Repository can be used to delegate CRUD operations against the data source: http://goo.gl/P1J8QH
 */
public interface WishRepository extends PagingAndSortingRepository<Wish, Long> {
    Page findAll(Pageable pageable);
}
