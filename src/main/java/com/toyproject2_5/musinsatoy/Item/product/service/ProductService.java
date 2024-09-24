package com.toyproject2_5.musinsatoy.Item.product.service;

import com.toyproject2_5.musinsatoy.Item.category.dao.CategoryDao;
import com.toyproject2_5.musinsatoy.Item.category.dto.CategoryHierarchyDto;
import com.toyproject2_5.musinsatoy.Item.product.dao.OptionDao;
import com.toyproject2_5.musinsatoy.Item.product.dao.ProductDao;
import com.toyproject2_5.musinsatoy.Item.product.dao.stock.StockDao;
import com.toyproject2_5.musinsatoy.Item.product.dto.*;
import com.toyproject2_5.musinsatoy.Item.product.dto.option.OptionDBregisterDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.PageInfo;
import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.adminEdit.EditPageInfo;
import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.cursor.ProductCursorPageDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.hasNextOffset.HasNextPageInfo;
import com.toyproject2_5.musinsatoy.Item.product.dto.pagination.hasNextOffset.SearchProductDto;
import com.toyproject2_5.musinsatoy.Item.product.dto.stock.StockRegisterDto;
import com.toyproject2_5.musinsatoy.Item.productDescription.dao.ProductDescriptionDao;
import com.toyproject2_5.musinsatoy.Item.productDescription.dto.ProductDescription;
import com.toyproject2_5.musinsatoy.Item.productDescription.dto.ProductDescriptionDto;
import com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dao.ProductDescriptionImgDao;
import com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dto.ImagePathDto;
import com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dto.ProductDescriptionImg;
import com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dto.ProductDescriptionImgDetailDto;
import com.toyproject2_5.musinsatoy.ETC.util.S3FileService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductDao productDao;
    private final ProductDescriptionDao productDescriptionDao;
    private final ProductDescriptionImgDao productDescriptionImgDao;
    private final OptionDao optionDao;

    private final S3FileService s3FileService;
    private final CategoryDao categoryDao;
    private final StockDao stockDao;


    //상품 등록.
    @Transactional
    public ImagePathDto registerSave(ProductRegisterDto productRegisterDto) throws Exception {

        //프론트에 반환 해줄 s3이미지 경로
        ImagePathDto imagePathDto = new ImagePathDto();

        //상세 설명이 있는지 확인. - 있으면 재사용.
        ProductDescriptionDto productDescriptionCheck = productDescriptionDao.findById(productRegisterDto.getProductDescriptionDto().getProductDescriptionId());


        try{

            //상세 설명을 만드는 경우
            if (productDescriptionCheck == null && !productRegisterDto.getDesImgs().isEmpty()) {

                //ProductDescription 객체 생성.
                ProductDescription productDescription = ProductRegisterDto.toProductDescription(productRegisterDto);

                //상품 설명 이미지, 상품 이미지 속성으로 구분 지어서 저장.
                List<ProductDescriptionImg> imgList = s3FileService.presigneUrltoImageList(productRegisterDto.getDesImgs(), productRegisterDto.getRepresenImgs(), productDescription.getProductDescriptionId(), productRegisterDto.getBrandId());

                //상품 상세 설명 저장
                productDescriptionDao.insert(productDescription);

                //상품 이미지, 상품 설명 이미지 저장.
                productDescriptionImgDao.insert(imgList);

                for(ProductDescriptionImg img: imgList){
                    if(img.getKindOf().equals(ProductDescriptionImg.DESCRIPTION)){

                        //프론트에 상품 설명 이미지 s3경로 반환
                        imagePathDto.addDescImgPath(img.getPath());
                    }else{
                        //프론트에 상품 이미지 s3경로 반환
                        imagePathDto.addProdImgPath(img.getPath());
                        }
                }
            }

            //productID를 UUID로 랜덤하게 생성.
            productRegisterDto.setProductId(UUID.randomUUID().toString().substring(0,25));

            //상품의 대표 이미지의 s3경로 구하기
            String repFilePrefix = s3FileService.repImgPrefixPath(productRegisterDto.getBrandId(), productRegisterDto.getProductId(),productRegisterDto.getRepImg());

            //Dto를 상품으로
            Product registerProduct = ProductRegisterDto.toProduct(productRegisterDto,repFilePrefix);


            //프론트에 상품 대표 이미지의 s3경로 반환.
            imagePathDto.setRepImgPath(repFilePrefix);

            //OptionName을 모두 optionCode로 바꿔 줌.
            //option테이블에 저장될 option코드, stock에 저장될 stockid와 재고수량을 짝지어줌.
            List<OptionDBregisterDto> options = optionRegisterFilter(productRegisterDto.getOption()
                    ,productRegisterDto.getStockOption()
                    ,productRegisterDto.getProductId()
                    ,productRegisterDto.getOptionCategory() );



            //상품 테이블에 상품 저장.
            productDao.insert(registerProduct);

            //option테이블에 저장
            optionDao.insertOption(options);

            //stock테이블에 저장
            stockDao.insertStock(productRegisterDto.getStockOption());

        //이거 처리하기
        }catch (Exception e ){
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        //s3에 저장될 경로 배열 return -> 나중에는 바로 presignedURL 반환 하도록.
        return imagePathDto;
    }


    /*
    *       상품 상세 정보
    *
    * */
    @Transactional(readOnly = true)
    public ProductDetailDto findProductDetailById(String productId) throws Exception {

        //product dao에서 한번에 리스트로 불러올지
        //다른 dao 사용해서 img는 리스트로 나머지는 단일로 불러올지 시험.
        ProductDetailDto productDetailDto = null;
        try {
            //product Table에 있는 컬럼 정보 가져오기.
            productDetailDto = productDao.findProductDetailById(productId);

            //대표이미지 s3버킷 경로 추가.
            productDetailDto.setRepImg(s3FileService.plusBucketPath(productDetailDto.getRepImg()));

        }catch (Exception e ){
            //여기 처리
            System.out.println(e.getMessage());
        }
        if(productDetailDto==null){
            throw new NotFoundException("해당 품목을 찾을 수 없습니다.");
        }



        //상품 설명 찾기.
        ProductDescriptionDto productDescriptionDto= productDescriptionDao.findByProductId(productId);
        productDetailDto.setProductDescription(productDescriptionDto);


        //상품 설명ID 로 상품 설명 이미지, 상품 이미지 찾기.
        List<ProductDescriptionImgDetailDto> imgList = productDescriptionImgDao.findAllByProductDescriptionId(productDescriptionDto.getProductDescriptionId());

        List<ProductDescriptionImgDetailDto> desImgs = new ArrayList<>();
        List<ProductDescriptionImgDetailDto> repImgs = new ArrayList<>();

        //상품 이미지와 상품 설명 이미지 분류, s3 버킷 경로 설정
        for(ProductDescriptionImgDetailDto imgs : imgList){

            imgs.setPath(s3FileService.plusBucketPath(imgs.getPath()));

            if(imgs.getKindOf().equals(ProductDescriptionImg.DESCRIPTION))
                desImgs.add(imgs);
            else repImgs.add(imgs);
        }

        //상품 이미지 Dto에 추가.
        productDetailDto.setDescriptionImages(desImgs);
        productDetailDto.setProductImages(repImgs);


        //상품 옵션 가지고 오기. 마지막 계층은 재고까지.
        productDetailDto.setOptionList(optionDao.findDetailOptionStockByProductId(productId));



        //해당 상품의 카테고리 Id 찾기.
        //모든 상위 카테고리 찾아옴.
        List<CategoryHierarchyDto> categoryHierarchyDtos = categoryDao.findUpperCategoryHierarchyById(productDetailDto.getCategoryId());
        productDetailDto.setParentCategorys(categoryHierarchyDtos);

        return productDetailDto;
    }

    /**
     * SalesOrder -Controller에서 사용
     * @param productId
     * @return ProductPageDto
     * @throws Exception
     */
    public ProductPageDto findProductPageDtoByProductId(String productId) throws Exception {
        return productDao.findProductPageDtoByProductId(productId);
    }


    /*
    *       커서 기반 상품 페이징
    *
    *       나중에 동적 sortCode를 동적 쿼리로 처리할 지 Service 단에서 처리할 지 생각해보기.
    *
    * */
    @Transactional(readOnly = true)
    public List<ProductCursorPageDto> findCursorList(HashMap<String, Object> map) throws Exception {

        //Map으로 해서 들고오던지 Dto로 해서 들고오던지 하기.
        map.put("size",Integer.parseInt(map.get("size").toString()));

        List<ProductCursorPageDto> productList;

        // state 패턴? 사용?
        //나중에 하드코딩 지우기 -> DTO로 만들어서 DTO 내부에서 처리하든지. - state패턴
        if(map.get("sortCode").equals("RANKING")) {
            productList= productDao.findCursorPageListOrderByRank(map);
        }else if(map.get("sortCode").equals("SALES")){
            productList =  productDao.findCursorPageListOrderBySales(map);
        }else if(map.get("sortCode").equals("HIGHPRICE")){
            productList =  productDao.findCursorPageListByHighPrice(map);
        }else if(map.get("sortCode").equals("LOWPRICE")){
            productList =  productDao.findCursorPageListByLowPrice(map);
        }else if(map.get("sortCode").equals("LATEST")){
            productList = productDao.findCursorPageListByLastest(map);
        }else{
            productList =productDao.findCursorList(map);
        }
        for (ProductCursorPageDto productCursorPageDto : productList) {
            productCursorPageDto.setRepImg(s3FileService.plusBucketPath(productCursorPageDto.getRepImg()));
        }

        return productList;
    }

    /*
    *       페이지 기반 상품 페이징
    *       현재 브랜드 상세 페이지 에서 브랜드별 상품 찾는거에 쓰임
    * */
    @Transactional(readOnly = true)
    public List<ProductPageDto> findPageList(PageInfo pageInfo) throws Exception {

        //조회시 필요한 정로를 Map으로 변경
        HashMap<String ,Object> pageMap = PageInfo.toHashMap(pageInfo);

        // 해당 브랜드의 상품 총 개수 조회
        pageInfo.productCountCal(productDao.countProduct(pageMap));

        //해당 페이지에서 상품 정보를 Map을 이용해서 조회
        List<ProductPageDto> productList= productDao.findPageList(pageMap);

        for(ProductPageDto product : productList){
            product.setRepImg(s3FileService.plusBucketPath(product.getRepImg()));
        }

        return productList;
    }

    /*
    * 관리자 상품 수정 페이지 상품 리스트
    *
    * */
    @Transactional(readOnly = true)
    public List<ProductAdminList> findProductAdminList(EditPageInfo editPageInfo) throws Exception {

        //offset 설정
        editPageInfo.getProductEditPageDto().setOffset();

        //Page에 표시될 정보 생성(페이지 번호, 맨끝 페이지 등.), 해당 브랜드의 모든 상품 개수 조회.
        editPageInfo.calEditPageInfo(productDao.countProductAdminList(editPageInfo.getProductEditPageDto()));

        // 상품 개수 조회 후에 상품 리스트 찾기

        // 현재 페이지의 정보 설정.
        List<ProductAdminList> productAdminList = productDao.findProductAdminList(editPageInfo.getProductEditPageDto());



        for(ProductAdminList productAdmin : productAdminList){
            productAdmin.setRepImg(s3FileService.plusBucketPath(productAdmin.getRepImg()));
        }
        return productAdminList;
    }


    /*
    * 조회수 올리기
    * */
    @Transactional
    public void viewCount(String productId) throws Exception {
        productDao.updateViewCount(productId);
    }

    /*
    *       상품 삭제
    *
    * */
    public void deleteProduct(String productId) throws Exception {
        int deleteNum = 0;
        try {
            //현재 productId에 해당하는 대표 이미지 s3에서 삭제
            s3FileService.deleteImageFromS3(productDao.findRepImgById(productId));

            //현재 productId DB에서 삭제
            deleteNum = productDao.deleteByProductId(productId);

        }catch (IOException e ){
            System.out.println(e.getMessage());
        }
        if(deleteNum==0){
            throw new NotFoundException("삭제하려는 상품이 존재하지 않습니다.");
        }
    }

    /*
    *   상품 수정
    *
    *   상품 수정 시 RepImg 도 같이 바뀌는 건지 확인.
    * */
    public String updateProduct(ProductUpdateDto productUpdateDto) throws NotFoundException {
        int i=0;

        //Repimg가 변경 되었을 때.
        if(productUpdateDto.getRepImg()!=null){
            try{
                s3FileService.deleteImageFromS3(productDao.findRepImgById(productUpdateDto.getProductId()));
            } catch (NotFoundException e) {
                throw new NotFoundException(e.getMessage());
            } catch (Exception e ){
                System.out.println(e.getMessage());
            }
            //productUpdateDto의 이미지 이름 s3Path+UUID 설정
            productUpdateDto.setRepImg(s3FileService.repImgPrefixPath(productUpdateDto.getBrandId(), productUpdateDto.getProductId(), productUpdateDto.getRepImg()));
        }


        try {
            i =productDao.updateProduct(productUpdateDto);
        }catch (Exception e ){
                System.out.println(e.getMessage());
        }
        if(i==0){
            throw new NotFoundException("수정 물품이 존재하지 않습니다.");
        }

        //null일 수 도 있음.
        return productUpdateDto.getRepImg();
    }


    /*
     * @option  - 계층 옵션을 받아오는 값[옵션 1 [L,XL,XXL],  옵션 2[blue, black, red], 옵션 3[check, noncheck]] - 옵션의 개수가 더 늘어날 수 도있을 경우를 가정해야함.
     *               한 계층에서의 옵션의 개수는 9개를 넘으면 안됨.
     *
     * @stockOption - 사용자가 선택한 옵션["L/blue/check", "L/blue/noncheck", "XL/black/check" ,... ] 무조건 옵션 depth-1 의 /가 있어야함.
     *                  사용자가 선택한 옵션은 모두 depth를 만족해야함. 옵션depth 보다 작은 옵션 상품을 등록할 수 없음 ->(["L/blue"] -불가.)
     *
     * stock은 처리하기. - 재고는 stockOption만큼의 length를 가져야함.
     *
     * option에는 판매량만, stock에는 사용자가 선택한 옵션의 재고까지.
     *
     * */


    List<OptionDBregisterDto> optionRegisterFilter(
            List<String[]> optionDto
            , List<StockRegisterDto> stockOption
            , String productId
            , List<String> optionCategory){

        //옵션 테이블에 저장할 각 계층별 모든 옵션 조합 종류.
        List<OptionDBregisterDto> OptionList = new ArrayList<>();

        //각계층별 옵션을 A1, B1, C1.. 식으로 바꿔서 map에 저장 (key : blue) : (value : A1) ...
        HashMap<String, String> optionMap = new HashMap<>();

        int optionMaxDepth = optionDto.size();


        //옵션 이름을 통해 A1, A2, B1, B2 ... 식의 코드로 바꾸기.
        for(int i=0;i<optionMaxDepth;i++){
            for(int j=0;j<optionDto.get(i).length;j++){

                //처음에는 65+i = 65 -> A
                String ascii = String.valueOf((char)(65+i));

                // 처음에는 A+1 -> A1
                String optionCode = ascii+(j+1);

                //optionDto[0][0]가 "L"일 경우 optionCode -A1 -> map.put("L", "A1")
                optionMap.put(optionDto.get(i)[j], optionCode);
            }
        }


        //List<String[]> 도 가능하지만 일단 Map으로 해봄.
        HashMap<String,String> optionRegisterMap = new HashMap<>();
        StringBuilder code = new StringBuilder();

        //"L/blue/check"로 들어오면
        for(StockRegisterDto option : stockOption){
            //{"L","blue","check"} 로 바꾸기.
            String[] optionSep = option.getOptionName().split("/");

            /*
            * "L" -> 1계층 , A1
            * "L/blue" -> 2계층 A1B1
            * "L/blue/check" -> 3계층 A1B1C1
            *
            * A1B1C1 을 재고에 fk로 저장.
            * */
            for(int i=0;i<optionMaxDepth;i++){
                code.append(optionMap.get(optionSep[i]));
                optionRegisterMap.put(code.toString(),optionSep[i]);
            }

                /*
                * 옵션 계층이 3일때
                * "L/blue/check" 라면
                * A1B1C1으로 StockRegisterDto의 optionId 에 저장됨.
                * */
                option.setOptionId(code.toString());
                option.setProductId(productId);

                //https://stackoverflow.com/questions/7168881/what-is-more-efficient-stringbuffer-new-or-delete0-sb-length
                code.setLength(0);
        }
        /*
         * dto 만들기. -> OptionDBregisterDto
         * optionDto - option_id, option_name, option depth (DB 테이블의 product_id는 등록시 가져오는 걸로 저장하기.)
         * */
        Iterator<Map.Entry<String, String>> itr = optionRegisterMap.entrySet().iterator();

        while(itr.hasNext())
        {
            Map.Entry<String, String> entry = itr.next();
            OptionList.add( new OptionDBregisterDto(productId, entry.getKey(), entry.getValue(), (entry.getKey().length()/2), optionCategory.get((entry.getKey().length()/2)-1) ));
        }

        return OptionList;

    }

    /*
    *  키워드로 상품 검색 시.
    * */
    @Transactional(readOnly = true)
    public SearchPageDto searchProduct(SearchProductDto searchDto) throws Exception {
        searchDto.calOffset();

        HasNextPageInfo data = new HasNextPageInfo();

        //이부분
        data.setPage(searchDto.getPage());
        data.setSize(searchDto.getSize());


        data.calNext(productDao.countSearchProduct(searchDto));

        List<ProductPageDto> productPageDtolist = productDao.findProductByKeyword(searchDto);

        productPageDtolist.forEach(x->x.setRepImg(s3FileService.plusBucketPath(x.getRepImg())));


        //List받아와서 data.setList(productDao.findByKeyword(searchDto)));

        return new SearchPageDto(data, productPageDtolist);
    }
}




