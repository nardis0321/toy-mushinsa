package com.toyproject2_5.musinsatoy.ETC;

public class FaqSearchCondition {
    private String keyword = "";

    public FaqSearchCondition() {}

    public FaqSearchCondition(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "FaqSearchCondition{" +
                "keyword='" + keyword + '\'' +
                '}';
    }
}

