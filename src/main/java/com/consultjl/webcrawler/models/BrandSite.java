package com.consultjl.webcrawler.models;

import javax.persistence.*;

@Entity
@Table(name = "brandsites")
public class BrandSite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String url;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return "http://"+url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
