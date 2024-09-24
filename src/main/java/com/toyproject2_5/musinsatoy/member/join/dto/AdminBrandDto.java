package com.toyproject2_5.musinsatoy.member.join.dto;

public class AdminBrandDto {
    private String id;
    private String brand_code;

    public AdminBrandDto(String id, String brand_code) {
        this.id = id;
        this.brand_code = brand_code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand_code() {
        return brand_code;
    }

    public void setBrand_code(String brand_code) {
        this.brand_code = brand_code;
    }

    @Override
    public String toString() {
        return "AdminBrandDto{" +
                "id='" + id + '\'' +
                ", brand_code='" + brand_code + '\'' +
                '}';
    }
}
