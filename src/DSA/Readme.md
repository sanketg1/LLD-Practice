# Complete List of Must-Do DSA Algorithms in Java

---

## 1. Arrays & Strings

- **Kadane’s Algorithm** (Maximum Subarray Sum)
- **Two Pointers Technique**
    - 3Sum, 4Sum
    - Trapping Rainwater
    - Container with Most Water
- **Sliding Window**
    - Longest Substring Without Repeating Characters
    - Anagrams
    - Maximum in Sliding Window
- **Prefix Sum & Difference Array**
- **Dutch National Flag** (Sort 0s,1s,2s)
- **Rotate Array / Reverse Techniques**

**String Matching Algorithms:**

- Naive Approach
- KMP (Knuth–Morris–Pratt)
- Rabin–Karp
- Z-algorithm

---

## 2. Sorting & Searching

- Merge Sort
- Quick Sort
- Heap Sort
- Counting Sort / Radix Sort (Non-comparison based)

**Binary Search Patterns:**

- First/Last Occurrence
- Binary Search on Answer
    - Aggressive Cows
    - Allocate Books
    - Median of Two Sorted Arrays
- Rotated Sorted Array Search
- Ternary Search (Unimodal Function)

---

## 3. Hashing & Maps

- HashMap & HashSet based problems
- Group Anagrams
- Longest Consecutive Sequence
- Subarray with Given Sum (Prefix Sum + HashMap)

---

## 4. Linked List

- Reversal (Iterative & Recursive)
- Detect Cycle (Floyd’s Tortoise & Hare)
- Merge Two Sorted Lists
- Merge K Sorted Lists (Heap)
- LRU Cache (LinkedHashMap or Custom Doubly Linked List + HashMap)
- Clone Linked List with Random Pointers

---

## 5. Stack & Queue

- Next Greater Element
- Next Smaller Element
- Min Stack / Max Stack
- Stock Span Problem
- Largest Rectangle in Histogram
- Sliding Window Maximum (Deque)
- Balanced Parentheses (Valid Expression)
- Implement Queue using Stacks and Vice Versa
- Monotonic Stack / Queue Techniques

---

## 6. Recursion & Backtracking

- Subsets / Subsequence Generation
- Permutations
- Combination Sum (I, II, III, IV)
- Word Search
- N-Queens
- Sudoku Solver
- Rat in a Maze
- M-Coloring Graph
- Generate Parentheses

---

## 7. Greedy Algorithms

- Activity Selection
- Interval Scheduling (Merge Intervals, Non-overlapping Intervals)
- Huffman Coding
- Job Sequencing with Deadlines
- Minimum Spanning Tree (Prim’s & Kruskal’s)
- Dijkstra’s Algorithm (Also Greedy + Graph)

---

## 8. Dynamic Programming (DP)

- Fibonacci (Memoization, Tabulation, Space Optimization)
- Longest Common Subsequence (LCS)
    - Variants: Longest Common Substring, Edit Distance, Min Insert/Delete
- Longest Increasing Subsequence (LIS)
- Matrix Chain Multiplication (MCM)
- DP on Grids (Unique Paths, Min Path Sum, Obstacle Grid)
- Subset Sum / Partition DP / Knapsack (0/1, Unbounded)
- Coin Change (Min Coins & Combinations)
- Palindrome DP (LPS, Palindrome Partitioning)
- Digit DP (Count Numbers with Constraints)
- Bitmask DP (Travelling Salesman, Assignments)
- Tree DP (Max Path Sum, Diameter, DP on Subtrees)
- Game DP (Nim Game, Grundy Numbers)

---

## 9. Graph Algorithms

- BFS & DFS (Iterative & Recursive)
- Cycle Detection (Directed & Undirected)
- Topological Sort (DFS, Kahn’s Algorithm)

**Shortest Path Algorithms:**

- Dijkstra’s Algorithm
- Bellman-Ford
- Floyd–Warshall

**Minimum Spanning Tree:**

- Kruskal’s Algorithm (Union-Find / DSU)
- Prim’s Algorithm

- Disjoint Set Union (Union by Rank + Path Compression)
- Bipartite Graph Check (BFS/DFS + Coloring)
- Kosaraju / Tarjan’s Algorithm (SCC - Strongly Connected Components)
- Bridges & Articulation Points
- Graph Coloring
- DSU + Kruskal Applications (Min Wires, Network Connectivity)
- Trie-Based Graph Problems (Word Search II)

---

## 10. Binary Trees & BST

**Traversals:**

- Inorder, Preorder, Postorder (Recursive, Iterative, Morris)
- Level Order, Zigzag Level Order

- Diameter of Binary Tree
- Lowest Common Ancestor (Binary Tree & Optimized BST)
- Vertical Order / Top View / Bottom View
- Serialize & Deserialize Binary Tree
- Maximum Path Sum
- BST Operations (Insert, Delete, Search)
- Kth Smallest / Largest in BST
- Validate BST

---

## 11. Heap / Priority Queue

- Heapify, Build Heap
- Kth Largest / Smallest Element
- Merge K Sorted Lists / Arrays
- Median of Data Stream
- Top K Frequent Elements
- Sliding Window Maximum (Heap version)
- Min-Cost Rope Connection

---

## 12. Advanced Data Structures

- Trie (Insert, Search, Prefix Search, Word Dictionary with Wildcards)
- Segment Tree (Sum, Min, Lazy Propagation)
- Fenwick Tree / Binary Indexed Tree (BIT)
- Suffix Array & Suffix Tree
- Sparse Table (Range Queries - RMQ)

---

## 13. Mathematical Algorithms

- GCD, LCM (Euclidean Algorithm)
- Modular Exponentiation
- Modular Inverse (Fermat’s Theorem)
- Sieve of Eratosthenes
- Prime Factorization
- Matrix Exponentiation
- Fast Doubling for Fibonacci
- Combinatorics (nCr using Pascal’s Triangle / Factorials + Mod Inverse)

---

## 14. Bit Manipulation

- Check Power of 2
- Count Set Bits (Brian Kernighan’s Algorithm)
- Subset Generation using Bits
- Single Number (XOR Trick)
- Two Single Numbers (XOR Partition Trick)
- Maximum XOR of Two Numbers (Trie + Bit DP)
- Bitmask DP Problems

---

## 15. Miscellaneous Must-Do

- Union-Find with Path Compression + Rank
- Kth Order Statistics (Quickselect)
- Reservoir Sampling
- Randomized Algorithms
- LFU Cache Design
- Design TinyURL (Hashing + Randomization)

---
---

## 16. System Design (SDE-2 Must-Have)

> High-level architecture skills expected for scalable systems.

- Fundamentals: Load Balancing, Caching, Sharding, Consistent Hashing
- CAP Theorem, PACELC, Trade-offs in Distributed Systems
- Designing:
    - URL Shortener (TinyURL)
    - Rate Limiter (Token Bucket, Leaky Bucket)
    - Notification Systems (Push/Pull, Pub-Sub)
    - Message Queues (Kafka, RabbitMQ basics)
    - Feed Generation (Pull, Push, Fan-out)
    - Chat Systems (WebSockets, Polling)
- Scalability: Horizontal vs Vertical Scaling, Database Partitioning
- API Rate Limiting Strategies
- Read vs Write-heavy system optimization
- Design Patterns (Singleton, Factory, Observer, Strategy, etc.)

---

## 17. Concurrency & Multithreading in Java

> Critical for real-time systems and parallel data handling.

- Thread Lifecycle & Management
- `synchronized`, `volatile`, `AtomicInteger`, `ReentrantLock`
- Producer-Consumer Problem (BlockingQueue)
- Thread-safe Collections (ConcurrentHashMap, CopyOnWriteArrayList)
- Fork/Join Framework
- Executors, Thread Pools
- Deadlock, Livelock, Starvation detection
- Semaphore, CountDownLatch, CyclicBarrier, Phaser
- Java Memory Model (JMM) basics

---

## 18. Design Patterns (OOP Excellence)

> Frequently tested in SDE-2 design interviews.

- SOLID Principles
- GOF Patterns:
    - Creational: Singleton, Factory, Builder
    - Structural: Adapter, Decorator, Proxy
    - Behavioral: Strategy, Observer, Command
- Dependency Injection (with frameworks like Spring)

---

## 19. Caching Strategies & Design

> Real-world systems always involve some caching logic.

- LRU, LFU, ARC (Advanced Replacement Cache)
- Write-through vs Write-back cache
- TTL, Expiry and Invalidation
- Local (in-memory) vs Distributed Cache (Redis, Memcached)
- Cache Stampede & Cache Avalanche Handling

---

## 20. Database Fundamentals (SQL + NoSQL)

> Needed for design and data-intensive roles.

- Normalization & Denormalization
- Indexing (B-tree, Hash Index, Composite Index)
- Query Optimization
- ACID vs BASE
- Transactions, Isolation Levels
- NoSQL Types:
    - Key-Value: Redis, DynamoDB
    - Document: MongoDB
    - Wide-Column: Cassandra
    - Graph: Neo4j

---

## 21. Real-World Project Structure in Java

> Shows maturity and clean architecture skills.

- Multi-module Maven or Gradle project setup
- Layered Architecture (Controller, Service, Repository)
- DTOs vs Entities
- Exception Handling strategy
- Logging frameworks (Log4j, SLF4J)
- Writing Unit & Integration Tests (JUnit, Mockito)
- Java Streams & Functional Programming style

---

## 22. Testing & Quality

> At SDE-2 level, you're expected to write testable, maintainable code.

- JUnit5 / TestNG
- Mockito / PowerMock
- Property-Based Testing
- Load Testing (JMeter, Gatling)
- Test-Driven Development (TDD) understanding
- Code Coverage (Jacoco)

---

## 23. Cloud & DevOps Basics

> Bonus points for knowing infra around your code.

- Docker Basics (images, containers, volumes)
- Kubernetes (Pods, Services, Deployments — high-level)
- CI/CD concepts (GitHub Actions, Jenkins Pipelines)
- AWS Basics:
    - EC2, S3, RDS, Lambda, CloudWatch
- Monitoring: Prometheus + Grafana overview
- Infrastructure as Code: Terraform (basics)

---
