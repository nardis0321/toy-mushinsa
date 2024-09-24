package com.toyproject2_5.musinsatoy.CS.Notice.dto;

import java.util.Objects;

public class NoticeImgDto {
    private Long    notice_image_id;  //공지사진일련번호
    private Long    notice_id;        //공지사항일련번호
    private String  image_link;       //사진링크
    private String  image_type;       //사진 확장자
    private Integer image_order;      //사진 순서
    private String  create_datetime;  //생성일
    private String  modify_datetime;  //수정일
    public  Integer writer;           //생성자
    public  Integer modifier;         //수정자

    //Constructor
    public NoticeImgDto() {}
    //공지 등록시 생성자
    public NoticeImgDto(Long notice_id, String image_link, String image_type, Integer image_order, Integer writer) {
        this.notice_id = notice_id;
        this.image_link = image_link;
        this.image_type = image_type;
        this.image_order = image_order;
        this.writer = writer;
    }

    //공지 사진 수정시 생성자
    public NoticeImgDto(Long notice_image_id, String image_link, String image_type, Integer image_order, String modify_datetime, Integer modifier){
        this.notice_image_id = notice_image_id;
        this.image_link = image_link;
        this.image_type = image_type;
        this.image_order = image_order;
        this.modify_datetime = modify_datetime;
        this.modifier = modifier;
    }

    //select 시 생성자
    public NoticeImgDto(Long notice_image_id, Long notice_id, String image_link, String image_type, Integer image_order, String create_datetime, String modify_datetime, Integer writer, Integer modifier) {
        this.notice_image_id = notice_image_id;
        this.notice_id = notice_id;
        this.image_link = image_link;
        this.image_type = image_type;
        this.image_order = image_order;
        this.create_datetime = create_datetime;
        this.modify_datetime = modify_datetime;
        this.writer = writer;
        this.modifier = modifier;
    }

    //Getter & Setter
    public Long getNotice_image_id() {
        return notice_image_id;
    }

    public void setNotice_image_id(Long notice_image_id) {
        this.notice_image_id = notice_image_id;
    }

    public Long getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(Long notice_id) {
        this.notice_id = notice_id;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getImage_type() {
        return image_type;
    }

    public void setImage_type(String image_type) {
        this.image_type = image_type;
    }

    public Integer getImage_order() {
        return image_order;
    }

    public void setImage_order(Integer image_order) {
        this.image_order = image_order;
    }

    public String getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(String create_datetime) {
        this.create_datetime = create_datetime;
    }

    public String getModify_datetime() {
        return modify_datetime;
    }

    public void setModify_datetime(String modify_datetime) {
        this.modify_datetime = modify_datetime;
    }

    public Integer getWriter() {
        return writer;
    }

    public void setWriter(Integer writer) {
        this.writer = writer;
    }

    public Integer getModifier() {
        return modifier;
    }

    public void setModifier(Integer modifier) {
        this.modifier = modifier;
    }

    //toString
    @Override
    public String toString() {
        return "NoticeImgDto{" +
                "notice_image_id=" + notice_image_id +
                ", notice_id=" + notice_id +
                ", image_link='" + image_link + '\'' +
                ", image_type='" + image_type + '\'' +
                ", image_order=" + image_order +
                ", create_datetime='" + create_datetime + '\'' +
                ", modify_datetime='" + modify_datetime + '\'' +
                ", writer=" + writer +
                ", modifier=" + modifier +
                '}';
    }

    //equals() & hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoticeImgDto that = (NoticeImgDto) o;
        return Objects.equals(notice_image_id, that.notice_image_id) && Objects.equals(notice_id, that.notice_id) && Objects.equals(image_link, that.image_link) && Objects.equals(image_type, that.image_type) && Objects.equals(image_order, that.image_order) && Objects.equals(writer, that.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notice_image_id, notice_id, image_link, image_type, image_order, writer);
    }
}
