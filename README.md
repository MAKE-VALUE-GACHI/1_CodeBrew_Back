# Doctor Geondam

건강기능식품 추천 서비스 API

## 📋 프로젝트 소개

Doctor Geondam은 사용자의 건강 정보를 기반으로 맞춤형 건강기능식품을 추천하는 서비스입니다.

## 🚀 주요 기능

### 회원 관리

- 회원가입 (이용약관 및 개인정보처리방침 동의 포함)
- 로그인/로그아웃
- JWT 기반 인증
- SMS 인증 코드 발송 및 확인
- 사용자 정보 관리

### 건강 프로필 관리

- 개인 건강 정보 등록
    - 출생년도, 수면시간, 운동 빈도
    - 흡연/음주 습관
    - 건강 고민사항 (관절/뼈, 혈액순환, 장 건강, 눈 건강, 만성 피로, 면역력)
    - 알레르기 정보
    - 복용 중인 약물/건강기능식품
    - 선호하는 제형 (캡슐, 정제, 분말, 젤리 등)
- 건강 프로필 조회/수정/삭제

### 메타데이터 API

- 건강 고민 목록 제공
- 건강기능식품 제형 목록 제공
- 흡연/음주 습관 선택지 제공

## 🛠 기술 스택

- **Backend**: Spring Boot 3.5.0, Java 21
- **Database**: MySQL 8.0
- **Security**: Spring Security, JWT
- **Documentation**: Swagger/OpenAPI 3
- **Testing**: JUnit 5, MockMvc, H2 Database
- **Build Tool**: Gradle (Kotlin DSL)

## 📚 API 문서

애플리케이션 실행 후 다음 URL에서 API 문서를 확인할 수 있습니다:

- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 🏃 실행 방법

### 1. Docker를 사용한 실행 (권장)

```bash
# 저장소 클론
git clone <repository-url>
cd doctorgeondam

# Docker Compose로 실행
docker-compose up -d
```

애플리케이션이 http://localhost:8080 에서 실행됩니다.

### 2. 로컬 환경에서 실행

#### 사전 요구사항

- Java 21
- MySQL 8.0

#### 실행 단계

```bash
# 저장소 클론
git clone <repository-url>
cd doctorgeondam

# 의존성 설치 및 빌드
./gradlew build

# 애플리케이션 실행
./gradlew bootRun
```

## 🧪 테스트

### 전체 테스트 실행

```bash
./gradlew test
```

### 테스트 커버리지 확인

```bash
./gradlew jacocoTestReport
```

### 특정 테스트 클래스 실행

```bash
./gradlew test --tests "UserHealthProfileServiceTest"
```

## 📁 프로젝트 구조

```
src/
├── main/
│   ├── java/codebrew/doctorgeondam/
│   │   ├── controller/          # REST API 컨트롤러
│   │   │   ├── dto/            # DTO 클래스들
│   │   │   ├── AuthController.java
│   │   │   ├── UserController.java
│   │   │   ├── UserHealthProfileController.java
│   │   │   └── MetadataController.java
│   │   ├── service/            # 비즈니스 로직
│   │   │   ├── auth/          # 인증 관련 서비스
│   │   │   ├── user/          # 사용자 관련 서비스
│   │   │   └── sms/           # SMS 관련 서비스
│   │   ├── domain/            # 도메인 모델
│   │   ├── entity/            # JPA 엔티티
│   │   ├── repository/        # 데이터 접근 계층
│   │   ├── exception/         # 예외 처리
│   │   ├── jwt/              # JWT 관련
│   │   ├── security/         # 보안 설정
│   │   └── config/           # 설정 클래스
│   └── resources/
│       ├── application.yml
│       └── application-test.yml
└── test/
    ├── java/codebrew/doctorgeondam/
    │   ├── controller/        # 컨트롤러 테스트
    │   ├── service/          # 서비스 테스트
    │   ├── repository/       # 리포지토리 테스트
    │   ├── domain/           # 도메인 테스트
    │   ├── integration/      # 통합 테스트
    │   └── config/           # 테스트 설정
    └── resources/
        └── application-test.yml
```

## 🔐 환경 설정

### 필수 환경 변수

```yaml
# application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/doctorgeondam
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:password}

jwt:
  secret: ${JWT_SECRET:your-secret-key}
  access-token-expiration: 3600000  # 1시간
  refresh-token-expiration: 86400000 # 24시간
```

### Docker 환경 설정

Docker Compose는 다음 설정을 사용합니다:

- **MySQL**: 포트 3306, 데이터베이스명 `doctorgeondam`
- **Spring Boot**: 포트 8080
- **데이터베이스 루트 패스워드**: `rootpassword`

## 📖 API 사용 예시

### 1. 회원가입

```bash
curl -X POST http://localhost:8080/signup \
  -H "Content-Type: application/json" \
  -d '{
    "name": "홍길동",
    "phoneNumber": "010-1234-5678",
    "email": "hong@example.com",
    "password": "password123",
    "termsAgreed": true,
    "privacyPolicyAgreed": true
  }'
```

### 2. 로그인

```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{
    "phoneNumber": "010-1234-5678",
    "password": "password123"
  }'
```

### 3. 건강 프로필 생성

```bash
curl -X POST http://localhost:8080/api/user/health-profile \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -d '{
    "birthYear": 1990,
    "sleepHours": 7,
    "exerciseFrequencyPerWeek": 3,
    "smokingHabit": "NON_SMOKER",
    "drinkingHabit": "SOCIAL_DRINKER",
    "healthConcerns": ["JOINT_BONE_HEALTH", "EYE_HEALTH"],
    "hasAllergies": false,
    "preferredSupplementForms": ["CAPSULE"]
  }'
```

### 4. 메타데이터 조회

```bash
# 건강 고민 목록
curl http://localhost:8080/api/metadata/health-concerns

# 제형 목록
curl http://localhost:8080/api/metadata/supplement-forms
```

## 🔍 테스트 구성

### 단위 테스트

- **도메인 테스트**: 도메인 객체의 비즈니스 로직 테스트
- **서비스 테스트**: Mock을 사용한 서비스 계층 테스트
- **리포지토리 테스트**: `@DataJpaTest`를 사용한 JPA 테스트

### 통합 테스트

- **컨트롤러 테스트**: `@WebMvcTest`를 사용한 웹 계층 테스트
- **전체 플로우 테스트**: `@SpringBootTest`를 사용한 end-to-end 테스트

### 테스트 데이터베이스

- H2 in-memory 데이터베이스 사용
- 각 테스트마다 독립적인 데이터베이스 상태 보장

## 🏗 아키텍처 특징

### Clean Architecture

- **도메인 계층**: 비즈니스 로직과 규칙
- **애플리케이션 계층**: 유스케이스와 서비스
- **인프라 계층**: 데이터베이스, 외부 API 연동
- **프레젠테이션 계층**: REST API 컨트롤러

### 설계 원칙

- **단일 책임 원칙**: 각 클래스는 하나의 책임만 가짐
- **의존성 역전**: 인터페이스를 통한 느슨한 결합
- **도메인 주도 설계**: 비즈니스 로직을 도메인 객체에 캡슐화

### 보안

- JWT 기반 인증/인가
- 비밀번호 BCrypt 암호화
- CORS 설정
- 입력값 검증 (Bean Validation)

## 🚢 배포

### Docker를 사용한 배포

```bash
# 프로덕션 환경에서 실행
docker-compose -f docker-compose.prod.yml up -d
```

### 컨테이너 중지

```bash
# 컨테이너 중지
docker-compose down

# 볼륨까지 삭제 (데이터베이스 데이터 삭제됨)
docker-compose down -v
```

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다. 자세한 내용은 `LICENSE` 파일을 참조하세요.

## 👥 팀

**CodeBrew Team**

- Email: contact@codebrew.com

## 📞 지원

문의사항이나 버그 리포트는 다음을 통해 연락해 주세요:

- GitHub Issues
- Email: contact@codebrew.com
