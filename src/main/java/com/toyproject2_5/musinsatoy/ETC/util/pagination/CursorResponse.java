package com.toyproject2_5.musinsatoy.ETC.util.pagination;

import java.util.List;

public record CursorResponse<T> (
        CursorRequest nextCursorRequest,    // 다음 요청
        List<T> body    // 불러온 contents
){ }