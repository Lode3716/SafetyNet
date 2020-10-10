package com.safetynet.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.dao.UtilsDao;
import com.safetynet.model.Persons;
import com.safetynet.utils.ParseJSON;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
@Repository
public class PersonsRepository implements BuisnessRepo<Persons> {

    private ParseJSON json = new ParseJSON();
    private byte[] jsonData = new byte[0];

    @Autowired
    RepositoryService repositoryService;

    @Getter
    private List<Persons> personsList;


    public PersonsRepository()
    {
        this.personsList=LoadListInit.getPersonsList();
    }

    @Override
    public List<Persons> findAllInit() {
        List<Persons> persons2 = new ArrayList<>();
        try {
            jsonData = Files.readAllBytes(Paths.get(UtilsDao.FILE_NAME));
            json.parseJsonObject("persons", jsonData)
                    .elements()
                    .forEachRemaining(s -> {
                        try {
                            ObjectMapper objectMapper = new ObjectMapper();

                            Persons persons = objectMapper.treeToValue(s, Persons.class);
                            persons2.add(persons);

                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return persons2;
    }

    @Override
    public List<Persons> findAll() {
       return getPersonsList();
    }

    @Override
    public Optional<Persons> add(Persons persons) {
        if(exist(persons)==Boolean.FALSE)
        {
            findAll().add(persons);
            return Optional.of(persons);
        };

        return Optional.empty();
    }

    @Override
    public boolean delete(Persons persons) {
        if(exist(persons))
        {
            findAll().remove(persons);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public boolean exist(Persons persons)
    {
        return findAll().stream()
                .filter(search-> persons.getLastName().equals(search.getLastName()) && persons.getFirstName().equals(search.getFirstName()))
                .findFirst()
                .isPresent();
    }

    @Override
    public Optional<Persons> update(Persons persons) {
        if(exist(persons)) {
            findAll().stream()
                    .filter(search-> persons.getLastName().equals(search.getLastName()) && persons.getFirstName().equals(search.getFirstName()))
                    .findFirst()
                    .ifPresent(maj->
                            {
                                maj.setAddress(persons.getAddress());
                                maj.setCity(persons.getCity());
                                maj.setEmail(persons.getEmail());
                                maj.setPhone(persons.getPhone());
                                maj.setZip(persons.getZip());
                            });
            return Optional.of(persons);
        }
        return Optional.empty();
    }

    public Optional<Persons> finByElements(String nom, String prenom) {
log.info("finby elements : "+nom+" / "+prenom);
        AtomicReference<Persons> atomicPers=new AtomicReference<>();
            findAll().stream()
                    .filter(search-> nom.equals(search.getLastName()) && prenom.equals(search.getFirstName()))
                    .findFirst()
                    .ifPresent(searchPers->
                    {
                        atomicPers.set(searchPers);
                    });
            return Optional.of(atomicPers.get());
    }

}
