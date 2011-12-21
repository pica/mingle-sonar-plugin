package com.pica.sonarplugins.mingle;

import com.sun.xml.internal.messaging.saaj.util.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.springframework.xml.xpath.XPathOperations;

import javax.xml.transform.Source;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.List;

public class MingleService {
    RestTemplate restTemplate = new RestTemplate();
    private String url;
    private XPathOperations xpathTemplate = new Jaxp13XPathTemplate();

    Logger logger = LoggerFactory.getLogger(getClass());

    public MingleService() {

    }

    public MingleService(final String username, final String password, final String url) {
        this.url = url;
        SimpleClientHttpRequestFactory connectionFactory = new SimpleClientHttpRequestFactory() {
                  @Override
                  protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                      super.prepareConnection(connection, httpMethod);

                      String authorisation = username + ":" + password;
                      byte[] encodedAuthorisation = Base64.encode(authorisation.getBytes());
                      connection.setRequestProperty("Authorization", "Basic " + new String(encodedAuthorisation));
                  }
              };

        restTemplate = new RestTemplate(connectionFactory);
    }

    public int countDefects(final String projectName, List<String> filters) {

        String path = "/api/v2/projects/{projectName}/cards.xml";

        if(CollectionUtils.isNotEmpty(filters))
        {
            logger.info(String.format("Filter string %s, adding filters(s)", filters));

            path += "?";
            Collection transformedFilters = CollectionUtils.collect(filters, new Transformer() {
                public Object transform(Object input) {
                    logger.debug(String.format("Input string: %s", input));
                    String param = "filters[]=" + StringUtils.trim((String) input);

                    logger.debug(String.format("Output string: %s", param));
                    return param;
                }
            });

            path += StringUtils.join(transformedFilters, "&");

            logger.info(String.format("Full path: %s", url + path));

        }


        Source source = restTemplate.getForObject(url + path, Source.class, projectName);

        CountingNodeCallbackHandler handler = new CountingNodeCallbackHandler();
        xpathTemplate.evaluate("//card", source, handler);
        return handler.getCount();
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setXpathTemplate(XPathOperations xpathTemplate) {
        this.xpathTemplate = xpathTemplate;
    }
}
