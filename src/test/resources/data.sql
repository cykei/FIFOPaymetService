CREATE TABLE product (
                         product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255),
                         image VARCHAR(255),
                         price BIGINT,
                         options VARCHAR(255),
                         created_at TIMESTAMP,
                         updated_at TIMESTAMP
);

-- ProductCategory 테이블 생성
CREATE TABLE product_category (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  category_id BIGINT,
                                  product_id BIGINT,
                                  created_at TIMESTAMP,
                                  updated_at TIMESTAMP
);

-- ProductOption 테이블 생성
CREATE TABLE product_option (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                product_id BIGINT,
                                option_name VARCHAR(255),
                                option_price INT,
                                option_image_url VARCHAR(255),
                                product_count INT,
                                created_at TIMESTAMP,
                                updated_at TIMESTAMP
);
INSERT INTO product (product_id, name) VALUES
                                                   (1, '프리미엄 티셔츠'),
                                                   (2, '클래식 청바지'),
                                                   (3, '캐주얼 셔츠'),
                                                   (4, '가죽 재킷'),
                                                   (5, '니트 스웨터');

-- Product Category 테이블 데이터 삽입
-- category_id 의미: 1=상의, 2=하의, 3=아우터
INSERT INTO product_category (product_id, category_id) VALUES
                                                           (1, 1),  -- 프리미엄 티셔츠는 상의 카테고리
                                                           (2, 2),  -- 클래식 청바지는 하의 카테고리
                                                           (3, 1),  -- 캐주얼 셔츠는 상의 카테고리
                                                           (4, 3),  -- 가죽 재킷은 아우터 카테고리
                                                           (5, 1),  -- 니트 스웨터는 상의 카테고리
                                                           (4, 1),  -- 가죽 재킷은 상의 카테고리도 됨 (다중 카테고리 예시)
                                                           (5, 3);  -- 니트 스웨터는 아우터 카테고리도 됨 (다중 카테고리 예시)

-- Options 테이블 데이터 삽입
INSERT INTO product_option (product_id, option_name) VALUES
                                            (1, '화이트'),    -- 프리미엄 티셔츠 컬러들
                                            (1, '블랙'),
                                            (1, '네이비'),
                                            (2, '블루'),      -- 클래식 청바지 컬러들
                                            (2, '블랙'),
                                            (3, '화이트'),    -- 캐주얼 셔츠 컬러들
                                            (3, '스카이블루'),
                                            (4, '브라운'),    -- 가죽 재킷 컬러들
                                            (4, '블랙'),
                                            (5, '베이지'),    -- 니트 스웨터 컬러들
                                            (5, '그레이');
