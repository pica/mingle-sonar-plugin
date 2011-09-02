package com.pica.sonarplugins.mingle;

import org.apache.commons.lang.math.RandomUtils;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;

public class MingleSensor implements Sensor {

    public boolean shouldExecuteOnProject(Project project) {
        return project.isRoot();
    }

    public void analyse(Project project, SensorContext sensorContext) {
        saveDefectsMeasure(sensorContext, RandomUtils.nextDouble());
    }

    private void saveDefectsMeasure(SensorContext context, double numberOfDefects) {
        Measure measure = new Measure(MingleMetrics.DEFECTS, numberOfDefects);
        context.saveMeasure(measure);
    }
}
