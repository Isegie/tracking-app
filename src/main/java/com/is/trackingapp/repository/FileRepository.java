package com.is.trackingapp.repository;

import com.is.trackingapp.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    Optional<FileEntity> findFileEntityByName(String name);

    @Query("SELECT f FROM Person pe join pe.files f WHERE f.person.id = :id")
    List<FileEntity> findFilesByPersonId(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE FileEntity f SET f.status = 'INACTIVE' WHERE f.person.id " +
            "IN (SELECT fi FROM FileEntity fi JOIN fi.person p WHERE p.id = :personId)")
    void setStatusToInactive(@Param("personId") Long personId);
}
