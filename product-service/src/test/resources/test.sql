CREATE TABLE `category` (
                            `category_id` bigint NOT NULL AUTO_INCREMENT,
                            `parent_category_id` bigint NOT NULL,
                            `category_name` varchar(255) DEFAULT NULL,
                            PRIMARY KEY (`category_id`),
                            `created_at` datetime(6) DEFAULT NULL,
                            `updated_at` datetime(6) DEFAULT NULL
);

-- Table structure for table `order_product`
CREATE TABLE `order_product` (
                                 `final_price` int NOT NULL,
                                 `product_count` int NOT NULL,
                                 `option_id` bigint NOT NULL,
                                 `order_id` bigint NOT NULL,
                                 `product_id` bigint NOT NULL,
                                 `order_product_id` bigint NOT NULL AUTO_INCREMENT,
                                 PRIMARY KEY (`order_product_id`),
                                 `created_at` datetime(6) DEFAULT NULL,
                                 `updated_at` datetime(6) DEFAULT NULL
);

-- Table structure for table `orders`
CREATE TABLE `orders` (
                          `order_id` bigint NOT NULL AUTO_INCREMENT,
                          `order_total_price` bigint NOT NULL,
                          `user_id` bigint NOT NULL,
                          `order_address` varchar(255) DEFAULT NULL,
                          `order_status` enum('DELIVERY_COMPLETE','DELIVERY_NOW','ORDER_CANCEL','ORDER_COMPLETE','RETURN_COMPLETE','RETURN_REQUEST') DEFAULT NULL,
                          PRIMARY KEY (`order_id`),
                          `created_at` datetime(6) DEFAULT NULL,
                          `updated_at` datetime(6) DEFAULT NULL
);

-- Table structure for table `product`
CREATE TABLE `product` (
                           `product_id` bigint NOT NULL AUTO_INCREMENT,
                           `price` int NOT NULL,
                           `image` varchar(255) DEFAULT NULL,
                           `name` varchar(255) DEFAULT NULL,
                           PRIMARY KEY (`product_id`),
                           `created_at` datetime(6) DEFAULT NULL,
                           `updated_at` datetime(6) DEFAULT NULL
);

-- Table structure for table `product_option`
CREATE TABLE `product_option` (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `option_price` int NOT NULL,
                                  `product_count` int NOT NULL,
                                  `product_id` bigint DEFAULT NULL,
                                  `option_image` varchar(255) DEFAULT NULL,
                                  `option_name` varchar(255) DEFAULT NULL,
                                  PRIMARY KEY (`id`),
                                  `created_at` datetime(6) DEFAULT NULL,
                                  `updated_at` datetime(6) DEFAULT NULL
);

-- Table structure for table `relationship_category_product`
CREATE TABLE `relationship_category_product` (
                                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                                 `category_id` bigint NOT NULL,
                                                 `product_id` bigint NOT NULL,
                                                 PRIMARY KEY (`id`),
                                                 `created_at` datetime(6) DEFAULT NULL,
                                                 `updated_at` datetime(6) DEFAULT NULL
);

-- Table structure for table `user`
CREATE TABLE `user` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `address` varchar(255) DEFAULT NULL,
                        `email` varchar(255) DEFAULT NULL,
                        `name` varchar(255) DEFAULT NULL,
                        `password` varchar(255) DEFAULT NULL,
                        `phone` varchar(255) DEFAULT NULL,
                        `roles` varchar(255) DEFAULT NULL,
                        PRIMARY KEY (`id`),
                        `created_at` datetime(6) DEFAULT NULL,
                        `updated_at` datetime(6) DEFAULT NULL
);

-- Table structure for table `wish`
CREATE TABLE `wish` (
                        `wish_id` bigint NOT NULL AUTO_INCREMENT,
                        `product_id` bigint NOT NULL,
                        `user_id` bigint NOT NULL,
                        PRIMARY KEY (`wish_id`),
                        `created_at` datetime(6) DEFAULT NULL,
                        `updated_at` datetime(6) DEFAULT NULL
);


-- product_category 테이블에 인덱스 추가
CREATE INDEX idx_categoryId_productId ON relationship_category_product(category_id, product_id);

-- options 테이블에 인덱스 추가
CREATE INDEX idx_productId ON product_option(product_id);

INSERT INTO product (product_id, name, price) VALUES
                                                  (1, '프리미엄 티셔츠', 1),
                                                  (2, '클래식 청바지', 1),
                                                  (3, '캐주얼 셔츠', 1),
                                                  (4, '가죽 재킷', 1),
                                                  (5, '니트 스웨터',1);

-- Product Category 테이블 데이터 삽입
-- category_id 의미: 1=상의, 2=하의, 3=아우터
INSERT INTO relationship_category_product (product_id, category_id) VALUES
                                                                        (1, 1),  -- 프리미엄 티셔츠는 상의 카테고리
                                                                        (2, 2),  -- 클래식 청바지는 하의 카테고리
                                                                        (3, 1),  -- 캐주얼 셔츠는 상의 카테고리
                                                                        (4, 3),  -- 가죽 재킷은 아우터 카테고리
                                                                        (5, 1),  -- 니트 스웨터는 상의 카테고리
                                                                        (4, 1),  -- 가죽 재킷은 상의 카테고리도 됨 (다중 카테고리 예시)
                                                                        (5, 3);  -- 니트 스웨터는 아우터 카테고리도 됨 (다중 카테고리 예시)

-- Options 테이블 데이터 삽입
INSERT INTO product_option (product_id, option_name, option_price, product_count) VALUES
                                                                                      (1, '화이트',1,1),    -- 프리미엄 티셔츠 컬러들
                                                                                      (1, '블랙',1,1),
                                                                                      (1, '네이비',1,1),
                                                                                      (2, '블루',1,1),      -- 클래식 청바지 컬러들
                                                                                      (2, '블랙',1,1),
                                                                                      (3, '화이트',1,1),    -- 캐주얼 셔츠 컬러들
                                                                                      (3, '스카이블루',1,1),
                                                                                      (4, '브라운',1,1),    -- 가죽 재킷 컬러들
                                                                                      (4, '블랙',1,1),
                                                                                      (5, '베이지',1,1),    -- 니트 스웨터 컬러들
                                                                                      (5, '그레이',1,1);


-- INSERT INTO category(category_id, parent_category_id, category_name) VALUES
--                                     (1,0,'상의'),
--                                     (2,0,'하의'),
--                                     (3,0,'아우터'),
--                                     (4,1,'티셔츠'),
--                                     (5,2, '바지'),
--                                     (6,2, '치마');

