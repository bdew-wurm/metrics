package net.bdew.wurm.metrics;

import java.util.concurrent.atomic.AtomicLong;

public class MetricsData {
    public static final AtomicLong tickCount = new AtomicLong(0);
    public static final AtomicLong tickTotal = new AtomicLong(0);
    public static final AtomicLong tickZones = new AtomicLong(0);

    public static volatile long lastAggregateData;
    public static volatile boolean started;
    public static volatile long creatures;
    public static volatile long players;
    public static volatile long items;
}
