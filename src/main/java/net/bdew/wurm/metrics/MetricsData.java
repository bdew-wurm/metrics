package net.bdew.wurm.metrics;

import net.bdew.wurm.metrics.metric.MetricLabel;
import net.bdew.wurm.metrics.metric.MetricLong;
import net.bdew.wurm.metrics.metric.MetricLongDiv;

public class MetricsData {


    public static final MetricLong tickCount =
            MetricsServer.register(new MetricLong("ticks", "counter", null));

    public static final MetricLongDiv tickZones =
            MetricsServer.register(new MetricLongDiv("tick_part", "counter", "seconds", 1_000_000_000, new MetricLabel("part", "zones")));
    public static final MetricLongDiv tickPlayers =
            MetricsServer.register(new MetricLongDiv("tick_part", "counter", "seconds", 1_000_000_000, new MetricLabel("part", "players")));
    public static final MetricLongDiv tickSocket =
            MetricsServer.register(new MetricLongDiv("tick_part", "counter", "seconds", 1_000_000_000, new MetricLabel("part", "socket")));
    public static final MetricLongDiv tickTiles =
            MetricsServer.register(new MetricLongDiv("tick_part", "counter", "seconds", 1_000_000_000, new MetricLabel("part", "tiles")));

    public static final MetricLongDiv tickFull =
            MetricsServer.register(new MetricLongDiv("tick_full", "counter", "seconds", 1_000_000_000));

    public static final MetricLong creatures =
            MetricsServer.register(new MetricLong("creatures", "gauge", null));
    public static final MetricLong players =
            MetricsServer.register(new MetricLong("players", "gauge", null));
    public static final MetricLong items =
            MetricsServer.register(new MetricLong("items", "gauge", null));

    public static final MetricLong memoryUsed =
            MetricsServer.register(new MetricLong("heap_used", "gauge", null));
    public static final MetricLong memoryAllocated =
            MetricsServer.register(new MetricLong("heap_allocated", "gauge", null));
    public static final MetricLong memoryMax =
            MetricsServer.register(new MetricLong("heap_max", "gauge", null));

}
