package com.toyproject2_5.musinsatoy.ETC.util;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dto.ProductDescriptionImg;
import com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dto.ProductDescriptionImgRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RequiredArgsConstructor
@Component
public class S3FileService {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.s3.folder.defaultImgParh}")
    private String bucketPath;



    @Value("${cloud.aws.s3.folder.brandFolder}")
    private String brandPath;

    @Value("${cloud.aws.s3.folder.repFolder}")
    private String repPath;

    @Value("${cloud.aws.s3.folder.descriptionFolder}")
    private String descriptionFolderPath;

    @Value("${cloud.aws.s3.folder.descriptionImgFolder}")
    private String descriptionImgPath;

    @Value("${cloud.aws.s3.folder.productImgFolder}")
    private String productImgPath;

    //프론트에서 직접 서버로 이미지 파일을 보냈을 때.

    //Controller나 다른 Service에서 이용할 메서드.
    public String upload(MultipartFile image) {
        //입력 받은 파일이 비었는지 확인.
        if(image.isEmpty() || Objects.isNull(image.getOriginalFilename())){
            throw new RuntimeException("ErrorCode.EMPTY_FILE_EXCEPTION");
        }
        //uploadImage를 호출하여 S3에 저장된 이미지의 public url을 반환한다.
        return this.uploadImage(image);
    }

    //
    private String uploadImage(MultipartFile image) {

        //파일 확장자가 올바른지 확인하는 메서드.
        this.validateImageFileExtention(image.getOriginalFilename());
        try {
            //S3에 직접 올리는 메서드.
            return this.uploadImageToS3(image);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    //파일 확장자가 올바른지.
    private void validateImageFileExtention(String filename) {
        int lastDotIndex = filename.lastIndexOf("."); // .이 없으면 -1반환
        if (lastDotIndex == -1) {
            throw new RuntimeException("ErrorCode.NO_FILE_EXTENTION");
        }

        String extention = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif");

        if (!allowedExtentionList.contains(extention)) {
            throw new RuntimeException("올바른 파일 확장자 명이 아닙니다.");
        }
    }

    //S3에 직접 업로드 하는 메서드.
    private String uploadImageToS3(MultipartFile image) throws IOException {
        //원본 파일 명
        String originalFilename = image.getOriginalFilename();

        //확장자 명
        String extention = originalFilename.substring(originalFilename.lastIndexOf("."));

        //s3에 저장될  파일 명(UUID 10자 + 원본파일명)
        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename;


        // 바이트를 순서대로 읽는다고함.
        InputStream is = image.getInputStream();

        //image를 byte[] 로 전환 -> image.getBytes();와 동일한 결과.
        //getInputStream을 통해 스트림을 사용하면 메모리 효율이 좋다고 함 -> 왜 좋을까
        //파일 크기가 작고 메모리 문제 고려x -> getBytes()
        //대용량 파일 > getInputStream().
        byte[] bytes = IOUtils.toByteArray(is);


        //메타 데이터 더 찾아보기
        ObjectMetadata metadata = new ObjectMetadata();
        //multipartfile - 해당 파일의 ContentType 설정.
        metadata.setContentType("image/" + extention);
        //파일의 크기.
        metadata.setContentLength(bytes.length);
        //바이트 배열을 차례로 읽어 들이기 위한 클래스
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        try{
            //S3로 putObject 할 때 사용할 요청 객체
            //생성자 : bucket 이름, 파일 명, byteInputStream, metadata
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead);

            // put image to S3
            amazonS3.putObject(putObjectRequest);
        }catch (Exception e){
            throw new RuntimeException("s3로 파일 저장에 실패했습니다.");
        }finally {
            byteArrayInputStream.close();
            is.close();
        }

        //해당 버킷에 파일을 저장한 파일 url을 가져온다.
        return amazonS3.getUrl(bucketName, s3FileName).toString();
    }



    public String getFile(String fileName){
        //getUrl(버킷 이름, 파일 이름(+확장자)
        URL url = amazonS3.getUrl(bucketName,fileName);
        return url.toString();
    }

//-------------------------------------------------------------------------------------------------------------------------------------
    /**
     * presigned url 발급
     * param : prefix 버킷안에 디렉터리 경로
     * param : fileName 클라이언트가 전달한 파일명 파라미터
     * return : presigned url
     * fileName은 UUID먼저 처리하고 받기.
     */

    public String getPreSignedUrl( String fileName) {


        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(bucketName, fileName);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        
        return URLDecoder.decode(url.toString(), StandardCharsets.UTF_8);
        
    }

    private boolean filterExtension(String filename) {

        String extension=filename.substring(filename.lastIndexOf('.')+1).toLowerCase();
        return (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png"));
    }

    /**
     * 파일 업로드용(PUT) presigned url 생성
     *  bucket 버킷 이름
     *  fileName S3 업로드용 파일 이름
     *  return presigned url
     */
    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String bucket, String fileName) {

        //Put에 대한 메서드로 presigned URL생성
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, fileName)
                                                                        .withMethod(HttpMethod.PUT)
                                                                        .withExpiration(getPreSignedUrlExpiration());

        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());

        return generatePresignedUrlRequest;
    }

    /**
     * presigned url 유효 기간 설정
     * return : 유효기간
     */
    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime(); // 현재 시간
        expTimeMillis += 1000 * 60 * 2; //2분  (1000 * 60 = 1분)
        expiration.setTime(expTimeMillis); //현재 시간 + 2분 까지 유효
        return expiration;
    }


    //서버에서 이미지 삭제.
    public void deleteImageFromS3(String imageAddress){
        String key = getKeyFromImageAddress(imageAddress);
        try{
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        }catch (Exception e){
            throw new RuntimeException("파일을 삭제하는데 실패했습니다. IOException");
        }
    }

    private String getKeyFromImageAddress(String imageAddress){
        try{
            URL url = new URL(bucketPath+imageAddress);
            //decoder 필요한지 보기.
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1); // 맨 앞의 '/' 제거
        }catch (MalformedURLException | UnsupportedEncodingException e){
            throw new RuntimeException("파일을 삭제하는데 실패했습니다. IOException");
        }
    }

    /**
     * 파일 고유 ID를 생성
     * return : 10자리의 UUID
     */
    public String createUUID() {
        return UUID.randomUUID().toString().substring(0,10);
    }

    /**
     * 파일의 전체 경로를 생성
     * param : prefix 디렉토리 경로
     * return : 파일의 전체 경로
     *
     * -> 이거를 데이터 베이스에 저장해야됨.
     */


    public String brandImgPrefixPath(String brandId, String fileName){
        return String.format("%s/%s", brandPath(brandId),createUUID()+fileName);
    }
    public String repImgPrefixPath(String brandId, String ProductId, String fileName){
        return String.format("%s/%s/%s/%s", brandPath(brandId), repPath,ProductId, createUUID()+fileName);
    }
    private String descriptionImgPrefixPath(String brandId,String productDescriptionId,String fileName){
        return String.format("%s/%s/%s/%s", brandPath(brandId), descriptionPath(productDescriptionId), descriptionImgPath,createUUID()+fileName);
    }
    public String productImgPrefixPath(String brandId,String productDescriptionId,String fileName){
        return String.format("%s/%s/%s/%s", brandPath(brandId), descriptionPath(productDescriptionId), productImgPath,createUUID()+fileName);

    }
    private String brandPath(String brandId){
        return String.format("%s/%s", brandPath, brandId);
    }
    private String descriptionPath(String productDescriptionId){
        return String.format("%s/%s", descriptionFolderPath, productDescriptionId);
    }





    public List<ProductDescriptionImg> presigneUrltoImageList(
            List<ProductDescriptionImgRegisterDto> descriptionImgs
            , List<ProductDescriptionImgRegisterDto> productImgs
            , String productDescriptionId
            ,String brandId) {

        //순서를 Mapper에서 만들어 놓을까.

        List<ProductDescriptionImg> imgList = new ArrayList<>();

        // Description Images 처리
        addImagesToList(imgList, descriptionImgs, productDescriptionId, brandId, true);

        // Product Images 처리 (비어있을 수 있음)
        if (productImgs != null && !productImgs.isEmpty()) {
            addImagesToList(imgList, productImgs, productDescriptionId, brandId, false);
        }

        return imgList;
    }

    private void addImagesToList(List<ProductDescriptionImg> imgList,
                                 List<ProductDescriptionImgRegisterDto> imageDtos,
                                 String productDescriptionId,
                                 String brandId,
                                 boolean isDescriptionImg) {

        byte order = 1;

        for (ProductDescriptionImgRegisterDto file : imageDtos) {
            String fileName = file.getFileName();

            //Exceiption
            if (fileName == null || fileName.isEmpty()) {
                throw new RuntimeException("파일 이름을 작성하셔야 합니다.");
            }

            if(!filterExtension(fileName)){
                throw new RuntimeException("이미지 파일 형식이 잘못 되었습니다.");
            }

            String filePath = isDescriptionImg
                    ? descriptionImgPrefixPath(brandId, productDescriptionId, fileName) : productImgPrefixPath(brandId, productDescriptionId, fileName);

            Long fileBytes = file.getFileSize();

            if (isDescriptionImg) {
                imgList.add(ProductDescriptionImg.imageToDescriptionImg(fileName, filePath, fileBytes, productDescriptionId, order));
            } else {
                imgList.add(ProductDescriptionImg.imageToProductImg(fileName, filePath, fileBytes, productDescriptionId, order));
            }
            order++;
        }
    }




    public String plusBucketPath(String imgPath) {
        if(imgPath.contains("https")){
            return imgPath;
        }
        return bucketPath+imgPath;
    }
}