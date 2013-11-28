package net.codelab.core.lucene.index;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Melzarek on 25/11/13.
 */
public class ResultsDTO<T> {

    private long searchtime;

    private int totalhits;

    private List<T> results = new LinkedList<T>();

    public ResultsDTO()
    {

    }

    public float getSearchtime() {
        return searchtime;
    }

    public void setSearchtime(long searchtime) {
        this.searchtime = searchtime;
    }

    public int getTotalhits() {
        return totalhits;
    }

    public void setTotalhits(int totalhits) {
        this.totalhits = totalhits;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
