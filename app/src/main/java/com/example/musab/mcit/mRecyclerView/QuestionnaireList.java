package com.example.musab.mcit.mRecyclerView;

/**
 * Created by musab on 12/11/2017.
 */

public class QuestionnaireList {
    int id;
    String title;
    String link;
    String date;
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDate() {
        return date;
    }

    public QuestionnaireList(int id, String title, String link, String date) {
        this.id=id;
        this.title = title;
        this.link = link;
        this.date = date;
    }

}
