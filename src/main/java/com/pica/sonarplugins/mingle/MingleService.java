package com.pica.sonarplugins.mingle;

import org.springframework.web.client.RestTemplate;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.springframework.xml.xpath.XPathOperations;

import javax.xml.transform.Source;

/**
 * Created by IntelliJ IDEA.
 * User: clehmann
 * Date: 9/9/11
 * Time: 9:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class MingleService {
    RestTemplate restTemplate;
    private String url;
    private XPathOperations xpathTemplate = new Jaxp13XPathTemplate();

    public int countDefects(final String projectName, final String filter) {
        Source source = restTemplate.getForObject(url + "/api/v2/projects/{projectName}/cards.xml?filters[]={filter}", Source.class, projectName, filter);

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
