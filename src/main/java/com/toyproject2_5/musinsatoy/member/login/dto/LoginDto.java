package com.toyproject2_5.musinsatoy.member.login.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginDto {
    private Integer member_number;
    private String member_state_code;
    private String id;
    private String password;
    private String name;
    private String login_datetime;
    private String modify_datetime;
    private Integer login_count;
    private String is_admin;

    public LoginDto(){
        this("", "");
    }

    public LoginDto(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public Integer getMember_number() {
        return member_number;
    }

    public void setMember_number(Integer member_number) {
        this.member_number = member_number;
    }

    public String getMember_state_code() {
        return member_state_code;
    }

    public void setMember_state_code(String member_state_code) {
        this.member_state_code = member_state_code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin_datetime() {
        return login_datetime;
    }

    public void setLogin_datetime(String login_datetime) {
        this.login_datetime = login_datetime;
    }

    public String getModify_datetime() {
        return modify_datetime;
    }

    public void setModify_datetime(String modify_datetime) {
        this.modify_datetime = modify_datetime;
    }

    public Integer getLogin_count() {
        return login_count;
    }

    public void setLogin_count(Integer login_count) {
        this.login_count = login_count;
    }

    public String getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(String is_admin) {
        this.is_admin = is_admin;
    }

    @Override
    public String toString() {
        return "LoginDto{" +
                "member_number=" + member_number +
                ", member_state_code='" + member_state_code + '\'' +
                ", id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", login_datetime='" + login_datetime + '\'' +
                ", modify_datetime='" + modify_datetime + '\'' +
                ", login_count=" + login_count +
                ", is_admin='" + is_admin + '\'' +
                '}';
    }
}
