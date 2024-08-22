package net.bdew.wurm.metrics;

import net.bdew.wurm.metrics.metric.MetricDouble;
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

    public static final MetricLongDiv cpuTime =
            MetricsServer.register(new MetricLongDiv("cpu_time", "counter", "seconds", 1_000_000_000));

    public static final MetricDouble processLoad =
            MetricsServer.register(new MetricDouble("process_load", "gauge", null));

    public static final MetricLongDiv tickFull =
            MetricsServer.register(new MetricLongDiv("tick_full", "counter", "seconds", 1_000_000_000));

    public static final MetricLong creaturesAgg =
            MetricsServer.register(new MetricLong("creatures", "gauge", null, new MetricLabel("type", "peaceful")));
    public static final MetricLong creaturesPeace =
            MetricsServer.register(new MetricLong("creatures", "gauge", null, new MetricLabel("type", "aggressive")));
    public static final MetricLong creaturesCap =
            MetricsServer.register(new MetricLong("creatures_cap", "gauge", null));
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

    public static final MetricLong netRecvPlayer =
            MetricsServer.register(new MetricLong("network_recv", "counter", "bytes", new MetricLabel("type", "player")));
    public static final MetricLong netSendPlayer =
            MetricsServer.register(new MetricLong("network_send", "counter", "bytes", new MetricLabel("type", "player")));
    public static final MetricLong netRecvIntra =
            MetricsServer.register(new MetricLong("network_recv", "counter", "bytes", new MetricLabel("type", "intra")));
    public static final MetricLong netSendIntra =
            MetricsServer.register(new MetricLong("network_send", "counter", "bytes", new MetricLabel("type", "intra")));
}
