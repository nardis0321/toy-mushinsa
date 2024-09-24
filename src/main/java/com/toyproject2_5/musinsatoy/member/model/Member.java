package com.toyproject2_5.musinsatoy.member.model;

import lombok.Data;

@Data
public class Member {
    private Integer member_number;
    private String id;
    private String password;

    private String name;
    private String login_datetime;
    private String modify_datetime;
    private Integer login_count;
    private String member_state_code;
    private String is_admin;

    private String birth;
    private String sex;
    private String phone_number;
    private String email;
    private String recommand_id;

    public Member(Integer member_number, String id, String password) {
        this.member_number = member_number;
        this.id = id;
        this.password = password;
    }
}
