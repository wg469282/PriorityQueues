#!/bin/bash

echo "🚀 Running Priority Queues demo..."

# Kompilacja jeśli potrzebna
if [ ! -d "target/classes" ]; then
    echo "📦 Compiling first..."
    mvn compile
fi

# Uruchomienie głównej klasy
echo "▶️  Starting main application..."
mvn exec:java -Dexec.mainClass="Main"
