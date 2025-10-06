#!/bin/bash

echo "🔨 Building Priority Queues project..."

# Czyszczenie projektu
echo "🧹 Cleaning previous build..."
mvn clean

# Kompilacja
echo "⚙️  Compiling sources..."
mvn compile

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful"
    
    # Uruchomienie testów
    echo "🧪 Running tests..."
    mvn test
    
    if [ $? -eq 0 ]; then
        echo "✅ All tests passed"
        
        # Generowanie raportu pokrycia
        echo "📊 Generating coverage report..."
        mvn jacoco:report
        
        echo "🎉 Build complete! Coverage report: target/site/jacoco/index.html"
    else
        echo "❌ Tests failed"
        exit 1
    fi
else
    echo "❌ Compilation failed"
    exit 1
fi
