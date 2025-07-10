# @Autowired vs @RequiredArgsConstructor 차이점
- 3. 장단점 비교
구분	@Autowired	@RequiredArgsConstructor
장점	간단함, 테스트 편의성 /	불변성, 명시적 의존성, 테스트 용이성
단점	순환 참조 위험, 테스트 어려움 /	코드가 약간 길어짐
권장	테스트에서만 사용 /	실제 코드에서 권장

왜 이런 차이가 생기는가?
1. 주입 시점의 차이
@Autowired: 객체 생성 후 런타임에 주입
@RequiredArgsConstructor: 객체 생성 시점에 주입

2. 주입 방식의 차이
@Autowired: 리플렉션을 통한 필드 직접 접근
@RequiredArgsConstructor: 생성자 파라미터를 통한 정상적인 주입

3. 검증 시점의 차이
@Autowired: 런타임에 의존성 검증
@RequiredArgsConstructor: 컴파일 타임에 의존성 검증

4. Spring 컨텍스트 의존성
@Autowired: Spring 컨텍스트가 반드시 필요
@RequiredArgsConstructor: 생성자만 있으면 Mock 객체로도 테스트 가능

※ 리플렉션 : 리플렉션은 프로그램이 실행 중에 자신의 구조와 동작을 검사하고 수정할 수 있게 해주는 기능입니다.

# 어노테이션 주입방식 vs 수동 생성자
이유:
- 개발 생산성: 빠른 개발이 중요한 현대 환경
- 코드 품질: 실수 방지와 일관성 보장
- 팀 협업: 표준화된 코딩 스타일
- 유지보수성: 자동 업데이트로 인한 안정성


# 5. 함수형 프로그래밍 방식의 장점
```java
// 명령형 방식 (기존)
public List<PointHistory> readPointHistories(long userId) {
    List<PointHistory> histories = pointHistoryTable.selectAllByUserId(userId);
    histories.sort((h1, h2) -> Long.compare(h2.updateMillis(), h1.updateMillis()));
    return histories;
}

// 함수형 방식 (현재 코드)
public List<PointHistory> readPointHistories(long userId) {
    return pointHistoryTable.selectAllByUserId(userId).stream()
        .sorted(Comparator.comparing(PointHistory::updateMillis).reversed())
        .toList();
}
```
장점:
가독성: 데이터 처리 흐름이 명확
불변성: 원본 리스트를 변경하지 않음
체이닝: 여러 연산을 연결하여 처리


# 6. 백엔드 계층별 역할
1. Controller
역할: 클라이언트(프론트엔드, 외부 시스템 등)로부터 HTTP 요청을 받아 처리하고, 적절한 Service 계층의 메서드를 호출하여 결과를 반환합니다.

주요 업무:

요청 데이터의 유효성 검사(Validation)

요청을 Service에 전달

Service에서 받은 결과를 응답 형태로 가공 및 반환

예외 발생 시 적절한 에러 응답 반환

2. Service
역할: 비즈니스 로직을 담당하는 계층입니다. 여러 Repository(데이터 접근 계층)와 연동하여 복잡한 연산, 트랜잭션, 비즈니스 규칙을 처리합니다.

주요 업무:

비즈니스 규칙 적용

데이터 가공 및 조합

트랜잭션 관리

예외 발생 시 적절한 예외 throw

3. Entity
역할: 데이터베이스 테이블과 매핑되는 객체로, 데이터의 구조와 상태를 표현합니다.

주요 업무:

데이터베이스의 테이블 구조와 1:1 매핑

데이터의 영속성 관리

주로 JPA, Hibernate 등 ORM 프레임워크와 함께 사용

4. Model (또는 DTO)
역할: 계층 간 데이터 전달을 위한 객체입니다. Entity와는 달리, 외부에 노출되는 데이터 구조를 정의하거나, 여러 Entity의 데이터를 조합해 전달할 때 사용합니다.

주요 업무:

Controller ↔ Service, Service ↔ Repository 간 데이터 전달

요청/응답 데이터 구조 정의

Entity와 분리하여 보안, 캡슐화, 유연성 확보