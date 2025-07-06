# HH+ TDD 포인트 관리 API 명세서

## 개요
사용자의 포인트 조회, 충전, 사용 및 거래 내역을 관리하는 REST API입니다.

## 기본 정보
- **Base URL**: `http://localhost:8080`
- **API 버전**: 1.0.0
- **Content-Type**: `application/json`

## API 엔드포인트

### 1. 사용자 포인트 조회

**GET** `/point/{id}`

특정 사용자의 현재 포인트 잔액을 조회합니다.

#### Path Parameters
| 파라미터 | 타입 | 필수 | 설명 | 예시 |
|---------|------|------|------|------|
| id | long | Y | 사용자 ID | 1 |

#### Response
```json
{
  "id": 1,
  "point": 15000,
  "updateMillis": 1703123456789
}
```

#### Response Fields
| 필드 | 타입 | 설명 | 예시 |
|------|------|------|------|
| id | long | 사용자 ID | 1 |
| point | long | 현재 포인트 잔액 | 15000 |
| updateMillis | long | 마지막 업데이트 시간 (밀리초) | 1703123456789 |

---

### 2. 포인트 내역 조회

**GET** `/point/{id}/histories`

특정 사용자의 포인트 충전/사용 내역을 조회합니다.

#### Path Parameters
| 파라미터 | 타입 | 필수 | 설명 | 예시 |
|---------|------|------|------|------|
| id | long | Y | 사용자 ID | 1 |

#### Response
```json
[
  {
    "id": 1,
    "userId": 1,
    "amount": 10000,
    "type": "CHARGE",
    "updateMillis": 1703123456789
  },
  {
    "id": 2,
    "userId": 1,
    "amount": 5000,
    "type": "USE",
    "updateMillis": 1703123556789
  }
]
```

#### Response Fields
| 필드 | 타입 | 설명 | 예시 |
|------|------|------|------|
| id | long | 거래 내역 ID | 1 |
| userId | long | 사용자 ID | 1 |
| amount | long | 거래 금액 | 10000 |
| type | TransactionType | 거래 타입 | "CHARGE" |
| updateMillis | long | 거래 시간 (밀리초) | 1703123456789 |

---

### 3. 포인트 충전

**PATCH** `/point/{id}/charge`

특정 사용자의 포인트를 충전합니다.

#### Path Parameters
| 파라미터 | 타입 | 필수 | 설명 | 예시 |
|---------|------|------|------|------|
| id | long | Y | 사용자 ID | 1 |

#### Request Body
```json
10000
```

#### Request Fields
| 필드 | 타입 | 필수 | 설명 | 예시 |
|------|------|------|------|------|
| amount | long | Y | 충전할 포인트 금액 | 10000 |

#### Response
```json
{
  "id": 1,
  "point": 25000,
  "updateMillis": 1703123456789
}
```

---

### 4. 포인트 사용

**PATCH** `/point/{id}/use`

특정 사용자의 포인트를 사용합니다.

#### Path Parameters
| 파라미터 | 타입 | 필수 | 설명 | 예시 |
|---------|------|------|------|------|
| id | long | Y | 사용자 ID | 1 |

#### Request Body
```json
5000
```

#### Request Fields
| 필드 | 타입 | 필수 | 설명 | 예시 |
|------|------|------|------|------|
| amount | long | Y | 사용할 포인트 금액 | 5000 |

#### Response
```json
{
  "id": 1,
  "point": 20000,
  "updateMillis": 1703123456789
}
```

---

## 데이터 모델

### UserPoint
사용자의 포인트 정보를 나타내는 모델입니다.

```json
{
  "id": 1,
  "point": 15000,
  "updateMillis": 1703123456789
}
```

### PointHistory
포인트 거래 내역을 나타내는 모델입니다.

```json
{
  "id": 1,
  "userId": 1,
  "amount": 10000,
  "type": "CHARGE",
  "updateMillis": 1703123456789
}
```

### TransactionType
포인트 거래 타입을 나타내는 열거형입니다.

| 값 | 설명 |
|----|------|
| CHARGE | 포인트 충전 |
| USE | 포인트 사용 |

---

## 에러 응답

### 400 Bad Request
잘못된 요청 형식이나 유효하지 않은 데이터

```json
{
  "error": "Invalid request",
  "message": "요청 데이터가 유효하지 않습니다."
}
```

### 404 Not Found
요청한 리소스를 찾을 수 없음

```json
{
  "error": "User not found",
  "message": "사용자를 찾을 수 없습니다."
}
```

### 500 Internal Server Error
서버 내부 오류

```json
{
  "error": "Internal server error",
  "message": "서버 내부 오류가 발생했습니다."
}
```

---

## 사용 예시

### 1. 사용자 포인트 조회
```bash
curl -X GET "http://localhost:8080/point/1"
```

### 2. 포인트 내역 조회
```bash
curl -X GET "http://localhost:8080/point/1/histories"
```

### 3. 포인트 충전
```bash
curl -X PATCH "http://localhost:8080/point/1/charge" \
  -H "Content-Type: application/json" \
  -d "10000"
```

### 4. 포인트 사용
```bash
curl -X PATCH "http://localhost:8080/point/1/use" \
  -H "Content-Type: application/json" \
  -d "5000"
```

---

## 주의사항

1. **포인트 사용 시**: 사용하려는 포인트가 현재 잔액보다 클 경우 에러가 발생할 수 있습니다.
2. **음수 금액**: 충전이나 사용 금액은 양수여야 합니다.
3. **사용자 ID**: 존재하지 않는 사용자 ID로 요청 시 404 에러가 반환됩니다.
4. **타임스탬프**: 모든 시간은 Unix timestamp (밀리초) 형식으로 저장됩니다.