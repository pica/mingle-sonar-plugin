package com.pica.sonarplugins.mingle;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.springframework.web.client.RestTemplate;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.springframework.xml.xpath.XPathOperations;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MingleServiceTest {

    private MingleService service;
    private static final String FILTER = "filter";
    private static final String PROJECT_NAME = "projectName";
    private static final String URL = "url";
    private static final String XML = "<xml><card /><card /></xml>";

    private RestTemplate restTemplate;
    private Source source;
    private XPathOperations xpathTemplate;

    @Before
    public void setUp() throws Exception {
        source = new StreamSource(new java.io.StringReader(XML));

        restTemplate = mock(RestTemplate.class);
        xpathTemplate = new Jaxp13XPathTemplate();

        service = new MingleService();
        service.setUrl(URL);
        service.setRestTemplate(restTemplate);
        service.setXpathTemplate(xpathTemplate);

        when(restTemplate.getForObject(Matchers.<String>any(), eq(Source.class), Matchers.<String>any(), Matchers.<String>any())).thenReturn(source);

    }

    @Test
    public void testCountResourcesCallsRestTemplateWithTheCorrectParameters() throws Exception {
        final String expectedPath = URL + "/api/v2/projects/{projectName}/cards.xml?filters[]={filter}";
        service.countDefects(PROJECT_NAME, FILTER);
        verify(restTemplate).getForObject(expectedPath, Source.class, PROJECT_NAME, FILTER);
    }

    @Test
    public void testCountResourcesCountsCardsInReturnedXmlProperly() throws Exception {
        assertThat(service.countDefects(PROJECT_NAME, FILTER), equalTo(2)) ;
    }




}
