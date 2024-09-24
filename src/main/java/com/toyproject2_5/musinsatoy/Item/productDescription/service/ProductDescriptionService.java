package com.toyproject2_5.musinsatoy.Item.productDescription.service;


import com.toyproject2_5.musinsatoy.Item.productDescription.dao.ProductDescriptionDao;
import com.toyproject2_5.musinsatoy.Item.productDescription.dto.ProductDescriptionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class ProductDescriptionService {

    private final ProductDescriptionDao productDescriptionDao;



    public ProductDescriptionDto findById(String productDescriptionId) throws Exception {
        return productDescriptionDao.findById(productDescriptionId);
    }


}
