package com.safetynet.service;

import com.googlecode.jmapper.JMapper;
import com.safetynet.dto.PersonsDto;
import com.safetynet.dto.PersonsEmailDTO;
import com.safetynet.dto.PersonsMedicationAdresseDTO;
import com.safetynet.dto.factory.ServiceFactory;
import com.safetynet.model.Persons;
import com.safetynet.repository.RepositoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class PersonsService {

    @Autowired
    RepositoryService repositorPersons;

    @Autowired
    JMapper<PersonsDto, Persons> personMapper;

    @Autowired
    JMapper<Persons, PersonsDto> personUnMapper;

    @Autowired
    ServiceFactory serviceFactory;

    public List<PersonsDto> getReadAllpersons() {
        List<PersonsDto> list = new ArrayList<>();
        repositorPersons.getPersonsRepository().findAll().forEach(persons ->
                {
                    list.add(personMapper.getDestination(persons));
                }
        );
        return list;
    }

    public Optional<Persons> addPersons(PersonsDto persons) {
        return repositorPersons.getPersonsRepository().add(personUnMapper.getDestination(persons));
    }


    public boolean deletePerson(@RequestBody PersonsDto person) {
        return repositorPersons.getPersonsRepository().delete(personUnMapper.getDestination(person));
    }

    public Optional<Persons> updatePerson(@RequestBody PersonsDto person) {
        return repositorPersons.getPersonsRepository().update(personUnMapper.getDestination(person));
    }

    public List<PersonsMedicationAdresseDTO> getPersonInfo(String firstName, String lastName) {
        List<PersonsMedicationAdresseDTO> dtoList = new ArrayList<>();
        repositorPersons.getPersonsRepository().searchAllName(lastName, firstName)
                .ifPresent(lst ->
                {
                    lst.forEach(persons ->
                    {
                        dtoList.add(serviceFactory.getPersonsMedicationAdresseFactory().createPersonMedical(persons));
                    });
                });
        return dtoList;
    }

    public List<PersonsEmailDTO> getPersonsEmail(String city) {
        List<PersonsEmailDTO> dtoList = new ArrayList<>();
        repositorPersons.getPersonsRepository()
                .searchEmailCity(city)
                .ifPresent(lst ->
                        lst.forEach(mail ->
                                dtoList.add(serviceFactory.getPersonEmailFactory().create(mail))));
        return dtoList;
    }

    public Optional<Persons> getFindPerson(String nom, String prenom) {
        log.info("Entre dans read Person by Elements " + nom + " " + prenom);
        return repositorPersons.getPersonsRepository().finByElements(nom, prenom);
    }

}