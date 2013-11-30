package net.codelab.core.entity.dto;

import org.apache.lucene.document.Document;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Melzarek on 25/11/13.
 */
public class ResultsDTO<T> {

    private long searchtime;

    private int totalhits;

    private Collection<T> results = new LinkedList<>();

    private Collection<String> suggestions = new LinkedList<>();

    private Collection<String> higlights = new LinkedList<>();

    public ResultsDTO()
    {

    }

    public Collection<String> getHiglights() {
        return higlights;
    }

    public void setHiglights(Collection<String> higlights) {
        this.higlights = higlights;
    }

    public Collection<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(Collection<String> suggestions) {
        this.suggestions = suggestions;
    }

    public long getSearchtime() {
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

    public Collection<T> getResults() {
        return results;
    }

    public void setResults(Collection<T> results) {
        this.results = results;
    }


}
