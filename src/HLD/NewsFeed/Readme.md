# Newsfeed System - High Level Design (HLD)

This document summarizes the **Newsfeed Design** for revision, enriched with algorithms, additional explanations, and system design considerations suitable for SDE-2 interviews at MAANG.

---

## 1. Problem Definition

A **Newsfeed System** continuously updates a list of content (posts, reels, ads, etc.) personalized for each user.

---

## 2. Requirements

### Functional

* **Newsfeed Generation**: Aggregate posts from friends, followers, groups, and pages.
* **Newsfeed Content Delivery**: Deliver relevant posts to users.
* **Newsfeed Ranking/Filtering**: Show most relevant posts using ranking algorithms.

### Non-Functional

* **Performance**: Low latency (< 200 ms ideally).
* **Fault tolerance**: No single point of failure.
* **Scalability**: Handle billions of users, millions of active users simultaneously.

---

## 3. Estimations

* **1 billion users/day**, **500 million daily active users (DAU)**.
* Average user → 200 friends → \~2000 posts/day.
* **Traffic**:

    * 500M DAU × 10 feed refreshes/day = **5B requests/day**.
    * \~58K requests/sec.
* **Storage**:

    * Avg 5 posts/user/day.
    * Each post ≈ 1KB metadata in memory (actual media in CDN).
    * Total = 500M × 5 = 2.5B posts/day.

---

## 4. High-Level Architecture

### Components:

1. **Client** → requests feed, posts content.
2. **Load Balancer (LB)** → distributes requests.
3. **Web Servers (WB)** → entry point for client requests.
4. **Newsfeed Generation Service** → aggregates posts for each user.
5. **Ranking Service** → scores posts for relevance.
6. **Newsfeed Publishing Service** → updates feeds and notifies users.
7. **Databases & Storage**:

    * **Graph DB** → stores social graph (who follows whom).
    * **User DB + User Cache** → user profiles.
    * **Post DB + Post Cache** → post metadata and caching.
    * **Blob Storage (CDN/S3)** → store media files (images, videos).
    * **Newsfeed Cache** → precomputed feeds for fast retrieval.

### Architecture Flow (from UML diagram):

* **Clients → LB → WB → Newsfeed Services**.
* **Newsfeed Generation Service** interacts with Graph DB, User DB, and caches.
* **Ranking Service** personalizes feed using ML models.
* **Publishing Service** updates feed + notifies followers.
* **Blob Storage** stores actual content, while DB stores metadata.

---

## 5. Data Modeling

### Relational Tables

1. **User**: `userId`, `name`, `email`, profile info.
2. **Entity**: friends, followers, groups.
3. **Post Item**: `postId`, `userId`, `timestamp`, metadata.

### Graph Database (for relationships)

* **Vertices** = users.
* **Edges** = relationships (follow, friend, block).
* PostgreSQL/Neo4j can be used.

Example:

```
(User 10) --[follows]--> (User 20)
```

---

## 6. Newsfeed Generation Approaches

### 1. Pull Model

* **Generate feed on-demand** when user opens app.
* **Pros**: Efficient storage, easier ranking.
* **Cons**: Higher read latency.

### 2. Push Model

* **Generate feed immediately** when a user posts.
* **Pros**: Low latency when reading.
* **Cons**: High storage cost, redundant feed entries.

### 3. Hybrid Model

* **Push for heavy users (celebrities with millions of followers)**.
* **Pull for regular/occasional users**.

---

## 7. Feed Ranking Algorithms

1. **Relevance Score Calculation**:

    * Factors: likes, comments, shares, recency, engagement.
    * Use **ML models** to predict post relevance.
    * Assign score (1–5).

2. **Top-K Selection**:

    * Select highest scoring posts.
    * Apply filters (spam, blocked users, ads insertion).

### Example Algorithm:

```pseudo
function generateFeed(userId):
    candidatePosts = fetchRecentPostsFromFriends(userId)
    for post in candidatePosts:
        score = relevanceModel(userId, post)
        assign score to post
    sortedPosts = sortByScore(candidatePosts)
    return topK(sortedPosts, 50)
```

---

## 8. Scalability Considerations

1. **Caching**:

    * Store precomputed feeds in Redis.
    * Expire/update when new posts arrive.

2. **Sharding**:

    * Partition posts by userId.
    * Geo-distributed clusters.

3. **CDN**:

    * Store heavy media files (images, videos).
    * DB stores only metadata + reference.

4. **Asynchronous Updates**:

    * Use Kafka/Pub-Sub to propagate new post events.

---

## 9. Availability & Fault Tolerance

* **Leader-Follower Replication** for DB.
* **Retry + Backoff** strategies for failed requests.
* **Multi-Region Deployment** to handle outages.

---

## 10. Key Tradeoffs

* **Push vs Pull** → Latency vs Storage.
* **Graph DB vs Relational DB** → Relationship queries vs Structured queries.
* **Cache vs Freshness** → Faster responses vs slightly stale data.

---

## 11. Interview Checklist

* ✅ Estimate scale (users, posts, QPS).
* ✅ Mention **hybrid feed generation**.
* ✅ Cover **ranking algorithm** basics.
* ✅ Explain **data modeling (SQL + Graph DB)**.
* ✅ Address **latency, caching, scalability**.
* ✅ Call out **failure handling & replication**.

---

## 12. Extra Notes for Interviews

* Mention **Machine Learning** models (logistic regression, neural networks) for ranking.
* Talk about **A/B testing** for improving relevance.
* Mention **Spam/Abuse detection**.
* Ads and Sponsored content insertion is similar to organic feed ranking but with priority boosts.

---

# 🚀 Summary

A **Newsfeed System** must:

* Efficiently **aggregate posts** from multiple sources.
* Use **ranking algorithms** (ML-based) to show relevant content.
* Handle **billions of requests** with **low latency** using caching + async pipelines.
* Achieve **high availability & scalability** with hybrid feed generation.


# 🔑 Add-ons for Newsfeed System HLD

## 1. Ranking & Personalization (Deeper ML angle)
- Use **Learning-to-Rank models** (e.g., XGBoost, Deep Neural Nets).
- Features used:
    - **User-based**: interests, location, activity time.
    - **Post-based**: freshness, media type, hashtags.
    - **Engagement-based**: CTR, dwell time, reshares.
- Online serving: Pre-compute relevance scores for new posts and update incrementally.

👉 Mention **real-time personalization** and **A/B testing** (interviewers love hearing that).

---

## 2. Hot vs Cold Storage
- **Hot storage**: Redis/Memcached → store latest posts + feed cache.
- **Cold storage**: S3/Blob → older posts, rarely accessed.
- Helps reduce infra costs while keeping latency low.

---

## 3. Consistency Tradeoffs
- Newsfeed doesn’t need strict consistency (not financial data).
- **Eventual consistency** works → use async pipelines (Kafka, Pub/Sub).
- Interview angle: “We trade strong consistency for availability and performance.”

---

## 4. Edge Delivery (CDN)
- Images/videos served via CDN close to user.
- Metadata (likes, comments count) updated asynchronously.
- This helps explain **low latency at scale**.

---

## 5. Observability & Monitoring
- Collect **metrics**: latency (p95, p99), cache hit ratio, throughput.
- Implement **circuit breakers** (if ranking service is down, fallback to reverse chronological order).
- Interviewers often ask: “What happens if your ranking service fails?” → fallback answers win points.

---

## 6. Security & Abuse Prevention
- Rate limiting → avoid spam posting.
- Content moderation pipeline → block offensive posts before distribution.
- Token-based auth (JWT/OAuth) → secure API calls.

---

## 7. Scalability Beyond Basics
- **Fan-out optimization**: batch updates when a celebrity posts (instead of pushing to millions instantly).
- **Tiered architecture**: keep separate feed clusters for high QPS vs low QPS users.
- **Pre-warming caches** for active users (early morning, evenings).

---

## 8. Ads Insertion
- Ads pipeline runs parallel with feed pipeline.
- Ads are scored using similar ranking models (but boosted for revenue).
- Interview win: “Ads are treated as just another content type with higher weight in ranking.”

---

## 9. Failure Scenarios
- Ranking service fails → fallback to reverse chronological.
- Cache miss → fetch from DB + refresh cache.
- DB failure → switch to replica / another region.

---

# 📝 Quick Interview Soundbites
- “We use a **hybrid push-pull model** to balance latency and storage.”
- “Ranking is done with **ML-based relevance scoring** plus business rules (ads, spam filters).”
- “We prefer **eventual consistency** and **asynchronous pipelines** for scalability.”
- “CDN + cache layers keep latency < 200ms at p95.”
- “In case of ranking service failure, we fallback to simple reverse chronological order.”

---

👉 If you say these 5–6 crisp lines with confidence, you’ll hit **all the major design signals** in a MAANG SDE-2 interview.

# 🚀 End of Newsfeed HLD Notes
👉 “The newsfeed system follows a hybrid **fan-out-on-write + fan-out-on-read** model.  
When a post is published, it’s stored in a post DB and cached, while async workers update fanout queues.  
At read time, the **newsfeed generation service** merges posts from friends/follow graph (Graph DB) and fetches metadata from caches.  
We apply a **ranking service** that blends ML relevance models with rules like freshness, ads, and moderation.  
Feeds are cached per user, with invalidation on updates, and served via CDN for low latency.  
The system is **eventually consistent**, but for critical paths we ensure strong consistency (e.g., self-posts).  
Fallback is reverse-chronological if ranking or caches fail.  
This keeps **p99 latency <200ms** while scaling to millions of users.”  

