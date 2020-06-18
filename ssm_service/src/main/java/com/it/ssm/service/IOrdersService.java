package com.it.ssm.service;

import com.it.ssm.domain.Orders;

import java.util.List;

public interface IOrdersService {
    List<Orders> findAll(int page,int pageSize) throws Exception;

    Orders findById(String ordersId) throws Exception;
}
