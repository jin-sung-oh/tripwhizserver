package com.example.demo.faq.repository;

import com.example.demo.faq.domain.FAQEntity;
import com.example.demo.faq.domain.QFAQEntity;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class FAQFilterImpl extends QuerydslRepositorySupport implements FAQFilter {

    public FAQFilterImpl() {
        super(FAQEntity.class);
    }

    @Override
    public Page<FAQEntity> list(Pageable pageable) {
        QFAQEntity faqEntity = QFAQEntity.fAQEntity;


        JPQLQuery<FAQEntity> query = from(faqEntity);


        query.groupBy(faqEntity);

        // 페이징 및 정렬 처리
        this.getQuerydsl().applyPagination(pageable, query);

        List<FAQEntity> faqList = query.fetch();

        // 총 개수 계산
        long total = query.fetchCount();

        return new PageImpl<>(faqList, pageable, total);
    }

//    @Override
//    public Page<FAQEntity> listByCategory(Pageable pageable) {
//        QFAQEntity faqEntity = QFAQEntity.fAQEntity;
//
//        JPQLQuery<FAQEntity> query = from(faqEntity);
//
//        // enum 카테고리로 필터링
//        query.where(faqEntity.category.eq(category));
//
//        query.groupBy(faqEntity);
//
//        // 페이징 및 정렬 처리
//        this.getQuerydsl().applyPagination(pageable, query);
//
//        // 선택한 필드들로 결과 매핑
//        List<FAQEntity> faqList = query.select(faqEntity).fetch();
//
//        long total = query.fetchCount();
//
//        // FAQ 목록을 Page 객체로 반환
//        return new PageImpl<>(faqList, pageable, total);
//    }
}
