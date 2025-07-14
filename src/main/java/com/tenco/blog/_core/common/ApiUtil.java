package com.tenco.blog._core.common;

/*
7.14
Rest API 공통 응답형식을 위한 클래스 설계
이를 통해 모든 API 응답을 통일된 형태로 관리한다

{
 "status" : 200,
 "msg" : "성공",
 "body" : { 실제 데이터 }
}
 */

import lombok.Data;

@Data
public class ApiUtil<T> {

    private Integer status; // HTTP 상태코드
    private String msg;     // 응답메세지
    private T body;         // 실제 응답 데이터

    //userBody, boardBody, replyBody 3개나 만들어야 되는 작업을 단축

    /**
     * 성공 응답 생성자
     */
    public ApiUtil(T body) {
        this.status = 200;
        this.msg = "성공";
        this.body = body;
    }

    /**
     * 실패 응답 생성자
     */
    public ApiUtil(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
        this.body = null;
    }


}
