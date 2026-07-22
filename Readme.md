# Simple Search Engine (in Kotlin!)

A high-performance search service built with Kotlin and Spring Boot. Evolving from a terminal-based prototype into a production-ready microservice, this project demonstrates pure functional query resolution (ALL, ANY, NONE), non-blocking asynchronous processing with Kotlin Coroutines, and Dockerized deployment on Java 21.

## 🛠️ Engineering & Development Journey

The service was built in two distinct phases: first mastering core algorithmic efficiency via a local console baseline, and then refactoring the engine into an asynchronous, production-ready microservice.


- [Phase 1: Algorithmic Engine & Inverted Indexing](#phase-1-algorithmic-engine--inverted-indexing)
- [Phase 2: Production-Oriented Refactoring](#phase-2-production-oriented-refactoring)

---

### Architecture Overview

| Phase | Architecture | Storage & Indexing | Execution Model | Delivery |
| :--- | :--- | :--- | :--- | :--- |
| **[Phase 1](#phase-1-algorithmic-engine--inverted-indexing)** | Interactive CLI Loop | In-Memory `Map<String, List<Int>>` | Synchronous / Single-Threaded | Standalone Executable JAR |
| **[Phase 2](#phase-2-production-oriented-refactoring)** | Layered REST Microservice | Thread-Safe Abstract Repository | Non-Blocking (Kotlin Coroutines) | Multi-Stage Docker Container |

---
### Phase 1: Algorithmic Engine & Inverted Indexing

Development began by iteratively building and testing the core query resolution mechanisms in isolation before introducing web-layer dependencies:

* **Linear Baseline to Inverted Mapping:** Initial implementations evaluated queries via sequential line scans. To achieve $O(1)$ query resolution time, data ingest was refactored to construct an **Inverted Index Map** (`Map<String, List<Int>>`) during initialization, mapping normalized unique tokens directly to target row indices.
* **Functional Set Strategy Resolutions:** The query engine was expanded to parse multi-word strings using pure Kotlin collection transforms (`fold`, `flatMap`, `reduce`) to execute distinct matching conditions:
    * **`ALL` Strategy:** Executes set intersection across matching index sets.
    * **`ANY` Strategy:** Executes set union across token hits.
    * **`NONE` Strategy:** Executes relative complement (set difference) to suppress unwanted terms.
* **CLI Data Ingestion & State Validation:** Handled external file sources safely via command-line arguments, wrapping input parsing with defensive error boundaries and interactive command loops.

---
### Phase 2: Production-Oriented Refactoring

Once the core logic was validated, the project was refactored into a layered, non-blocking Spring Boot microservice:

* **Clean Layered Architecture:** Decoupled business responsibility into explicit architectural boundaries:
    * **Controller Layer:** Exposes RESTful endpoints (`GET /api/search`, `POST /api/upload`) with strict request validation and type-safe enum deserialization.
    * **Service Layer:** Manages in-memory inverted index state updates and query strategy dispatching.
    * **Repository Layer:** Abstracted dataset persistence for in-memory thread-safe reads and writes.
* **Non-Blocking Asynchronous Concurrency:** Replaced synchronous execution with **Kotlin Coroutines** (`Dispatchers.Default`) for index generation, preventing thread starvation during high-volume dataset uploads.
* **Idiomatic Error Handling:** Replaced verbose `try-catch` structures with Kotlin's idiomatic `Result<T>` wrapping and functional `.fold()` constructs for predictable error propagation.
* **Cloud-Native Delivery:** Containerized via a multi-stage **Dockerfile** leveraging JDK 21 compilation and a lightweight Temurin JRE runtime, preventing local environment leakage while ensuring consistent cross-platform execution.

## 🔌Getting Started

### Prerequisites

Ensure you have the following installed on your local machine before building or running the service:

* **Java Development Kit (JDK):** Version 21 or higher
* **Docker Engine & Docker Compose:** Required for containerized execution
* **Git:** For cloning the repository
* *(Optional)* **cURL** or an HTTP client (e.g., IntelliJ HTTP Client, Postman) for testing REST endpoints

---

### Installing

1. **Clone the repository**
   ```bash
   git clone https://github.com/gcclaviiovino/simple_search_engine.git
   cd simple-search-engine
2. **Verify data files** <br>
Ensure `dataset.txt` or your target dataset is placed in src/main/resources/dataset.txt before building.

> ⚠️ **IMPORTANT**<br>
> Additional data can be provided while the program is running, but only JSON format will be accepted in such operation.

3. **Build the application JAR**
```
./gradlew clean build
```

### Executing program
You can run the application either directly on your host machine or as a containerized Docker service.

**Option A: Running locally with Gradle**
1. Launch the Spring Boot application:
```
./gradlew bootRun
```
The service will start and listen on port `8080`

**Running with Docker (Advised)**

1. Build the Docker image:
```
docker build -t search-engine-service .
```
2. Start the container and map port `8080`:
```
docker run -p 8080:8080 --name search-engine search-engine-service
```
### Verifying the Installation
Once running, verify the service health by executing a query via cURL:
```
curl "http://localhost:8080/api/search"
```

## 👩‍💻Authors

Lavinia Iovino (me!)

## 👀Acknowledgments

* [Hyperskill Project](https://hyperskill.org/projects/89)