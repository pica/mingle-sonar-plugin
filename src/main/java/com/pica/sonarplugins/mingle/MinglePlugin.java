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
                description = "Example : http://jira.codehaus.org",
                global = true,
                project = true,
                module = false
        ),
        @Property(
                key = MinglePlugin.USER,
                defaultValue = "",
                name = "Username",
                global = true,
                project = true,
                module = false
        ),
        @Property(
                key = MinglePlugin.PASSWORD,
                defaultValue = "",
                name = "Password",
                global = true,
                project = true,
                module = false
        ),
        @Property(
                key = MinglePlugin.VIEW,
                defaultValue = "",
                name = "View name",
                description = "Case sensitive, example : SONAR-current-iteration",
                global = false,
                project = true,
                module = true
        )
})
public class MinglePlugin implements Plugin {

    private static final String URL = "mingleplugin.url";
    private static final String USER = "mingleplugin.user";
    private static final String PASSWORD = "mingleplugin.password";
    private static final String VIEW = "mingleplugin.filter";

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
        return "My Sonar plugin";
    }

    /**
     * @deprecated this is not used anymore
     */
    public String getDescription() {
        return "You shouldn't expect too much from this plugin except displaying the Hello World message.";
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
