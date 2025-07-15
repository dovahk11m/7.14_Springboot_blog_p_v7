package com.tenco.blog.user;

import com.tenco.blog._core.common.ApiUtil;
import com.tenco.blog.utils.Define;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //http://localhost:8080/login
    //로긴 API 설계
    @PostMapping("/login")
    public ResponseEntity<ApiUtil<UserResponse.LoginDTO>> login(
            @RequestBody UserRequest.LoginDTO reqDTO,
            HttpSession session) {

        log.info("로그인 API 호출 - 사용자명:{}", reqDTO.getUsername());

        reqDTO.validate();

        UserResponse.LoginDTO loginUser = userService.login(reqDTO); //서비스단에서 loginDTO 반환

        //세션에 정보저장
        //session.setAttribute(Define.SESSION_USER, loginUser);

        return ResponseEntity.ok(new ApiUtil<>(loginUser));

    }

    //회원정보 조회 API 설계
    //http://localhost:8080/api/users/1
    @GetMapping("/api/users/{id}")
    public ResponseEntity<ApiUtil<UserResponse.DetailDTO>> getUserInfo(
            @PathVariable(name = "id") Long id, HttpSession session) {

        log.info("회원정보 조회 API 호출 - ID:{}", id);

        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);

        userService.findUserById(id, sessionUser);

        UserResponse.DetailDTO userDetail = userService.findUserById(id, sessionUser);

        return ResponseEntity.ok(new ApiUtil<>(userDetail));
    }

    //회원정보 수정
    //http://localhost:8080/api/users/1
    @PutMapping("/api/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable(name = "id") Long id,
                                        @RequestBody UserRequest.UpdateDTO updateDTO) {
        log.info("회원정보 수정 API 호출 - ID:{}", id);

        //인증검사는 인터셉터에서 처리됨
        //유효성 검사
        updateDTO.validate();

        UserResponse.UpdateDTO updateUser = userService.updateById(id, updateDTO);

        return ResponseEntity.ok(new ApiUtil<>(updateUser));
    }


    //로갓 처리

    //http://localhost:8080/logout
    //로그아웃 API 설계
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        log.info("로그아웃 API 호출");
        session.invalidate();
        return ResponseEntity.ok(new ApiUtil<>("로그아웃 성공"));
    }


}//
