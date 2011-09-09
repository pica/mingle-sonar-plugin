package com.pica.sonarplugins.mingle;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class CountingNodeCallbackHandler implements org.springframework.xml.xpath.NodeCallbackHandler{
    int count = 0;
    public void processNode(Node node) throws DOMException {
        count++;
    }

    public int getCount() {
        return count;
    }
}
