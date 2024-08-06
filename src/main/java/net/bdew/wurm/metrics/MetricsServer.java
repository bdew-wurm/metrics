package net.bdew.wurm.metrics;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class MetricsServer {
    private static HttpServer httpServer;
    private static final Logger logger = Logger.getLogger(MetricsServer.class.getName());
    private static String instanceLabel;
    private static Runtime runtime;

    public static void start(String serverName) throws IOException {
        instanceLabel = String.format("instance=\"%s\"", Utils.escapeLabelValue(serverName));

        runtime = Runtime.getRuntime();

        InetSocketAddress address = new InetSocketAddress((InetAddress) null, MetricsMod.listenPort);
        httpServer = HttpServer.create(address, 0);
        httpServer.createContext("/metrics", new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                logger.info(String.format("Request %s from %s", httpExchange.getRequestURI(), httpExchange.getRemoteAddress()));
                byte[] data = generateMetrics();
                httpExchange.sendResponseHeaders(200, data.length);
                try (OutputStream outStream = httpExchange.getResponseBody()) {
                    outStream.write(data);
                }
            }
        });

        httpServer.start();
        logger.info(String.format("Metrics server listening on %s", address));
    }

    public static void stop() {
        httpServer.stop(0);
    }

    private static byte[] generateMetrics() {
        if (!MetricsData.started) return new byte[]{};

        StringBuilder sb = new StringBuilder();

        String tsNow = Long.toString(System.currentTimeMillis());

        if (MetricsData.started) {
            String tsLast = Long.toString(MetricsData.lastAggregateData);
            appendValue(sb, "wurm_players", "gauge", MetricsData.players, tsLast);
            appendValue(sb, "wurm_creatures", "gauge", MetricsData.creatures, tsLast);
        }

        appendValue(sb, "wurm_ticks_total", "counter", MetricsData.tickCount.longValue(), tsNow);

        appendValue(sb, "wurm_ticks_seconds_total", "counter", MetricsData.tickZones.longValue() / 1_000_000_000.0, tsNow, "part=\"zones\"");
        appendValue(sb, "wurm_ticks_seconds_total", null, (MetricsData.tickTotal.longValue() - MetricsData.tickZones.longValue()) / 1_000_000_000.0, tsNow, "part=\"other\"");

        appendValue(sb, "wurm_heap_used_bytes", "gauge", runtime.totalMemory() - runtime.freeMemory(), tsNow);
        appendValue(sb, "wurm_heap_size_bytes", "gauge", runtime.totalMemory(), tsNow);
        appendValue(sb, "wurm_heap_max_bytes", "gauge", runtime.maxMemory(), tsNow);

        sb.append("# EOF");

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private static void appendValue(StringBuilder sb, String name, String type, long value, String ts) {
        appendValue(sb, name, type, value, ts, null);
    }

    private static void appendValue(StringBuilder sb, String name, String type, long value, String ts, String addLabel) {
        appendValue(sb, name, type, Long.toString(value), ts, addLabel);
    }

    private static void appendValue(StringBuilder sb, String name, String type, double value, String ts) {
        appendValue(sb, name, type, value, ts, null);
    }

    private static void appendValue(StringBuilder sb, String name, String type, double value, String ts, String addLabel) {
        String v;
        if (Double.isNaN(value))
            v = "NaN";
        else if (Double.POSITIVE_INFINITY == value)
            v = "+Inf";
        else if (Double.NEGATIVE_INFINITY == value)
            v = "-Inf";
        else
            v = String.format("%.6f", value);
        appendValue(sb, name, type, v, ts, addLabel);
    }

    private static void appendValue(StringBuilder sb, String name, String type, String value, String ts, String addLabel) {
        if (type != null)
            sb.append(String.format("# TYPE %s %s\n", name, type));
        sb.append(String.format("%s{%s%s%s} %s %s\n", name, instanceLabel, addLabel == null ? "" : ",", addLabel == null ? "" : addLabel, value, ts));
    }
}
