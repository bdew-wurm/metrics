package net.bdew.wurm.metrics.metric;

import java.util.concurrent.atomic.AtomicLong;

public class MetricLong extends BaseMetric {
    protected AtomicLong value = new AtomicLong();

    public MetricLong(String name, String type, String units, MetricLabel... labels) {
        super(name, type, units, labels);
    }

    public void update(long value) {
        this.value.set(value);
        this.updated();
    }

    public void increment(long inc) {
        this.value.getAndAdd(inc);
        this.updated();
    }

    public void increment() {
        this.increment(1);
    }

    @Override
    protected void writeValue(StringBuilder sb) {
        sb.append(this.value.get());
    }
}
