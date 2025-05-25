# Docker purpose variables
POSTGRES_PORT=5432
POSTGRES_USER=postgres
POSTGRES_PASSWORD=P@ssw0rd
POSTGRES_DB=department
KAFKA_PORT=9092
APP_PORT=8081
NETWORK=app-network
KAFKA_CLUSTER_ID=CcsfQr-zTweZlDPJZf-4EQ

# Generating JWT keys
# This will generate the keys and store them in src/main/resources
generate-jwt-keys:
	@echo "Generating JWT keys..."
	@./src/main/resources/generate-jwt-keys.sh
	@echo "JWT keys generated successfully."

# Running Quarkus in development mode
dev:
	@echo "Running Quarkus in development mode..."
	./mvnw quarkus:dev

# Building the application as a JAR file
# This will run Maven Lifecycle phase "package": clean → validate → compile → test → package, 
# which cleans the target directory, compiles the code, runs tests, and packages the application into a JAR file.
package:
	@echo "Building the application as a JAR file..."
	./mvnw clean package -DskipTests

# Running tests
# This will run the tests defined in the project.
test:
	@echo "Running tests..."
	./mvnw test

# Cleaning the project
# This will clean the target directory, removing all compiled files and JARs.
clean:
	@echo "Cleaning the project..."
	./mvnw clean


# Docker related targets
# Create a Docker network if it does not exist
docker-create-network:
	docker network inspect $(NETWORK) >NUL 2>&1 || docker network create $(NETWORK)

# Remove the Docker network if it exists
docker-remove-network:
	docker network rm $(NETWORK)

# Build PostgreSQL
docker-build-postgres:
	docker build -t custom-postgres ./src/main/docker/postgres

# Run PostgreSQL
docker-run-postgres:
	docker run --name postgres-server --network $(NETWORK) -p $(POSTGRES_PORT):5432 \
	-e POSTGRES_DB=$(POSTGRES_DB) \
	-e POSTGRES_USER=$(POSTGRES_USER) \
	-e POSTGRES_PASSWORD=$(POSTGRES_PASSWORD) \
	-d custom-postgres

# Build and run PostgreSQL container
docker-build-run-postgres: docker-build-postgres docker-run-postgres

# Remove PostgreSQL container
docker-remove-postgres:
	docker stop postgres-server
	docker rm postgres-server

# Run Kafka with KRaft mode (Kafka without Zookeeper)
docker-run-kafka:
	docker run --name kafka-server --network $(NETWORK) -p $(KAFKA_PORT):$(KAFKA_PORT) \
	-e KAFKA_CFG_NODE_ID=1 \
	-e KAFKA_CFG_PROCESS_ROLES=broker,controller \
	-e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=1@kafka-server:9093 \
	-e KAFKA_CFG_LISTENERS=PLAINTEXT://:$(KAFKA_PORT),CONTROLLER://:9093 \
	-e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://kafka-server:$(KAFKA_PORT) \
	-e KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT \
	-e KAFKA_CFG_INTER_BROKER_LISTENER_NAME=PLAINTEXT \
	-e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER \
	-e KAFKA_CLUSTER_ID=$(KAFKA_CLUSTER_ID) \
	-v kafka-data:/bitnami/kafka \
	-d bitnami/kafka:latest

# Remove Kafka
docker-remove-kafka:
	docker stop kafka-server
	docker rm kafka-server

# Build the Quarkus application in Docker
docker-build-app:
	docker build -f src/main/docker/app/Dockerfile -t my-quarkus-app .

# Wait for Kafka to be ready
# This target is used to wait for Kafka to be ready before running the Quarkus application (on Windows version).
docker-wait-kafka-on-windows:
	@call wait-kafka.bat

# This target is used to wait for Kafka to be ready before running the Quarkus application (Linux version).
docker-wait-kafka-on-linux:
	@echo "Waiting for Kafka to be ready..."
	@until docker exec kafka-server kafka-topics.sh --bootstrap-server localhost:$(KAFKA_PORT) --list >/dev/null 2>&1; do \
		sleep 1; \
		echo "Waiting..."; \
	done
	@echo "Kafka is ready!"

# Run the Quarkus application in Docker
docker-run-app: 
	docker run --name quarkus-app --network $(NETWORK) -p $(APP_PORT):$(APP_PORT) \
	-d my-quarkus-app

# Build and run the Quarkus application
docker-build-run-app: docker-build-app docker-run-app

# Remove the Quarkus application container
docker-remove-app:
	docker stop quarkus-app
	docker rm quarkus-app

# Start all services: PostgreSQL, Kafka, and the Quarkus application
docker-start-all: docker-create-network docker-build-postgres docker-run-postgres docker-run-kafka docker-build-app docker-wait-kafka-on-windows docker-run-app

# Stop all services: PostgreSQL, Kafka, and the Quarkus application
docker-stop-all: docker-remove-app docker-remove-kafka docker-remove-postgres docker-remove-network

.PHONY: generate-jwt-keys dev package test clean \
	docker-create-network docker-remove-network docker-build-postgres docker-run-postgres \
	docker-build-run-postgres docker-remove-postgres docker-run-kafka docker-remove-kafka \
	docker-build-app docker-wait-kafka-on-windows docker-wait-kafka-on-linux \
	docker-run-app docker-build-run-app docker-remove-app \
	docker-start-all docker-stop-all