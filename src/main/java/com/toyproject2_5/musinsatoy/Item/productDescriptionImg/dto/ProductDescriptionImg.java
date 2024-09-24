package com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dto;

import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class ProductDescriptionImg {

    public static String DEFAULT_USE ="Y";
    public static String DESCRIPTION = "DES";
    public static String REPRESENTATION = "REP";

    private Long productDescriptionImgId;

    private String productDescriptionId;

    private String name;

    private byte orderNum;

    private String path;

    private String isUsed;

    private String kindOf;

    private Long size;





    public static ProductDescriptionImg imageToDescriptionImg(String fileName, String filePath, Long fileBytes , String productDescriptionId, byte order){

        ProductDescriptionImg productDescriptionImg
                = ProductDescriptionImg.builder()
                .productDescriptionId(productDescriptionId)
                .name(fileName)
                .path(filePath)
                .orderNum(order)
                .size(fileBytes)
                .kindOf(ProductDescriptionImg.DESCRIPTION)
                .isUsed(ProductDescriptionImg.DEFAULT_USE)
                .build();

        return productDescriptionImg;
    }

    public static ProductDescriptionImg imageToProductImg(String fileName, String filePath, Long fileBytes , String productDescriptionId, byte order){

        ProductDescriptionImg productDescriptionImg
                = ProductDescriptionImg.builder()
                .productDescriptionId(productDescriptionId)
                .name(fileName)
                .path(filePath)
                .orderNum(order)
                .size(fileBytes)
                .kindOf(ProductDescriptionImg.REPRESENTATION)
                .isUsed(ProductDescriptionImg.DEFAULT_USE)
                .build();

        return productDescriptionImg;
    }

//    public static List<ProductDescriptionImg> toImgList(
//            List<MultipartFile> desImgs
//            , List<MultipartFile> repImgs
//            , String productDescriptionId) {
//
//
//    }
}
