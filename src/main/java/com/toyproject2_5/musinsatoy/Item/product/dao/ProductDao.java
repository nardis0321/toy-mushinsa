package com.toyproject2_5.musinsatoy.Item.product.dao;

import com.toyproject2_5.musinsatoy.Item.product.dto.*;
import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.adminEdit.ProductEditPageDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.cursor.ProductCursorPageDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.*;
import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.hasNextOffset.SearchProductDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;


public interface ProductDao{

    int insert(@Param("product") Product product) throws Exception;


    String findNameById(@Param("productId") String productId)throws Exception;
    String findRepImgById(@Param("productId") String productId)throws Exception;

    ProductDetailDto findProductDetailById(@Param("productId") String productId) throws Exception;
    ProductPageDto findProductPageDtoByProductId(String productId) throws Exception;

    List<ProductAdminList> findProductAdminList(ProductEditPageDto productEditPageDto) throws Exception;
    int countProductAdminList(ProductEditPageDto productEditPageDto) throws Exception;

    int countSearchProduct(SearchProductDto searchProductDto)throws Exception;
    List<ProductPageDto> findProductByKeyword(SearchProductDto searchProductDto) throws Exception;

    List<ProductCursorPageDto> findCursorList(HashMap<String, Object> map) throws Exception;
    List<ProductCursorPageDto> findCursorPageListOrderByRank(HashMap<String, Object> map) throws Exception;
    List<ProductCursorPageDto> findCursorPageListOrderBySales(HashMap<String, Object> map) throws Exception;
    List<ProductCursorPageDto> findCursorPageListByHighPrice(HashMap<String, Object> map) throws Exception;
    List<ProductCursorPageDto> findCursorPageListByLowPrice(HashMap<String, Object> map) throws Exception;
    List<ProductCursorPageDto> findCursorPageListByLastest(HashMap<String, Object> map) throws Exception;


    List<ProductPageDto> findPageList(HashMap<String, Object> map) throws Exception;

    int countProduct(HashMap<String, Object> map) throws Exception;


    int updateProduct(@Param("product") ProductUpdateDto productUpdateDto) throws Exception;
    int updateViewCount(@Param("productId")String productId) throws Exception;

    int deleteByProductId(@Param("productId") String productId) throws Exception;



}
