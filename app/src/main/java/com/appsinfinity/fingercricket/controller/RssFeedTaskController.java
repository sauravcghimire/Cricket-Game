package com.appsinfinity.fingercricket.controller;

import android.os.AsyncTask;

import com.appsinfinity.fingercricket.interfaces.ControllerInterface;
import com.appsinfinity.fingercricket.models.NewsDto;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saurav on 8/6/2014.
 */
public class RssFeedTaskController extends AsyncTask<Void, Void, List<NewsDto>> {
    private String title = null;
    private String link = null;
    private String date = null;
    private String description = null;
    int event;
    private String text = null;
    private ControllerInterface<List<NewsDto>> controllerInterface;
    ArrayList<NewsDto> newsDtoList = new ArrayList<>();
    private XmlPullParserFactory xmlFactoryObject;


    public RssFeedTaskController(ControllerInterface<List<NewsDto>> context) {
        this.controllerInterface = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        controllerInterface.onTaskStarted();
    }

    @Override
    protected ArrayList<NewsDto> doInBackground(Void... params) {
        try {
            URL url = new URL("http://www.espncricinfo.com/rss/content/story/feeds/6.xml");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            InputStream stream = conn.getInputStream();
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();
            myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myparser.setInput(stream, null);

            int event = myparser.getEventType();
            boolean isTrue = true;
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myparser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = myparser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        switch (name) {
                            case "title":
                                title = text;
                                break;
                            case "link":
                                link = text;
                                break;
                            case "description":
                                description = text;
                                break;
                            case "pubDate":
                                date = text;
                                break;
                            default:
                                break;
                        }
                        break;
                }

                event = myparser.next();

                if (link != null && title != null && date != null
                        && description != null) {
                    NewsDto newsDto = new NewsDto(link, title, description, date);
                    if(!isTrue && !description.contains("&amp")){
                        newsDtoList.add(newsDto);
                    }
                    isTrue = false;
                    link = title = date = description = null;
                }
            }
            stream.close();

        } catch (Exception ignored) {
        }

        return newsDtoList;
    }

    @Override
    protected void onPostExecute(List<NewsDto> result) {
        controllerInterface.onTaskFinished(result);
    }
}
