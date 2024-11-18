# 선착순 구매 쇼핑몰 사이트
다양한 상품을 구매할 수 있는 사이트의 백엔드를 제공합니다. 
상품들을 자유롭게 카테고리화해서 사람들에게 보여줄 수 있습니다.
상품들을 보여줄 때는 인기도 또는 등록순으로 사람들에게 보여줄 수 있습니다.  
상품들은 미리 등록한 재고 개수 만큼 선착순으로 구매할 수 있습니다.
사용자가 선호상품을 저장하고 조회할 수 있습니다. 

<details>
   <summary> API 명세서 </summary>
- 회원가입

![image](https://github.com/user-attachments/assets/4fb18921-305e-49dd-8254-2468d7e4dabb)


- 로그인

![image](https://github.com/user-attachments/assets/06b93037-d24d-4201-b060-3400dbeebae8)


- 상품조회

![image](https://github.com/user-attachments/assets/d038c7cb-c156-4335-853b-4f4d9bdb5512)


- 조회결과
    
    ```jsx
    {
        "totalCount": 4,
        "cursor": null,
        "orderResponses": [
            {
                "productId": 5,
                "name": "니트 스웨터",
                "mainImage": null,
                "price": 1,
                "options": [
                    "베이지",
                    "그레이"
                ]
            },
            {
                "productId": 4,
                "name": "가죽 재킷",
                "mainImage": null,
                "price": 1,
                "options": [
                    "브라운",
                    "블랙"
                ]
            },
            {
                "productId": 3,
                "name": "캐주얼 셔츠",
                "mainImage": null,
                "price": 1,
                "options": [
                    "화이트",
                    "스카이블루"
                ]
            },
            {
                "productId": 1,
                "name": "프리미엄 티셔츠",
                "mainImage": null,
                "price": 1,
                "options": [
                    "화이트",
                    "블랙",
                    "네이비"
                ]
            }
        ]
    }
    ```
    

- 상품 상세조회

![image](https://github.com/user-attachments/assets/4d8e8ca5-0c38-4c75-99f9-312efe275557)


- wish_list 조회

![image](https://github.com/user-attachments/assets/a3ca9707-73a2-41b1-9dbc-dab9443a6fe3)

- wish_list 추가

![image](https://github.com/user-attachments/assets/e995714a-3db9-4fd6-be00-3982f410d4e3)

- wish_list 삭제

![image](https://github.com/user-attachments/assets/05c5b28f-db84-42a0-af70-ba51bdde53ad)

- 상품주문

![image](https://github.com/user-attachments/assets/08a4208d-9420-4e39-8c37-5fcc1631a06c)

- 상품주문취소

![image](https://github.com/user-attachments/assets/ccd1a5ec-4483-4125-aee9-140b96e95617)

</details>

## 설치 방법
- 사전준비 : docker, python3 가 설치되어 있어야 합니다. 

```
python build.py
```

## 프로젝트 구조

### 아키텍쳐
![시스템아키텍처](https://github.com/user-attachments/assets/637cbf36-14de-4f20-97e6-c2856f98dd78)


### ERD
![ecommerce](https://github.com/user-attachments/assets/3869f074-1e8f-4537-a578-838c98153285)


### User flow
![image](https://github.com/user-attachments/assets/be04aaba-0834-4674-b020-c57df76a9563)
<br/>
## 개발환경
```
 Spring Boot Version : 3.3.4
 Spring Cloud Version : 2023.0.3
 JDK Version : 21
 Java Language Version : 21
 MySQL : 8.0
 Redis : 7.4
```

<br/>

## 주요기능
1. Spring Cloud Eureka 와 SpringGateway, FeignClient, Docker 를 이용하여 MSA 환경 구축
2. Redis를 이용한 재고관리 동시성처리
3. Redis Cache 를 이용한 인기상품 조회 성능 향상
4. SpringSecurity + JWT Token 방식으로 사용자 인증 인가 구현
5. Docker를 사용하여 빠른 실행환경 구축 가능   

<br/>

## 모듈별 상세 기능
- product-service
  * (ProductService) 상품을 카테고리별로 페이징처리하여 조회할 수 있습니다. 
  * (PopularProductService) 인기상품을 조회할 수 있습니다.
 
- order-service
  * 상품을 주문할 수 있고, 주문한것을 조회할 수 있습니다.
  * 아직 배송이 시작하지 않은 상품은 주문취소 할 수 있습니다.
  * 배송완료후 3일이 지나지 않은 상품의 경우 환불처리할 수 있습니다.
  * 카드결제는 15분내로 완료되어야합니다. 초과되는 경우 주문내역이 삭제됩니다.
  * 무통장입금의 경우 3일내로 완료되어야 완료되어야합니다. 초과되는 경우 주문내역이 삭제됩니다.
 
- wish-service
   * 선호 상품을 등록하고 조회할 수 있습니다.
   * 선호 상품 등록은 Product 단위로 선택할 수 있습니다.

- user-service
   * 사용자의 로그인 인증을 하면 JWT 토큰을 발행합니다.
   * 회원가입을 할 수 있습니다.
 
- gateway-service
   * 사용자에게 Autorization 헤더가 있으면 X-Claim-UserId 헤더에 userId 를 장착해서 반환합니다.
   * 인증이필요한 요청에 인증을 하지 않은 사용자가 접근하면 거부합니다.
 
- eureka-service
  * springCloud의 구현체인 유레카 서버를 띄우고 각 모듈의 상태를 관리합니다.

<br/>

## 성능개선
* JWT SecretKey 보안 취약성 보완
   * JWT 토큰에서 UserId 를 가져오기 위해 모든 서비스가 SecreteKey를 가진상태에서 API 호출이 들어오면 헤더에 있는 JWT 토큰을 SecreteKey로 복호화하는 과정을 시행했었습니다. SecretKey를 변경하는 경우 모든 서비스의 SecretKey를 같이 변경해줘야하는 불편함이 있었고, 분산저장되어 있다는 점에서 보안이 취약하는 문제점이 있었습니다.
   * Gateway가 SecretKey를 가지고 있게 하여 API호출이 들어오면 Gateway 단에서 JWT 토큰을 분석하여 UserId를 추출하고 헤더에 다시 장착해주는 방식으로 개선했습니다. 이를 통해서 SecretKey는 JWT토큰을 생성하는 UserService와 Gateway 두 모듈만 갖고 있게 하여 보안 취약성이 개선되었습니다.  

* 재고관리 동시성처리를 Redis 분산락 방식에서 Redis 캐싱 방싱으로 전환하여 속도 개선
  * AS-IS: 분산락
    * redis를 이용하여 구매하고자 하는 상품(productOption)에 락을 걸고, ProductService를 호출하여 해당 상품의 재고를 감소시킵니다.
    * 처리량: 평균 1초당 25건
    * 장점: 복잡한 트랜잭션 처리가 필요할 경우 유용하다. 장애처리가 간편하다. 
    * 단점: 서버 시간이 안맞을 경우 동시성 문제가 생길 수 있는 등 레디스로 구현하는 분산락에는 한계가 있다. 
  * TO-BE: 캐싱
    * product 를 등록한후, StockController의 initialize 를 호출하여 모든 상품의 재고상황을 redis에 올려서 관리합니다. StockService 에서 주기적으로 redis에 있는 재고 상황을 DB에 저장합니다.
    * 처리량: 평균 1초당 200건.
    * 장점: 재고처리와 같은 간단한 작업인 경우 DB를 접근하지 않기 때문에 처리속도가 빨라진다. 
    * 단점: 상품이 많아지는 경우 redis의 용량이 중요해집니다.
   
<br/> 

## todo
- 상품등록 및 조회 기능. (현재는 MySQL 에 직접 데이터를 입력해서 테스트 합니다)
- refreshToken을 사용한 인증인가 개발.
- gateway에 여러번 요청을 보내는 미인증 사용자의 IP 차단.
- 상품 이미지 테이블 생성
