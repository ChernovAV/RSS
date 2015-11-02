package com.chernov.android.android_rss;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 29.10.2015.
 */

public class RssService extends IntentService {

    public final static String ENDPOINT = "http://www.cbc.ca/cmlink/rss-topstories";
    public static final String ITEMS = "items";
    public static final String RECEIVER = "receiver";
    static boolean isStopped = false;
    List<Item> news;
    Document doc = null;

    public RssService() {
        super("RssService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        /**
         *  считываем ссылку ENDPOINT
         *  получаем распарсенную страницу,  List
         */
        Log.e("myLog", "RssService onHandleIntent");
        connect();

        /**
         *  создаем объект bundle для передачи данных
         *  ложим/конвертируем ключь(ITEMS)-значение(Serializable)
         */

        ResultReceiver receiver = intent.getParcelableExtra(RECEIVER);

        if(news!=null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ITEMS, (Serializable) news);
            receiver.send(1, bundle);
        } else {
            receiver.send(0, null);
        }

        isStopped = true;
    }

    // подключаем к сети, считываем ссылку ENDPOINT
    private void connect() {

        try {
            doc = Jsoup.connect(ENDPOINT).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // если вернулся не пустой doc
        if(doc!=null) {
            addItem();
        }
    }

    // возвращаем элементы Item
    private void addItem() {
        news = new ArrayList<>();
        // обрабатываем элементы item
        for(Element item : doc.select("item")) {
            // создаем объект Item, сохраняем в него полученные значения
            Item it = new Item();
            final String title = item.select("title").first().text();
            it.setTitle(title);
            final String date = item.select("pubDate").first().text();
            it.setDate(date);
            final String author = item.select("author").first().text();
            it.setAuthor(author);
            final String link = item.select("link").first().text();
            it.setLink(link);
            Elements description = item.select("description");

            for (Element descr:description){
                String src = Jsoup.parse(descr.text()).select("img").first().attr("src");
                it.setImage(src);
            }
            news.add(it);
        }
    }
}
