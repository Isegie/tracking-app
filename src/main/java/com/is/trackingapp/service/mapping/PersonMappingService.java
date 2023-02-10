package com.is.trackingapp.service.mapping;


import com.is.trackingapp.entity.Person;
import com.is.trackingapp.entity.command.PersonCommand;
import com.is.trackingapp.entity.dto.PersonDTO;
import com.is.trackingapp.entity.enums.Status;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;

@Service
public class PersonMappingService {

    public PersonDTO toDto(Person person) {
        return PersonDTO.builder()
                .id(person.getId())
                .firstName(person.getFirstName())
                .lastName(person.getLastName()).oib(person.getOib())
                .status(person.getStatus()).build();
    }

    public Person toEntity(PersonDTO personDTO) {
        Person person = new Person();

        person.setId(personDTO.getId());
        person.setFirstName(personDTO.getFirstName());
        person.setLastName(personDTO.getLastName());
        person.setOib(personDTO.getOib());
        person.setStatus(personDTO.getStatus());

        return person;
    }

    public PersonDTO commandToDto(PersonCommand personCommand) {
        Status status = Arrays.stream(Status.values())
                .filter(s -> s.name().equals(personCommand.getStatus())).findFirst().orElseThrow(EntityNotFoundException::new);

        return PersonDTO.builder()
                .firstName(personCommand.getFirstName())
                .lastName(personCommand.getLastName()).oib(personCommand.getOib())
                .status(status).build();
    }

}
