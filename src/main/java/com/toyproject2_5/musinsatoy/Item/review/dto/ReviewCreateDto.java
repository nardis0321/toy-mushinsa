package com.toyproject2_5.musinsatoy.Item.review.dto;


import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreateDto {

    private String productId;



    private Long memberNumber;

    private String content;

    //리뷰 이미지 따로 들고올건지 생각.
    private float star;
    private String reviewImg;

//    create시에는 default 0
//    private int like;



}
