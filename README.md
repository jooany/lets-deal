# lets-deal
개인과 개인의 거래에서 부담없이 가격을 제안 및 거절하는 시스템을 중점으로 하는 중고거래 플랫폼 서비스
## 프로젝트 목표
- 부담없이 간단하게 사용 가능한 가격 제안 시스템 구축
- 단위테스트를 활용하여 코드의 신뢰성을 검증
- 1000만 건의 대용량 데이터 처리
- 가독성이 높고 유지보수에 용이한 코드 작성
- 객체지향 프로그래밍 기반의 기능 모듈화 및 재사용성 증진
## 사용 기술 및 개발 환경
Java, Spring Boot, IntelliJ, Gradle, Spring Data JPA, QueryDSL, MyBatis, PostgreSQL, AWS(EC2, S3, RDS,CodeDeploy), Redis

### 시스템 아키텍처
![image](https://github.com/jooany/lets-deal/assets/83267254/ac095380-eab8-4487-b21f-cc613f0ef36a)






### Technical Issue
* **[사용자 인증 및 인가 - Spring Security & JWT & Redis](https://jooany.tistory.com/2)**<br/>
* **[Github Actions를 활용한 CI 구축](https://jooany.tistory.com/3)**<br/>
* **[Github Actions와 AWS CodeDeploy를 활용한 CD 구축](https://jooany.tistory.com/4)**<br/>
* 구현,작성 필요- 조회 쿼리 성능 개선 (적절한 DB INDEX 설정, 1000만건, 성능 측정)
* 작성 필요 - JPA 한계 극복을 위한 MyBatis 도입(QueryDSL, nativeQuery, mybatis 도입 중 고민)<br/>
