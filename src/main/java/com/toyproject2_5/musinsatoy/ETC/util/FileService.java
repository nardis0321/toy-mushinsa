package com.toyproject2_5.musinsatoy.ETC.util;

import com.toyproject2_5.musinsatoy.Item.productDescriptionImg.dto.ProductDescriptionImg;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class FileService {

    private final S3FileService s3FileService;
    @Value("${productImgLocation}")
    private String imgLocation;

    @Value(("${productRepImgLocation}"))
    private String imgRepLocation;

    public FileService(S3FileService s3FileService) {
        this.s3FileService = s3FileService;
    }

    public String checkExtention(String extention) {

        String originalFileExtension;

        if(extention.contains("image/jpeg")){
            originalFileExtension = ".jpg";
        }
        else if(extention.contains("image/png")){
            originalFileExtension = ".png";
        }
        else if(extention.contains("image/gif")){
            originalFileExtension = ".gif";
        }else{
            throw new RuntimeException("사진 파일 확장자가 올바르지 않습니다.");
        }

        return originalFileExtension;
    }


    public String uploadFile(String uploadPath, MultipartFile file) throws IOException {

        file.getOriginalFilename();
        UUID uuid = UUID.randomUUID();
        String extension = checkExtention(Objects.requireNonNull(file.getContentType()));
        String fileCode = uuid+ extension;
        String fileUploadFullUrl = uploadPath +"/"+ fileCode;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(file.getBytes());
        fos.close();
        return fileCode;
    }

    public String uploadRepImg(MultipartFile productRepImg) throws IOException {

        return "/images/rep/"+uploadFile(imgRepLocation, productRepImg);


    }


    public void deleteFile(String filePath) throws IOException {
        File deleteFile = new File(filePath);
        if(deleteFile.exists()) deleteFile.delete();

    }

    public List<ProductDescriptionImg> toImageList(
            List<MultipartFile> DescriptionImgs
            , List<MultipartFile> productImgs
            , String productDescriptionId ) throws IOException {

        List<ProductDescriptionImg> imgList = new ArrayList<>();
        //순서를 Mapper에서 만들어 놓을까.
        byte i =1;

        for(MultipartFile file : DescriptionImgs){
            System.out.println("이미지 저장 시작");

            String filename = file.getOriginalFilename();

            String fileCode = uploadFile(imgLocation, file);

            //이런 부분 클래스(ProductDescriptionImg) 안에서 정의해도 되는가?
            ProductDescriptionImg productDescriptionImg
                    = ProductDescriptionImg.builder()
                    .productDescriptionId(productDescriptionId)
                    .name(filename)
                    .path("/images/"+fileCode)
                    .orderNum(i++)
                    .size(file.getSize())
                    .kindOf(ProductDescriptionImg.DESCRIPTION)
                    .isUsed(ProductDescriptionImg.DEFAULT_USE)
                    .build();

            imgList.add(productDescriptionImg);
        }
        if(!productImgs.isEmpty()){
            i=1;

            for(MultipartFile file : productImgs){
                System.out.println("이미지  표시 저장 시작");

                String filename = file.getOriginalFilename();
                String fileCode = uploadFile(imgLocation, file);

                ProductDescriptionImg productDescriptionImg
                        = ProductDescriptionImg.builder()
                        .productDescriptionId(productDescriptionId)
                        .name(filename)
                        .path("/images/"+fileCode)
                        .orderNum(i++)
                        .size(file.getSize())
                        .kindOf(ProductDescriptionImg.REPRESENTATION)
                        .isUsed(ProductDescriptionImg.DEFAULT_USE)
                        .build();
                imgList.add(productDescriptionImg);
            }
        }

        return imgList;

    }


    public static byte[] convertFileContentToBlob (String filePath) {

        byte[] result = null;
        System.out.println(filePath);
        try {
            result = FileUtils.readFileToByteArray( new File(filePath));
        } catch (IOException ie) {
            System.out.println("file convert Error");
        }

        return result;
    }

    public static String convertBlobToBase64 (byte[] blob) {
        return new String(Base64.getEncoder().encode(blob));
    }

    public static String getFileContent (String filePath) {
        byte[] filebyte = convertFileContentToBlob("C:/Users/user/Desktop/git5/toyproject2_5/src/main/resources/static"+filePath);
        if(filebyte == null){
            return filePath;
        }
        return convertBlobToBase64(filebyte);

    }
}
