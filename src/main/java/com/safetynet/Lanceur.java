package com.safetynet;

import com.safetynet.repository.RepositoryService;
import com.safetynet.repository.RepositoryServiceImpl;
import lombok.extern.log4j.Log4j2;

import java.nio.file.Files;
import java.nio.file.Paths;

@Log4j2
public class Lanceur {
    public static void main(String[] args) {

        String fileName = "D:\\Developpement\\workSpaceOC\\SafetyNet\\src\\main\\java\\com\\safetynet\\dao\\data.json";
        RepositoryService repositoryService = new RepositoryServiceImpl();
        try {

            byte[] jsonData = Files.readAllBytes(Paths.get(fileName));

            repositoryService.getPersonsRepository().read(jsonData);
            repositoryService.getFirestationsRepository().read(jsonData);
            repositoryService.getMedicalrecordsRepository().read(jsonData);

        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

}
