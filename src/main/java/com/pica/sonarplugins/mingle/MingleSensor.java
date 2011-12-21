package com.pica.sonarplugins.mingle;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;

import java.util.List;

public class MingleSensor implements Sensor {

    Logger logger = LoggerFactory.getLogger(getClass());

    MingleService service;

    public boolean shouldExecuteOnProject(Project project) {
        return project.isRoot();
    }

    public void analyse(Project project, SensorContext sensorContext) {

        String url = project.getConfiguration().getString(MinglePlugin.URL);
        String user = project.getConfiguration().getString(MinglePlugin.USER);
        String password = project.getConfiguration().getString(MinglePlugin.PASSWORD);
        List<String> filtersList = project.getConfiguration().getList(MinglePlugin.FILTER);
//        String filter = StringUtils.join(filtersList, ",");
        List<String> projects = project.getConfiguration().getList(MinglePlugin.PROJECTS);

        logger.debug(String.format("%s = %s", MinglePlugin.URL, url));
        logger.debug(String.format("%s = %s", MinglePlugin.USER, user));
        logger.debug(String.format("%s = %s", MinglePlugin.PASSWORD, password.replaceAll(".", "*")));
        logger.debug(String.format("%s = %s", MinglePlugin.FILTER, filtersList));
        logger.debug(String.format("%s = %s", MinglePlugin.PROJECTS, projects));

        int defects = 0;

        try {
            service = new MingleService(user, password, url);

            for (String projectName : projects) {
                logger.info(String.format("Counting defects for project %s", project), MinglePlugin.PROJECTS, projects);
                defects += service.countDefects(StringUtils.trim(projectName), filtersList);
            }

        } catch (Exception e) {
            logger.error("Error fetching defect info: ", e);
        }


        logger.info(String.format("Total defects: %d", defects));
        saveDefectsMeasure(sensorContext, defects);
    }

    private void saveDefectsMeasure(SensorContext context, double numberOfDefects) {
        Measure measure = new Measure(MingleMetrics.DEFECTS, numberOfDefects);
        context.saveMeasure(measure);
    }
}