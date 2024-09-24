package com.toyproject2_5.musinsatoy.Item.review.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewListDto {

    private Long reviewId;

    private Long memberNumber;
    private String memberName;


    private String content;

    // 평점은 여기서 바꿔서 ? 화면에 송출하기?
    private float star;
    private String reviewImg;
    private int likeCount;
    private LocalDateTime createDatetime;

}
