package net.bdew.wurm.metrics.metric;

import java.util.concurrent.atomic.AtomicLong;

public abstract class BaseMetric {
    public final String name;
    public final String type;
    public final String units;
    public final String fullName;
    public final MetricLabel[] labels;

    private final AtomicLong timestamp = new AtomicLong();

    public BaseMetric(String name, String type, String units, MetricLabel... labels) {
        this.name = name;
        this.type = type;
        this.units = units;

        String fullName = "wurm_" + name;
        if (units != null) fullName += "_" + units;
        if ("counter".equals(type))
            fullName += "_total";
        this.fullName = fullName;

        this.labels = labels;
    }

    protected void updated() {
        this.timestamp.set(System.currentTimeMillis());
    }

    protected abstract void writeValue(StringBuilder sb);

    protected void writeMeta(StringBuilder sb) {
        if (this.type != null) {
            sb.append(String.format("# TYPE %s %s\n", this.fullName, this.type));
        }
    }

    public void writeMetric(StringBuilder sb, boolean withMeta) {
        if (withMeta) this.writeMeta(sb);
        sb.append(fullName);
        if (MetricLabel.defaultLabel != null || this.labels.length > 0) {
            sb.append("{");
            if (MetricLabel.defaultLabel != null) {
                MetricLabel.defaultLabel.write(sb);
                if (this.labels.length > 0)
                    sb.append(",");
            }
            for (int i = 0; i < this.labels.length; i++) {
                this.labels[i].write(sb);
                if (i < this.labels.length - 1)
                    sb.append(",");
            }
            sb.append("}");
        }
        sb.append(" ");
        this.writeValue(sb);
        sb.append(" ");
        sb.append(this.timestamp.get());
        sb.append("\n");
    }
}
