package com.toyproject2_5.musinsatoy.Item.product.dto;

import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.hasNextOffset.HasNextPageInfo;
import com.toyproject2_5.musinsatoy.Item.productDescription.dto.ProductDescriptionDto;
import com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dto.ProductDescriptionImgRegisterDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchPageDto {

    private HasNextPageInfo pagination;
    List<ProductPageDto> productList;
}
