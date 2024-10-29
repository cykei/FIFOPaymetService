CREATE DATABASE IF NOT EXISTS `ecommerce` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `ecommerce`;

-- Table structure for table `category`
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

CREATE INDEX IX_relationship_category_product ON relationship_category_product(category_id, product_id);
CREATE INDEX IX_product_option ON product_option(product_id);
CREATE INDEX IX_wish ON wish(user_id);
CREATE INDEX IX_order_product ON order_product(order_id);
CREATE INDEX IX_orders ON orders(user_id);
