# Transporte Público - Cloud Native (DYS2206)

## Estructura
- ms-productor   Puerto 8081 | Publica ubicaciones cada 1s (Cron)
- ms-procesador  Puerto 8082 | Consume ubicaciones, produce horarios
- ms-monitor     Puerto 8083 | Consume ambos tópicos, guarda en H2

## Levantar solo el cluster Kafka
docker-compose up zookeeper-1 zookeeper-2 zookeeper-3 kafka-1 kafka-2 kafka-3 kafka-ui

## Levantar todo
docker-compose up --build

## Kafka UI
http://localhost:8080

## H2 Consoles
- Productor:  http://localhost:8081/h2-console
- Procesador: http://localhost:8082/h2-console
- Monitor:    http://localhost:8083/h2-console
