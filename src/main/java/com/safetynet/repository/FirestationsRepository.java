package com.safetynet.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.dao.UtilsDao;
import com.safetynet.model.Firestations;
import com.safetynet.utils.ParseJSON;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Repository
public class FirestationsRepository implements BuisnessRepo<Firestations> {

    private ParseJSON json = new ParseJSON();
    private byte[] jsonData = new byte[0];
    @Getter
    private List<Firestations> firestationsList;

    public FirestationsRepository() {
        this.firestationsList = LoadListInit.getFirestationsList();
    }

    @Override
    public List<Firestations> findAllInit() {
        List<Firestations> firestationsList = new ArrayList<>();
        try {
            jsonData = Files.readAllBytes(Paths.get(UtilsDao.FILE_NAME));
            json.parseJsonObject("firestations", jsonData)
                    .elements()
                    .forEachRemaining(s -> {
                        try {
                            ObjectMapper objectMapper = new ObjectMapper();
                            Firestations firestations = objectMapper.treeToValue(s, Firestations.class);
                            firestationsList.add(firestations);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return firestationsList;
    }

    @Override
    public List<Firestations> findAll() {
        return getFirestationsList();
    }

    @Override
    public Optional<Firestations> add(Firestations firestations) {
      return Optional.empty();
    }

    @Override
    public void remove(Firestations firestations) {

    }
}
