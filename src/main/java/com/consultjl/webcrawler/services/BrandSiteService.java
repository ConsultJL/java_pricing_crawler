package com.consultjl.webcrawler.services;

import com.consultjl.webcrawler.models.BrandSite;
import com.consultjl.webcrawler.repositories.BrandSiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandSiteService {

    @Autowired
    private BrandSiteRepository brandSiteRepository;

    public Iterable<BrandSite> list() {
        return brandSiteRepository.findAll();
    }
}
