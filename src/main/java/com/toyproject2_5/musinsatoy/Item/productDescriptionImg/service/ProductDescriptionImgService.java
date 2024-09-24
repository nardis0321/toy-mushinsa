package com.toyproject2_5.musinsatoy.Item.productDescriptionImg.service;


import com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dao.ProductDescriptionImgDao;
import com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dto.ProductDescriptionImgDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductDescriptionImgService {

    private final ProductDescriptionImgDao productDescriptionImgDao;

    public List<String> findAllByProductDescriptionId(String productDescriptionId) throws Exception {
        List<ProductDescriptionImgDetailDto> descriptionimgList = productDescriptionImgDao.findAllByProductDescriptionId(productDescriptionId);
        List<String> list = new ArrayList<>();
        for (ProductDescriptionImgDetailDto descriptionImg : descriptionimgList) {
           list.add(descriptionImg.getPath());
        }
        return list;
    }
}
