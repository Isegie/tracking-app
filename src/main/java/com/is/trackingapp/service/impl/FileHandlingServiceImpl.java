package com.is.trackingapp.service.impl;

import com.is.trackingapp.entity.FileEntity;
import com.is.trackingapp.entity.dto.PersonDTO;
import com.is.trackingapp.entity.enums.Status;
import com.is.trackingapp.repository.FileRepository;
import com.is.trackingapp.service.FileHandlingService;
import com.is.trackingapp.service.PersonService;
import com.is.trackingapp.service.mapping.PersonMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.is.trackingapp.constants.Constants.*;

@Service
@Slf4j
public class FileHandlingServiceImpl implements FileHandlingService {

    @Value("${folder.data.path}")
    private String filesFolder;

    private final FileRepository fileRepository;

    private final PersonService personService;

    private final PersonMappingService personMappingService;

    public FileHandlingServiceImpl(FileRepository fileRepository, PersonService personService, PersonMappingService personMappingService) {
        this.fileRepository = fileRepository;
        this.personService = personService;
        this.personMappingService = personMappingService;
    }

    @Override
    public FileEntity createFile(PersonDTO personDTO) {
        try {
            FileEntity fileEntity = new FileEntity();
            Optional<PersonDTO> person = this.personService.findPersonByOIB(personDTO.getOib());

            if (person.isPresent()) {

                this.checkIfHasActiveFilesAndChangeStatus(person.get().getId());

                String fileName = person.get().getOib().concat(LocalDateTime.now().toString());
                Optional<FileEntity> fileEntityOptional = this.fileRepository.findFileEntityByName(fileName);

                if (fileEntityOptional.isPresent()) {
                    throw new RuntimeException(String.format(FILE_ALREADY_EXISTS, fileName));
                }

                Path path = this.writePersonInfoToFile(fileName.concat(TEXT), person.get().toString());

                fileEntity.setName(fileName);
                fileEntity.setPayload(this.convertFileToBytes(path.toFile()));
                fileEntity.setStatus(Status.ACTIVE);
                fileEntity.setPerson(this.personMappingService.toEntity(personDTO));

                return this.fileRepository.save(fileEntity);
            }
        } catch (IOException e) {
            log.error(FAILED_TO_CREATE_FILE);
        }
        throw new RuntimeException(FAILED_TO_CREATE_FILE);
    }

    private void checkIfHasActiveFilesAndChangeStatus(Long id) {
        List<FileEntity> fileEntities = this.fileRepository.findFilesByPersonId(id);

        if (fileEntities != null && !fileEntities.isEmpty()) {
            fileEntities.stream().
                    filter(f -> f.getStatus().equals(Status.ACTIVE)).map(m -> {
                        m.setStatus(Status.INACTIVE);
                        return m;
                    }).forEach(this.fileRepository::save);
        }
    }

    private Path writePersonInfoToFile(String fileName, String content) throws IOException {
        final Path path = Paths.get(filesFolder);
        File filesDirectory;
        File fileWithRelativePath;

        if (Files.exists(path)) {
            fileWithRelativePath = new File(path.toFile(), fileName);
            return Files.writeString(Paths.get(fileWithRelativePath.getAbsolutePath()), content);
        }

        filesDirectory = Files.createDirectory(path).toFile();
        fileWithRelativePath = new File(filesDirectory, fileName);
        return Files.writeString(Paths.get(fileWithRelativePath.getAbsolutePath()), content);
    }

    private byte[] convertFileToBytes(File file) throws IOException {
        if (file != null) {
            byte[] data = Files.readAllBytes(file.toPath());
            if (data.length > 0) {
                return data;
            }
            throw new IOException("Failed to convert file to bytes");
        }
        return new byte[0];
    }
}
