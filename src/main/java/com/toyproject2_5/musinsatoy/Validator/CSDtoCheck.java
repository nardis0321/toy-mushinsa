package com.toyproject2_5.musinsatoy.Validator;

import com.toyproject2_5.musinsatoy.CS.FAQ.dto.FaqDto;
import com.toyproject2_5.musinsatoy.CS.Notice.dto.NoticeDto;

public class CSDtoCheck {
    public static void validateNoticeDto(NoticeDto noticeDto)throws Exception{
        //제목 150자 이상이면 예외 던진다.
        if(noticeDto.getNotice_title().length() > 150) {
            throw new Exception("Notice_title is over 150 characters");
        }
        //is_top_post 1자 이상이면 예외
        if(noticeDto.getIs_top_post().length() > 1) {
            throw new Exception("Is_top_post is over 1 characters");
        }
        //is_post 1자 이상이면 예외
        if(noticeDto.getIs_post().length() > 1) {
            throw new Exception("Is_post is over 1 characters");
        }
    }

    public static void validateFaqDto(FaqDto faqDto) throws Exception {

    }
}

//이름변경