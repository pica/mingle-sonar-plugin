package com.pica.sonarplugins.mingle;

import com.sun.xml.internal.messaging.saaj.util.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.springframework.xml.xpath.XPathOperations;

import javax.xml.transform.Source;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collection;

public class MingleService {
    RestTemplate restTemplate = new RestTemplate();
    private String url;
    private XPathOperations xpathTemplate = new Jaxp13XPathTemplate();

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

    public int countDefects(final String projectName, String filter) {

        String path = "/api/v2/projects/{projectName}/cards.xml";

        if(StringUtils.isNotEmpty(filter))
        {
            path += "?";
            String[] filters = StringUtils.split(filter, ",");
            Collection transformedFilters = CollectionUtils.collect(Arrays.asList(filters), new Transformer() {
                public Object transform(Object input) {
                    return "filters[]=" + (String)input;
                }
            });

            path += StringUtils.join(transformedFilters, "&");
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
