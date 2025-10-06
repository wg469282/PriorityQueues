#!/bin/bash

# Maven Project Structure Setup Script
# Skrypt organizuje pliki projektu PriorityQueues w strukturę Maven

echo "🚀 Konfigurowanie struktury projektu Maven dla PriorityQueues..."

# Sprawdź czy jesteśmy w katalogu z plikami projektu
if [ ! -f "BSTPriorityQueue.java" ] || [ ! -f "Main.java" ]; then
    echo "❌ Błąd: Nie znaleziono wymaganych plików Java w bieżącym katalogu"
    echo "Upewnij się, że jesteś w katalogu z plikami projektu"
    exit 1
fi

# Utworzenie struktury katalogów Maven
echo "📁 Tworzenie struktury katalogów Maven..."
mkdir -p src/main/java
mkdir -p src/test/java
mkdir -p src/test/resources
mkdir -p target

# Sprawdzenie czy katalogi zostały utworzone
if [ ! -d "src/main/java" ]; then
    echo "❌ Błąd podczas tworzenia katalogów Maven"
    exit 1
fi

echo "✅ Katalogi Maven utworzone pomyślnie"

# Klasyfikacja i przenoszenie plików głównych (src/main/java)
echo "📦 Przenoszenie klas głównych do src/main/java/..."

MAIN_CLASSES=(
    "BSTPriorityQueue.java"
    "BucketPriorityQueue.java" 
    "SortedStackPriorityQueue.java"
    "PriorityQueue.java"
    "WeightedGraph.java"
    "Main.java"
)

for class in "${MAIN_CLASSES[@]}"; do
    if [ -f "$class" ]; then
        echo "  📄 Przenoszę $class"
        mv "$class" src/main/java/
    else
        echo "  ⚠️  Nie znaleziono pliku $class"
    fi
done

# Klasyfikacja i przenoszenie testów (src/test/java)
echo "🧪 Przenoszenie klas testowych do src/test/java/..."

# Znajdź wszystkie pliki Test*.java i *Test.java
TEST_FILES=($(find . -maxdepth 1 -name "*Test.java" -o -name "Test*.java"))

if [ ${#TEST_FILES[@]} -eq 0 ]; then
    echo "  ⚠️  Nie znaleziono plików testowych w bieżącym katalogu"
    echo "  💡 Skopiuj pliki testowe z wcześniej wygenerowanych plików"
else
    for test_file in "${TEST_FILES[@]}"; do
        echo "  🧪 Przenoszę $(basename $test_file)"
        mv "$test_file" src/test/java/
    done
fi

# Tworzenie pliku pom.xml
echo "⚙️ Tworzenie pliku pom.xml..."
cat > pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.github.wiktorgeralt</groupId>
    <artifactId>priority-queues</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <name>Priority Queues Implementation</name>
    <description>Advanced data structures project implementing three different priority queue variants with Dijkstra's algorithm integration</description>
    <url>https://github.com/WiktorGeralt/PriorityQueues</url>
    
    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <junit.version>5.10.1</junit.version>
        <jacoco.version>0.8.11</jacoco.version>
    </properties>
    
    <dependencies>
        <!-- JUnit 5 -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>
        
        <!-- JUnit Platform Suite Engine -->
        <dependency>
            <groupId>org.junit.platform</groupId>
            <artifactId>junit-platform-suite-engine</artifactId>
            <version>1.10.1</version>
            <scope>test</scope>
        </dependency>
        
        <!-- AssertJ for fluent assertions (optional) -->
        <dependency>
            <groupId>org.assertj</groupId>
            <artifactId>assertj-core</artifactId>
            <version>3.24.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <!-- Maven Compiler Plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>
            
            <!-- Maven Surefire Plugin for running tests -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.2</version>
                <configuration>
                    <includes>
                        <include>**/*Test.java</include>
                        <include>**/Test*.java</include>
                    </includes>
                    <reportFormat>plain</reportFormat>
                    <consoleOutputReporter>
                        <disable>false</disable>
                    </consoleOutputReporter>
                </configuration>
            </plugin>
            
            <!-- JaCoCo for code coverage -->
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>${jacoco.version}</version>
                <executions>
                    <execution>
                        <id>prepare-agent</id>
                        <goals>
                            <goal>prepare-agent</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>report</id>
                        <phase>test</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>check</id>
                        <goals>
                            <goal>check</goal>
                        </goals>
                        <configuration>
                            <rules>
                                <rule>
                                    <element>BUNDLE</element>
                                    <limits>
                                        <limit>
                                            <counter>INSTRUCTION</counter>
                                            <value>COVEREDRATIO</value>
                                            <minimum>0.80</minimum>
                                        </limit>
                                    </limits>
                                </rule>
                            </rules>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            
            <!-- Maven Javadoc Plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-javadoc-plugin</artifactId>
                <version>3.6.2</version>
                <configuration>
                    <encoding>UTF-8</encoding>
                    <docencoding>UTF-8</docencoding>
                    <charset>UTF-8</charset>
                </configuration>
            </plugin>
            
            <!-- Exec Maven Plugin for running main class -->
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <version>3.1.0</version>
                <configuration>
                    <mainClass>Main</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>
    
    <profiles>
        <!-- Profile for performance tests -->
        <profile>
            <id>performance</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-surefire-plugin</artifactId>
                        <configuration>
                            <includes>
                                <include>**/PerformanceTest.java</include>
                            </includes>
                        </configuration>
                    </plugin>
                </plugins>
            </build>
        </profile>
        
        <!-- Profile for integration tests -->
        <profile>
            <id>integration</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-surefire-plugin</artifactId>
                        <configuration>
                            <includes>
                                <include>**/*IntegrationTest.java</include>
                            </includes>
                        </configuration>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </profiles>
</project>
EOF

echo "✅ pom.xml utworzony pomyślnie"

# Tworzenie .gitignore dla projektu Maven
echo "🔧 Tworzenie pliku .gitignore..."
cat > .gitignore << 'EOF'
# Maven
target/
pom.xml.tag
pom.xml.releaseBackup
pom.xml.versionsBackup
pom.xml.next
release.properties
dependency-reduced-pom.xml
buildNumber.properties
.mvn/timing.properties
.mvn/wrapper/maven-wrapper.jar

# IDEs
.idea/
*.iml
*.ipr
*.iws
.vscode/
*.swp
*.swo
*~

# Eclipse
.project
.classpath
.settings/

# NetBeans
/nbproject/private/
/nbbuild/
/dist/
/nbdist/
/.nb-gradle/
build/
!**/src/main/**/build/
!**/src/test/**/build/

# macOS
.DS_Store
.DS_Store?
._*
.Spotlight-V100
.Trashes
ehthumbs.db
Thumbs.db

# Logs
*.log

# Temporary files
*.tmp
*.temp

# JaCoCo
*.exec

# Original files backup
*.txt
EOF

echo "✅ .gitignore utworzony pomyślnie"

# Sprawdzenie czy zostały jakieś pliki .java w głównym katalogu
REMAINING_JAVA=($(find . -maxdepth 1 -name "*.java"))
if [ ${#REMAINING_JAVA[@]} -gt 0 ]; then
    echo "⚠️  Znalezione pozostałe pliki Java w głównym katalogu:"
    for file in "${REMAINING_JAVA[@]}"; do
        echo "     $(basename $file)"
    done
    echo "💭 Rozważ czy należą do src/main/java czy src/test/java"
fi

# Tworzenie podstawowych klas testowych jeśli ich brak
if [ ! -f "src/test/java/TestUtils.java" ]; then
    echo "🧪 Tworzenie podstawowej klasy TestUtils..."
    cat > src/test/java/TestUtils.java << 'EOF'
import java.util.*;
import java.util.stream.IntStream;

public class TestUtils {
    
    public static IntElement[] createIntElements(int... values) {
        return Arrays.stream(values)
                .mapToObj(IntElement::new)
                .toArray(IntElement[]::new);
    }
    
    public static List<IntElement> createRandomElements(int count, int maxValue) {
        Random random = new Random(42); // Seed dla powtarzalności
        return IntStream.range(0, count)
                .map(i -> random.nextInt(maxValue + 1))
                .mapToObj(IntElement::new)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    public static List<IntElement> createSortedElements(int count, boolean ascending) {
        List<IntElement> elements = IntStream.range(0, count)
                .mapToObj(IntElement::new)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        if (!ascending) Collections.reverse(elements);
        return elements;
    }
    
    public static boolean isMinHeapProperty(PriorityQueue<IntElement> pq) {
        List<IntElement> extracted = new ArrayList<>();
        PriorityQueue<IntElement> copy = null; // Implementuj kopiowanie jeśli potrzebne
        
        while (!pq.isEmpty()) {
            extracted.add(pq.extractMin());
        }
        
        for (int i = 1; i < extracted.size(); i++) {
            if (extracted.get(i).wartość() < extracted.get(i-1).wartość()) {
                return false;
            }
        }
        return true;
    }
}
EOF
    echo "✅ TestUtils.java utworzony"
fi

# Sprawdzenie czy istnieją klasy wymagane przez testy
echo "🔍 Sprawdzanie klas wymaganych przez testy..."
REQUIRED_CLASSES=(
    "src/main/java/IntElement.java"
    "src/main/java/Position.java"
    "src/main/java/HasValue.java"
)

for class in "${REQUIRED_CLASSES[@]}"; do
    if [ ! -f "$class" ]; then
        echo "⚠️  Brak wymaganej klasy: $(basename $class)"
    fi
done

# Tworzenie skryptu budowania
echo "🔨 Tworzenie skryptu build.sh..."
cat > build.sh << 'EOF'
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
EOF

chmod +x build.sh
echo "✅ build.sh utworzony i oznaczony jako wykonywalny"

# Tworzenie skryptu uruchamiającego
echo "🚀 Tworzenie skryptu run.sh..."
cat > run.sh << 'EOF'
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
EOF

chmod +x run.sh
echo "✅ run.sh utworzony i oznaczony jako wykonywalny"

# Podsumowanie
echo ""
echo "🎉 Struktura projektu Maven została pomyślnie utworzona!"
echo ""
echo "📁 Struktura katalogów:"
echo "   ├── src/"
echo "   │   ├── main/java/     (klasy główne)"
echo "   │   └── test/java/     (testy)"
echo "   ├── target/            (artefakty budowania)"
echo "   ├── pom.xml            (konfiguracja Maven)"
echo "   ├── build.sh           (skrypt budowania)"
echo "   ├── run.sh             (skrypt uruchamiania)"
echo "   └── .gitignore         (ignorowane pliki)"
echo ""
echo "📝 Następne kroki:"
echo "   1. Sprawdź czy wszystkie pliki są na właściwych miejscach"
echo "   2. Dodaj brakujące klasy testowe (IntElement, Position, HasValue)"
echo "   3. Uruchom: ./build.sh"
echo "   4. Przetestuj: ./run.sh"
echo "   5. Sprawdź pokrycie: open target/site/jacoco/index.html"
echo ""
echo "🛠️  Przydatne komendy Maven:"
echo "   mvn clean compile      # Kompilacja"
echo "   mvn test              # Uruchom testy"
echo "   mvn jacoco:report     # Raport pokrycia"
echo "   mvn exec:java         # Uruchom aplikację"
echo "   mvn package           # Utwórz JAR"
echo ""
echo "✨ Projekt gotowy do publikacji na GitHub!"

# Sprawdzenie czy istnieje README.md i ewentualna aktualizacja
if [ -f "README.md" ]; then
    echo ""
    echo "📖 Znaleziono README.md - rozważ aktualizację o instrukcje Maven"
    echo "   Dodaj sekcje o budowaniu i uruchamianiu testów"
fi

echo ""
echo "🏁 Skrypt zakończony pomyślnie!"
EOF