package com.tenco.blog.board;

import com.tenco.blog._core.errors.exception.Exception403;
import com.tenco.blog._core.errors.exception.Exception404;
import com.tenco.blog.reply.Reply;
import com.tenco.blog.user.User;
import com.tenco.blog.utils.Define;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Board 관련 비즈니스 로직을 처리하는 Service 계층
 */
@RequiredArgsConstructor
@Service // IoC 대상
@Transactional(readOnly = true)
// 모든 메서드를 일기 전용 트랜잭션으로 실행(findAll, findById 최적화)
// 성능 최적화 (변경 감지 비활성화), 데이터 수정 방지 ()
// 데이터이스 락(lock) 최소화 하여 동시성 성능 개선
public class BoardService {

    private static final Logger log = LoggerFactory.getLogger(BoardService.class);
    private final BoardJpaRepository boardJpaRepository;

    /**
     * 게시글 목록 조회 - 페이징 처리
     */
    public List<BoardResponse.MainDTO> list(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Board> boardPage = boardJpaRepository.findAllJoinUser(pageable);

        List<BoardResponse.MainDTO> boardList = new ArrayList<>();

        for (Board board : boardPage.getContent()) {

            BoardResponse.MainDTO mainDTO = new BoardResponse.MainDTO(board);

            boardList.add(mainDTO);

        }
        return null;
    }

    /**
     * 게시글 상세보기 + DTO 변환 책임
     * 리퀘스트 요청이 올때 컨트롤러까지 들어온다
     * JWT 토큰 정보에서 User 오브젝트를 추출해서 만든다
     *
     */
    public BoardResponse.DetailDTO detail(Long id, User sessionUser) {
        // 1. 게시글 조회
        Board board =  boardJpaRepository.findByIdJoinUser(id).orElseThrow(
                () -> new Exception404("게시글을 찾을 수 없습니다"));

        // 2. 게시글 작성자 정보 포함해 주어야 함
        // 3. 게시글 소유권 설정(수정/ 삭제버튼 표 시용)
        if(sessionUser != null) {
            boolean isBoardOwner = board.isOwner(sessionUser.getId());
            board.setBoardOwner(isBoardOwner);
        }

        //DTO 변환 책임 처리 (댓글정보는 DTO에서 처리)
        return new BoardResponse.DetailDTO(board, sessionUser);
    }

    /**
     * 게시글 저장 + DTO 변환 책임
     */
    @Transactional
    public BoardResponse.SaveDTO save(BoardRequest.SaveDTO saveDTO, User sessionUser) {

        //영속성 컨텍스트 (1차캐스, 쓰기지연)
        Board board = saveDTO.toEntity(sessionUser); //그냥 객체만 만든 것

        //JPA 저장 처리
        Board savedBoard = boardJpaRepository.save(board); //영속성 컨텍스트에서 관리됨

        //DTO 변환 책임 처리
        return new BoardResponse.SaveDTO(savedBoard);
    }

    /**
     * 게시글 삭제 + DTO 변환 책임
     */
    @Transactional
    public void delete(Long id, User sessionUser) {

        Board board = boardJpaRepository.findById(id).orElseThrow(() -> {
            return new Exception404("삭제하려는 게시글이 없습니다");
        });
        if(!board.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 게시글만 삭제할 수 있습니다");
        }
        boardJpaRepository.deleteById(id);

    }//delete

    /**
     *  게시글 수정 + DTO 변환 책임
     */
    @Transactional
    public BoardResponse.UpdateDTO update(Long id, BoardRequest.UpdateDTO updateDTO,
                            User sessionUser) {

        Board board = boardJpaRepository.findById(id).orElseThrow(() -> { //영속성 컨텍스트에 넣는게 더 빠르다
            return new Exception404("해당 게시글이 존재하지 않습니다");
        });

        if(!board.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 게시글만 수정 가능");
        }

        board.update(updateDTO);

        return new BoardResponse.UpdateDTO(board);

    }//update


}//BoardService
