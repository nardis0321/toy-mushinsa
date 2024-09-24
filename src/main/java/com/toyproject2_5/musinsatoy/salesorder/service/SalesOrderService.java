package com.toyproject2_5.musinsatoy.salesorder.service;

import com.toyproject2_5.musinsatoy.ETC.util.pagination.*;
import com.toyproject2_5.musinsatoy.salesorder.dto.SalesOrderDto;

public interface SalesOrderService {
    String addOrder(SalesOrderDto order) throws Exception;
    SalesOrderDto getOrderDetails(String orderId) throws Exception;
    void updateOrderState(SalesOrderDto order) throws Exception;
    CursorResponse<SalesOrderDto> getOrders(int memberId, CursorRequest<String> cursorRequestByStringKey) throws Exception;
    void deleteAll();
}