package com.six.hack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.springframework.beans.factory.annotation.Autowired;

import com.six.hack.model.News;
import com.six.hack.model.Outlier;
import com.six.hack.model.Price;
import com.six.hack.model.PriceStream;
import com.six.hack.model.User;

public class QueueWorker extends Thread {
    @Autowired
    private WebController controller;

    public QueueWorker() {
    }

    @PostConstruct
    public void startWorker() {
        start();
    }

    private void populateQueue() {
        Set<Long> outlierGSN = new HashSet<Long>();
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
                    "/sample/data/Solution_short.csv")));
            while (reader.ready()) {
                final String line = reader.readLine();
                if (line == null) {
                    break;
                }
                if (line.startsWith("GSN")) {
                    continue;
                }

                final String[] cols = line.split(";");
                outlierGSN.add(Long.valueOf(cols[0]));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Price price : new PriceStream()) {
            if (outlierGSN.contains(Long.valueOf(price.getGsn()))) {
                controller.toQueue(new Outlier(price));
            }
        }
    }

    @Override
    public void run() {
        System.out.println("queue worker start");
        //populateQueue();
        while (true) {

            if (controller.queueEmpty()) {
                sleep();
            } else {
                boolean found = false;
                for (User participant : controller.users()) {
                    if (participant.isReady()) {
                        Outlier outlier = controller.next();

                        outlier.setPrices(getRange(outlier.getPrice()));
                        outlier.setNews(getNews(outlier.getPrice()));

                        controller.toUser(participant, outlier);

                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println("queue worker no user found");
                    sleep();
                }
            }
        }
    }

    private List<News> getNews(final Price outlierPrice) {
        final List<News> news = new ArrayList<News>();
        try {
            int counter = 0;
            final BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
                    "/sample/data/news_stream.csv")));
            while (reader.ready()) {
                final String line = reader.readLine();
                if (line == null) {
                    break;
                }
                if (line.startsWith("#date;time;headline")) {
                    continue;
                }
                final String[] cols = line.split(";");
                final Date date = new SimpleDateFormat("yyyy-MM-dd").parse(cols[0]);
                if (counter < 3) {
                    news.add(new News(date, cols[2]));
                } else {
                    break;
                }
                counter++;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return news;
    }

    private List<Price> getRange(final Price outlierPrice) {
        CircularFifoQueue<Price> beforePrices = new CircularFifoQueue<>(3);
        int afterCounter = 0;
        List<Price> prices = new ArrayList<Price>();
        for (Price price : new PriceStream()) {
            if (price.getMarketCode() == outlierPrice.getMarketCode()
                    && price.getCurrencyCode() == outlierPrice.getCurrencyCode()
                    && price.getValorNumber() == outlierPrice.getValorNumber()
                    && price.getValueType() == outlierPrice.getValueType()
                    && price.getStatisticType() == outlierPrice.getStatisticType()) {

                if (price.getGsn() == outlierPrice.getGsn()) {
                    afterCounter++;
                    prices.addAll(beforePrices);
                }
                if (afterCounter > 0) {
                    prices.add(price);
                    afterCounter++;
                } else {
                    beforePrices.add(price);
                }
                if (afterCounter > 4) {
                    break;
                }
            }
        }
        return prices;
    }

    private void sleep() {
        System.out.println("queue worker sleep for 3 sec");
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            System.err.println("queue worker was interrupted");
        }
    }
}
