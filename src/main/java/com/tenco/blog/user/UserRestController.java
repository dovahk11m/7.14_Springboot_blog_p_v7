package com.tenco.blog.user;

import com.tenco.blog._core.common.ApiUtil;
import com.tenco.blog._core.errors.exception.Exception401;
import com.tenco.blog._core.utils.Define;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController // 컨트롤러 + 리스폰스바디
@RequiredArgsConstructor //final 초기화
public class UserRestController {

    private final UserService userService;

    //회원가입 API (인증불필요)
    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody UserRequest.JoinDTO joinDTO, Errors errors
    ) {
        UserResponse.JoinDTO joinedUser = userService.join(joinDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiUtil<>(joinedUser));
    }

    //로그인 API
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserRequest.LoginDTO loginDTO, Errors errors
    ) {
        String jwtToken = userService.login(loginDTO);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + jwtToken)
                .body(new ApiUtil<>(null));
    }

    //회원정보 조회 API
    @GetMapping("/api/users/{id}")
    public ResponseEntity<?> getUserInfo(@PathVariable(name = "id") Long id,
                                         @RequestAttribute(Define.SESSION_USER) SessionUser sessionUser
    ) {
        //인증체크
        if (sessionUser == null) {
            throw new Exception401("인증정보가 없습니다");
        }

        UserResponse.DetailDTO userInfo = userService.findUserById(id, sessionUser.getId());
        return ResponseEntity.ok(new ApiUtil<>(userInfo));

    }//getUserInfo

    //회원정보 수정 API
    @PutMapping("/api/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable(name = "id") Long id,
                                        @RequestAttribute(Define.SESSION_USER) SessionUser sessionUser,
                                        @Valid @RequestBody UserRequest.UpdateDTO updateDTO, Errors errors
    ) {
        //인증체크
        if (sessionUser == null) {
            throw new Exception401("인증정보가 없습니다");
        }

        UserResponse.UpdateDTO updatedUser = userService.updateById(id, sessionUser.getId(), updateDTO);

        return ResponseEntity.ok(new ApiUtil<>(updatedUser));

    }//updateUser

    //로그아웃 API
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {

        return ResponseEntity.ok(new ApiUtil<>("로그아웃 성공"));

    }
}//
