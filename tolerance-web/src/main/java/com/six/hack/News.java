package com.six.hack;

import java.util.Date;

public class News {
    private Date date;
    private String headline;

    public News(Date date, String headline) {
        this.date = date;
        this.headline = headline;
    }

    public Date getDate() {
        return date;
    }

    public String getHeadline() {
        return headline;
    }
}
