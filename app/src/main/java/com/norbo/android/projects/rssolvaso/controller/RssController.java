package com.norbo.android.projects.rssolvaso.controller;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ProgressBar;

import com.norbo.android.projects.rssolvaso.model.RssItem;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
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
    private ProgressDialog progressBar;
    private Context context;

    public RssController(Context context) {
        this.context = context;
    }

    public List<RssItem> getRssItems(String url) {
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
                Bitmap bitmap = null;
                try {
                    Object enclosureUrl = xPath.compile("./enclosure/@url").evaluate(nodelist.item(i), XPathConstants.STRING);
                    URL url1 = new URL(enclosureUrl
                            .toString());
                    bitmap = BitmapFactory.decodeStream(url1.openConnection().getInputStream());
                } catch (MalformedURLException malex) {
                    bitmap = null;
                }

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
                                .toString(),
                        bitmap

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

    public void showProgress() {
        if (progressBar == null) {
            progressBar = new ProgressDialog(context);
            progressBar.setTitle("Betöltés...");
        }

        progressBar.show();


    }

    public void dismissProgress() {
        if(progressBar != null && progressBar.isShowing()) {
            progressBar.dismiss();
        }
    }
}
