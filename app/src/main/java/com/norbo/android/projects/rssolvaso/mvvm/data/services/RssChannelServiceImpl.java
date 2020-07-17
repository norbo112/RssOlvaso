package com.norbo.android.projects.rssolvaso.mvvm.data.services;

import android.util.Log;

import com.norbo.android.projects.rssolvaso.mvvm.data.api.RssChannelService;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Article;
import com.norbo.android.projects.rssolvaso.mvvm.data.model.Channel;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class RssChannelServiceImpl implements RssChannelService {
    private static final String TAG = "RssServiceImpl";

    @Inject
    public RssChannelServiceImpl() {
    }

    @Override
    public Channel getChannel(String url) throws XMLExeption, AdatOlvasasExeption{
        Channel channel = new Channel();
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            con.setReadTimeout(5000);
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                    + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36");

            if(con.getResponseCode() == -1)
                Log.i(TAG, "fetchFeed: Connection status is -1 ");

            String srcString = getStringSource(con);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder doc = factory.newDocumentBuilder();
            Document document = doc.parse(new InputSource(new StringReader(srcString)));

            XPath xPath = XPathFactory.newInstance().newXPath();
            Node node = (Node) xPath.compile("//channel").evaluate(document, XPathConstants.NODE);
            channel.setTitle(xPath.compile("./title").evaluate(node, XPathConstants.STRING).toString());
            channel.setDescription(xPath.compile("./description").evaluate(node, XPathConstants.STRING).toString());
            channel.setLink(xPath.compile("./link").evaluate(node, XPathConstants.STRING).toString());
            channel.setLanguage(xPath.compile("./language").evaluate(node, XPathConstants.STRING).toString());
            channel.setPubDate(xPath.compile("./pubDate").evaluate(node, XPathConstants.STRING).toString());
            channel.setUrl(url);
        } catch (SAXException | ParserConfigurationException | XPathExpressionException ex) {
            throw new XMLExeption("Hír forrásból eredő hiba történt", ex);
        } catch (IOException ex) {
            throw new AdatOlvasasExeption("Hiba az adatok olvasása közben", ex);
        }

        return channel;
    }

    private String getStringSource(URLConnection con) throws IOException {
        String line;
        StringBuilder sb = new StringBuilder();

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
