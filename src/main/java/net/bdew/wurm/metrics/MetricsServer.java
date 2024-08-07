package net.bdew.wurm.metrics;

import com.sun.net.httpserver.HttpServer;
import net.bdew.wurm.metrics.metric.BaseMetric;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class MetricsServer {
    private static HttpServer httpServer;
    private static final Logger logger = Logger.getLogger(MetricsServer.class.getName());

    private static final List<BaseMetric> metrics = new ArrayList<>();

    public static <T extends BaseMetric> T register(T metric) {
        synchronized (metrics) {
            metrics.add(metric);
            metrics.sort(Comparator.comparing(baseMetric -> baseMetric.fullName));
            return metric;
        }
    }

    public static void start(int port) throws IOException {
        InetSocketAddress address = new InetSocketAddress((InetAddress) null, port);

        httpServer = HttpServer.create(address, 0);

        httpServer.createContext("/metrics", httpExchange -> {
            logger.info(String.format("Request %s from %s", httpExchange.getRequestURI(), httpExchange.getRemoteAddress()));
            byte[] data = generateMetrics();
            httpExchange.sendResponseHeaders(200, data.length);
            try (OutputStream outStream = httpExchange.getResponseBody()) {
                outStream.write(data);
            }
        });

        httpServer.start();

        logger.info(String.format("Metrics server listening on %s", address));
    }

    public static void stop() {
        httpServer.stop(0);
    }

    private static byte[] generateMetrics() {
        synchronized (metrics) {
            StringBuilder sb = new StringBuilder();

            String lastMetric = null;
            for (BaseMetric metric : metrics) {
                metric.writeMetric(sb, !metric.fullName.equals(lastMetric));
                lastMetric = metric.fullName;
            }
            return sb.toString().getBytes(StandardCharsets.UTF_8);
        }
    }
}
