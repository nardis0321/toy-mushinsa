package com.toyproject2_5.musinsatoy.Item.product.dao;

import com.toyproject2_5.musinsatoy.Item.product.dto.*;
import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.adminEdit.ProductEditPageDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.cursor.ProductCursorPageDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.hasNextOffset.SearchProductDto;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductDaoMysql implements ProductDao {

    private final SqlSession sqlSession;

    private String namespace ="productMapper.";


    @Override
    public int insert(Product product) throws Exception{
        return sqlSession.insert(namespace+"insert", product);
    }

    @Override
    public String findNameById(String productId) throws Exception {
        return sqlSession.selectOne(namespace+"findNameById", productId);
    }

    @Override
    public String findRepImgById(String productId) throws Exception {
        return sqlSession.selectOne(namespace+"findRepImgById", productId);
    }


    @Override
    public ProductDetailDto findProductDetailById(String productId) throws Exception {
        return sqlSession.selectOne(namespace + "findProductDetailDtoById", productId);
    }

    // 주문 리스트/상세에 사용
    @Override
    public ProductPageDto findProductPageDtoByProductId(String productId) throws Exception {
        return sqlSession.selectOne(namespace + "findProductPageDtoByProductId", productId);
    }

    @Override
    public List<ProductAdminList> findProductAdminList(ProductEditPageDto productEditPageDto) throws Exception {
        return sqlSession.selectList(namespace + "findProductAdminList", productEditPageDto);
    }

    @Override
    public int countProductAdminList(ProductEditPageDto productEditPageDto) throws Exception {
        return sqlSession.selectOne(namespace + "countProductAdminList", productEditPageDto);
    }

    @Override
    public int countSearchProduct(SearchProductDto searchProductDto) throws Exception{
        return sqlSession.selectOne(namespace + "countSearchProduct", searchProductDto);
    }

    @Override
    public List<ProductPageDto> findProductByKeyword(SearchProductDto searchProductDto) throws Exception {
        return sqlSession.selectList(namespace + "findProductByKeyword", searchProductDto);
    }

    @Override
    public List<ProductCursorPageDto> findCursorList(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList(namespace + "cursorPaging", map);
    }

    @Override
    //별점 순
    public List<ProductCursorPageDto> findCursorPageListOrderByRank(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList(namespace + "cursorRankingPage", map);
    }

    @Override
    //판매량 순
    public List<ProductCursorPageDto> findCursorPageListOrderBySales(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList(namespace + "cursorSalesQuantityPage", map);
    }

    @Override
    public List<ProductCursorPageDto> findCursorPageListByHighPrice(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList(namespace + "cursorHighPricePage", map);
    }

    @Override
    public List<ProductCursorPageDto> findCursorPageListByLowPrice(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList(namespace + "cursorLowPricePage", map);
    }

    @Override
    public List<ProductCursorPageDto> findCursorPageListByLastest(HashMap<String, Object> map) throws Exception {
        return sqlSession.selectList(namespace + "cursorLatestPage", map);
    }


    //페이지 기반
    //총 상품 개수 / 현재 페이지  / 보여줄 페이지 / 페이지당 사이즈
    //offset, count, page, size
    @Override
    public List<ProductPageDto> findPageList(HashMap<String, Object> pageMap) throws Exception {
        return sqlSession.selectList(namespace + "pagePaging", pageMap);
    }
    @Override
    public int countProduct(HashMap<String, Object> countMap) throws Exception {
        return sqlSession.selectOne(namespace + "countProduct", countMap);
    }

    @Override
    public int updateProduct(ProductUpdateDto productUpdateDto) throws Exception {
        return sqlSession.update(namespace+"updateProduct", productUpdateDto);
    }

    @Override
    public int updateViewCount(String productId) throws Exception {
        return sqlSession.update(namespace+"updateViewCount", productId);
    }


    @Override
    public int deleteByProductId(String productId) throws Exception {
        return sqlSession.delete(namespace + "deleteByProductId", productId);
    }


}
