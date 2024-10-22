package com.example.demo.qna.repository.search;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.qna.domain.QQuestionsEntity;
import com.example.demo.qna.domain.QnAStatus;
import com.example.demo.qna.domain.QuestionsEntity;
import com.example.demo.qna.dto.QuestionDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Log4j2
public class QuestionSearchImpl extends QuerydslRepositorySupport implements QuestionSearch {

    public QuestionSearchImpl() {
        super(QuestionsEntity.class);
    }

    // 키워드와 상태에 따른 질문 목록을 페이징 처리하여 반환하는 메서드
    @Override
    public Page<QuestionsEntity> list(String keyword, QnAStatus status, Pageable pageable) {

        // Q 엔티티를 사용해 검색할 필드를 정의합니다.
        QQuestionsEntity questions = QQuestionsEntity.questionsEntity;

        JPQLQuery<QuestionsEntity> query = from(questions);
        query.where(questions.qno.gt(0L));  // 기본 조건: 질문 번호가 0보다 큰 경우

        // 키워드 검색: 키워드가 null이 아니면 제목 또는 질문 내용에 키워드가 포함된 질문을 검색
        query.where(questions.title.like("%" + keyword + "%")
                .or(questions.qcontent.like("%" + keyword + "%")));


        // 상태 필터: 질문 상태가 null이 아니면, 해당 상태와 일치하는 질문을 검색
        if (status != null) {
            query.where(questions.status.eq(status));
        }

        // 페이징 처리: Querydsl을 사용하여 페이지네이션 적용
        this.getQuerydsl().applyPagination(pageable, query);

        // 결과 리스트를 쿼리로부터 fetch하여 가져옵니다.
        List<QuestionsEntity> list = query.fetch();

        // 총 결과 수를 fetchCount()를 통해 구합니다.
        long total = query.fetchCount();

        // 페이징 결과를 PageImpl 객체로 반환합니다.
        return new PageImpl<>(list, pageable, total);
    }

    // 질문 목록을 페이징 처리하여 반환하는 메서드
    public PageResponseDTO<QuestionDTO> List1(PageRequestDTO pageRequestDTO) {
        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("qno").descending());

        QQuestionsEntity questions = QQuestionsEntity.questionsEntity;

        // JPQLQuery 객체 생성
        JPQLQuery<QuestionsEntity> query = from(questions);

        // 조건 추가: 질문 번호가 0보다 큰 경우만 조회
        query.where(questions.qno.gt(0L));

        // 질문 번호로 그룹화
        query.groupBy(questions.qno);

        // 페이징 처리
        this.getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<QuestionDTO> tupleJPQLQuery = query.select(
                Projections.bean(QuestionDTO.class,
                        questions.qno.as("qno"),
                        questions.title.as("title"),
                        questions.qcontent.as("qcontent"),  // question을 qcontent로 변경
                        questions.status.as("status"),
                        questions.writer.as("writer"),
                        questions.viewCount.as("viewCount"),
                        questions.images.as("images")  // img1을 images로 변경
                )
        );


        log.info(tupleJPQLQuery);

        // DTO 리스트 페치
        List<QuestionDTO> dtoList = tupleJPQLQuery.fetch();

        // 각 DTO를 로그로 출력
        dtoList.forEach(log::info);

        // 총 레코드 수 계산
        long total = tupleJPQLQuery.fetchCount();

        // long 값을 int로 변환
        int totalCount = (int) total;

        // PageResponseDTO를 빌더 패턴으로 생성하여 반환
        return PageResponseDTO.<QuestionDTO>withAll()
                .totalCount(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .build();
    }
}
