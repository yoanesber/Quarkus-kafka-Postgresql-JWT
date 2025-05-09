# Variabel untuk nama proyek dan profil
APP_NAME = quarkus-kafka-postgresql
PROFILE = dev

# Direktori yang digunakan
QUARKUS_DIR = ./target
JAR_FILE = $(QUARKUS_DIR)/$(APP_NAME)-runner.jar

# Makefile Targets

# 1. Generate JWT Keys by running the generate-jwt-keys.sh that is in the resources directory (src/main/resources)
#    This will generate the keys and store them in src/main/resources
#    This is a prerequisite for running the application
generate-jwt-keys:
	@echo "Menghasilkan kunci JWT..."
	@./src/main/resources/generate-jwt-keys.sh
	@echo "Kunci JWT telah dihasilkan dan disimpan di src/main/resources"

# 2. Install dependencies dan build aplikasi
install:
	@echo "Memasang dependensi dan menyiapkan aplikasi..."
	./mvnw clean install -DskipTests

# 3. Menjalankan Quarkus dalam mode pengembangan
dev:
	@echo "Menjalankan Quarkus dalam mode pengembangan..."
	./mvnw quarkus:dev

# 4. Menjalankan aplikasi dalam mode produk
run:
	@echo "Menjalankan aplikasi Quarkus..."
	./mvnw compile quarkus:dev

# 5. Build aplikasi dalam bentuk jar
package:
	@echo "Membangun aplikasi dalam bentuk JAR..."
	./mvnw clean package -DskipTests

# 6. Menguji aplikasi (junit tests)
test:
	@echo "Menjalankan pengujian aplikasi..."
	./mvnw test

# 7. Menjalankan aplikasi sebagai JAR setelah dibangun
start-jar:
	@echo "Menjalankan aplikasi Quarkus sebagai JAR..."
	java -jar $(JAR_FILE)

# 8. Membersihkan hasil build
clean:
	@echo "Membersihkan hasil build..."
	./mvnw clean

.PHONY: install dev run package test start-jar clean
