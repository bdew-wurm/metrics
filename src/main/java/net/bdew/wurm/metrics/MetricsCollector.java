package net.bdew.wurm.metrics;

import com.sun.management.OperatingSystemMXBean;
import com.wurmonline.server.Items;
import com.wurmonline.server.Players;
import com.wurmonline.server.Servers;
import com.wurmonline.server.creatures.Creatures;

import java.lang.management.ManagementFactory;
import java.util.logging.Logger;

public class MetricsCollector {
    private static final Logger logger = Logger.getLogger(MetricsCollector.class.getName());
    private final Runtime runtime = Runtime.getRuntime();
    private Long lastData = Long.MIN_VALUE;

    private OperatingSystemMXBean osBean;

    public MetricsCollector() {
        logger.info("Metrics collector initialized");
        java.lang.management.OperatingSystemMXBean tmp = ManagementFactory.getOperatingSystemMXBean();
        if (tmp instanceof OperatingSystemMXBean) {
            osBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        } else {
            logger.warning("Java runtime does not support com.sun.management.OperatingSystemMXBean - process cpu metric will not be available");
        }
    }

    @SuppressWarnings({"unused", "UnreachableCode"})
    public void onServerTickEnd(long nanos) {
        long now = System.currentTimeMillis();
        MetricsData.tickFull.increment(nanos);
        MetricsData.tickCount.increment();

        MetricsData.memoryAllocated.update(runtime.totalMemory());
        MetricsData.memoryMax.update(runtime.maxMemory());
        MetricsData.memoryUsed.update(runtime.totalMemory() - runtime.freeMemory());

        if (lastData + MetricsMod.collectionPeriod * 1000L < now) {
            lastData = now;
            MetricsData.creaturesPeace.update(Creatures.getInstance().getNumberOfCreatures() - Creatures.getInstance().getNumberOfAgg());
            MetricsData.creaturesAgg.update(Creatures.getInstance().getNumberOfAgg());
            MetricsData.creaturesCap.update(Servers.localServer.maxCreatures);
            MetricsData.players.update(Players.getInstance().getNumberOfPlayers());
            MetricsData.items.update(Items.getNumberOfItems());
            if (osBean != null) {
                MetricsData.processLoad.update(osBean.getProcessCpuLoad());
                MetricsData.cpuTime.update(osBean.getProcessCpuTime());
            }
        }
    }

    @SuppressWarnings({"unused"})
    public void onPollZonesEnd(long nanos) {
        MetricsData.tickZones.increment(nanos);
    }

    @SuppressWarnings({"unused"})
    public void onPollPlayersEnd(long nanos) {
        MetricsData.tickPlayers.increment(nanos);
    }

    @SuppressWarnings({"unused"})
    public void onSocketServerTickEnd(long nanos) {
        MetricsData.tickSocket.increment(nanos);
    }

    @SuppressWarnings({"unused"})
    public void onPollTilesEnd(long nanos) {
        MetricsData.tickTiles.increment(nanos);
    }

    @SuppressWarnings({"unused"})
    public void onSocketRead(boolean player, int bytes) {
        if (player)
            MetricsData.netRecvPlayer.increment(bytes);
        else
            MetricsData.netRecvIntra.increment(bytes);
    }

    @SuppressWarnings({"unused"})
    public void onSocketWrite(boolean player, int bytes) {
        if (player)
            MetricsData.netSendPlayer.increment(bytes);
        else
            MetricsData.netSendIntra.increment(bytes);
    }
}
