package net.codelab.core.entity.dto;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Melzarek on 01/12/13.
 */
public class Inproceedings {

    private Collection<String> author;
    private String title;
    private String pages;
    private String year;
    private String booktitle;
    private String ee;
    private String url;

    public Inproceedings() {
       author = new LinkedList<>();
       this.title = "";
       this.pages = "";
       this.year = "";
       this.booktitle = "";
       this.ee = "";
       this.url = "";
    }

    public Collection<String> getAuthor() {
        return author;
    }

    public void setAuthor(Collection<String> author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBooktitle() {
        return booktitle;
    }

    public void setBooktitle(String booktitle) {
        this.booktitle = booktitle;
    }

    public String getEe() {
        return ee;
    }

    public void setEe(String ee) {
        this.ee = ee;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
