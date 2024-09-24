package com.toyproject2_5.musinsatoy.Item.product.dao;

import com.toyproject2_5.musinsatoy.Item.product.dto.Product;
import com.toyproject2_5.musinsatoy.Item.product.dto.ProductDetailDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.ProductPageDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.ProductUpdateDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.hasNextOffset.SearchProductDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@Transactional
class ProductDaoMysqlTest {

    @Autowired
    private ProductDao productDao;


    @Test
    @DisplayName("간단한 입력 테스트")
    public void  insertTest1() throws Exception {
        Product product = Product.builder()
                .productOptionDepth(2)
                .productId("Paaaaaa")
                .productDescriptionId("NIKE000000001")
                .name("asdf")
                .repImg("/ss/ss/ss/.img")
                .brandId("NIKE")
                .categoryId("C01")
                .registerManager("manager1")
                .build();

        productDao.insert(product);
        assertTrue(productDao.findNameById(product.getProductId()).equals(product.getName()));

    }

    @Test
    @DisplayName("productId로 DetailDto 찾아오기")
    void searchProduct1() throws Exception {

        Product product = Product.builder()
                .productOptionDepth(2)
                .productId("asdf")
                .price(1234)
                .productDescriptionId("NIKE000000001")
                .name("ALLREUES")
                .repImg("Asdfasdf")
                .brandId("NIKE")
                .categoryId("C01")
                .registerManager("asdf")
                .build();

        productDao.insert(product);

        ProductDetailDto productDetailDto = productDao.findProductDetailById(product.getProductId());

        assertTrue(productDetailDto.getProductId().equals(product.getProductId()));
        assertTrue(productDetailDto.getName().equals(product.getName()));

        System.out.println(productDetailDto);

    }

    @Test
    @DisplayName("제품 수정")
    void updateProduct1() throws Exception {

        Product product = Product.builder()
                .productOptionDepth(2)
                .productId("asdf")
                .price(1234)
                .productDescriptionId("NIKE000000001")
                .name("ALLREUES")
                .repImg("Asdfasdf")
                .brandId("NIKE")
                .categoryId("C01")
                .registerManager("asdf")
                .build();

        productDao.insert(product);

        ProductUpdateDto productUpdateDto1 = new ProductUpdateDto();

        productUpdateDto1.setProductId(product.getProductId());
        productUpdateDto1.setName("상품 이름 수정");

        productDao.updateProduct(productUpdateDto1);
        assertTrue(productDao.findNameById(product.getProductId()).equals("상품 이름 수정"));

        ProductUpdateDto productUpdateDto2 = new ProductUpdateDto();
        productUpdateDto2.setProductId(product.getProductId());
        productUpdateDto2.setPrice(1423);
        productUpdateDto2.setName("상품 가격, 이름, 카테고리 수정");
        productDao.updateProduct(productUpdateDto2);
        assertTrue(productDao.findNameById(product.getProductId()).equals("상품 가격, 이름, 카테고리 수정"));


        String deletename =productDao.findNameById(product.getProductId());
    }

    @Test
    @DisplayName("키워드 상품 찾기")
    public void keywordSearch() throws Exception {
        SearchProductDto searchProductDto = new SearchProductDto();

        searchProductDto.setSize(20);
        searchProductDto.setPage(2);
        searchProductDto.setKeyword("티셔츠");

        searchProductDto.calOffset();
        List<ProductPageDto> list = productDao.findProductByKeyword(searchProductDto);
        System.out.println(productDao.countSearchProduct(searchProductDto));
        list.forEach(System.out::println);

        list.forEach(x->assertTrue(x.getProductName().contains(searchProductDto.getKeyword())));

    }
}