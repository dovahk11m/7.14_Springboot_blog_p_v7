package com.tenco.blog.user;

import com.tenco.blog._core.common.ApiUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController // 컨트롤러 + 리스폰스바디
@RequiredArgsConstructor //final 초기화
public class UserRestController {

    // @Slf4j 선언시 로깅 자동화
    // private static final Logger log = LoggerFactory.getLogger(UserRestController.class)

    @Autowired
    private final UserService userService; //생성자 의존주입

    //http://localhost:8080/join
    //회원가입 API 설계
    @PostMapping("/join")
    //public ResponseEntity<ApiUtil<UserResponse.JoinDTO>> join() {
    //JSON 형태의 데이터를 추출할 때 @RequestBody 선언
    public ResponseEntity<?> join(@RequestBody UserRequest.JoinDTO reqDTO) {
        log.info("회원가입 API 호출 - 사용자명:{}, 이메일:{}",
                reqDTO.getUsername(), reqDTO.getEmail());

        reqDTO.validate();

        //서비스에 위임 처리
        UserResponse.JoinDTO joinUser = userService.join(reqDTO);

        //status 에는 201, body 에는 joinUser
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiUtil<>(joinUser));
    }


    //로긴 요청

    //회원정보 조회

    //회원정보 수정

    //로갓 처리


}//
