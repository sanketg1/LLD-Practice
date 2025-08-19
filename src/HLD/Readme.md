# 🛠️ System Design Interview Prep Plan (SDE-2 | MAANG-level)

## 🎯 Goal
Crack SDE-2 system design interviews using:
- 📘 Educative (Grokking)
- 📝 Donamartin GitHub
- 🎥 Gaurav Sen
- ✏️ Tools: Excalidraw / Pen & paper / Notion

---

## 🗓️ 6-Week Plan (10–12 hours/week)

| Week | Focus | Primary Resource | Outcomes |
|------|-------|------------------|----------|
| 1 | Core concepts + warm-up designs | Educative | System design framework, 3 core problems |
| 2 | Scale estimations + deeper designs | Educative | 4 more designs, start noting patterns |
| 3 | Structured interview-style practice | Donamartin | Solve 3 designs, simulate interview style |
| 4 | Advanced systems + trade-offs | Donamartin + Gaurav Sen | Solve 3 advanced problems, build real-world thinking |
| 5 | Deep dives + optimization | Gaurav Sen | Study 3 real-world systems in detail |
| 6 | Mock rounds + revision | Self-practice + GitHub | Simulate 2–3 full mock interviews |

---

## 📘 Week-by-Week Breakdown

### 🔹 Week 1: Foundation & Patterns
**Goal:** Understand core components and build a checklist  
**Time:** ~2 hrs/day

**Topics:**
- How to approach system design
- Design patterns: caching, load balancing, sharding, etc.
- Throughput, latency, CAP theorem

**Practice (Educative):**
- URL Shortener
- TinyURL / Pastebin
- Rate Limiter

**Tasks:**
- Note common patterns
- Build your own system design checklist
- Sketch designs using Excalidraw or notebook

---

### 🔹 Week 2: Scale Estimation & Core Systems
**Goal:** Learn to estimate and handle scale  
**Time:** ~2 hrs/day

**Practice (Educative):**
- Messaging Queue
- YouTube / Video Streaming
- Instagram
- Web Crawler

**Tasks:**
- Estimate: QPS, storage, bandwidth
- Compare DB/Cache decisions
- Write brief API contracts

---

### 🔹 Week 3: Real Interview Simulations
**Goal:** Practice MAANG-style open-ended design  
**Time:** 1.5 hrs/day

**Practice (Donamartin GitHub):**
- Twitter
- Distributed Cache (like Redis)
- Notification System

**Tasks:**
- Simulate 60-min interview: Clarify → Estimate → Design → Improve
- Record yourself or write detailed notes
- Review Donamartin's solution after solving

---

### 🔹 Week 4: Advanced System Design Thinking
**Goal:** Focus on trade-offs and real-world constraints  
**Time:** ~1.5 hrs/day

**Watch (Gaurav Sen):**
- Load Balancer
- Kafka internals
- Dropbox / File storage systems

**Practice (Donamartin):**
- WhatsApp
- Dropbox
- Ad Server

**Tasks:**
- Extract key trade-offs from each video
- Try redesigning each system on your own

---

### 🔹 Week 5: Real-World Deep Dives
**Goal:** Build real-world intuition  
**Time:** 1 hr/day

**Watch/Read:**
- Gaurav Sen (Uber, Kafka, distributed DBs)
- ByteByteGo YouTube (optional)
- Netflix Architecture Blog (optional)

**Tasks:**
- Note design patterns in real systems
- Recreate the architecture in your own words

---

### 🔹 Week 6: Mock Interviews + Final Revision
**Goal:** Simulate interviews under time pressure  
**Time:** ~Flexible

**Tasks:**
- Redo 3 complete designs from scratch  
  E.g., Instagram, WhatsApp, Messaging Queue
- Do 2 mock interviews (with peer or solo):
    - 30 min design
    - 15 min discussion and bottlenecks

**Use this System Design Checklist:**
Clarify use-case and constraints

Define core features + out-of-scope

Estimate: users, QPS, bandwidth

High-level architecture

Component design (DB, cache, queue, etc.)

Data partitioning, replication

Bottlenecks + improvements

Trade-offs: latency, consistency, cost

### 🔹 Week 6: Mock Interviews + Final Revision

---

## 🧠 Optional Enhancements (if you have time)

| Resource | Why | When |
|----------|-----|------|
| ByteByteGo (Book or YouTube) | Visuals + real-world systems | After Week 3 |
| DesignGurus.io | Deeper than Educative | Alternative to Grokking |
| Excalidraw / Whimsical | Diagram drawing practice | Ongoing |
| Interviewing.io / Pramp | Mock interviews | Week 5–6 |

---

## ✅ Final Notes

- Focus on **thinking out loud** and **trade-offs**, not perfect diagrams
- Practice sketching on paper or whiteboard
- Review your designs every week
