package com.diverse.dife.repository.schoolInfo;

import com.diverse.dife.entity.schoolInfo.building.Building;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<Building, Long> {
}
