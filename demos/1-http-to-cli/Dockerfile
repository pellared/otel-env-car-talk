FROM golang:1.25-bookworm AS go-build

WORKDIR /src

COPY go.mod go.sum ./
RUN go mod download

COPY cmd ./cmd

RUN CGO_ENABLED=0 GOOS=linux go build -o /out/report-api ./cmd/report-api

FROM maven:3.9-eclipse-temurin-21 AS java-build

WORKDIR /src

COPY client-java/pom.xml ./pom.xml
RUN mvn --batch-mode dependency:go-offline

COPY client-java/src ./src
RUN mvn --batch-mode --no-transfer-progress package

FROM python:3.13-slim-bookworm AS api

ENV PYTHONDONTWRITEBYTECODE=1 \
    PYTHONUNBUFFERED=1

WORKDIR /app

COPY python/requirements.txt ./requirements.txt
RUN pip install --no-cache-dir --requirement requirements.txt

COPY python/report.py ./report.py
COPY --from=go-build /out/report-api /usr/local/bin/report-api

FROM eclipse-temurin:21-jre-jammy AS client

WORKDIR /app

COPY --from=java-build /src/target/report-client-1.0.0.jar ./report-client.jar

CMD ["java", "-jar", "/app/report-client.jar"]
