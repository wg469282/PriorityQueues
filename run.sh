#!/usr/bin/env bash
set -euo pipefail

# Domyślne ustawienia
SKIP_TESTS=false

# Pomoc
function usage() {
  echo "Użycie: $0 [--skip-tests]"
  echo
  echo "  --skip-tests   Pomiń uruchamianie testów JUnit podczas budowania"
  exit 1
}

# Parsowanie argumentów
while [[ $# -gt 0 ]]; do
  case "$1" in
    --skip-tests)
      SKIP_TESTS=true
      shift
      ;;
    -h|--help)
      usage
      ;;
    *)
      echo "Nieznana opcja: $1"
      usage
      ;;
  esac
done

# Ścieżka do skompilowanych klas
CLASSES_DIR="target/classes"

# Funkcja budowania
function build_project() {
  if [ "$SKIP_TESTS" = true ]; then
    echo "=== Budowanie (pomijanie testów) ==="
    mvn clean package -DskipTests
  else
    echo "=== Budowanie (z testami) ==="
    mvn clean package
  fi
}

# Jeśli katalog klas nie istnieje lub pominęliśmy testy (by odświeżyć artefakty), budujemy
if [ ! -d "$CLASSES_DIR" ]; then
  echo "Katalog $CLASSES_DIR nie istnieje. Buduję projekt..."
  build_project
fi

echo "=== Uruchamianie aplikacji PriorityQueues ==="
echo "--> java -cp $CLASSES_DIR Main"
java -cp "$CLASSES_DIR" Main
