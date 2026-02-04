# btgg-api

Spring Boot 기반 백엔드 API 서버

## 기술 스택

- Spring Boot 3.2
- Spring Security + JWT
- Spring Data JPA + QueryDSL
- H2 (로컬) / PostgreSQL (운영)
- Lombok

## 실행 방법

### 로컬 환경

```bash
./gradlew bootRun
```

### 운영 환경

```bash
./gradlew bootRun --args='--spring.profiles.active=prod'
```

환경 변수 설정 필요:
- `DB_HOST`: PostgreSQL 호스트
- `DB_PORT`: PostgreSQL 포트 (기본: 5432)
- `DB_NAME`: 데이터베이스 이름
- `DB_USERNAME`: 데이터베이스 사용자명
- `DB_PASSWORD`: 데이터베이스 비밀번호

## API 엔드포인트

### 인증

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/signup | 회원가입 |
| POST | /api/auth/login | 로그인 (JWT 토큰 발급) |

### 게시글

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/posts | 게시글 목록 조회 (검색/페이징) |
| GET | /api/posts/{id} | 게시글 상세 조회 |
| POST | /api/posts | 게시글 작성 |
| PUT | /api/posts/{id} | 게시글 수정 |
| DELETE | /api/posts/{id} | 게시글 삭제 |
| POST | /api/posts/{id}/like | 좋아요 토글 |
| GET | /api/posts/{id}/like | 좋아요 여부 확인 |

### 댓글

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/posts/{postId}/comments | 댓글 목록 조회 |
| POST | /api/posts/{postId}/comments | 댓글 작성 |
| PUT | /api/comments/{id} | 댓글 수정 |
| DELETE | /api/comments/{id} | 댓글 삭제 |

## H2 Console

로컬 환경에서 H2 Console 접속: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:btggdb`
- User Name: `sa`
- Password: (빈값)
