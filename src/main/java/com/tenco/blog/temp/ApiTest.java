package com.tenco.blog.temp;

import com.tenco.blog.user.User;
import com.tenco.blog.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController //컨트롤러 + 리스폰스
//해당 컨트롤러에서 직접 접근 허용하는 방법
//@CrossOrigin(origins = "*") //모든 도메인에서 요청 허용
public class ApiTest {

    //DI처리
    private final UserService userService;

    @GetMapping("/api-test/user")
    public User getUsers() {
        return userService.findById(1L);
    }






}//
