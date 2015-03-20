package com.six.hack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

public class PriceStream implements Iterable<Price>, Iterator<Price> {

    private BufferedReader reader;

    private String line;

    public PriceStream() {
        reader = createReader();
        next();
    }

    private BufferedReader createReader() {
        return new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/sample/data/mdf_stream.csv")));
    }

    @Override
    public boolean hasNext() {
        return line != null;
    }

    @Override
    public Price next() {
        try {
            String oldLine = line;

            if (reader.ready()) {
                line = reader.readLine();
                if (line != null && line.startsWith("GSN;date")) {
                    line = reader.readLine();
                    return null;
                }
            } else {
                line = null;
            }

            return new Price(oldLine);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove() {
        throw new RuntimeException("not supported");

    }

    @Override
    public Iterator<Price> iterator() {
        return this;
    }
}
