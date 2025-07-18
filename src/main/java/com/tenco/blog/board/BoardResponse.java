package com.tenco.blog.board;

import com.tenco.blog.reply.Reply;
import com.tenco.blog.user.SessionUser;
import com.tenco.blog.user.User;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardResponse {

    public static class MainDTO {
        private long id;
        private String title;
        private String content;
        private String writerName;
        private String createdAt;

        @Builder
        public MainDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.writerName = board.getUser().getUsername();
            this.createdAt = board.getCreatedAt().toString();
        }
    }//MainDTO

    //게시글 상세보기 DTO 설계
    @Data
    public static class DetailDTO {
        private long id;
        private String title;
        private String content;
        private String writerName;
        private String createdAt;
        private boolean isBoardOwner;

        private List<ReplyDTO> replies;

        public DetailDTO(Board board, SessionUser sessionUser) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.writerName = board.getUser().getUsername();
            this.createdAt = board.getCreatedAt().toString();
            //권한체크
            this.isBoardOwner = sessionUser != null && board.isOwner(sessionUser.getId());

            this.replies = new ArrayList<>(); //조심, 많이 실수하는 부분
            //갯수만큼 반복하면서 응답 DTO 변수 안에 값을 할당.. 자료구조 다듬기, 실무에서 많이 사용된다.
            for (Reply reply : board.getReplies()) {
                this.replies.add(new ReplyDTO(reply, sessionUser));
            }
        }
    }//DetailDTO

    @Data
    public static class ReplyDTO {

        private Long id;
        private String comment;
        private String writerName;
        private String createdAt;
        private boolean isReplyOwner;

        @Builder
        public ReplyDTO(Reply reply, SessionUser sessionUser) {
            this.id = reply.getId();
            this.comment = reply.getTime();
            this.writerName = reply.getUser().getUsername();
            this.createdAt = reply.getCreatedAt().toString();
            this.isReplyOwner = sessionUser != null && reply.isOwner(sessionUser.getId());
        }
    }

    //게시글 작성 응답 DTO
    @Data
    public static class SaveDTO {

        private long id;
        private String title;
        private String content;
        private String writerName;
        private String createdAt;

        @Builder
        public SaveDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.writerName = board.getUser().getUsername();
            this.createdAt = board.getCreatedAt().toString();
        }

    }//SaveDTO

    //게시글 수정 응답 DTO
    @Data
    public static class UpdateDTO {

        private long id;
        private String title;
        private String content;
        private String writerName;
        private String createdAt;

        @Builder
        public UpdateDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.writerName = board.getUser().getUsername();
            this.createdAt = board.getCreatedAt().toString();
        }

    }//UpdateDTO

}//BoardResponse
