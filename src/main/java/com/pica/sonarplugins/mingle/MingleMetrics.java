package com.pica.sonarplugins.mingle;

import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import java.util.Arrays;
import java.util.List;

public class MingleMetrics implements Metrics {
    public static final String DOMAIN = "Issues";


    public static final Metric DEFECTS = new Metric("mingle_defects", "Number of defects",
            "This is a metric to store the number of defects in ", Metric.ValueType.INT, -1, false,
            DOMAIN);


    // getMetrics() method is defined in the Metrics interface and is used by
    // Sonar to retrieve the list of new Metric
    public List<Metric> getMetrics() {
        return Arrays.asList(DEFECTS);
    }
}
