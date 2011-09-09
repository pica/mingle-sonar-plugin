package com.pica.sonarplugins.mingle;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CountingNodeCallbackHandlerTest {
    CountingNodeCallbackHandler handler;

    @Before
    public void setUp() throws Exception {
        handler = new CountingNodeCallbackHandler();
    }

    @Test
    public void testProcessNodeIncrementsCount() throws Exception {
        handler.processNode(null);
        assertThat(handler.getCount(), equalTo(1));
    }

    @Test
    public void testCountStartsAtZero() throws Exception {
        assertThat(handler.getCount(), equalTo(0));
    }
}
