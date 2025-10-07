#!/usr/bin/env bash
set -euo pipefail

echo "=== Budowanie projektu PriorityQueues ==="

# 1. Czyszczenie artefaktów
echo "--> mvn clean"
mvn clean

# 2. Kompilacja kodu źródłowego
echo "--> mvn compile"
mvn compile

# 3. Uruchomienie testów
echo "--> mvn test"
mvn test

# 4. Paketowanie aplikacji
echo "--> mvn package"
mvn package

echo "=== Budowanie zakończone pomyślnie ==="
