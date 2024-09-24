package com.toyproject2_5.musinsatoy.member.join.repository;

import com.toyproject2_5.musinsatoy.member.join.dto.AdminBrandDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminBrandDao {
    void deleteAll();
    int count();
    AdminBrandDto selectAdminBrand(String id);
    int insertAdminBrand(AdminBrandDto adminBrandDto);

}
