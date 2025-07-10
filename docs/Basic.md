# 의존성 주입 및 백엔드 설계 방식 보고서

## 1. @Autowired vs @RequiredArgsConstructor 비교

### 1.1. 장단점 및 권장 사용처

| 구분      | @Autowired                        | @RequiredArgsConstructor             |
|-----------|-----------------------------------|--------------------------------------|
| 장점      | 간단함, 테스트 편의성             | 불변성, 명시적 의존성, 테스트 용이성 |
| 단점      | 순환 참조 위험, 테스트 어려움      | 코드가 약간 길어짐                   |
| 권장 사용 | 테스트에서만 사용                 | 실제 코드에서 권장                   |

### 1.2. 차이점의 원인

- **주입 시점**
  - @Autowired: 객체 생성 후 런타임에 주입
  - @RequiredArgsConstructor: 객체 생성 시점에 주입

- **주입 방식**
  - @Autowired: 리플렉션을 통한 필드 직접 접근
  - @RequiredArgsConstructor: 생성자 파라미터를 통한 정상적인 주입

- **검증 시점**
  - @Autowired: 런타임에 의존성 검증
  - @RequiredArgsConstructor: 컴파일 타임에 의존성 검증

- **Spring 컨텍스트 의존성**
  - @Autowired: Spring 컨텍스트 필수
  - @RequiredArgsConstructor: 생성자만 있으면 Mock 객체로도 테스트 가능

> ※ **리플렉션**: 프로그램이 실행 중에 자신의 구조와 동작을 검사하고 수정할 수 있게 해주는 기능

---

## 2. 어노테이션 주입방식 vs 수동 생성자 방식

### 2.1. 어노테이션 주입방식의 선택 이유

- **개발 생산성**: 빠른 개발이 중요한 현대 환경에 적합
- **코드 품질**: 실수 방지 및 일관성 보장
- **팀 협업**: 표준화된 코딩 스타일 유지
- **유지보수성**: 자동 업데이트로 인한 안정성 확보

---

## 3. 함수형 프로그래밍 방식의 장점

### 3.1. 코드 예시

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

### 3.2. 장점

- **가독성**: 데이터 처리 흐름이 명확하게 드러남
- **불변성**: 원본 리스트를 변경하지 않음
- **체이닝**: 여러 연산을 연결하여 간결하게 처리 가능

---

## 4. 백엔드 계층별 역할

### 4.1. Controller

- 클라이언트(프론트엔드, 외부 시스템 등)로부터 HTTP 요청을 받아 처리
- 요청 데이터의 유효성 검사(Validation)
- Service 계층에 요청 전달
- 결과를 응답 형태로 가공 및 반환
- 예외 발생 시 적절한 에러 응답 반환

### 4.2. Service

- 비즈니스 로직 담당
- 여러 Repository와 연동하여 복잡한 연산, 트랜잭션, 비즈니스 규칙 처리
- 데이터 가공 및 조합
- 트랜잭션 관리
- 예외 발생 시 적절한 예외 throw

### 4.3. Entity

- 데이터베이스 테이블과 매핑되는 객체
- 데이터의 구조와 상태 표현
- 데이터의 영속성 관리
- 주로 JPA, Hibernate 등 ORM 프레임워크와 함께 사용

---

## 5. 테스트 대역(Test Double) 개념

Spring Boot 기반 TDD(Test Driven Development)에서 테스트 대역이란 실제 객체나 모듈을 테스트 환경에서 대체하는 **가짜 객체**를 의미합니다. 이를 통해 실제 구현체 대신 테스트 목적에 맞는 단순한 동작을 하는 객체로 교체하여, 테스트의 효율성과 신뢰성을 높일 수 있습니다.

### 5.1. 테스트 대역의 종류

- **스텁(Stub)**: 입력에 대해 미리 정해진 값을 반환하는 단순 객체
- **가짜(Fake)**: 실제 구현과 비슷하지만 제품에 적합하지 않은 간단한 버전(예: 메모리 DB)
- **스파이(Spy)**: 메서드 호출 내역 및 전달값을 기록하여 검증 가능한 객체
- **모의(Mock)**: 특정 상호작용이 기대대로 일어났는지 검증, 기대 동작 미달 시 예외 발생

---

이 보고서는 의존성 주입 방식, 함수형 프로그래밍, 백엔드 계층별 역할, 그리고 테스트 대역의 개념과 실무 적용 시 장단점을 종합적으로 정리하였습니다.
