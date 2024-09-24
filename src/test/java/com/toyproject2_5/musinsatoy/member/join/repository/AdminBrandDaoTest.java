package com.toyproject2_5.musinsatoy.member.join.repository;

import com.toyproject2_5.musinsatoy.member.join.dto.AdminBrandDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminBrandDaoTest {
    @Autowired
    AdminBrandDao adminBrandDao;

    /* insertAdminBrand Test */
    @Test
    public void insertAdminBrandTest(){
        adminBrandDao.deleteAll();
        assertEquals(0, adminBrandDao.count());

        AdminBrandDto adminBrandDto = new AdminBrandDto(
                "20240101001",
                "CHANEL"
        );
        assertTrue(adminBrandDao.insertAdminBrand(adminBrandDto)==1);
        assertEquals(1, adminBrandDao.count());
    }


    /* selectAdminBrand Test */
    @Test
    public void selectAdminBrandTest(){
        adminBrandDao.deleteAll();
        assertEquals(0, adminBrandDao.count());

        AdminBrandDto adminBrandDto = new AdminBrandDto(
                "asdf1234",
                "CHANEL"
        );
        assertTrue(adminBrandDao.insertAdminBrand(adminBrandDto)==1);
        assertEquals(1, adminBrandDao.count());

        assertEquals(adminBrandDto.getId(), adminBrandDao.selectAdminBrand(adminBrandDto.getId()).getId());
        assertEquals(adminBrandDto.getBrand_code(), adminBrandDao.selectAdminBrand(adminBrandDto.getId()).getBrand_code());
    }

}