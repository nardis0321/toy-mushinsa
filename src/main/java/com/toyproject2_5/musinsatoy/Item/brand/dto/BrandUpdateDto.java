package com.toyproject2_5.musinsatoy.Item.brand.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BrandUpdateDto {

    //이거를 BrandDto에 상속 시킬지 고민


    @Size(min = 3, max = 25, message = "브랜드 Id는 3글자 이상, 25글자 이하여야 합니다.")
    @NotNull(message = "브랜드 Id 는 null이 되면 안됩니다.")
    private String brandId;

    @Size(min = 3, max = 25, message = "브랜드 이름은 3글자 이상, 25글자 이하여야 합니다.")
    @NotNull(message = "브랜드 Id는 null이 되면 안됩니다.")
    private String name;

    //브랜드 대표 이미지 경로.
    //따로 받아올건지 생각.
    //@Size(min = 3, max = 500, message = "이미지의 경로 에러")
    //브랜드 이미지를 여러개로 할 경우 필요할듯.

}
