package com.is.trackingapp.service;

import com.is.trackingapp.entity.FileEntity;
import com.is.trackingapp.entity.dto.PersonDTO;

public interface FileHandlingService {

    FileEntity createFile(PersonDTO personDTO);

}
