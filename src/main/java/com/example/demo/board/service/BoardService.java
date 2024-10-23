package com.example.demo.board.service;

import com.example.demo.board.domain.BoardEntity;
import com.example.demo.board.dto.BoardDTO;
import com.example.demo.board.repository.BoardRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    //공지사항 작성
    public Long createBoard(BoardDTO boardDTO){
        BoardEntity boardEntity = boardDTO.toEntity();
        boardRepository.save(boardEntity);
        return boardEntity.getBno();
    }

    //공지사항 조회(번호로)
    public BoardDTO getBoard(Long bno){
        Optional<BoardEntity> boardEntity = boardRepository.findByBno(bno);
        return boardEntity.map(BoardDTO::fromEntity).orElse(null);
    }

    //게시글 목록 조회(삭제되지 않은 게시글)
    public List<BoardDTO> getBoardList(){
        List<BoardEntity> boardEntities = boardRepository.findByDelFlagFalse();
        return boardEntities.stream()
                .map(BoardDTO::fromEntity)
                .collect(Collectors.toList());
    }

    //게시글 수정
    public void updateBoard(Long bno, BoardDTO boardDTO){
        Optional<BoardEntity> boardEntityOptional = boardRepository.findByBno(bno);
        boardEntityOptional.ifPresent(boardEntity -> {
            boardEntity.setTitle(boardDTO.getTitle());
            boardEntity.setBcontent(boardDTO.getBcontent());
            boardEntity.setWriter(boardDTO.getWriter());
            boardEntity.setViewCount(boardDTO.getViewCount());
            boardRepository.save(boardEntity);
        });
    }
    // 게시글 삭제 (delFlag를 true로 설정)
    public void deleteBoard(Long bno) {
        Optional<BoardEntity> boardEntityOptional = boardRepository.findByBno(bno);
        boardEntityOptional.ifPresent(boardEntity -> {
            boardEntity.changeDelFlag(true);
            boardRepository.save(boardEntity);
        });
    }
}
