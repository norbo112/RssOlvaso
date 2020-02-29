package com.norbo.android.projects.rssolvaso.controller;

import android.util.Log;

import com.norbo.android.projects.rssolvaso.model.RssItem;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class RssController {
    public static List<RssItem> getRssItems(String url) {
        List<RssItem> rssitems = new ArrayList<>();
        try {

            URLConnection con = new URL(url).openConnection();

            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                    + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36");
            String line = null;
            StringBuilder sb = new StringBuilder();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            while((line = br.readLine()) != null) {
                sb.append(line);
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder doc = factory.newDocumentBuilder();
            Document document = doc.parse(new InputSource(new StringReader(sb.toString())));

            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList nodelist = (NodeList)xPath.compile("//item").evaluate(document, XPathConstants.NODESET);
            for (int i = 0; i < nodelist.getLength(); i++) {
                RssItem rs = new RssItem(
                        xPath.compile("./title").evaluate(nodelist.item(i), XPathConstants.STRING)
                                .toString(),
                        xPath.compile("./link").evaluate(nodelist.item(i), XPathConstants.STRING)
                                .toString(),
                        xPath.compile("./guid").evaluate(nodelist.item(i), XPathConstants.STRING)
                                .toString(),
                        xPath.compile("./description").evaluate(nodelist.item(i), XPathConstants.STRING)
                                .toString(),
                        xPath.compile("./category").evaluate(nodelist.item(i), XPathConstants.STRING)
                                .toString(),
                        xPath.compile("./pubDate").evaluate(nodelist.item(i), XPathConstants.STRING)
                                .toString()
                );
                rssitems.add(rs);
            }

        } catch (SAXException ex) {
            Log.e(RssController.class.getSimpleName(), null, ex);
        } catch (IOException ex) {
            Log.e(RssController.class.getSimpleName(), null, ex);
        } catch (XPathExpressionException ex) {
            Log.e(RssController.class.getSimpleName(), null, ex);
        } catch (ParserConfigurationException ex) {
            Log.e(RssController.class.getSimpleName(), null, ex);
        }

        return rssitems;
    }
}
