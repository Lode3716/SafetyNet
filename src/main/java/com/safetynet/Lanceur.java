package com.safetynet;

import com.safetynet.repository.RepositoryService;
import com.safetynet.repository.RepositoryServiceImpl;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Lanceur {


    public void run() {

        String fileName = "D:\\oc\\workspace\\SafetyNet\\src\\main\\java\\com\\safetynet\\dao\\data.json";
        RepositoryService repositoryService = new RepositoryServiceImpl();
        try {
            repositoryService.getPersonsRepository().findAll().forEach(s -> log.info(s));
            repositoryService.getFirestationsRepository().findAll();
            repositoryService.getMedicalrecordsRepository().findAll();

        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }
}
