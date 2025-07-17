package com.tenco.blog.user;

import com.tenco.blog._core.errors.exception.Exception400;
import com.tenco.blog._core.errors.exception.Exception401;
import com.tenco.blog._core.errors.exception.Exception403;
import com.tenco.blog._core.errors.exception.Exception404;
import com.tenco.blog._core.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true) // 클래스 레벨에서의 읽기 전용 설정
public class UserService {

    private final UserJpaRepository userJpaRepository;

    // 회원가입 처리
    @Transactional // 쓰기 활성화
    public UserResponse.JoinDTO join(UserRequest.JoinDTO joinDTO) {

        userJpaRepository.findByUsername(joinDTO.getUsername())
                .ifPresent(user1 -> {
                    throw new Exception400("이미 존재하는 사용자명입니다");
                });

        User savedUser = userJpaRepository.save(joinDTO.toEntity());

        return new UserResponse.JoinDTO(savedUser);
    }

    // 로그인 처리
    public String login(UserRequest.LoginDTO loginDTO) {

        User selectedUser = userJpaRepository
                .findByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword())
                .orElseThrow(() -> {
                    throw new Exception401("사용자명 또는 비밀번호가 틀렸어요");
                });

        String jwt = JwtUtil.create(selectedUser);

        return jwt;
    }

    // 회원정보 조회 처리 ( jwt로 들어와서 인증검사 굳이임 )
    public UserResponse.DetailDTO findUserById(Long requestUserId, Long sessionUserId) {

        if (!requestUserId.equals(sessionUserId)) {
            throw new Exception403("본인 정보만 조회할 수 있습니다");
        }

        User selectedUser = userJpaRepository.findById(requestUserId).orElseThrow(() -> {
            throw new Exception404("사용자를 찾을 수 없습니다");
        });

        return new UserResponse.DetailDTO(selectedUser);

    }//findUserById

    // 회원정보 수정 처리
    @Transactional
    public UserResponse.UpdateDTO updateById(Long requestUserId, Long sessionUserId, UserRequest.UpdateDTO updateDTO) {

        if (!requestUserId.equals(sessionUserId)) {
            throw new Exception403("본인 정보만 조회할 수 있습니다");
        }

        User selectedUser = userJpaRepository.findById(requestUserId).orElseThrow(() -> {
            throw new Exception404("사용자를 찾을 수 없습니다");
        });

        selectedUser.update(updateDTO);

        return new UserResponse.UpdateDTO(selectedUser);
    }


}//
