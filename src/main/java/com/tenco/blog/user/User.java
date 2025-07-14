package com.tenco.blog.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
@Table(name = "user_tb")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자 이름 중복 방지를 위한 유니크 제약 설정
    @Column(unique = true)
    private String username;

    private String password;
    private String email;
    // now() -> x
    // 엔티티가 영속화 될 때 자동으로 pc 현재시간을 설정해 준다
    @CreationTimestamp
    private Timestamp createdAt;

    // 객체 생성시 가독성과 안정성 향상
    @Builder
    public User(Long id, String username, String password, String email, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdAt = createdAt;
    }

    //
    public void update(UserRequest.UpdateDTO updateDTO) {
        updateDTO.validate();

        //영속상태 엔티티의 필드값 변경
        this.password = updateDTO.getPassword();
        this.email = updateDTO.getEmail();

        //변경감시.. 더티체킹
        //1.영속성 컨텍스트가 최초 상태를 스냅샷으로 보관
        //2.필드값 변경시 현재상태와 스냅샷 상태를 비교
        //3.트랜잭션 커밋이 되면 변경된 필드만 자동으로 업데이트 쿼리 생성
        //4.반영처리.. 커밋

    }//update

}//
