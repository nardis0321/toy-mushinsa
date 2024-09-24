package com.toyproject2_5.musinsatoy.Item.brand.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(description = "브랜드 Dto")
public class BrandCreateDto {

    //브랜드 id 무신사의 경우 영어이름으로 해둠.
    @Size(min = 3, max = 25, message = "브랜드 Id는 3글자 이상, 25글자 이하여야 합니다.")
    @NotNull(message = "브랜드 Id 는 null이 되면 안됩니다.")
    @Schema(description = "브랜드 Id", defaultValue = "A00000000002")
    private String brandId;

    @Size(min = 3, max = 25, message = "브랜드 이름은 3글자 이상, 25글자 이하여야 합니다.")
    @NotNull(message = "브랜드 Id는 null이 되면 안됩니다.")
    @Schema(description = "브랜드 이름", defaultValue = "NIKE")
    private String name;

    //브랜드 대표 이미지 경로.
    //따로 받아올건지 생각.
    //@Size(min = 3, max = 500, message = "이미지의 경로 에러")

    private String img;



}
