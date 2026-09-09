package com.industrial.equipment.repository;

import com.industrial.equipment.entity.Site;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site,UUID>{
}
