package com.cykei.fifopaymentservice.product.repository;

public interface ProductOptionRepositoryCustom {
    void decreaseStock(long optionId, int count);
    void increaseStock(long optionId, int count);
}
