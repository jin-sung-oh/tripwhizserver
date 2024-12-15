package com.tripwhiz.tripwhizadminback.order.entity;

public enum OrderStatus {
    CANCELLED, // 주문이 취소된 상태
    PREPARING, // 주문이 준비 중인 상태
    APPROVED, // 점주가 주문을 승인한 상태
    READY_FOR_PICKUP, // 주문 픽업 준비 완료 상태
    PICKED_UP // 주문이 픽업된 상태
}
