#!/usr/bin/env bash

echo "📁 Generating FlossPay / OpenPay UPI rail  Phase 1 Scaffold..."

# API Service
mkdir -p api-service/src/main/java/com/openpay/api/{controller,dto,service,config,util}
mkdir -p api-service/src/main/resources
mkdir -p api-service/src/test/java/com/openpay/api
touch api-service/src/main/java/com/openpay/api/Application.java

# Worker Service
mkdir -p worker-service/src/main/java/com/openpay/worker/{processor,client}
mkdir -p worker-service/src/main/resources
mkdir -p worker-service/src/test/java/com/openpay/worker
touch worker-service/src/main/java/com/openpay/worker/WorkerApplication.java

# Shared Libs
mkdir -p shared-libs/src/main/java/com/openpay/shared/{dto,exception,util}
mkdir -p shared-libs/src/main/resources

# Database (Flyway migrations)
mkdir -p database/migrations
touch database/migrations/V1__init.sql
touch database/README.md

# Scripts
mkdir -p scripts
cp "$0" scripts/scaffold.sh 2>/dev/null || touch scripts/scaffold.sh

# Top-level files
touch pom.xml README.md .gitignore

echo "✅ Folder structure ready for native local development."
