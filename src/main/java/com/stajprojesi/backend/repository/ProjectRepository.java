package com.stajprojesi.backend.repository;

import com.stajprojesi.backend.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.files LEFT JOIN FETCH p.owner")
    List<Project> findAllWithFiles();
}
