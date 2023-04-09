#base
FROM openjdk:11
# 변수 (빌드파일 경로)
ARG JAR_FILE=build/libs/*.jar
# 컨테이너로 복사
COPY $JAR_FILE app.jar
# 인코딩 툴인 ffmpeg를 설치 /usr/bin/ffmpeg 기본경로
RUN apt-get update
RUN apt-get install -y ffmpeg
# 포트 8080 씀
EXPOSE 8080/tcp

ENTRYPOINT ["java","-jar","app.jar"]