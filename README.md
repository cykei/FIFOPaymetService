# 선착순 구매 쇼핑몰 사이트
다양한 상품을 구매할 수 있는 사이트의 백엔드를 제공합니다. 
상품들을 자유롭게 카테고리화해서 사람들에게 보여줄 수 있습니다.
상품들을 보여줄 때는 인기도 또는 등록순으로 사람들에게 보여줄 수 있습니다.  
상품들은 미리 등록한 재고 개수 만큼 선착순으로 구매할 수 있습니다.
사용자가 선호상품을 저장하고 조회할 수 있습니다. 

# 설치 방법
- 사전준비 : docker, python3 가 설치되어 있어야 합니다. 

```
python build.py
```

# 프로젝트 구조

## 아키텍쳐
![image](https://github.com/user-attachments/assets/8deab6ff-4543-4eb7-9223-6520e447b13d)

## ERD
![image](https://github.com/user-attachments/assets/5766e5a4-1ff6-42d1-9696-de697ead7c6b)

## User flow
![image](https://github.com/user-attachments/assets/99560745-3498-4d26-ada8-ed34e9dbd1ab)

## 재고 관리 방법
- 분산락을 이용한 방법과 캐싱을 이용한 방법 두가지를 제공하고 있습니다.
- 분산락(redlock 브랜치)
    * redis를 이용하여 구매하고자 하는 상품(productOption)에 락을 걸고, ProductService를 호출하여 해당 상품의 재고를 감소시킵니다.
    * 처리량: 평균 1초당 25건. (환경에 따라 다를 수 있습니다.)
    * 장점: 캐싱처리의 단점
    * 단점: 처리량이 느립니다. 
- 캐싱(caching 브랜치)
    * product 를 등록한후, StockController의 initialize 를 호출하여 모든 상품의 재고상황을 redis에 올려서 관리합니다. StockService 에서 주기적으로 redis에 있는 재고 상황을 DB에 저장합니다.
    * 처리량: 평균 1초당 200건.
    * 장점: 처리량이 빠릅니다.
    * 단점
      * 상품이 많아지는 경우 redis의 용량이 중요해집니다. redis 장애발생 또는 product service 의 장애 발생시 재고 상황이 날라갈 수 있습니다. (아직 장애처리 안되있음)
      * 상품의 재고가 증가 할때마다 redis에 등록하는 을 initialize를 호출해야합니다.

# 모듈별 상세 기능
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
 
# todo
- 상품등록 및 조회 기능. (현재는 MySQL 에 직접 데이터를 입력해서 테스트 합니다)
- refreshToken을 사용한 인증인가 개발.
- gateway에 여러번 요청을 보내는 미인증 사용자의 IP 차단.
- 상품 이미지 테이블 생성
