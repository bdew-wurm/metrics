package net.bdew.wurm.metrics;

import com.wurmonline.server.Items;
import com.wurmonline.server.Players;
import com.wurmonline.server.creatures.Creatures;

public class MetricsCollector {
    @SuppressWarnings({"unused", "UnreachableCode"})
    public void onServerTickEnd(long nanos) {
        long now = System.currentTimeMillis();

        MetricsData.tickCount.getAndIncrement();
        MetricsData.tickTotal.addAndGet(nanos);

        if (!MetricsData.started || MetricsData.lastAggregateData + MetricsMod.collectionPeriod * 1000L < now) {
            MetricsData.lastAggregateData = now;
            MetricsData.creatures = Creatures.getInstance().getNumberOfCreatures();
            MetricsData.players = Players.getInstance().getNumberOfPlayers();
            MetricsData.items = Items.getNumberOfItems();
            MetricsData.started = true;
        }
    }

    @SuppressWarnings({"unused"})
    public void onPollZonesEnd(long nanos) {
        MetricsData.tickZones.addAndGet(nanos);
    }
}
