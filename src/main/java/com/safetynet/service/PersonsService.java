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

    public Optional<PersonsDto> addPersons(PersonsDto persons) {
        Optional<Persons> person = repositorPersons.getPersonsRepository().add(personUnMapper.getDestination(persons));
        return Optional.of(personMapper.getDestination(person.get()));
    }


    public boolean deletePerson(PersonsDto person) {
        return repositorPersons.getPersonsRepository().delete(personUnMapper.getDestination(person));
    }

    public Optional<PersonsDto> updatePerson(PersonsDto personsDto) {
        Optional<Persons> person = repositorPersons.getPersonsRepository().update(personUnMapper.getDestination(personsDto));
        if (person.isPresent()) {
            return Optional.of(personMapper.getDestination(person.get()));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Construct List all the people with the same names
     *
     * @param firstName
     * @param lastName
     * @return list person with sames names
     */
    public Optional<List<PersonsMedicationAdresseDTO>> getPersonInfo(String firstName, String lastName) {
        List<PersonsMedicationAdresseDTO> dtoList = new ArrayList<>();
        repositorPersons.getPersonsRepository().searchAllName(lastName, firstName)
                .ifPresent(lst ->
                {
                    lst.forEach(persons ->
                    {
                        dtoList.add(serviceFactory.getPersonsMedicationAdresseFactory().createPersonMedical(persons));
                    });
                });
        if (dtoList.isEmpty()) {
            return Optional.empty();
        }
        log.info("SERVICE : construct list same Name : {}", dtoList.size());
        return Optional.of(dtoList);
    }

    /**
     * Construct List the email addresses of all the inhabitants of the city
     *
     * @param city
     * @return List the email
     */
    public Optional<List<PersonsEmailDTO>> getPersonsEmail(String city) {
        List<PersonsEmailDTO> dtoList = new ArrayList<>();
        repositorPersons.getPersonsRepository()
                .searchEmailCity(city)
                .ifPresent(lst ->
                        lst.forEach(mail ->
                                dtoList.add(serviceFactory.getPersonEmailFactory().create(mail))));
        if (dtoList.isEmpty()) {
            return Optional.empty();
        }
        log.info("SERVICE : construct list all email by city : {}", dtoList.size());
        return Optional.of(dtoList);
    }

    public Optional<Persons> getFindPerson(String nom, String prenom) {
        log.info("Entre dans read Person by Elements " + nom + " " + prenom);
        return repositorPersons.getPersonsRepository().findByElements(nom, prenom);
    }

}