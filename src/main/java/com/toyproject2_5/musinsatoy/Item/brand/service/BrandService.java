package com.toyproject2_5.musinsatoy.Item.brand.service;


import com.toyproject2_5.musinsatoy.Item.brand.dao.BrandDao;
import com.toyproject2_5.musinsatoy.Item.brand.dto.BrandDto;
import com.toyproject2_5.musinsatoy.Item.brand.dto.BrandListDto;
import com.toyproject2_5.musinsatoy.Item.brand.dto.page.BrandPageInfo;
import com.toyproject2_5.musinsatoy.Item.brand.dto.BrandCreateDto;
import com.toyproject2_5.musinsatoy.Item.brand.dto.BrandUpdateDto;
import com.toyproject2_5.musinsatoy.ETC.util.S3FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BrandService {

    private final S3FileService s3fileService;
    private final BrandDao brandDao;


    /*
    * 모든 브랜드의 정보 표시
    * */
    @Transactional(readOnly = true)
    public List<BrandListDto> findBrandMainPage(BrandPageInfo brandPageInfo) throws Exception {
        //조회시 필요한 정보 Map으로 변환
        HashMap<String,Object> map = BrandPageInfo.toHashMap(brandPageInfo);

        //브랜드 개수 조회
        brandPageInfo.brandCountCal(brandDao.countAllBrand());

        //해당 페이지의 브랜드 리스트 조회
        List<BrandListDto> brandList =brandDao.brandListPaging(map);


        for (BrandListDto brandDto : brandList) {
            brandDto.setImg(s3fileService.plusBucketPath(brandDto.getImg()));
        }

        return brandList;
    }

    @Transactional(readOnly = true)
    public BrandDto findById(String id) {
        return brandDao.findById(id);
    }

    /*
    * 브랜드 생성 - 미완
    * */
    public String createBrand(BrandCreateDto brand) throws Exception {

        //이미지 이름.
        String brandImgPath = s3fileService.brandImgPrefixPath(brand.getBrandId(), brand.getImg());
        brand.setImg(brandImgPath);
        //DE 저장하는 DAO 함수 호출
        brandDao.insert(brand);

        return brandImgPath;
    }

    public int updateBrand(BrandUpdateDto brandUpdateDto)throws Exception{

        //기존 이미지 이름 가져오기.
        //기존 이미지 삭제
        //기존 이미지 이름으로 신규 이미지 로컬 저장 --UUID로 다시 만들 필요 없게.
        // 브랜드 이미지를 여러개로 할 경우 필요할듯.

        return brandDao.update(brandUpdateDto);
    }

}
