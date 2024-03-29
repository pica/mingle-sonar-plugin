package com.pica.sonarplugins.mingle;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.springframework.web.client.RestTemplate;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MingleServiceTest {

    private MingleService service;
    private static final List<String> FILTERS = Arrays.asList("filter");
    private static final String PROJECT_NAME = "projectName";
    private static final String URL = "url";
    private static final String XML = "<xml><card /><card /></xml>";

    private RestTemplate restTemplate;
    private Source source;

    @Before
    public void setUp() throws Exception {
        source = new StreamSource(new java.io.StringReader(XML));

        restTemplate = mock(RestTemplate.class);

        service = new MingleService();
        service.setUrl(URL);
        service.setRestTemplate(restTemplate);

        when(restTemplate.getForObject(Matchers.<String>any(), eq(Source.class), Matchers.<String>any())).thenReturn(source);
    }

    @Test
    public void testCountDefects_singleFilters_callsGetForObject() throws Exception {
        final String expectedPath = URL + "/api/v2/projects/{projectName}/cards.xml?filters[]=filter";
        service.countDefects(PROJECT_NAME, FILTERS);
        verify(restTemplate).getForObject(expectedPath, Source.class, PROJECT_NAME);
    }

    @Test
    public void testCountDefects_multipleFilters_callsGetForObject() throws Exception {
        final String expectedPath = URL + "/api/v2/projects/{projectName}/cards.xml?filters[]=[type][is][defect]&filters[]=[filter][is][on]";
        service.countDefects(PROJECT_NAME, Arrays.asList("[type][is][defect]","[filter][is][on]"));
        verify(restTemplate).getForObject(expectedPath, Source.class, PROJECT_NAME);
    }

    @Test
    public void testCountDefects_noFilters_callsGetForObject() throws Exception {
        final String expectedPath = URL + "/api/v2/projects/{projectName}/cards.xml";
        service.countDefects(PROJECT_NAME, new ArrayList<String>());
        verify(restTemplate).getForObject(expectedPath, Source.class, PROJECT_NAME);
    }

    @Test
    public void testCountDefects_singleFilter_parsesXml() throws Exception {
        assertThat(service.countDefects(PROJECT_NAME, FILTERS), equalTo(2)) ;
    }    

}
