# Kotlin Search Microservice — Project Blueprint

This blueprint provides a comprehensive guide for building the **Simple Search Engine** core and pivoting it into a production-ready **Kotlin Microservice**. This architectural roadmap bridges the gap between basic console logic and enterprise backend engineering.

---

## 📋 Phase 1: The Core Algorithmic Engine (Console Baseline)

Before exposing logic to the web, build and validate the search mechanisms.

### Stage 1: String Theory
* **Main Task:** Read a target dataset (a single string with space-separated words) and a search query from the standard input. Output the index of the query word or a "Not found" message.
* **Kotlin Tools:** `Scanner(System.`$`in)` or `readln()`, `String.split(" ")`.
* **Input Example:**
  ```text
  Words: first second third
  Query: second
  ```
* **Checkpoint:** Ensure that searching for the first word outputs `1` and searching for a completely non-existent word cleanly prints `Not found`.

### Stage 2: Expand the Search
* **Main Task:** Read multiple independent text lines representing distinct data rows. Read a single query and print all lines containing that query using a case-insensitive linear search.
* **Kotlin Tools:** `MutableList<String>`, `String.contains(..., ignoreCase = true)`.
* **Input Example:**
```text
Enter number of lines: 3
Katie Jacobs kjacobs@gmail.com
John Doe
Alice Smith
Enter search query: Katie
```
* **Output Example:** `Katie Jacobs kjacobs@gmail.com`
* **Checkpoint:** Verify that case-mismatched queries (e.g., `katie` matching `Katie Jacobs`) still return the full line.

### Stage 3: User Menu
* **Main Task:** Wrap Stage 2 logic inside an interactive text menu loop (`1. Search`, `2. Print all data`, `0. Exit`). Handle invalid choices gracefully.
* **Kotlin Tools:** `while(true)` loop, `when` conditional expression.
* **Input/Output Example:**
```text
1. Find a person
2. Print all people
0. Exit
> 3
Incorrect option! Try again.
```
* 
* **Checkpoint:** Verify menu bounds: `3` triggers an error, `2` prints all entries, `0` exits safely.

### Stage 4: Inverted Indexing
* **Main Task:** Performance optimization! Build an **Inverted Index Map** upon application initialization. Keys represent distinct, lowercase words, and values hold collections of line numbers (indices) where those words occur.
* **Input Example:**
  ```text
  Line 0: John Doe
  Line 1: Jane Doe
  ```
* **Data Structure Representation:**
  Your compiled map should internally mirror this layout:
  `"john" -> [0]`, `"jane" -> [1]`, `"doe" -> [0, 1]`
* **Kotlin Tools:** `Map<String, List<Int>>` or `MutableMap<String, MutableList<Int>>`.
* **Checkpoint:** Ensure queries execute in $O(1)$ time by querying the map directly rather than scanning lines iteratively.

### Stage 5: File Input
* **Main Task:** Read the baseline data lines from a source text file passed through command-line arguments using a flag (`--data filename.txt`).
* **Kotlin Tools:** `args` array parsing within the `main` entry point, `File(filename).readLines()`.
* **Input Example:** `java -jar engine.jar --data dataset.txt`
* **Checkpoint:** Ensure the program parses args dynamically regardless of its position and safeguards against missing file exceptions.

### Stage 6: Search Strategies
* **Main Task:** Upgrade the search module to handle multi-word strings based on three filtering conditions:
    * **ALL:** Matches lines containing *every single* query word (Set Intersection).
    * **ANY:** Matches lines containing *at least one* query word (Set Union).
    * **NONE:** Matches lines that *do not* contain any of the provided words (Set Difference).
* **Kotlin Tools:** Built-in Set operations: `setA.intersect(setB)`, `setA.union(setB)`, `setA.subtract(setB)`.
* **Checkpoint:** Verify that selecting `NONE` for a term accurately suppresses matches containing that specific token.

---

## 🚀 Phase 2: Microservice Evolution

Transform your console tool into a decoupled, asynchronous Web API using **Ktor** or **Spring Boot**.

```text
    [ Client / Frontend ]
              │
      HTTP Requests (JSON)
              ▼
 ┌─────────────────────────────────────────┐
 │          KTOR / SPRING BOOT             │
 │                                         │
 │  ┌───────────────────────────────────┐  │
 │  │      Controller Layer (REST)      │  │
 │  └─────────────────┬─────────────────┘  │
 │                    ▼                    │
 │  ┌───────────────────────────────────┐  │
 │  │     Service Layer (Engine)        │  │
 │  │  (Async Inverted Index Matching)  │  │
 │  └─────────────────┬─────────────────┘  │
 │                    ▼                    │
 │  ┌───────────────────────────────────┐  │
 │  │     Repository Layer (Storage)    │  │
 │  └───────────────────────────────────┘  │
 └─────────────────────────────────────────┘
```

### 1. Architectural Restructuring (Clean Architecture)
Break your application into distinct, testable layers:
* **Controller / Routing Layer:** Exposes RESTful HTTP endpoints and handles JSON payload validation.
* **Service Layer:** Houses the core search engine logic, inverted index mapping, and strategy execution.
* **Repository / Storage Layer:** Encapsulates how data lines are saved and retrieved.

### 2. Microservice API Design
Replace standard input/output loops with a structured REST API layer:
* `POST /api/upload`
    * **Description:** Dynamically append new text strings or files to the dataset.
    * **Action:** Triggers a background update to update the in-memory inverted index map.
    * **Payload:** `{"text": "John Doe"}`
* `GET /api/search?query=John&strategy=ALL`
    * **Description:** Query the search engine via URL parameters.
    * **Response:** Returns an array of matching text strings in a `200 OK` JSON format.

### 3. Production-Ready Kotlin Features to Showcase
Implement the following advanced Kotlin techniques:

* **Asynchronous Concurrency (Coroutines):** Use Kotlin Coroutines (`Dispatchers.Default`) when processing file uploads or high-volume dataset insertions. This prevents structural thread blocking during computationally expensive index updates.
* **Idiomatic Type Safety & Null Safety:** Handle missing keys or invalid requests using Kotlin's expressive `Result` wrapping or explicit nullable operators (`?`) instead of scattering risky try-catch blocks everywhere.
* **Functional Programming Constructs:** Leverage high-order collection transforms (`.flatMap`, `.fold`, `.filter`) to compute matching sets during multi-word strategy resolutions (`ALL`, `ANY`, `NONE`).

### 4. Advanced Ecosystem Tooling
* **Unit Testing:** Write robust unit tests with **JUnit 5** and utilize **MockK** (a Kotlin-first mocking framework) to mock data layers inside service tests.
* **Containerization:** Include a standard multi-stage `Dockerfile` to allow reviewers to run your search microservice with a single command.

---