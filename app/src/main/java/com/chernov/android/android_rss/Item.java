package com.chernov.android.android_rss;

/**
 * Created by Android on 29.10.2015.
 */
public class Item {
    // название новости
    private String title;
    // дата публикации
    private String date;
    // автор публикации
    private String author;
    // ссылка на image
    private String image;
    // ссылка на новость
    private String link;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public String getImage() {
        return image;
    }

    public String getLink() {
        return link;
    }

}