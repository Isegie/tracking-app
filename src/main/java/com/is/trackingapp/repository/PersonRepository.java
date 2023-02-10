package com.is.trackingapp.repository;

import com.is.trackingapp.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByOib(String oib);

    void deleteByOib(String oib);

}