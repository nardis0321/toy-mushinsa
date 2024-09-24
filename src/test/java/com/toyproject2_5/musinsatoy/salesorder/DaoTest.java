package com.toyproject2_5.musinsatoy.salesorder;

import com.toyproject2_5.musinsatoy.salesorder.dao.SalesOrderDao;
import com.toyproject2_5.musinsatoy.salesorder.entity.SalesOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DaoTest {
    @Autowired
    SalesOrderDao dao;

    /**
     * 테스트용 메소드 - C - R - U - D 순서
     */

    // 현재 시간으로 orderId값 생성
    private String makeOrderId(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmssSSSSSS");
        return now.format(formatter);
    }

    // 주문 만들기
    private SalesOrder makeSalesOrder() {
        String orderId = makeOrderId();

        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setOrderId(orderId);
        salesOrder.setMemberId(1);
        salesOrder.setOrderDatetime(LocalDateTime.now());
        salesOrder.setStateCode(0);
        salesOrder.setRecipient("John Doe");
        salesOrder.setPhone("01012345678");
        salesOrder.setAddressA("1234 Test Street");
        salesOrder.setAddressB("Apt 101");
        salesOrder.setPostcode("12345");
        salesOrder.setDeliveryRequest("Leave at the door");
        salesOrder.setPaymentId(123456L);
        salesOrder.setPaymentMethod("Credit Card");
        salesOrder.setPaymentAmount(50000);

        return salesOrder;
    }



    /**
     * CREATE 테스트
     */
    @Test
    public void insertTest() {
        // 추가 테스트 (검증 없음)
        String orderId = makeOrderId();

        SalesOrder order = new SalesOrder();
        order.setOrderId(orderId);
        order.setMemberId(1);
        order.setRecipient("패스트");
        order.setPhone("01011112222");
        order.setAddressA("서울특별시 강남구 강남대로 364");
        order.setAddressB("10층");
        order.setPostcode("06241");
        order.setDeliveryRequest("문 앞에 놔주세요");
        order.setPaymentId(111122223333L);
        order.setPaymentMethod("CREDIT_CARD");
        order.setPaymentAmount(50000);

        dao.insert(order);
    }

    @Test
    public void insertTestWithCheck(){
        // 추가 작업
        SalesOrder inserted = makeSalesOrder();   // 하드코딩을 고려
        dao.insert(inserted);

        // 검증  작업
        SalesOrder selected = dao.selectByOrderId(inserted.getOrderId());
        assertEquals(inserted.getOrderId(), selected.getOrderId());

    }


    /**
     * READ 테스트
     */
    @Test
    public void selectTest() {
        // 전체 삭제
        dao.deleteAll();

        SalesOrder order = makeSalesOrder();
        dao.insert(order);

        // 추가한 것을 선택했음을 확인
        SalesOrder selectedOrder = dao.selectByOrderId(order.getOrderId());
        assertNotNull(selectedOrder);
        assertEquals(selectedOrder.getRecipient(), order.getRecipient());
        assertEquals(selectedOrder.getPostcode(), order.getPostcode());
        assertEquals(selectedOrder.getPaymentId(),order.getPaymentId());
    }

    // 사용자 pk로 주문 정보 선택
    @Test
    public void selectByMemberIdTest(){
        // 삭제
        dao.deleteAll();
        // 추가
        SalesOrder inserted = makeSalesOrder();
        String orderId = inserted.getOrderId();
        dao.insert(inserted);
        // 선택해서 비교
        List<SalesOrder> orders = dao.selectAllByMemberIdAndOrderByDesc(1, 5);
        assertEquals(orderId, orders.get(0).getOrderId());
    }

    // row 개수 세기
    @Test
    public void countTest() {
        // 삭제
        dao.deleteAll();
        assertEquals(0, dao.count());

        // 추가
        dao.insert(makeSalesOrder());

        // 개수 비교
        assertEquals(1, dao.count());

        // 여러개 추가해서 비교
        for(int i = 0; i<10; i++){
            dao.insert(makeSalesOrder());
        }
        assertEquals(11, dao.count());
    }

    @Test
    public void testInsertAndSelectAllByLessThanIdAndMemberIdAndOrderByDesc() {
        // Given: 테스트를 위한 SalesOrderDetail 리스트 생성
        SalesOrder insertedFormer = makeSalesOrder();
        dao.insert(insertedFormer);

        SalesOrder insertedLatest = makeSalesOrder();
        dao.insert(insertedLatest);

        // When: 특정 orderId보다 작은 주문 목록을 조회
        String orderId = insertedLatest.getOrderId();
        int memberId = 1;
        int size = 10;

        List<SalesOrder> result = dao.selectAllByLessThanIdAndMemberIdAndOrderByDesc(orderId, memberId, size);

        // Then: 결과를 검증
        assertNotNull(result); // 결과가 null이 아니어야 함
        assertFalse(result.isEmpty()); // 결과 리스트가 비어 있지 않아야 함
        assertTrue(result.stream().allMatch(o -> o.getOrderId().compareTo(orderId) < 0)); // 모든 orderId가 입력된 orderId보다 작은지 확인
    }

    /**
     * UPDATE 테스트
     */
    // 상태를 변경
    @Test
    public void updateStateTest(){
        // 추가 작업, insertion시 state code는 0
        SalesOrder inserted = makeSalesOrder();
        String orderId = inserted.getOrderId();
        dao.insert(inserted);
        SalesOrder insertedOrder = dao.selectByOrderId(orderId);
        System.out.println("insertedOrder = " + insertedOrder);
        assertEquals(0, insertedOrder.getStateCode());

        // state code를 수정해서 비교
        SalesOrder updateInfo = new SalesOrder();
        updateInfo.setOrderId(orderId);
        updateInfo.setStateCode(3);

        dao.updateState(updateInfo);
        assertEquals(3, dao.selectByOrderId(orderId).getStateCode());
    }

    /**
     * DELETE 테스트
     */
    @Test
    public void deleteAllTest(){
        dao.deleteAll();
        assertEquals(0, dao.count());
    }

    @Test
    public void deleteAllTestWithInsert(){
        dao.deleteAll();
        assertEquals(0, dao.count());
        dao.deleteAll();
        dao.insert(makeSalesOrder());
        assertEquals(1, dao.count());
    }

    // 다른 것이 삭제되지는 않는지를 검증하기 추가
    // 실패 테스트 추가
}
