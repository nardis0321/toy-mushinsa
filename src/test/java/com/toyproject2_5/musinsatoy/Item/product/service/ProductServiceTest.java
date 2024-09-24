package com.toyproject2_5.musinsatoy.Item.product.service;

import com.toyproject2_5.musinsatoy.Item.product.dao.ProductDao;
import com.toyproject2_5.musinsatoy.Item.product.dao.ProductDaoMysql;
import com.toyproject2_5.musinsatoy.Item.product.dto.ProductAdminList;
import com.toyproject2_5.musinsatoy.Item.product.dto.ProductDetailDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.ProductRegisterDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.SearchPageDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.option.OptionDBregisterDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.adminEdit.EditPageInfo;
import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.adminEdit.ProductEditPageDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.hasNextOffset.SearchProductDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.stock.StockRegisterDto;
import com.toyproject2_5.musinsatoy.Item.productDescription.dto.ProductDescriptionDto;
import com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dto.ImagePathDto;
import com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dto.ProductDescriptionImgRegisterDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private StockService stockService;
    @Autowired
    private ProductDao productDaoMysql;


    @Test
    public void optionService(){

        String[] optionsCategory = {"사이즈","컬러","무늬"};

        List<String> optionCategoryList = Arrays.asList(optionsCategory);


        String[][] optionString = {{"L","XL","XXL"},{"blue","black","red"},{"check", "uncheck"}};
        String[][] optionString2 = {{"L","XL"}};

        List<String[]> option = new ArrayList<>(Arrays.asList(optionString));

        String[] optionRegister = {"L/blue/check","L/black/uncheck","L/red/check","XL/blue/uncheck","XL/black/uncheck","XL/red/check","XXL/blue/uncheck","XXL/black/check","XXL/red/check"};
        String[] optionRegister2 = {"L","XL"};

        List<StockRegisterDto> stockRegisterDtos = new ArrayList<>();
        for(int i = 0; i < optionRegister.length; i++){
             stockRegisterDtos.add(new StockRegisterDto("P001",optionRegister[i],null,i));
        }




        List<OptionDBregisterDto> optionDtos = productService.optionRegisterFilter(option,stockRegisterDtos,"P001",optionCategoryList);


        optionDtos.forEach(System.out::println);

        stockRegisterDtos.forEach(System.out::println);
    };

    @Test
    @DisplayName("product Register Test creating description images and product images")
    public void productSaveTest() throws Exception {
        String[] optionCategory = {"사이즈","색상","무늬"};
        String[] optionCategory2 = {"사이즈"};
        List<String> optionCategoryList = Arrays.asList(optionCategory);

        String[][] optionString = {{"L","XL","XXL"},{"blue","black","red"},{"check", "uncheck"}};
        String[][] optionString2 = {{"L","XL"}};

        List<String[]> option = new ArrayList<>(Arrays.asList(optionString));

        String[] optionRegister = {"L/blue/check","L/black/uncheck","L/red/check","XL/blue/uncheck","XL/black/uncheck","XL/red/check","XXL/blue/uncheck","XXL/black/check","XXL/red/check"};
        String[] optionRegister2 = {"L","XL"};

        List<StockRegisterDto> stockRegisterDtos = new ArrayList<>();
        for(int i = 0; i < optionRegister.length; i++){
            stockRegisterDtos.add(new StockRegisterDto(null,optionRegister[i],null,i));
        }


        List<ProductDescriptionImgRegisterDto> descriptionImgs = new ArrayList<>();
        descriptionImgs.add(new ProductDescriptionImgRegisterDto("상세설명 이미지.png",35000L,"image/png"));


        List<ProductDescriptionImgRegisterDto> productImgs=new ArrayList<>();
        productImgs.add(new ProductDescriptionImgRegisterDto("상품 이미지.png",35000L,"image/png"));


        ProductDescriptionDto productDescriptionDto = new ProductDescriptionDto("ADIDAS0000001","상세 설명은 짧고 간단하게.");
        ProductRegisterDto productRegisterDto = ProductRegisterDto.builder()
                .price(25900)
                .productId("CREATE_DESCRIPTION")
                .brandId("NIKE")
                .productDescriptionDto(productDescriptionDto)
                .categoryId("C08")
                .managerName("manager10")
                .name("상세 설명 만드는 테스트용 의류")
                .option(option)
                .stockOption(stockRegisterDtos)
                .optionCategory(optionCategoryList)
                .represenImgs(productImgs)
                .desImgs(descriptionImgs)
                .repImg("대표 이미지")
                .build();

        ImagePathDto imagePathDto = productService.registerSave(productRegisterDto);

        ProductDetailDto dto = productService.findProductDetailById(productRegisterDto.getProductId());
        System.out.println(dto);


        stockService.findByProductId(productRegisterDto.getProductId()).forEach(System.out::println);

    }

    //현재 최종 옵션 계층이 아닌 계층에서도 int 로 설정하여 재고가 0이 나옴.
    @Test
    @DisplayName("상품 상세 페이지 조회시 옵션 조회")
    public  void DetailPageOption() throws Exception {



        String[] optionCategory = {"사이즈","색상","무늬"};
        String[] optionCategory2 = {"사이즈"};
        List<String> optionCategoryList = Arrays.asList(optionCategory);

        String[][] optionString = {{"L","XL","XXL"},{"blue","black","red"},{"check", "uncheck"}};
        String[][] optionString2 = {{"L","XL"}};

        List<String[]> option = new ArrayList<>(Arrays.asList(optionString));

        String[] optionRegister = {"L/blue/check","L/black/uncheck","L/red/check","XL/blue/uncheck","XL/black/uncheck","XL/red/check","XXL/blue/uncheck","XXL/black/check","XXL/red/check"};
        String[] optionRegister2 = {"L","XL"};

        List<StockRegisterDto> stockRegisterDtos = new ArrayList<>();
        for(int i = 0; i < optionRegister.length; i++){
            stockRegisterDtos.add(new StockRegisterDto(null,optionRegister[i],null,i));
        }


        List<ProductDescriptionImgRegisterDto> descriptionImgs = new ArrayList<>();
        descriptionImgs.add(new ProductDescriptionImgRegisterDto("상세설명 이미지.png",35000L,"image/png"));


        List<ProductDescriptionImgRegisterDto> productImgs=new ArrayList<>();
        productImgs.add(new ProductDescriptionImgRegisterDto("상품 이미지.png",35000L,"image/png"));


        ProductDescriptionDto productDescriptionDto = new ProductDescriptionDto("ADIDAS0000001","상세 설명은 짧고 간단하게.");
        ProductRegisterDto productRegisterDto = ProductRegisterDto.builder()
                .price(25900)
                .productId("CREATE_DESCRIPTION")
                .brandId("NIKE")
                .productDescriptionDto(productDescriptionDto)
                .categoryId("C08")
                .managerName("manager10")
                .name("상세 설명 만드는 테스트용 의류")
                .option(option)
                .stockOption(stockRegisterDtos)
                .optionCategory(optionCategoryList)
                .represenImgs(productImgs)
                .desImgs(descriptionImgs)
                .repImg("대표 이미지")
                .build();

        ImagePathDto imagePathDto = productService.registerSave(productRegisterDto);

        ProductDetailDto dto =productService.findProductDetailById(productRegisterDto.getProductId());

        dto.getOptionList().forEach(System.out::println);


    }

    @Test
    @DisplayName("adminEditPage 브랜드 리스트 및 페이지 검증")
    public void adminEditPageTest() throws Exception {
        ProductEditPageDto productEditPageDto = ProductEditPageDto.builder()
                .pageNum(1)
                .pageSize(2)
                .brandId("NIKE")
                .build();
        EditPageInfo editPageInfo = new EditPageInfo();
        editPageInfo.setProductEditPageDto(productEditPageDto);

        List<ProductAdminList> list =productService.findProductAdminList(editPageInfo);

        assertTrue(list.size()== productEditPageDto.getPageSize());
        System.out.println(editPageInfo);
        list.forEach(System.out::println);
    }









}