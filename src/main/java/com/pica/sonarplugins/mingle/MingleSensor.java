package com.pica.sonarplugins.mingle;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;

public class MingleSensor implements Sensor {


    MingleService service;

    public boolean shouldExecuteOnProject(Project project) {
        return project.isRoot();
    }

    public void analyse(Project project, SensorContext sensorContext) {

        String url = project.getConfiguration().getString(MinglePlugin.URL);
        String user = project.getConfiguration().getString(MinglePlugin.USER);
        String password = project.getConfiguration().getString(MinglePlugin.PASSWORD);
        String filter = project.getConfiguration().getString(MinglePlugin.FILTER);
        String projects = project.getConfiguration().getString(MinglePlugin.PROJECTS);

        int defects = 0;

        try {
            service = new MingleService(user, password, url);


            if (StringUtils.isNotBlank(projects)) {
                for (String projectName : projects.split(",")) {
                    defects += service.countDefects(StringUtils.trim(projectName), filter);
                }

            }
        } catch (Exception e) {
            //TODO - Don't do this...handle errors properly
        }


        saveDefectsMeasure(sensorContext, defects);
    }

    private void saveDefectsMeasure(SensorContext context, double numberOfDefects) {
        Measure measure = new Measure(MingleMetrics.DEFECTS, numberOfDefects);
        context.saveMeasure(measure);
    }
}