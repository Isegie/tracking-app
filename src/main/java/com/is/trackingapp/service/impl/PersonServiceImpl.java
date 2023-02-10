package com.is.trackingapp.service.impl;

import com.is.trackingapp.entity.Person;
import com.is.trackingapp.entity.dto.PersonDTO;
import com.is.trackingapp.repository.FileRepository;
import com.is.trackingapp.repository.PersonRepository;
import com.is.trackingapp.service.PersonService;
import com.is.trackingapp.service.mapping.PersonMappingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.is.trackingapp.constants.Constants.PERSON_ALREADY_EXISTS;
import static com.is.trackingapp.constants.Constants.PERSON_NOT_FOUND;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final FileRepository fileRepository;

    private final PersonMappingService personMappingService;

    public PersonServiceImpl(PersonRepository personRepository, FileRepository fileRepository, PersonMappingService personMappingService) {
        this.personRepository = personRepository;
        this.fileRepository = fileRepository;
        this.personMappingService = personMappingService;
    }

    @Override
    public List<PersonDTO> findAll() {
        return this.personRepository.findAll().stream()
                .map(this.personMappingService::toDto).collect(Collectors.toList());
    }

    @Override
    public Person save(PersonDTO personDTO) {
        Optional<Person> person = this.personRepository.findByOib(personDTO.getOib());

        if (person.isPresent()) {
            throw new RuntimeException(String.format(PERSON_ALREADY_EXISTS, personDTO.getId()));
        }

        return this.personRepository.save(this.personMappingService.toEntity(personDTO));
    }

    @Override
    public Optional<PersonDTO> findPersonByOIB(String oib) {
        return this.personRepository.findByOib(oib).map(this.personMappingService::toDto);
    }

    @Override
    public void deletePersonByOIB(String oib) {
        Optional<Person> person = this.personRepository.findByOib(oib);
        if (!person.isPresent()) {
            throw new EntityNotFoundException(String.format(PERSON_NOT_FOUND, oib));
        }

        this.fileRepository.setStatusToInactive(person.get().getId());

        person.get().getFiles().forEach(e -> e.setPerson(null));

        this.personRepository.deleteByOib(oib);
    }

}
