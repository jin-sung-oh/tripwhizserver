package com.tripwhiz.tripwhizadminback.board;

import com.tripwhiz.tripwhizadminback.board.entity.Board;
import com.tripwhiz.tripwhizadminback.board.repository.BoardRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Log4j2
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    // 1. 100개의 더미 게시글 생성 테스트 (Create)
    @Test
    @Transactional
    @Commit
    public void insertDummies() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Board board = Board.builder()
                    .title("행사 상품 안내")
                    .bcontent("저녁메뉴추천받습니다 " + i)
                    .writer("관리자")
                    .viewCount(0)
                    .delFlag(false)
                    .createdDate(LocalDateTime.now())
                    .build();
            boardRepository.save(board);
            log.info("Saved Board: " + board);
        });
    }

    // 2. 단일 게시글 조회 테스트 (Read)
    @Test
    public void testRead() {
        Long bno = 1L;

        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElse(null);

        log.info(board);
        assertThat(board).isNotNull();
    }

    // 3. 게시글 수정 테스트 (Update)
    @Test
    @Transactional
    @Commit
    public void testUpdateBoard() {
        Long bno = 1L;

        Board board = boardRepository.findById(bno).orElseThrow();
        board.setTitle("업데이트된 제목");
        board.setBcontent("업데이트된 내용");
        boardRepository.save(board);

        Board updatedBoard = boardRepository.findById(bno).orElseThrow();
        assertThat(updatedBoard.getTitle()).isEqualTo("업데이트된 제목");
        assertThat(updatedBoard.getBcontent()).isEqualTo("업데이트된 내용");

        log.info("Updated Board: " + updatedBoard);
    }

    // 4. 게시글 삭제 테스트 (Delete)
    @Test
    @Transactional
    @Commit
    public void testDeleteBoard() {
        Long bno = 2L;

        boardRepository.deleteById(bno);
        Optional<Board> deletedBoard = boardRepository.findById(bno);

        assertThat(deletedBoard.isPresent()).isFalse();
        log.info("Deleted Board with bno: " + bno);
    }
}