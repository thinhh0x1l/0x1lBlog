package top.blogapi.infra.cache;

import java.util.concurrent.atomic.AtomicLong;

public class CacheMetrics {
    private final AtomicLong l1Hit = new AtomicLong();
    private final AtomicLong l1Miss = new AtomicLong();
    private final AtomicLong l2Hit = new AtomicLong();
    private final AtomicLong l2Miss = new AtomicLong();
    private final AtomicLong dbHit = new AtomicLong();

    public AtomicLong l1Hit() { return l1Hit; }
    public AtomicLong l1Miss() { return l1Miss; }
    public AtomicLong l2Hit() { return l2Hit; }
    public AtomicLong l2Miss() { return l2Miss; }
    public AtomicLong dbHit() { return dbHit; }

    public double hitRate() {
        long hits = l1Hit.get() + l2Hit.get();
        long total = hits + l1Miss.get();
        return total == 0 ? 1.0 : (double) hits / total;
    }

    public long totalRequests() { return l1Hit.get() + l1Miss.get(); }

    public void reset() {
        l1Hit.set(0); l1Miss.set(0);
        l2Hit.set(0); l2Miss.set(0);
        dbHit.set(0);
    }
}
