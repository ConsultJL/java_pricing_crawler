package com.consultjl.webcrawler.repositories;

import com.consultjl.webcrawler.models.BrandSite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandSiteRepository extends CrudRepository<BrandSite, Integer> {
}
