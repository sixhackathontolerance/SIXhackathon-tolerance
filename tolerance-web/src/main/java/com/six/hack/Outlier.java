package com.six.hack;

import java.util.List;

public class Outlier {

    private Price price;
    private List<Price> prices;
    private List<News> news;

    public Outlier(Price price) {
        this.price = price;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }

    public List<News> getNews() {
        return news;
    }
}
