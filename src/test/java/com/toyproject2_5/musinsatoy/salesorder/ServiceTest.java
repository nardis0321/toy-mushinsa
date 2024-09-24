package com.toyproject2_5.musinsatoy.salesorder;

import com.toyproject2_5.musinsatoy.ETC.util.pagination.CursorRequest;
import com.toyproject2_5.musinsatoy.ETC.util.pagination.CursorResponse;
import com.toyproject2_5.musinsatoy.salesorder.dto.*;
import com.toyproject2_5.musinsatoy.salesorder.entity.*;
import com.toyproject2_5.musinsatoy.salesorder.mapper.SalesOrderDtoMapper;
import com.toyproject2_5.musinsatoy.salesorder.service.SalesOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ServiceTest {
    @Autowired
    SalesOrderService service;
    @Autowired
    SalesOrderDtoMapper mapper;

    @Test
    public void simpleDeleteTest(){
        service.deleteAll();
    }

    @Test
    public void simpleAddTest() throws Exception {
        service.addOrder(mapper.toDto(generateOrder()));
    }

    @Test
    public void addAndGetDetailsTest() throws Exception {
        service.deleteAll();

        SalesOrder order = generateOrder();
        String orderId = service.addOrder(mapper.toDto(order));

        SalesOrderDto retrieved = service.getOrderDetails(orderId);

        // 주문 검증
        assertNotNull(retrieved);
        assertEquals(orderId, retrieved.orderId());
        assertEquals(order.getMemberId(), retrieved.memberId());
        assertEquals(order.getPhone(), retrieved.phone());
        assertEquals(order.getPaymentAmount(), retrieved.paymentAmount());

        // 상품 검증
        List<SalesOrderProduct> originalProducts = order.getProducts();
        List<SalesOrderProductDto> retrievedProducts = retrieved.products();
        assertNotNull(retrievedProducts);
        assertEquals(originalProducts.size(), retrievedProducts.size());

        // 상품 검증을 위해 list -> map 변환
        Map<String, SalesOrderProduct> originalProductMap = originalProducts.stream()
                .collect(Collectors.toMap(SalesOrderProduct::getProductId, Function.identity()));
        Map<String, SalesOrderProductDto> retrievedProductMap = retrievedProducts.stream()
                .collect(Collectors.toMap(SalesOrderProductDto::productId, Function.identity()));

        for (String productId : originalProductMap.keySet()) {
            SalesOrderProduct originalProduct = originalProductMap.get(productId);
            SalesOrderProductDto retrievedProduct = retrievedProductMap.get(productId);

            assertNotNull(retrievedProduct);
            assertEquals(originalProduct.getProductId(), retrievedProduct.productId());
            assertEquals(originalProduct.getPrice(), retrievedProduct.price());
            assertEquals(originalProduct.getAmount(), retrievedProduct.amount());

            // 주문 상품의 옵션 검증
            List<SalesOrderProductOption> originalOptions = originalProduct.getOptions();
            List<SalesOrderProductOptionDto> retrievedOptions = retrievedProduct.options();
            assertNotNull(retrievedOptions);
            assertEquals(originalOptions.size(), retrievedOptions.size());

            // 옵션 리스트를 정렬
            Comparator<SalesOrderProductOption> optionComparator = Comparator
                    .comparing(SalesOrderProductOption::getProductId)
                    .thenComparing(SalesOrderProductOption::getOptionDepth);

            Comparator<SalesOrderProductOptionDto> optionDtoComparator = Comparator
                    .comparing(SalesOrderProductOptionDto::productId)
                    .thenComparing(SalesOrderProductOptionDto::optionDepth);

            originalOptions.sort(optionComparator);
            retrievedOptions.sort(optionDtoComparator);

            // 옵션 검증
            for (int j = 0; j < originalOptions.size(); j++) {
                SalesOrderProductOption originalOption = originalOptions.get(j);
                SalesOrderProductOptionDto retrievedOption = retrievedOptions.get(j);

                assertNotNull(retrievedOption);
                assertEquals(originalOption.getProductId(), retrievedOption.productId());
                assertEquals(originalOption.getOptionCategory(), retrievedOption.optionCategory());
                assertEquals(originalOption.getOptionName(), retrievedOption.optionName());
                assertEquals(originalOption.getOptionDepth(), retrievedOption.optionDepth());
            }
        }

    }

    @Test
    public void addAndGetListTest() throws Exception {
        // 삭제 작업과 검증
        service.deleteAll();
        CursorRequest<String> emptyCursorRequest = new CursorRequest<>(null, 10);
        CursorResponse<SalesOrderDto> emptyOrders = service.getOrders(1, emptyCursorRequest);
        assertNotNull(emptyOrders);
        assertTrue(emptyOrders.body().isEmpty());

        // 주문 추가
        int cursorSize = 10;
        int addUnit = cursorSize/2;
        List<SalesOrderDto> originalOrdersFirstPage = new ArrayList<>();
        List<SalesOrderDto> originalOrdersSecondPage = new ArrayList<>();
        for(int i=0; i<addUnit; i++){
            SalesOrderDto order = mapper.toDto(generateOrder());
            originalOrdersSecondPage.add(order);

            service.addOrder(order);
        }
        for(int i=0; i<cursorSize; i++){
            SalesOrderDto order = mapper.toDto(generateOrder());
            originalOrdersFirstPage.add(order);

            service.addOrder(order);
            System.out.println("order = " + order.toString());
            System.out.println();
        }
        System.out.println();

        // 첫번째 페이지 조회와 검증
        CursorRequest<String> cursorRequest = new CursorRequest<>(null, cursorSize);
        CursorResponse<SalesOrderDto> cursorResponse = service.getOrders(1, cursorRequest);

        assertNotNull(cursorResponse);
        assertFalse(cursorResponse.body().isEmpty());
        assertEquals(cursorSize, cursorResponse.body().size());

        Collections.reverse(originalOrdersFirstPage); // cursorResponse는 최신순 조회
        for(int i=0; i<cursorSize; i++){
            SalesOrderDto original = originalOrdersFirstPage.get(i);
            SalesOrderDto retrieved = cursorResponse.body().get(i);

            // 주문 검증
            assertEquals(original.memberId(), retrieved.memberId());
            assertEquals(original.recipient(),retrieved.recipient());
            assertEquals(original.phone(), retrieved.phone());
            assertEquals(original.addressA(), retrieved.addressA());
            assertEquals(original.addressB(), retrieved.addressB());
            assertEquals(original.postcode(), retrieved.postcode());
            assertEquals(original.deliveryRequest(), retrieved.deliveryRequest());
            assertEquals(original.paymentId(), retrieved.paymentId());
            assertEquals(original.paymentMethod(), retrieved.paymentMethod());
            assertEquals(original.paymentAmount(), retrieved.paymentAmount());

            // 상품 검증
            List<SalesOrderProductDto> originalProducts = original.products();
            List<SalesOrderProductDto> retrievedProducts = retrieved.products();
            assertNotNull(retrievedProducts);
            assertEquals(originalProducts.size(), retrievedProducts.size());

            // 상품 검증을 위해 list -> map 변환
            Map<String, SalesOrderProductDto> originalProductMap = originalProducts.stream()
                    .collect(Collectors.toMap(SalesOrderProductDto::productId, Function.identity()));
            Map<String, SalesOrderProductDto> retrievedProductMap = retrievedProducts.stream()
                    .collect(Collectors.toMap(SalesOrderProductDto::productId, Function.identity()));

            for (String productId : originalProductMap.keySet()) {
                SalesOrderProductDto originalProduct = originalProductMap.get(productId);
                SalesOrderProductDto retrievedProduct = retrievedProductMap.get(productId);

                assertNotNull(retrievedProduct);
                assertEquals(originalProduct.productId(), retrievedProduct.productId());
                assertEquals(originalProduct.price(), retrievedProduct.price());
                assertEquals(originalProduct.amount(), retrievedProduct.amount());

                // 주문 상품의 옵션 검증
                List<SalesOrderProductOptionDto> originalOptions = originalProduct.options();
                List<SalesOrderProductOptionDto> retrievedOptions = retrievedProduct.options();
                assertNotNull(retrievedOptions);
                 assertEquals(originalOptions.size(), retrievedOptions.size());

                // 옵션 리스트를 정렬
                Comparator<SalesOrderProductOptionDto> optionComparator = Comparator
                        .comparing(SalesOrderProductOptionDto::productId)
                        .thenComparing(SalesOrderProductOptionDto::optionDepth);

                Comparator<SalesOrderProductOptionDto> optionDtoComparator = Comparator
                        .comparing(SalesOrderProductOptionDto::productId)
                        .thenComparing(SalesOrderProductOptionDto::optionDepth);

                originalOptions.sort(optionComparator);
                retrievedOptions.sort(optionDtoComparator);

                // 옵션 검증
                for (int j = 0; j < originalOptions.size(); j++) {
                    SalesOrderProductOptionDto originalOption = originalOptions.get(j);
                    SalesOrderProductOptionDto retrievedOption = retrievedOptions.get(j);

                    assertNotNull(retrievedOption);
                    assertEquals(originalOption.productId(), retrievedOption.productId());
                    assertEquals(originalOption.optionCategory(), retrievedOption.optionCategory());
                    assertEquals(originalOption.optionName(), retrievedOption.optionName());
                    assertEquals(originalOption.optionDepth(), retrievedOption.optionDepth());
                }
            }
        }

        // 두 번째 페이지 조회와 검증
        CursorRequest<String> secondPageCursorRequest = new CursorRequest<>((String) cursorResponse.nextCursorRequest().key(), cursorSize);
        CursorResponse<SalesOrderDto> secondPageCursorResponse = service.getOrders(1, secondPageCursorRequest);

        assertNotNull(secondPageCursorResponse);
        assertFalse(secondPageCursorResponse.body().isEmpty());
        assertEquals(addUnit, secondPageCursorResponse.body().size());

        Collections.reverse(originalOrdersSecondPage); // cursorResponse는 최신순 조회
        for(int i=0; i<addUnit; i++){
            SalesOrderDto original = originalOrdersSecondPage.get(i);
            SalesOrderDto retrieved = secondPageCursorResponse.body().get(i);

            // 주문 검증
            assertEquals(original.memberId(), retrieved.memberId());
            assertEquals(original.recipient(),retrieved.recipient());
            assertEquals(original.phone(), retrieved.phone());
            assertEquals(original.addressA(), retrieved.addressA());
            assertEquals(original.addressB(), retrieved.addressB());
            assertEquals(original.postcode(), retrieved.postcode());
            assertEquals(original.deliveryRequest(), retrieved.deliveryRequest());
            assertEquals(original.paymentId(), retrieved.paymentId());
            assertEquals(original.paymentMethod(), retrieved.paymentMethod());
            assertEquals(original.paymentAmount(), retrieved.paymentAmount());

            // 상품 검증
            List<SalesOrderProductDto> originalProducts = original.products();
            List<SalesOrderProductDto> retrievedProducts = retrieved.products();
            assertNotNull(retrievedProducts);
            assertEquals(originalProducts.size(), retrievedProducts.size());

            // 상품 검증을 위해 list -> map 변환
            Map<String, SalesOrderProductDto> originalProductMap = originalProducts.stream()
                    .collect(Collectors.toMap(SalesOrderProductDto::productId, Function.identity()));
            Map<String, SalesOrderProductDto> retrievedProductMap = retrievedProducts.stream()
                    .collect(Collectors.toMap(SalesOrderProductDto::productId, Function.identity()));

            for (String productId : originalProductMap.keySet()) {
                SalesOrderProductDto originalProduct = originalProductMap.get(productId);
                SalesOrderProductDto retrievedProduct = retrievedProductMap.get(productId);

                assertNotNull(retrievedProduct);
                assertEquals(originalProduct.productId(), retrievedProduct.productId());
                assertEquals(originalProduct.price(), retrievedProduct.price());
                assertEquals(originalProduct.amount(), retrievedProduct.amount());

                // 주문 상품의 옵션 검증
                List<SalesOrderProductOptionDto> originalOptions = originalProduct.options();
                List<SalesOrderProductOptionDto> retrievedOptions = retrievedProduct.options();
                assertNotNull(retrievedOptions);
                assertEquals(originalOptions.size(), retrievedOptions.size());

                // 옵션 리스트를 정렬
                Comparator<SalesOrderProductOptionDto> optionComparator = Comparator
                        .comparing(SalesOrderProductOptionDto::productId)
                        .thenComparing(SalesOrderProductOptionDto::optionDepth);

                originalOptions.sort(optionComparator);
                retrievedOptions.sort(optionComparator);

                // 옵션 검증
                for (int j = 0; j < originalOptions.size(); j++) {
                    SalesOrderProductOptionDto originalOption = originalOptions.get(j);
                    SalesOrderProductOptionDto retrievedOption = retrievedOptions.get(j);

                    assertNotNull(retrievedOption);
                    assertEquals(originalOption.productId(), retrievedOption.productId());
                    assertEquals(originalOption.optionCategory(), retrievedOption.optionCategory());
                    assertEquals(originalOption.optionName(), retrievedOption.optionName());
                    assertEquals(originalOption.optionDepth(), retrievedOption.optionDepth());
                }
            }
        }
    }


    @Test
    public void addAndUpdateTest() throws Exception {
        SalesOrder entity = generateOrder();
        String orderId = service.addOrder(mapper.toDto(entity));

        SalesOrderDto retrieved = service.getOrderDetails(orderId);
        int newStateCode = 3;
        assertNotEquals(newStateCode, retrieved.stateCode());

        entity.setOrderId(orderId);
        entity.setStateCode(newStateCode);
        service.updateOrderState(mapper.toDto(entity));

        retrieved = service.getOrderDetails(orderId);
        assertEquals(entity.getStateCode(), retrieved.stateCode());
    }

    /**
     * ---------------테스트를 위한 dummy data 만드는 메서드들 ----------------------
     */

    // productId는 db에 저장된 리스트를 사용할 것 (혹은 연동한 코드로 바꾸기)
    private static final String[] PRODUCT_IDS = {
            "2459458", "2459459", "2635319", "2708684", "2758362", "2758363", "2786316", "2786317", "2786320", "2814800",
    };
    private static final String[] SIZES = {"S", "M", "L", "XL", "XXL"};
    private static final String[] COLORS = {"Red", "Green", "Blue", "White", "Black"};
    private static final String[] PAYMENT_METHODS = {"신용카드", "계좌이체", "간편결제"};

    // 위의 요소들 중 랜덤한 값을 선택하기 위한 메서드
    private static String randomElement(String[] array) {
        return array[(int) (Math.random() * array.length)];
    }

    // option generator
    private static List<SalesOrderProductOption> generateRandomOptions(String productId) {
//        int optionsSize = (int) (Math.random() * 2) + 1;
        List<SalesOrderProductOption> options = new ArrayList<>();
        SalesOrderProductOption option = new SalesOrderProductOption();

        Random random = new Random();
        if(random.nextBoolean()) {
            option.setProductId(productId);
            option.setOptionId("test");
            option.setOptionCategory("사이즈");
            option.setOptionName(randomElement(SIZES));
            option.setOptionDepth("0");
            options.add(option);
        }
        if(random.nextBoolean()) {
            option.setProductId(productId);
            option.setOptionId("test");
            option.setOptionCategory("컬러");
            option.setOptionName(randomElement(COLORS));
            option.setOptionDepth("1");
            options.add(option);
        }
        return options;
    }

    // product generator
    private static List<SalesOrderProduct> generateRandomProducts() {
        int numProducts = (int) (Math.random() * 5) + 1; // 1~5개의 제품을 생성
        List<SalesOrderProduct> products = new ArrayList<>();
        Set<String> productIds = new HashSet<>();
        while(productIds.size()<numProducts){
            productIds.add(randomElement(PRODUCT_IDS));
        }

        while(productIds.iterator().hasNext()){
            String productId = productIds.iterator().next();

            SalesOrderProduct product = new SalesOrderProduct();
            product.setProductId(productId);
            product.setPrice((int) (Math.random() * 100000) + 10000); // 가격: 10000~110000
            product.setAmount((int) (Math.random() * 5) + 1); // 수량: 1~5개
            product.setOptions(generateRandomOptions(productId));
            products.add(product);

            productIds.remove(productId);
        }
        return products;
    }

    // orderGenerator
    private static SalesOrder generateOrder() {
        int randomNumber = (int)(Math.random()*100);

        SalesOrder salesOrder = new SalesOrder();
        // 제외된 값 : orderId, stateCode
        salesOrder.setMemberId(1);
        salesOrder.setOrderDatetime(LocalDateTime.now());
        salesOrder.setRecipient("Recipient " + randomNumber);
        salesOrder.setPhone("010" + String.format("%08d", (int) (Math.random() * 100000000)));
        salesOrder.setAddressA("Address A " + randomNumber);
        salesOrder.setAddressB("Address B " + randomNumber);
        salesOrder.setPostcode(String.format("%05d", (int) (Math.random() * 100000)));
        salesOrder.setDeliveryRequest("Request " + randomNumber);
        salesOrder.setPaymentId(1L + randomNumber);
        salesOrder.setPaymentMethod(randomElement(PAYMENT_METHODS));

        List<SalesOrderProduct> products = generateRandomProducts();
        salesOrder.setProducts(products);

        // 총 결제 금액 계산
        int paymentAmount = products.stream()
                .mapToInt(p -> p.getPrice() * p.getAmount())
                .sum();
        salesOrder.setPaymentAmount(paymentAmount);

        return salesOrder;
    }
}