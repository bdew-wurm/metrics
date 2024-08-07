package net.bdew.wurm.metrics.metric;

public class MetricLongDiv extends MetricLong {
    public final double divisor;

    public MetricLongDiv(String name, String type, String units, double divisor, MetricLabel... labels) {
        super(name, type, units, labels);
        this.divisor = divisor;
    }

    @Override
    protected void writeValue(StringBuilder sb) {
        sb.append(1.0 * this.value.get() / this.divisor);
    }
}
