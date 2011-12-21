package com.pica.sonarplugins.mingle;

import org.sonar.api.Plugin;
import org.sonar.api.Properties;
import org.sonar.api.Property;

import java.util.Arrays;
import java.util.List;

@Properties({
        @Property(
                key = MinglePlugin.URL,
                defaultValue = "",
                name = "Server URL",
                description = "URL to your mingle server Example : http://mingle.yourcompany.com",
                global = true,
                project = false,
                module = false
        ),
        @Property(
                key = MinglePlugin.USER,
                defaultValue = "",
                name = "Username",
                global = true,
                project = false,
                module = false
        ),
        @Property(
                key = MinglePlugin.PASSWORD,
                defaultValue = "",
                name = "Password",
                global = true,
                project = false,
                module = false
        ),
        @Property(
                key = MinglePlugin.PROJECTS,
                defaultValue = "",
                description = "Comma separated list of projects to query",
                name = "Projects",
                global = true,
                project = false,
                module = false
        ),
        @Property(
                key = MinglePlugin.FILTER,
                defaultValue = "[type][is][Defect]",
                name = "Filter Strings",
                description = "Comma separated list of mingle api filter strings to find defect cards",
                global = false,
                project = true,
                module = false
        )
})
public class MinglePlugin implements Plugin {

    public static final String URL = "mingleplugin.url";
    public static final String USER = "mingleplugin.user";
    public static final String PASSWORD = "mingleplugin.password";
    public static final String FILTER = "mingleplugin.filter";
    public static final String PROJECTS = "mingleplugin.projects";

    /**
     * @deprecated this is not used anymore
     */
    public String getKey() {
        return "mingle";
    }

    /**
     * @deprecated this is not used anymore
     */
    public String getName() {
        return "Mingle Sonar Plugin";
    }

    /**
     * @deprecated this is not used anymore
     */
    public String getDescription() {
        return "Sonar plugin to gather defect information from mingle.";
    }

    // This is where you're going to declare all your Sonar extensions
    public List getExtensions() {
        return Arrays.asList(MingleMetrics.class, MingleSensor.class, MingleDashboardWidget.class);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
