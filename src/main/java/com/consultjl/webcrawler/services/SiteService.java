package com.consultjl.webcrawler.services;

import com.consultjl.webcrawler.models.Site;
import com.consultjl.webcrawler.repositories.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SiteService {

    @Autowired
    private SiteRepository siteRepository;

    public Iterable<Site> list() {
        return siteRepository.findAll();
    }
}
