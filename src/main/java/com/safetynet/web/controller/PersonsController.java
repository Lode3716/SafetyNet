package com.safetynet.web.controller;

import com.safetynet.dto.PersonsDto;
import com.safetynet.dto.PersonsEmailDTO;
import com.safetynet.dto.PersonsMedicationAdresseDTO;
import com.safetynet.model.Persons;
import com.safetynet.service.PersonsService;
import com.safetynet.web.exceptions.BadArgumentsException;
import com.safetynet.web.exceptions.PersonsIntrouvableException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
@RestController
public class PersonsController {

    @Autowired
    PersonsService personsService;

    @GetMapping(value = "persons")
    public List<PersonsDto> readAllpersons() {
        log.info("GET list all persons.");
        return personsService.getReadAllpersons();
    }

    @GetMapping(value = "person/{nom}/{prenom}")
    public Persons readPerson(@PathVariable("nom") String nom, @PathVariable("prenom") String prenom) {
        log.info("Get search person by Elements : {}, {}", nom, prenom);
        return personsService.getFindPerson(nom, prenom)
                .orElseThrow(() -> new PersonsIntrouvableException("La personne se nommant " + nom + " " + prenom + "est introuvable."));
    }

    /**
     * Person to add
     * @param person to save
     * @return ResponseEntity<PersonsDto> if exist else throw BadArgumentsException
     */
    @PostMapping(value = "person")
    public ResponseEntity<PersonsDto> addPersons(@RequestBody PersonsDto person) {
        log.debug("POST:  create person : {}", person);

        if(person.getLastName().isBlank() || person.getFirstName().isBlank())
        {
            throw new BadArgumentsException("POST : firstName and lastName are not null for create personDTO");
        }
        AtomicReference<ResponseEntity> rep = new AtomicReference<>();
        personsService.addPersons(person)
                .ifPresent(retour ->
                {
                    log.info("POST: create person - SUCCESS");
                    rep.set(ResponseEntity.status(HttpStatus.CREATED).body(retour));
                });

        return rep.get();
    }

    @DeleteMapping(value = "person")
    public ResponseEntity<Void> deletePerson(@RequestBody PersonsDto person) {
        log.info("DELETE person : {}", person);
        Boolean retour = personsService.deletePerson(person);
        if (!retour) {
            log.error("Delete person not found : {}", person);
            throw new PersonsIntrouvableException("The person with the name " + person.getLastName() + " could not be found.");
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping(value = "person")
    public PersonsDto updatePerson(@RequestBody PersonsDto person) {
        log.info("PUT update person : {}", person);
        return personsService.updatePerson(person)
                .orElseThrow(PersonsIntrouvableException::new);
    }


    @GetMapping(value = "personInfo")
    public List<PersonsMedicationAdresseDTO> personInfo(@RequestParam String firstName, @RequestParam String lastName) {
        log.info("GET list info person by name : {}, {} ", firstName, lastName);
        List<PersonsMedicationAdresseDTO> dtoList = new ArrayList<>();
        Optional.ofNullable(personsService.getPersonInfo(firstName, lastName))
                .ifPresent(lst ->
                {
                    dtoList.addAll(lst);
                });
        if (dtoList.isEmpty()) {
            log.error("GET list info person by name and firstname not found : {}, {} ", firstName, lastName);
            throw new PersonsIntrouvableException("La personne se nommant " + firstName + " " + firstName + "est introuvable.");
        }
        log.debug("GET list info person by name : {}", dtoList.size());
        return dtoList;
    }

    @GetMapping(value = "communityEmail")
    public List<PersonsEmailDTO> personsEmail(@RequestParam String city) {
        log.info("GET search all email by city : " + city);
        List<PersonsEmailDTO> dtoList = new ArrayList<>();
        Optional.ofNullable(personsService.getPersonsEmail(city))
                .ifPresent(mail -> dtoList.addAll(mail));

        if (dtoList.isEmpty()) {
            log.error("GET search all email by city, it null {}", dtoList.size());
            throw new PersonsIntrouvableException("La ville " + city + "ne dispose pas d'email de la population. ");
        }
        log.debug("GET search all email by city : {}", dtoList.size());
        return dtoList;
    }
}
