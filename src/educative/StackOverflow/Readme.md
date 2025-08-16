# Follow-up Questions for SDE-2 Interview on Stack Overflow Design

## 🔹 Scalability
- How would you modify the design to handle **millions of users and questions efficiently**?
- Consider caching, sharding, and load balancing to scale.

## 🔹 Concurrency
- How does the system handle **concurrent edits or votes** on the same question or answer?
- Discuss optimistic locking, versioning, and conflict resolution.

## 🔹 Reputation System
- How would you design the **reputation calculation and update mechanism** to be consistent and performant?
- Ensure updates are **idempotent** and resilient to race conditions.

## 🔹 Search Optimization
- How would you improve the **search functionality** for faster and more relevant results?
- Consider full-text search engines (e.g., Elasticsearch), ranking algorithms, and autocomplete.

## 🔹 Notification Delivery
- How would you ensure **reliable and timely delivery of notifications**?
- Use **message queues (Kafka/RabbitMQ)**, retries, and fallback channels (email, push).

## 🔹 Security
- How would you prevent abuse such as **spam, fake accounts, or vote manipulation**?
- Implement **rate limiting, CAPTCHA, fraud detection, anomaly detection**.

---

# Missing Edge Cases in Current Flow

## 🔸 Handling Deleted or Closed Questions
- What happens when a **question is deleted or closed**?
- How are **answers and comments** handled?

## 🔸 User Suspension or Ban
- How does the system handle **suspended or banned users**?
- Prevent login, posting, voting, and reputation gain.

## 🔸 Bounty Expiry
- What happens if a **bounty expires** without any accepted answer?
- Refund? Partial credit? Expire silently?

## 🔸 Answer Editing and Versioning
- How are **edits to answers tracked or managed**?
- Consider **version history**, rollback, and audit trails.

## 🔸 Notification Failures
- What if **notification delivery fails** (e.g., email bounce)?
- Retry, mark as undeliverable, or notify via alternate channel.

## 🔸 Tag Synonyms and Merging
- How does the system handle **duplicate or synonymous tags**?
- Automatic redirection? Merge history? Moderator approval?

---
# Stack Overflow–Style System Design — SDE-2 Follow-ups (with Answers)

## 📈 Scalability — How to handle millions of users & questions?
- **Service decomposition:** Q/A, Search, Votes, Tags, Users, Reputation, Notifications as separate services (clear ownership + independent scaling).
- **Data partitioning:**
    - Questions/answers sharded by `question_id` (hash) or by hot tag buckets.
    - Users sharded by `user_id` (hash-ranged); heavy read models get replicas.
- **Caching layers:**
    - Redis/Memcached for hot question pages, user profiles, tag pages; short TTL + cache keys with version suffixes.
    - CDN for images/avatars and pre-rendered question snapshots.
- **CQRS:** Read-optimized views (denormalized) for question listing/search; write path remains normalized.
- **Asynchrony:** Kafka/Pub/Sub for non-blocking fan-out: indexing, notifications, analytics, badge awarding.
- **Back-pressure & autoscaling:** Token buckets at API gateway; horizontal pods per SLA; circuit breakers to downstreams.
- **Cold storage & archiving:** Move very old closed questions to colder partitions; still searchable via index.

---

## 🧵 Concurrency — Concurrent edits & votes
- **Optimistic concurrency control (OCC):** version column on posts; if version mismatch → return 409 with merge UI.
- **Conflict resolution UI:** show diff, allow “apply mine / theirs / manual merge” for body & title.
- **Idempotent votes:** votes table keyed by `(user_id, post_id, vote_type)` with UPSERT; toggling updates same row.
- **Transactional boundaries:**
    - Vote write + reputation event enqueue in one DB transaction.
    - Edits are append-only in `post_edits`; latest pointer updated atomically.

---

## 🏅 Reputation System — Consistency & performance
- **Event-sourced ledger:** Append events (`UPVOTE_ANSWER:+10`, `ACCEPTED:+15`, `DOWNVOTE:-2`); immutable, auditable.
- **Materialized balances:** Reputation totals per user kept in a cache + DB counter updated by a consumer; eventual consistency O(seconds).
- **Corrections/rollbacks:** Deletion/undeletion emits compensating events; nightly reconciliation job rebuilds from event log for drift detection.
- **Rate guards:** Cap per-day rep to deter gaming; throttle reputation-affecting actions.

---

## 🔎 Search Optimization — Faster, more relevant
- **External search engine:** Elasticsearch/OpenSearch with:
    - Inverted index over title/body/tags; analyzers per language.
    - Signals in scoring: upvotes, acceptance, author rep, freshness, view count.
- **Denormalized index docs:** include tags, counts, accepted answer id; updated via async pipeline.
- **Query features:** BM25, phrase boosting, typo tolerance, synonyms (per tag), filters (score ≥ X, has accepted answer).
- **Autocomplete:** prefix index on titles/tags; cache top N suggestions per tag.

---

## 🔔 Notification Delivery — Reliable & timely
- **Outbox pattern:** Producer writes domain event + outbox row in same transaction; relay publishes to MQ.
- **Multi-channel workers:** email, mobile push, in-app; each with retry + exponential backoff, DLQ on repeated failure.
- **Idempotency keys:** prevent duplicate sends on retries.
- **User preferences & digests:** per-event type settings; batch low-priority into daily/weekly digests.

---

## 🔐 Security — Spam, fake accounts, vote manipulation
- **Abuse prevention:**
    - Rate limits per user/IP/device; proof-of-work or CAPTCHA on spikes.
    - ML spam scoring on content (features: links, velocity, reputation).
- **Account integrity:** email/2FA, device fingerprints, suspicious login alerts.
- **Vote fraud detection:** graph/temporal anomalies (mutual voting rings, time-window bursts); quarantine suspicious votes for review.
- **Content safety:** XSS/CSRF protection, HTML sanitizer allowlist, SSRF-safe link unfurling.
- **Least privilege:** RBAC for moderator tools; audit every privileged action.

---

# 🧩 Missing Edge Cases (with handling)

## 🗑️ Deleted or Closed Questions
- **Soft delete:** `deleted_at`, `deleted_by`; hide from public, preserve for moderators.
- **Reputation reversal:** emit compensating events for votes tied to deleted content (configurable policy).
- **Closure states:** `duplicate`, `needs details`, `opinion-based`; show closure banner + canonical duplicate link; still indexable internally but demoted.

## 🚫 User Suspension or Ban
- **State machine:** `ACTIVE → SUSPENDED(temp) → ACTIVE` or `BANNED(permanent)`.
- **Enforcement:** block posting/voting/editing; allow read + data export; persist reasons & expiry, auto-lift on expiry.
- **Reputation:** retained but frozen for privileges while suspended.

## 🎯 Bounty Expiry
- **Timer job:** when bounty window ends without accepted answer → mark `BOUNTY_EXPIRED`, burn bounty rep, optionally award to top-scored eligible answer via rule.
- **Notifications:** warn owner 24/48h before expiry.

## ✏️ Answer Editing & Versioning
- **Append-only `post_edits`:** stores diffs, editor, timestamps; `posts.current_edit_id` points to latest.
- **Rollback & compare:** moderators (and owners within window) can revert; diff viewer in UI.
- **Re-indexing:** search doc refresh on new edit.

## 📭 Notification Failures
- **Retries & DLQ:** exponential backoff; DLQ with reason (bounce, invalid token).
- **Fallback:** mark as “undelivered”; surface in in-app center; prompt user to verify email/device.

## 🏷️ Tag Synonyms & Merging
- **Synonyms table:** `synonym -> canonical_tag`; applied at write and search time.
- **Merge operation:** re-tag all affected posts in batches; update subscriptions; keep redirects.
- **Governance:** moderator approval workflow; popularity signals to propose merges.

---

## 🧰 Reference Data Models (high-level)
- `posts(id, type, owner_id, title, body, tags[], score, accepted_answer_id, version, deleted_at, created_at, updated_at)`
- `votes(user_id, post_id, vote_type, created_at)  -- PK(user_id, post_id, vote_type)`
- `reputation_events(id, user_id, post_id, type, delta, created_at)`
- `post_edits(id, post_id, editor_id, diff, version, created_at)`
- `notifications(id, user_id, kind, payload, status, idempotency_key, created_at)`
- `tags(tag, created_at)`, `tag_synonyms(synonym, canonical_tag)`
- `users(id, email, rep, roles[], suspended_until, created_at)`

---

## 🏗️ Patterns & Practices
- **CQRS + Event Sourcing** for reputation and activity streams.
- **Outbox + Saga** for cross-service workflows (e.g., bounty lifecycle).
- **OCC, Idempotency, Retries** for safe writes under concurrency.
- **Rate Limiting, Circuit Breakers, Bulkheads** for resilience.

---
1. Handling Deleted or Closed Questions

Design:

Add a status attribute to the Question class (OPEN, CLOSED, DELETED).

When a question is closed or deleted, restrict actions like answering or voting.

Code:

class Question {
enum Status { OPEN, CLOSED, DELETED }
private Status status;

    public void closeQuestion() {
        this.status = Status.CLOSED;
        // Notify users or log the event
    }

    public void deleteQuestion() {
        this.status = Status.DELETED;
        // Remove from search catalog or hide
    }
}

2. User Suspension or Ban

Design:

Extend User class with a status field (ACTIVE, SUSPENDED, BANNED).

Enforce restrictions based on user status.

Code:

class User {
enum Status { ACTIVE, SUSPENDED, BANNED }
private Status status;

    public boolean canPost() {
        return this.status == Status.ACTIVE;
    }
}

3. Bounty Expiry

Design:

Add an expiry timestamp to Bounty.

When expired without an accepted answer, refund or remove bounty.

Use a scheduler or periodic check to handle expired bounties.

Code:

class Bounty {
private long expiryTime;

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

4. Answer Editing and Versioning

Design:

Maintain a list of AnswerVersion objects inside Answer to track edits.

Store history for rollback or audit purposes.

Code:

class Answer {
private List<AnswerVersion> versions;

    public void editAnswer(String newContent) {
        versions.add(new AnswerVersion(newContent, System.currentTimeMillis()));
    }
}

class AnswerVersion {
private String content;
private long timestamp;

    public AnswerVersion(String content, long timestamp) {
        this.content = content;
        this.timestamp = timestamp;
    }
}

5. Notification Failures

Design:

Implement retry logic and failure tracking in Notification.

Use queues/logs for retrying failed notifications.

Code:

class Notification {
public boolean send() {
try {
// send notification
return true;
} catch (Exception e) {
// log failure and retry later
return false;
}
}
}

6. Tag Synonyms and Merging

Design:

Maintain a TagSynonym mapping to redirect synonymous tags to a canonical tag.

Resolve synonyms when adding or searching tags.

Code:

class TagList {
private Map<String, String> tagSynonyms; // synonym -> canonical

    public String getCanonicalTag(String tag) {
        return tagSynonyms.getOrDefault(tag, tag);
    }
}
