package com.toyproject2_5.musinsatoy.salesorder.service;

import com.toyproject2_5.musinsatoy.salesorder.dao.*;
import com.toyproject2_5.musinsatoy.salesorder.dto.SalesOrderDto;
import com.toyproject2_5.musinsatoy.salesorder.dto.SalesOrderProductOptionDTOwithOrderIdAndProductId;
import com.toyproject2_5.musinsatoy.salesorder.entity.SalesOrder;
import com.toyproject2_5.musinsatoy.ETC.util.pagination.CursorRequest;
import com.toyproject2_5.musinsatoy.ETC.util.pagination.CursorResponse;
import com.toyproject2_5.musinsatoy.salesorder.entity.SalesOrderProduct;
import com.toyproject2_5.musinsatoy.salesorder.entity.SalesOrderProductOption;
import com.toyproject2_5.musinsatoy.salesorder.mapper.SalesOrderDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class SalesOrderServiceImpl implements SalesOrderService {
    @Autowired
    SalesOrderDao salesOrderDao;
    @Autowired
    SalesOrderProductDao salesOrderProductDao;
    @Autowired
    SalesOrderProductOptionDao salesOrderProductOptionDao;

    @Autowired
    SalesOrderDtoMapper salesOrderDtoMapper;

    @Transactional
    @Override
    public String addOrder(SalesOrderDto orderDto){
        // Dto -> Entity
        SalesOrder orderEntity = salesOrderDtoMapper.toEntity(orderDto);
        // orderId 만들기
        LocalDateTime orderDate = orderEntity.getOrderDatetime();
        String orderId = orderDate.format(DateTimeFormatter.ofPattern("yyMMddHHmmssSSSSSS"));   // orderId가 pk이므로 중복 검증 - 괜찮을 것 같은데 둬도 되는지? / 시퀀스(날마다 초기화) / 랜덤값 붙이기 고려 / 나중에 다중서버환경 동시성 이슈를 디비 트랜젝션 격리 수준과 락으로 해결해보기
        orderEntity.setOrderId(orderId);
        // insert
        salesOrderDao.insert(orderEntity);
        for(SalesOrderProduct product : orderEntity.getProducts()){
            product.setOrderId(orderId);
            salesOrderProductDao.insert(product);
            for(SalesOrderProductOption option : product.getOptions()){
                option.setOrderId(orderId);
                salesOrderProductOptionDao.insert(option);
            }
        }

        return orderId;
    }

    @Transactional
    @Override
    public SalesOrderDto getOrderDetails(String orderId) {
        SalesOrder orderEntity = salesOrderDao.selectByOrderId(orderId);
        List<SalesOrderProduct> products = salesOrderProductDao.findById(orderId);
        for(SalesOrderProduct product : products){
            SalesOrderProductOptionDTOwithOrderIdAndProductId orderIdAndProductId =
                    new SalesOrderProductOptionDTOwithOrderIdAndProductId(product.getOrderId(), product.getProductId());
            List<SalesOrderProductOption> options = salesOrderProductOptionDao.findById(orderIdAndProductId);
            product.setOptions(options);
        }
        orderEntity.setProducts(products);
        return salesOrderDtoMapper.toDto(orderEntity);
    }

    @Override
    public void updateOrderState(SalesOrderDto orderDto) throws Exception {    // orderId, stateCode만 필요
        if (orderDto.orderId() == null) {
            throw new Exception("주문 상태 수정에 필요한 값이 없습니다");
        }
        SalesOrder orderEntity = salesOrderDtoMapper.toEntity(orderDto);
        salesOrderDao.updateState(orderEntity);
    }

    @Override
    public CursorResponse<SalesOrderDto> getOrders(int memberId, CursorRequest<String> cursorRequestByStringKey) throws Exception {
        List<SalesOrder> orderEntities;
        List<SalesOrderDto> orderDtos = new ArrayList<>();
        try {
            // 주문 목록 조회
            if (cursorRequestByStringKey.hasKey())      // key가 있음
                orderEntities = salesOrderDao.selectAllByLessThanIdAndMemberIdAndOrderByDesc(cursorRequestByStringKey.key(), memberId, cursorRequestByStringKey.size());
            else                                        // key가 없음 - 가장 최신부터 조회
                orderEntities = salesOrderDao.selectAllByMemberIdAndOrderByDesc(memberId, cursorRequestByStringKey.size());

            // 주문 상품 조회
            for (SalesOrder order : orderEntities) {
                List<SalesOrderProduct> products = salesOrderProductDao.findById(order.getOrderId());

                // 상품 옵션 조회
                for(SalesOrderProduct product : products){
                    SalesOrderProductOptionDTOwithOrderIdAndProductId orderIdAndProductId =
                            new SalesOrderProductOptionDTOwithOrderIdAndProductId(product.getOrderId(), product.getProductId());
                    List<SalesOrderProductOption> options = salesOrderProductOptionDao.findById(orderIdAndProductId);
                    product.setOptions(options);
                }
                order.setProducts(products);

                // dto로 변환
                orderDtos.add(salesOrderDtoMapper.toDto(order));
            }

        } catch (Exception e) {
            throw new Exception("주문 목록 조회에 실패했습니다", e.initCause(e.getCause()));
        }

        // 다음 키값 찾기
        String nextKey = orderDtos.stream()
                .map(SalesOrderDto::orderId).min(Comparator.naturalOrder()) // desc 정렬 -> 가장 작은 값이 cursor
                .orElse(null);    // 주어진 Key 없을 때

        return new CursorResponse<>(cursorRequestByStringKey.next(nextKey), orderDtos);
    }

    @Override
    public void deleteAll() {
        salesOrderProductOptionDao.deleteAll();
        salesOrderProductDao.deleteAll();
        salesOrderDao.deleteAll();
    }

}
