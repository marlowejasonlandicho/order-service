package com.demo.order.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.demo.order.entity.Order;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {

}
