package com.example.demo.qna.repository.search;

import com.example.demo.qna.domain.QQuestionsEntity;
import com.example.demo.qna.domain.QnAStatus;
import com.example.demo.qna.domain.QuestionsEntity;
import com.example.demo.qna.dto.QuestionDTO;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Log4j2
public class QuestionSearchImpl extends QuerydslRepositorySupport implements QuestionSearch {

   public QuestionSearchImpl() {
       super(QuestionsEntity.class);
   }

    @Override
    public Page<QuestionsEntity> list(String keyword, QnAStatus status, Pageable pageable) {

        // Q 엔티티를 사용해 검색할 필드를 정의합니다.
        QQuestionsEntity questions = QQuestionsEntity.questionsEntity;

        JPQLQuery<QuestionsEntity> query = from(questions);
        query.where(questions.qno.gt(0L));

        // 키워드 검색: 키워드가 null이 아니면 질문리스트에 키워드를 포함하는 질문을 검색
        if (keyword != null) {
            query.where(questions.title.like("%" + keyword + "%"));
            query.where(questions.qcontent.like("%" + keyword + "%"));
        }
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
}
