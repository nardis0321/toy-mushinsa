package com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class ImagePathDto {
    private String repImgPath;

    private List<String> descImgPath ;

    private List<String> prodImgPath ;


    public ImagePathDto(){
        this.descImgPath = new ArrayList<>();
        this.prodImgPath =  new ArrayList<>();
    }

    public void addDescImgPath(String descImgPath) {
        this.descImgPath.add(descImgPath);
    }

    public void addProdImgPath(String prodImgPath) {
        this.prodImgPath.add(prodImgPath);
    }


}
