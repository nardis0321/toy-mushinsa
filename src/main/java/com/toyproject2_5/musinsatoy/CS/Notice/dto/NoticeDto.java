package com.toyproject2_5.musinsatoy.CS.Notice.dto;

import java.util.Objects;

public class NoticeDto {
    private Long    notice_id;          //공지id
    private String  notice_title;       //공지제목
    private String  notice_content;     //공지내용
    private String  create_datetime;    //공지작성일 - dateform을 쓰려면 String
    private String  modify_datetime;    //공지수정일
    private String writer;             //공지작성자
    private String modifier;           //공지수정자
    private String  is_top_post;        //상단게시여부
    private String  is_post;            //게시여부

    //Constructor
    public NoticeDto(){}
    //create용 생성자
    public NoticeDto(String notice_title, String notice_content, String writer, String is_top_post, String is_post) {
        this.notice_title = notice_title;
        this.notice_content = notice_content;
        this.writer = writer;
        this.is_top_post = is_top_post;
        this.is_post = is_post;
    }
    //modify용 생성자 -
    public NoticeDto(Long notice_id, String notice_title, String notice_content, String modify_datetime, String modifier, String is_top_post, String is_post) {
        this.notice_id = notice_id;
        this.notice_title = notice_title;
        this.notice_content = notice_content;
        this.modify_datetime = modify_datetime;
        this.modifier = modifier;
        this.is_top_post = is_top_post;
        this.is_post = is_post;
    }
    //select용 생성자
    public NoticeDto(Long notice_id, String notice_title, String notice_content, String create_datetime, String modify_datetime, String writer, String modifier, String is_top_post, String is_post) {
        this.notice_id = notice_id;
        this.notice_title = notice_title;
        this.notice_content = notice_content;
        this.create_datetime = create_datetime;
        this.modify_datetime = modify_datetime;
        this.writer = writer;
        this.modifier = modifier;
        this.is_top_post = is_top_post;
        this.is_post = is_post;
    }

    //Getter
    public Long getNotice_id() {
        return notice_id;
    }

    public String getNotice_title() {
        return notice_title;
    }

    public String getNotice_content() {
        return notice_content;
    }

    public String getCreate_datetime() {
        return create_datetime;
    }

    public String getModify_datetime() {
        return modify_datetime;
    }

    public String getWriter() {
        return writer;
    }

    public String getModifier() {
        return modifier;
    }

    public String getIs_top_post() {
        return is_top_post;
    }

    public String getIs_post() {
        return is_post;
    }

    //Setter
    public void setNotice_id(Long notice_id) {
        this.notice_id = notice_id;
    }

    public void setNotice_title(String notice_title) {
        this.notice_title = notice_title;
    }

    public void setNotice_content(String notice_content) {
        this.notice_content = notice_content;
    }

    public void setCreate_datetime(String create_datetime) {
        this.create_datetime = create_datetime;
    }

    public void setModify_datetime(String modify_datetime) {
        this.modify_datetime = modify_datetime;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public void setIs_top_post(String is_top_post) {
        this.is_top_post = is_top_post;
    }

    public void setIs_post(String is_post) {
        this.is_post = is_post;
    }

    //toString
    @Override
    public String toString() {
        return "NoticeDTO{" +
                "notice_id=" + notice_id +
                ", notice_title='" + notice_title + '\'' +
                ", notice_content='" + notice_content + '\'' +
                ", create_datetime='" + create_datetime + '\'' +
                ", modify_datetime='" + modify_datetime + '\'' +
                ", writer=" + writer +
                ", modifier=" + modifier +
                ", is_top_post='" + is_top_post + '\'' +
                ", is_post='" + is_post + '\'' +
                '}';
    }

    //equals - notice_id, notice_title, notice_content, writer, modifier
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoticeDto noticeDto = (NoticeDto) o;
        return Objects.equals(notice_id, noticeDto.notice_id) && Objects.equals(notice_title, noticeDto.notice_title) && Objects.equals(notice_content, noticeDto.notice_content) && Objects.equals(writer, noticeDto.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notice_id, notice_title, notice_content, writer);
    }
}
