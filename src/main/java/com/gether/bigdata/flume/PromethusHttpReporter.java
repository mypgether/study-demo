package com.gether.bigdata.flume;


import com.google.common.collect.Lists;
import io.prometheus.client.Collector;
import io.prometheus.client.exporter.common.TextFormat;
import org.apache.commons.lang3.StringUtils;
import org.apache.flume.Context;
import org.apache.flume.instrumentation.MonitorService;
import org.apache.flume.instrumentation.util.JMXPollUtil;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class PromethusHttpReporter implements MonitorService {

    private Server jettyServer;
    private int port;
    private static Logger LOG = LoggerFactory.getLogger(PromethusHttpReporter.class);
    public static int DEFAULT_PORT = 41414;
    public static String CONFIG_PORT = "port";

    @Override
    public void start() {
        jettyServer = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setReuseAddress(true);
        connector.setPort(this.port);
        this.jettyServer.setConnectors(new Connector[]{connector});
        jettyServer.setHandler(new PromethusHttpReporter.HTTPMetricsHandler());
        try {
            jettyServer.start();
            while (!jettyServer.isStarted()) {
                Thread.sleep(500);
            }
        } catch (Exception ex) {
            LOG.error("Error starting Jetty. JSON Metrics may not be available.", ex);
        }
    }

    @Override
    public void stop() {
        try {
            jettyServer.stop();
            jettyServer.join();
        } catch (Exception ex) {
            LOG.error("Error stopping Jetty. JSON Metrics may not be available.", ex);
        }
    }

    @Override
    public void configure(Context context) {
        port = context.getInteger(CONFIG_PORT, DEFAULT_PORT);
    }

    private class HTTPMetricsHandler extends AbstractHandler {

        private HTTPMetricsHandler() {
        }

        public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException {
            if (!request.getMethod().equalsIgnoreCase("TRACE") && !request.getMethod().equalsIgnoreCase("OPTIONS")) {
                if (target.equals("/")) {
                    response.setContentType("text/html;charset=utf-8");
                    response.setStatus(200);
                    response.getWriter().write("For Flume metrics please click <a href = \"./metrics\"> here</a>.");
                    response.flushBuffer();
                    ((org.mortbay.jetty.Request) request).setHandled(true);
                } else if (target.equalsIgnoreCase("/metrics")) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType(TextFormat.CONTENT_TYPE_004);
                    Writer writer = response.getWriter();
                    try {
                        TextFormat.write004(writer, new FlumeMetricFamilySamplesEnumeration());
                        writer.flush();
                    } finally {
                        writer.close();
                    }
                    ((org.mortbay.jetty.Request) request).setHandled(true);
                } else {
                    response.sendError(404);
                    response.flushBuffer();
                }
            } else {
                response.sendError(403);
                response.flushBuffer();
                ((org.mortbay.jetty.Request) request).setHandled(true);
            }
        }
    }

    class FlumeMetricFamilySamplesEnumeration implements Enumeration<Collector.MetricFamilySamples> {

        private Iterator<Collector.MetricFamilySamples> metricFamilySamples;
        private Collector.MetricFamilySamples next;

        FlumeMetricFamilySamplesEnumeration() {
            HashSet<Collector.MetricFamilySamples> metricFamilySamples = new HashSet();
            Map<String, Map<String, String>> metricsMap = JMXPollUtil.getAllMBeans();
            List<String> labelNames = Arrays.asList("flumeName");
            for (String key : metricsMap.keySet()) {
                String[] splits = StringUtils.split(key, ".", 2);
                String type = splits[0];
                String flumeName = splits[1];

                Map<String, String> snMetricsMap = metricsMap.get(key);
                for (String snKey : snMetricsMap.keySet()) {
                    Double lableValue = null;
                    try {
                        lableValue = Double.valueOf(snMetricsMap.get(snKey));
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    // metricsName:SINK_EventDrainSuccessCount
                    String metricsName = type + "_" + snKey;
                    List<Collector.MetricFamilySamples.Sample> samples = Lists.newArrayList();
                    samples.add(new Collector.MetricFamilySamples.Sample(metricsName, labelNames, Arrays.asList(flumeName), lableValue));
                    metricFamilySamples.add(new Collector.MetricFamilySamples(metricsName, Collector.Type.GAUGE, "this is " + metricsName, samples));
                }
            }
            this.metricFamilySamples = metricFamilySamples.iterator();
        }

        @Override
        public boolean hasMoreElements() {
            return metricFamilySamples.hasNext();
        }

        @Override
        public Collector.MetricFamilySamples nextElement() {
            return metricFamilySamples.next();
        }
    }
}