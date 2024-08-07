package net.bdew.wurm.metrics;

import com.wurmonline.server.Servers;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import net.bdew.wurm.metrics.metric.MetricLabel;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.*;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class MetricsMod implements WurmServerMod, Initable, PreInitable, Configurable, ServerStartedListener, ServerShutdownListener {
    private static final Logger logger = Logger.getLogger(MetricsMod.class.getName());

    public static int listenPort;
    public static int collectionPeriod;
    public static int listenPortOffset;

    @Override
    public void configure(Properties properties) {
        listenPort = Integer.parseInt(properties.getProperty("listenPort", "5000"));
        listenPortOffset = Integer.parseInt(properties.getProperty("listenPortOffset", "0"));
        collectionPeriod = Integer.parseInt(properties.getProperty("collectionPeriod", "15"));
    }

    @Override
    public void preInit() {
        try {
            ClassPool cp = HookManager.getInstance().getClassPool();
            MetricsCollector hooks = new MetricsCollector();

            CtClass ctServer = cp.getCtClass("com.wurmonline.server.Server");
            HookManager.getInstance().addCallback(ctServer, "_bdew_metrics_cb", hooks);

            CtMethod mRun = ctServer.getMethod("run", "()V");
            mRun.addLocalVariable("_bdew_start_ts", CtClass.longType);
            mRun.addLocalVariable("_bdew_tmp_ts", CtClass.longType);
            mRun.insertBefore("_bdew_start_ts = java.lang.System.nanoTime();");
            mRun.insertAfter("_bdew_metrics_cb.onServerTickEnd(java.lang.System.nanoTime() - _bdew_start_ts);");

            mRun.instrument(new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals("com.wurmonline.server.zones.Zones") && m.getMethodName().equals("pollNextZones")) {
                        logger.info(String.format("Hooked %s.%s call in %s.%s line %d", m.getClassName(), m.getMethodName(), m.where().getDeclaringClass().getSimpleName(), m.where().getName(), m.getLineNumber()));
                        m.replace("_bdew_tmp_ts = java.lang.System.nanoTime(); $proceed($$); _bdew_metrics_cb.onPollZonesEnd(java.lang.System.nanoTime() - _bdew_tmp_ts);");
                    } else if (m.getClassName().equals("com.wurmonline.server.zones.TilePoller") && m.getMethodName().equals("pollNext")) {
                        logger.info(String.format("Hooked %s.%s call in %s.%s line %d", m.getClassName(), m.getMethodName(), m.where().getDeclaringClass().getSimpleName(), m.where().getName(), m.getLineNumber()));
                        m.replace("_bdew_tmp_ts = java.lang.System.nanoTime(); $proceed($$); _bdew_metrics_cb.onPollTilesEnd(java.lang.System.nanoTime() - _bdew_tmp_ts);");
                    } else if (m.getClassName().equals("com.wurmonline.server.Players") && m.getMethodName().equals("pollPlayers")) {
                        logger.info(String.format("Hooked %s.%s call in %s.%s line %d", m.getClassName(), m.getMethodName(), m.where().getDeclaringClass().getSimpleName(), m.where().getName(), m.getLineNumber()));
                        m.replace("_bdew_tmp_ts = java.lang.System.nanoTime(); $proceed($$); _bdew_metrics_cb.onPollPlayersEnd(java.lang.System.nanoTime() - _bdew_tmp_ts);");
                    } else if (m.getClassName().equals("com.wurmonline.communication.SocketServer") && m.getMethodName().equals("tick")) {
                        logger.info(String.format("Hooked %s.%s call in %s.%s line %d", m.getClassName(), m.getMethodName(), m.where().getDeclaringClass().getSimpleName(), m.where().getName(), m.getLineNumber()));
                        m.replace("_bdew_tmp_ts = java.lang.System.nanoTime(); $proceed($$); _bdew_metrics_cb.onSocketServerTickEnd(java.lang.System.nanoTime() - _bdew_tmp_ts);");
                    }
                }
            });

        } catch (NotFoundException | CannotCompileException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void onServerStarted() {
        try {
            MetricLabel.defaultLabel = new MetricLabel("server", Servers.localServer.name);
            if (listenPortOffset == 1)
                MetricsServer.start(Integer.parseInt(Servers.localServer.INTRASERVERPORT) + listenPortOffset);
            else
                MetricsServer.start(listenPort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onServerShutdown() {
        MetricsServer.stop();
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
