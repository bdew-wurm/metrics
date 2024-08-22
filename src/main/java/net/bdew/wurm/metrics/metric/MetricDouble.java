package net.bdew.wurm.metrics.metric;

import java.util.concurrent.atomic.AtomicLong;

public class MetricDouble extends BaseMetric {
    protected AtomicLong value = new AtomicLong();

    public MetricDouble(String name, String type, String units, MetricLabel... labels) {
        super(name, type, units, labels);
    }

    public void update(double value) {
        this.value.set(Double.doubleToLongBits(value));
        this.updated();
    }

    @Override
    protected void writeValue(StringBuilder sb) {
        double tmp = Double.longBitsToDouble(this.value.get());
        if (Double.isNaN(tmp))
            sb.append("NaN");
        else if (tmp == Double.POSITIVE_INFINITY)
            sb.append("+Inf");
        else if (tmp == Double.NEGATIVE_INFINITY)
            sb.append("-Inf");
        else
            sb.append(tmp);
    }
}
