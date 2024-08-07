package net.bdew.wurm.metrics;

import com.wurmonline.server.Items;
import com.wurmonline.server.Players;
import com.wurmonline.server.creatures.Creatures;

public class MetricsCollector {
    private final Runtime runtime = Runtime.getRuntime();
    private Long lastData = Long.MIN_VALUE;

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
            MetricsData.creatures.update(Creatures.getInstance().getNumberOfCreatures());
            MetricsData.players.update(Players.getInstance().getNumberOfPlayers());
            MetricsData.items.update(Items.getNumberOfItems());
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
}
