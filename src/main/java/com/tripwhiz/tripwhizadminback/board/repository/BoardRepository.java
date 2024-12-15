package com.tripwhiz.tripwhizadminback.board.repository;

import com.tripwhiz.tripwhizadminback.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    // 기본적인 CRUD는 JpaRepository에서 제공됨

    // 1. 제목으로 게시글 찾기 (Containing은 부분 일치 검색)
    List<Board> findByTitleContaining(String keyword);

    // 2. 작성자로 게시글 찾기
    List<Board> findByWriter(String writer);

    // 3. 삭제되지 않은 게시글 찾기
    List<Board> findByDelFlagFalse();

    // 4. bno로 게시글 찾기 (존재하지 않는 경우 대비 Optional)
    Optional<Board> findByBno(Long bno);

    // 5. 조회수 높은 게시글 상위 N개 가져오기 (쿼리 예시)
    @Query("SELECT b FROM Board b WHERE b.delFlag = false ORDER BY b.viewCount DESC")
    List<Board> findTopByOrderByViewCountDesc();
}
