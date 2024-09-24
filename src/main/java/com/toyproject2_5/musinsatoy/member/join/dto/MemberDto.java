package com.toyproject2_5.musinsatoy.member.join.dto;

public class MemberDto {
    private String id;
    private String password;
    private String name;
    private String birth;
    private String sex;
    private String phone_number;
    private String email;
    private String recommand_id;
    private String is_admin;

    public MemberDto(){
        this("", "", "", "", "", "", "", "", "");
    }
    public MemberDto(String id, String password, String name, String birth, String sex, String phone_number,
                     String email, String recommand_id, String is_admin) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.birth = birth;
        this.sex = sex;
        this.phone_number = phone_number;
        this.email = email;
        this.recommand_id = recommand_id;
        this.is_admin = is_admin;
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

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRecommand_id() {
        return recommand_id;
    }

    public void setRecommand_id(String recommand_id) {
        this.recommand_id = recommand_id;
    }

    public String getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(String is_admin) {
        this.is_admin = is_admin;
    }

    @Override
    public String toString() {
        return "memberDto{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", birth='" + birth + '\'' +
                ", sex='" + sex + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", email='" + email + '\'' +
                ", recommand_id='" + recommand_id + '\'' +
                ", is_admin='" + is_admin + '\'' +
                '}';
    }
}
