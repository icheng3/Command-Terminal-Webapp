package edu.brown.cs32.examples.moshiExample.csv;

import java.util.List;

public class ColRowCounter {
    private List<List<String>> data;

    public ColRowCounter(List<List<String>> data) {
        this.data = data;
    }

    /** Counts the rows, cols, chars, and words given data. */
    public int rowCount() {
        Integer rowCount = this.data.size();
        return rowCount;
    }

    public int colCount() {
        Integer colCount = this.data.get(0).size();
        return colCount;
    }
}
