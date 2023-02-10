package com.is.trackingapp.service;

import com.is.trackingapp.entity.Person;
import com.is.trackingapp.entity.dto.PersonDTO;

import java.util.List;
import java.util.Optional;

public interface PersonService {


    List<PersonDTO> findAll();

    Person save(PersonDTO personDTO);

    Optional<PersonDTO> findPersonByOIB(String oib);

    void deletePersonByOIB(String oib);

}
