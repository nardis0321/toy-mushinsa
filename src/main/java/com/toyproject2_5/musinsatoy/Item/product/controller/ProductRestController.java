package com.toyproject2_5.musinsatoy.Item.product.controller;

import com.toyproject2_5.musinsatoy.Item.product.dto.ProductUpdateDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.SearchPageDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.hasNextOffset.SearchProductDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.cursor.ProductCursorPageDto;
import com.toyproject2_5.musinsatoy.Item.product.service.ProductService;
import com.toyproject2_5.musinsatoy.Item.brand.service.BrandService;
import com.toyproject2_5.musinsatoy.Item.category.service.CategoryService;
import com.toyproject2_5.musinsatoy.Item.product.dto.ProductRegisterDto;
import com.toyproject2_5.musinsatoy.Item.product.service.chatbot.ChatBotClientService;
import com.toyproject2_5.musinsatoy.Item.product.service.chatbot.ClientMessage;
import com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dto.ImagePathDto;
import com.toyproject2_5.musinsatoy.ETC.util.S3FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(name ="Product API", description ="상품 관련 API")
public class ProductRestController {

    private final ProductService productService;

    private final BrandService brandService;

    private final S3FileService s3FileService;

    private final CategoryService categoryService;


    //이거 좀더 생각해보기.
    @ExceptionHandler(AssertionError.class)
    public ResponseEntity<?> IllegalArgumentException(AssertionError exception){
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message",exception.getMessage()+"오류발생"));
    }

    /*
    *   상품 등록
    *   ProductRegisterDto
    *   Product_id를 입력할때 프론트에 검증 버튼 만들어서 DB에 duplicated가 있는지 확인하는 방식으로. ㄱㅊ을 듯
    *   아니면 productId 입력시에 2개로 놓기, productId : UUID로 서버에서 설정하고 브랜드 상품 코드를 입력 받는 것으로.(브랜드 상품 코드 컬럼 추가)
    *
    *  상품 등록시 이미지에 대한 presigned url 경로를 반환해줌. -> 프론트에서 모든 url에 대해 비동기 동작 할 수 있도록 확인.
    * */
    @Operation(summary = "상품 등록", description = "상품 등록 Dto, 상품 대표 이미지, 상품 설명 이미지 , 상품 이미지를 매개변수로 받아온다.")
    @PostMapping(value = {"/admin"}, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> presignedUrlRegister(@Valid @RequestBody ProductRegisterDto productRegisterDto) {

        //나중에 보고 브랜드 조회 및 Service에 같이 넣어주기(동시성 - readonly 가 아니도록 하는게 맞는 것 같음)
        if (brandService.findById(productRegisterDto.getBrandId()) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message",productRegisterDto.getName() + " 불가(brand 없음)"));

        }

        if (categoryService.findById(productRegisterDto.getCategoryId()) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message",productRegisterDto.getName() + " 불가(카테고리 없음)"));
        }

        //AWS s3 내에서의 이미지 파일 경로.
        ImagePathDto imagePathDto;


        //사진 저장했는데 브랜드나 카테고리를 제대로 안받아 온거면 안됨, 맨 마지막.
        //product_description_id 가 있는지 확인 - 없으면 product_description과 product_description_img 둘다 생성해야함.

        try {
            //상품 저장
            imagePathDto = productService.registerSave(productRegisterDto);

            //imagePathDto를 그냥 presignedUrl로 전달하도록 하는것도 나쁘지 않을 듯.
            return ResponseEntity.status(HttpStatus.OK).body(imagePathDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message",e.getMessage()));
        }
    }


    /*
    *   상품 수정
    *   이름, 대표 이미지, 카테고리, 가격, 화면 표시유무, 등록자(register_manager), 상세 설명 기존에 있는거로 변경.
    *   상세 설명 새로 생성은 productDescription에서 처리하기 .
    *
    *   상품 수정페이지에서 수정시에
    *   1.
    *       상품 수정시에 프론트에서 기존 내용과 바뀐게 있으면 바뀐 내용 Dto에 넣어주고, 기존내용과 바뀐게 없으면 null로 넣어주기. - 전체 내용에 대한 request 버튼을 두기
    *   2.
    *       하나씩만 변경 request가 가도록 - 하나씩 request 버튼을 두기.
    * */
    @Operation(summary="상품 수정", description = "상품을 수정할 때 상세 설명, 상세 설명 이미지, 상품 이미지들을 수정할 때에는 다른 Controller에서 처리할 수 있도록 다른 페이지로 넘어가도록 처리.")
    @PatchMapping(value ={"/admin/{productId}"})
    public ResponseEntity<?> productupdate(@PathVariable(name = "productId") String productId
            , @Validated @RequestBody ProductUpdateDto productUpdateDto) {
        //해당 상품이 존재하는지 확인?

        String repImgPath;
        try {
            repImgPath = productService.updateProduct(productUpdateDto);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }

        //null이면 RepImg가 안바뀌었다는것.
        return ResponseEntity.status(HttpStatus.OK).body(repImgPath);
    }




    @Operation(summary="상품 삭제", description = "productId를 통한 상품 삭제")
    @DeleteMapping(value = "/admin/{productId}")
    public ResponseEntity<?> productdelete(@PathVariable("productId") String productId)  {
        try {
            productService.deleteProduct(productId);
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(productId+ "상품 삭제 완료");
    }



    @Operation(summary = "상품 리스트", description ="상품 리스트 커서기반 불러오기 \n\n" +
            "cursorProductId(cursor), sortCode(정렬 기준), category(where절), keyword(where절), size 반환해 줘야 다음 게시물 가져다줌.")
    @GetMapping(value ="/list")
    public List<ProductCursorPageDto> productCursorList(@RequestParam HashMap<String, Object> cursorMap) throws Exception {
        //cursorMap.get("key")가 null 이면 mapper에서 정렬된 데이터의 맨 첫번째부터 데이터 가져옴.
        //GetMapping 은 RequestBody 사용 불가? - 사용가능 하다하는 곳도 있고 못하다 하는 곳도 있고...
        // http 에서 Get으로 body 전송이 안좋다는 얘기도...

        List<ProductCursorPageDto> cursorList = productService.findCursorList(cursorMap);

        //List안에 내용이 null이면 더이상 안보여 주도록 프론트에서 설정하기.
        return cursorList;
    }

    //https://api.musinsa.com/api2/dp/v1/plp/goods?gf=A&keyword=%EB%82%98%EC%9D%B4%ED%82%A4&sortCode=POPULAR&page=104&size=10&caller=SEARCH
    @Operation()
    @GetMapping("/searchScroll")
    public ResponseEntity<?> searchKeyword(@ModelAttribute SearchProductDto searchProductDto) throws Exception {

        //SearchPageDto의 HasNextPageInfo에서 hasNextPage가 false이면 프론트에서 요청 보내지 않기로.
        SearchPageDto data =  productService.searchProduct(searchProductDto);


        return ResponseEntity.status(HttpStatus.OK).body(data);
    }





    /*
    *       상품 좋아요.
    *       회원 테이블과 찜 테이블 조인 해서 회원이 자신이 누른 좋아요 찾을 수 있게
    *       찜 누르면 찜 테이블에 찜 갯수 +1
    *       or
    *        join해서 카운트 한 다음 찜 갯수 가져올지
    *
    * */


    /*
    *       후기 작성
    *       후기 작성하게 되면 해당 상품의 후기 글에
    *       후기 에는 별점 작성하도록 생성.
    *       후기 작성하면 상품의 테이블에 있는 후기 카운트 +1
    *       + 상품의 별점 평균 내기.
    * */


//-------------------------------------------------------------------------------------------------------------------------
    /*
     * s3
     *
     *
     * */





    //url만 DB에 저장하면 이거 없어도 됨
    //현재 사용 안함. test
    @GetMapping("/getImage")
    public ResponseEntity<?> getimage() {

        //getURL(S3에 저장되어있는 fileName);

        String urltext = s3FileService.getFile("images/9fc116ab-adbc-4bd5-8f0b-a7c3a4181aab스크린샷 2024-08-20 004053.png");

        return new ResponseEntity<>(urltext, HttpStatus.OK);
    }


    //presigned url 가져오기. ->  나중에 프론트에서 다시 요청이 아니라 상품 저장 시에 presigned url 반환하도록 바꾸기
    //프론트에서 사진 업로드.
    @GetMapping("geturl")
    public ResponseEntity<?> geturl(@RequestParam("imagePath") String imagePath) {
        String decodedPath = URLDecoder.decode(imagePath, StandardCharsets.UTF_8);

        //s3FileServivce에서 확장자 검증하기 - png, jpg 만 가능하도록
        return new ResponseEntity<>(s3FileService.getPreSignedUrl(decodedPath), HttpStatus.OK);
    }


    /*
    *
    *
    * */
    @PostMapping("/chatbot")
    public Mono<?> chatbot(@RequestBody ClientMessage message) {
        ChatBotClientService client = new ChatBotClientService();

        return client.chatBot(message.getMessage());
    }





}

