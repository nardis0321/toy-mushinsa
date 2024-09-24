package com.toyproject2_5.musinsatoy.CS.FAQ.service;

import com.toyproject2_5.musinsatoy.CS.FAQ.dto.FaqDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FaqServiceImplTest {
    @Autowired
    FaqService faqService;

    @Test
    void selectById_test_success() throws Exception{
        FaqDto tempDto = faqService.selectById(1);
        System.out.println("tempDto = " + tempDto);
    }
}