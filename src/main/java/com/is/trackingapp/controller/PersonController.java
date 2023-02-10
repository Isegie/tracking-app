package com.is.trackingapp.controller;

import com.is.trackingapp.entity.Person;
import com.is.trackingapp.entity.command.PersonCommand;
import com.is.trackingapp.entity.dto.PersonDTO;
import com.is.trackingapp.service.FileHandlingService;
import com.is.trackingapp.service.PersonService;
import com.is.trackingapp.service.mapping.PersonMappingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.is.trackingapp.constants.Constants.PERSON_ALREADY_EXISTS;
import static com.is.trackingapp.constants.Constants.PERSON_NOT_FOUND;

@RestController
@RequestMapping("/api/v1/person")
public class PersonController {

    private final PersonService personService;

    private final FileHandlingService fileHandlingService;

    private final PersonMappingService personMappingService;


    public PersonController(PersonService personService, FileHandlingService fileHandlingService, PersonMappingService personMappingService) {
        this.personService = personService;
        this.fileHandlingService = fileHandlingService;
        this.personMappingService = personMappingService;
    }

    @GetMapping
    public ResponseEntity<List<PersonDTO>> findAll() {
        List<PersonDTO> personList = this.personService.findAll();
        return new ResponseEntity<>(personList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PersonDTO> create(@Valid @RequestBody PersonCommand personCommand) {
        Optional<PersonDTO> personDTO = this.personService.findPersonByOIB(personCommand.getOib());

        if (personDTO.isPresent()) {
            throw new RuntimeException(String.format(PERSON_ALREADY_EXISTS, personCommand.getOib()));
        }

        Person person = this.personService.save(this.personMappingService.commandToDto(personCommand));

        return new ResponseEntity<>(this.personMappingService.toDto(person), HttpStatus.CREATED);
    }

    @GetMapping("/{oib}")
    public ResponseEntity<PersonDTO> findByOIB(@PathVariable String oib) {
        PersonDTO personDTO = this.personService.findPersonByOIB(oib)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PERSON_NOT_FOUND, oib)));

        this.fileHandlingService.createFile(personDTO);

        return new ResponseEntity<>(personDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{oib}")
    public ResponseEntity deleteByOib(@PathVariable String oib) {
        this.personService.deletePersonByOIB(oib);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
