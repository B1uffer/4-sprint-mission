# Amazon Corretto 17 이미지를 베이스로 사용하기, 이미지 이름 + 태그 + 다이제스트 조합
FROM amazoncorretto:17@sha256:51842f6c1745a8fb131acadd6788950531d52b37af3caa0c193c14e05ab046b9

# 작업 디렉토리 설정하기
WORKDIR /app

# 프로젝트 파일을 컨테이너로 복사하기, 불필요한 파일은 .dockerignore를 활용해서 제외하기
COPY . .

# Gradle Wrapper를 사용해서 애플리케이션 빌드하기
# gradlew 실행 가능하게 만들기
RUN chmod +x /app/gradlew
# Gradle Wrapper로 빌드하기
RUN ./gradlew clean bootJar --no-daemon -x test

# 80 포트를 노출하도록 설정하기
EXPOSE 80

# 프로젝트 환경 변수 설정하기, 실행할 jar파일의 이름을 추론하는데 사용함
ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8

# JVM 옵션을 환경변수로 설정하기, JVM_OPTS=기본값은 빈 문자열로 정의
ENV JVM_OPTS=""

# 애플리케이션 실행 명령어 설정하기, 환경변수로 정의한 프로젝트 정보 활용하기
ENTRYPOINT ["/bin/sh", "-c", "exec java $JVM_OPTS -jar $(ls /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}*.jar | head -n 1)"]
