package com.toyproject2_5.musinsatoy.CS.FAQ.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import java.util.Objects;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class FaqDto {
    private Integer  faq_id;
    private String   faq_category_code;
    private Integer  faq_subcategory_id;
    private String   faq_category_name;
    private String   faq_subcategory_name;
    private String   faq_title;
    private String   faq_content;
    private String   create_datetime;
    private String   modify_datetime;
    private String  writer;
    private String  modifier;
    private String   is_post;

    //Constructor
    public FaqDto() {}
    //create용 생성자
    public FaqDto(String faq_category_code, Integer faq_subcategory_id, String faq_title, String faq_content, String writer, String is_post) {
        this.faq_category_code = faq_category_code;
        this.faq_subcategory_id = faq_subcategory_id;
        this.faq_title = faq_title;
        this.faq_content = faq_content;
        this.writer = writer;
        this.is_post = is_post;
    }
    //modify용 생성자
    public FaqDto(Integer faq_id, String faq_category_code, Integer faq_subcategory_id, String faq_title, String faq_content, String modify_datetime, String modifier, String is_post) {
        this.faq_id = faq_id;
        this.faq_category_code = faq_category_code;
        this.faq_subcategory_id = faq_subcategory_id;
        this.faq_title = faq_title;
        this.faq_content = faq_content;
        this.modify_datetime = modify_datetime;
        this.modifier = modifier;
        this.is_post = is_post;
    }
    //select용 생성자
    public FaqDto(Integer faq_id, String faq_category_code, Integer faq_subcategory_id, String faq_category_name, String faq_subcategory_name, String faq_title, String faq_content, String create_datetime, String modify_datetime, String writer, String modifier, String is_post) {
        this.faq_id = faq_id;
        this.faq_category_code = faq_category_code;
        this.faq_subcategory_id = faq_subcategory_id;
        this.faq_category_name = faq_category_name;
        this.faq_subcategory_name = faq_subcategory_name;
        this.faq_title = faq_title;
        this.faq_content = faq_content;
        this.create_datetime = create_datetime;
        this.modify_datetime = modify_datetime;
        this.writer = writer;
        this.modifier = modifier;
        this.is_post = is_post;
    }


    //equals - faq_id, faq_subcategory_code, faq_subcategory_id, faq_title, faq_content, writer
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FaqDto faqDto = (FaqDto) o;
        return faq_id == faqDto.faq_id && faq_subcategory_id == faqDto.faq_subcategory_id && writer == faqDto.writer && Objects.equals(faq_category_code, faqDto.faq_category_code) && Objects.equals(faq_title, faqDto.faq_title) && Objects.equals(faq_content, faqDto.faq_content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(faq_id, faq_category_code, faq_subcategory_id, faq_title, faq_content, writer);
    }

    //toString
    @Override
    public String toString() {
        return "FaqDto{" +
                "faq_id=" + faq_id +
                ", faq_category_code='" + faq_category_code + '\'' +
                ", faq_subcategory_id=" + faq_subcategory_id +
                ", faq_category_name='" + faq_category_name + '\'' +
                ", faq_subcategory_name='" + faq_subcategory_name + '\'' +
                ", faq_title='" + faq_title + '\'' +
                ", faq_content='" + faq_content + '\'' +
                ", create_datetime='" + create_datetime + '\'' +
                ", modify_datetime='" + modify_datetime + '\'' +
                ", writer=" + writer +
                ", modifier=" + modifier +
                ", is_post='" + is_post + '\'' +
                '}';
    }

    public Integer getFaq_id() {
        return faq_id;
    }

    public String getFaq_category_code() {
        return faq_category_code;
    }

    public Integer getFaq_subcategory_id() {
        return faq_subcategory_id;
    }

    public String getFaq_category_name() {
        return faq_category_name;
    }

    public String getFaq_subcategory_name() {
        return faq_subcategory_name;
    }

    public String getFaq_title() {
        return faq_title;
    }

    public String getFaq_content() {
        return faq_content;
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

    public String getIs_post() {
        return is_post;
    }

    public void setFaq_id(Integer faq_id) {
        this.faq_id = faq_id;
    }

    public void setFaq_category_code(String faq_category_code) {
        this.faq_category_code = faq_category_code;
    }

    public void setFaq_subcategory_id(Integer faq_subcategory_id) {
        this.faq_subcategory_id = faq_subcategory_id;
    }

    public void setFaq_category_name(String faq_category_name) {
        this.faq_category_name = faq_category_name;
    }

    public void setFaq_subcategory_name(String faq_subcategory_name) {
        this.faq_subcategory_name = faq_subcategory_name;
    }

    public void setFaq_title(String faq_title) {
        this.faq_title = faq_title;
    }

    public void setFaq_content(String faq_content) {
        this.faq_content = faq_content;
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

    public void setIs_post(String is_post) {
        this.is_post = is_post;
    }
}
