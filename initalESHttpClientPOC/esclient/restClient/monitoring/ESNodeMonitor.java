package oracle.kv.impl.tif.esclient.restClient.monitoring;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.http.HttpHost;

import oracle.kv.impl.tif.esclient.esRequest.GetHttpNodesRequest;
import oracle.kv.impl.tif.esclient.esResponse.GetHttpNodesResponse;
import oracle.kv.impl.tif.esclient.httpClient.ESHttpClient;


public class ESNodeMonitor extends ESHttpClient.FailureListener {

    private static final AtomicBoolean isTaskRunning = new AtomicBoolean(false);

    private final MonitoringClient monitorClient;

    private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
                                                                                         1);

    private final List<ESHttpClient> registeredESHttpClients;

    private long monitorDelayMillis = 2000L;

    public ESNodeMonitor(MonitoringClient monitorClient,  List<ESHttpClient> registeredESHttpClients) {
        this.monitorClient = monitorClient;
        this.registeredESHttpClients = registeredESHttpClients;
    }

    public ESNodeMonitor(MonitoringClient monitorClient,
            long monitorDelayMillis,
            List<ESHttpClient> registeredESHttpClients) {
        this.monitorClient = monitorClient;
        this.monitorDelayMillis = monitorDelayMillis;
        this.registeredESHttpClients = registeredESHttpClients;
    }

    /*
     * If Scheduled Executor is not already running, start it.
     */
    public void start() {
        if (!executor.isShutdown() && !executor.isTerminated()
                && !executor.isTerminating()) {
            executor.scheduleWithFixedDelay(new GetNodesTask(monitorClient,
                                                             registeredESHttpClients),
                                            0L, monitorDelayMillis,
                                            TimeUnit.MILLISECONDS);
        }
    }

    /*
     * Shutdown executor service.
     */
    public void stop() {
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
    }


    public void resetAvailableNodes() {
        List<HttpHost> availableNodes = null;
        GetHttpNodesResponse resp = null;
        try {
            resp = monitorClient.getHttpNodesResponse(new GetHttpNodesRequest());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (resp != null) {
            availableNodes = resp.getHttpHosts(ESHttpClient.Scheme.HTTP);
        }

        monitorClient.compareAndSet(availableNodes, registeredESHttpClients);
    }

    /*
     * There was a failure on this host. May be the node was not available.
     * 
     * Check and set ES available nodes once more.
     */
    @Override
    public void onFailure(HttpHost host) {
        List<HttpHost> currentNodes = monitorClient.getAvailableNodes();
        // Remove this node where request failed.
        if (currentNodes.size() <= 1) {
            currentNodes = monitorClient.getAllESHttpNodes();
        }
        currentNodes.remove(host);

        monitorClient.compareAndSet(currentNodes, registeredESHttpClients);
        resetAvailableNodes();
    }

    private static class GetNodesTask implements Runnable {

        private final MonitoringClient monitorClient;
        private final List<ESHttpClient> registeredESClients;

        public GetNodesTask(MonitoringClient monitorClient,
                List<ESHttpClient> registeredESClients) {
            this.monitorClient = monitorClient;
            this.registeredESClients = registeredESClients;
        }

        @Override
        public void run() {

            try {
                if (!isTaskRunning.compareAndSet(false, true)) {
                    return;
                }

                List<HttpHost> availableNodes = null;
                GetHttpNodesResponse resp = monitorClient.getHttpNodesResponse(new GetHttpNodesRequest());
                availableNodes = resp.getHttpHosts(ESHttpClient.Scheme.HTTP);

                monitorClient.compareAndSet(availableNodes,
                                            registeredESClients);
            } catch (IOException e) {
                // TODO log this exception. The next schedule should run.
                e.printStackTrace();
            } finally {
                isTaskRunning.compareAndSet(true, false);
            }

        }

    }

}
