package com.tenco.blog.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinDTO {

        @NotEmpty(message = "사용자명을 입력해주세요")
        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자/2~20자로 작성해주세요")
        //캐로셋은 시작한다는 의미
        private String username;
        @NotEmpty(message = "비밀번호를 입력해주세요") //Null, 공백 입력 차단
        @Size(min = 4, max = 20, message = "비밀번호는 4~20자로 작성해주세요")
        private String password;
        @NotEmpty(message = "이메일을 입력해주세요")
        @Pattern(
                regexp = "^[a-zA-Z0-9]{2,10}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$",
                message = "이메일 형식으로 작성해주세요"
        )
        private String email;

        // JoinDTO 를 User Object 변환 하는 메서드 추가
        // 계층간 데이터 변환을 위해 명확하게 분리
        public User toEntity() {
            return User.builder()
                    .username(this.username)
                    .password(this.password)
                    .email(this.email)
                    .build();
        }

    }//JoinDTO
    
    // 로그인 용 DTO
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginDTO {
        @NotEmpty(message = "사용자명을 입력해주세요")
        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자/2~20자로 작성해주세요")
        //캐로셋은 시작한다는 의미
        private String username;
        @NotEmpty(message = "비밀번호를 입력해주세요") //Null, 공백 입력 차단
        @Size(min = 4, max = 20, message = "비밀번호는 4~20자로 작성해주세요")
        private String password;

    }//LoginDTO

    // 회원 정보 수정용 DTO
    @Data
    public static class UpdateDTO {
        @NotEmpty(message = "비밀번호를 입력해주세요") //Null, 공백 입력 차단
        @Size(min = 4, max = 20, message = "비밀번호는 4~20자로 작성해주세요")
        private String password;
        @NotEmpty(message = "이메일을 입력해주세요")
        @Pattern(
                regexp = "^[a-zA-Z0-9]{2,10}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$",
                message = "이메일 형식으로 작성해주세요"
        )
        private String email;
        // username <-- 유니크로 설정 함

        // toEntity (더티체킹 사용)

    }//UpdateDTO

}//
