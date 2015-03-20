package six.tolerance.check;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Price {
    private final String line;
    private long gsn;
    private Date date;
    private int marketCode;
    private int currencyCode;
    private long valorNumber;
    private int valueType;
    private int statisticType;
    private int valueStyle;
    private double value;

    public Price(String line) {
        this.line = line;
        String[] cols = line.split(";");
        gsn = Long.parseLong(cols[0]);
        date = getDate(cols[1]);
        marketCode = Integer.parseInt(cols[2]);
        currencyCode = Integer.parseInt(cols[3]);
        valorNumber = Long.parseLong(cols[4]);
        valueType = Integer.parseInt(cols[5]);
        statisticType = Integer.parseInt(cols[6]);
        if ("".equals(cols[7])) {
            valueStyle = 0;
        } else {
            valueStyle = Integer.parseInt(cols[7]);
        }
        value = Double.parseDouble(cols[8]);
    }

    private Date getDate(String string) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(string);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getLine() {
        return line;
    }

    public long getGsn() {
        return gsn;
    }

    public Date getDate() {
        return date;
    }

    public int getMarketCode() {
        return marketCode;
    }

    public int getCurrencyCode() {
        return currencyCode;
    }

    public long getValorNumber() {
        return valorNumber;
    }

    public int getValueType() {
        return valueType;
    }

    public int getStatisticType() {
        return statisticType;
    }

    public int getValueStyle() {
        return valueStyle;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return line;
    }

}
