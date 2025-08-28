# High-Level Design (HLD) — Non-Functional Requirements (NFRs)

In SDE-2 interviews, apart from functional requirements, interviewers focus heavily on **Non-Functional Requirements (NFRs)**.  
Here’s a consolidated set of **interview-ready notes** for the most commonly asked NFRs with examples.

---

## 1. Scalability
**Definition:** Ability of a system to handle increasing load (more users, requests, or data) by scaling horizontally/vertically.

**How to Achieve:**
- Horizontal scaling with load balancers (e.g., Nginx, HAProxy).
- Sharding & partitioning databases.
- Caching layers (Redis, Memcached).
- Queue-based async processing (Kafka, RabbitMQ).

**Examples:**
- Instagram’s feed generation → fan-out with caching + async workers.
- Amazon product catalog → sharded databases.

**Interview Statement:**  
_"To ensure scalability, I would design stateless services behind a load balancer, use caching for hot data, and partition data to handle growth. For example, Instagram scales feed delivery using fan-out with Redis and Kafka."_

---

## 2. Availability
**Definition:** Ensuring system is operational and accessible most of the time (measured in uptime %).

**How to Achieve:**
- Redundancy & replication across regions.
- Failover mechanisms (active-active / active-passive).
- Health checks + auto-healing.
- Distributed consensus (Raft, Paxos, Zookeeper).

**Examples:**
- Netflix Chaos Engineering to test availability.
- Google Spanner for multi-region data replication.

**Interview Statement:**  
_"To ensure availability, I would deploy services in multiple regions with automatic failover. For example, Netflix uses Chaos Monkey to test resilience and ensure service availability even during failures."_

---

## 3. Reliability
**Definition:** Consistency of correct system behavior over time.

**How to Achieve:**
- Retry with exponential backoff.
- Idempotent APIs to avoid duplication.
- Monitoring & alerting (Prometheus, Grafana).
- Durable storage with replication.

**Examples:**
- Payment systems → retries with idempotent transaction IDs.
- Kafka guarantees durability with replication factor.

**Interview Statement:**  
_"I would ensure reliability by using idempotent APIs, retries with backoff, and data replication. For example, payments use transaction IDs to ensure the same transaction is not processed twice."_

---

## 4. Performance (Low Latency & High Throughput)
**Definition:** Speed at which a system responds under load.

**How to Achieve:**
- Caching static/derived data.
- Using CDNs for static assets.
- Indexing in databases.
- Asynchronous and parallel processing.

**Examples:**
- Twitter timeline → pre-computed and cached for fast reads.
- YouTube video delivery → via CDNs.

**Interview Statement:**  
_"To ensure performance, I would use caching, CDN distribution, and efficient indexing. For example, Twitter caches timelines to serve them in milliseconds."_

---

## 5. Fault Tolerance
**Definition:** Ability of a system to continue functioning despite failures.

**How to Achieve:**
- Replication across nodes/regions.
- Circuit breakers (Hystrix pattern).
- Graceful degradation of non-critical features.
- Message queues to handle retries.

**Examples:**
- Netflix → fallback streams when primary fails.
- Amazon S3 → 11 9’s durability with replication.

**Interview Statement:**  
_"For fault tolerance, I’d replicate services across regions, use circuit breakers to prevent cascading failures, and design graceful degradation. Netflix achieves this with Hystrix and fallback mechanisms."_

---

## 6. Consistency
**Definition:** Guarantee that users see the same data across the system.

**How to Achieve:**
- Strong vs Eventual Consistency (CAP theorem).
- Distributed consensus protocols.
- Write-through or write-back caching strategies.

**Examples:**
- Google Spanner → strong consistency across regions.
- DynamoDB → eventual consistency for high availability.

**Interview Statement:**  
_"Depending on the use case, I’d choose between strong and eventual consistency. For example, DynamoDB uses eventual consistency for availability, while Spanner ensures strong consistency with TrueTime."_

---

## 7. Security
**Definition:** Protecting system from unauthorized access, breaches, and vulnerabilities.

**How to Achieve:**
- Authentication & Authorization (OAuth, JWT).
- Encryption (TLS in transit, AES at rest).
- Role-Based Access Control (RBAC).
- DDoS mitigation & rate limiting.

**Examples:**
- Google services secured with OAuth2.
- AWS IAM for fine-grained access control.

**Interview Statement:**  
_"To ensure security, I would implement authentication, authorization, encryption, and monitoring. For example, AWS IAM provides role-based access control to ensure only authorized entities access resources."_

---

## 8. Maintainability
**Definition:** Ease of updating, debugging, and extending the system.

**How to Achieve:**
- Microservices architecture.
- Clean API contracts.
- CI/CD pipelines.
- Logging & observability.

**Examples:**
- Kubernetes microservices → easier rolling updates.
- GitHub Actions for CI/CD pipelines.

**Interview Statement:**  
_"For maintainability, I would ensure modular design, clear APIs, and automated CI/CD. For example, Kubernetes allows rolling updates with minimal downtime."_

---

## 9. Observability (Monitoring & Debugging)
**Definition:** Ability to measure and understand system health in production.

**How to Achieve:**
- Logs (ELK stack).
- Metrics (Prometheus, Grafana).
- Tracing (Jaeger, OpenTelemetry).

**Examples:**
- Uber uses Jaeger for distributed tracing.
- Twitter uses monitoring dashboards for latency spikes.

**Interview Statement:**  
_"To ensure observability, I’d implement logging, metrics, and tracing. For example, Uber uses Jaeger for distributed tracing to debug latency issues."_

---

## 10. Cost Efficiency
**Definition:** Designing systems that scale without unnecessary costs.

**How to Achieve:**
- Autoscaling (Kubernetes HPA).
- Spot instances (AWS, GCP).
- Storage tiering (hot, warm, cold).

**Examples:**
- Netflix uses autoscaling for peak traffic.
- AWS S3 → tiered storage reduces costs.

**Interview Statement:**  
_"I would ensure cost efficiency by autoscaling, using spot instances, and optimizing storage tiers. For example, Netflix scales infrastructure up and down based on traffic patterns."_

---

# ✅ Quick Tip for Interviews
- Always **tie the NFR back to the system you’re designing** (e.g., news feed, payment system, ride sharing app).
- Use **real-world company examples** to make your answer memorable.
- Show **trade-offs** (e.g., CAP theorem: Consistency vs Availability).

---
"""