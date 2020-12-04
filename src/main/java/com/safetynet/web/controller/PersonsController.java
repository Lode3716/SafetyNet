package com.safetynet.web.controller;

import com.safetynet.dto.PersonsDto;
import com.safetynet.dto.PersonsEmailDTO;
import com.safetynet.dto.PersonsMedicationAdresseDTO;
import com.safetynet.model.Persons;
import com.safetynet.service.IPersonsService;
import com.safetynet.service.PersonsService;
import com.safetynet.web.exceptions.BadArgumentsException;
import com.safetynet.web.exceptions.PersonsNotFoundException;
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
    IPersonsService personsService;

    @GetMapping(value = "persons")
    public List<PersonsDto> readAllpersons() {
        log.info("GET list all persons.");
        return personsService.getReadAllpersons();
    }

    @GetMapping(value = "person/{nom}/{prenom}")
    public Persons readPerson(@PathVariable("nom") String nom, @PathVariable("prenom") String prenom) {
        log.info("Get search person by Elements : {}, {}", nom, prenom);
        return personsService.getFindPerson(nom, prenom)
                .orElseThrow(() -> new PersonsNotFoundException("La personne se nommant " + nom + " " + prenom + "est introuvable."));
    }

    /**
     * New Person Add
     *
     * @param person to save
     * @return ResponseEntity<PersonsDto> person return when is create,if arguments to search is not good : BadArgumentsException
     */
    @PostMapping(value = "person")
    public ResponseEntity<PersonsDto> addPersons(@RequestBody PersonsDto person) {
        log.debug("POST:  create person : {}", person);

        if (person.getLastName().isBlank() || person.getFirstName().isBlank()) {
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

    /**
     * Person to delete
     *
     * @param person to delete
     * @return ResponseEntity<void> if exist delete person else PersonsNotFoundException, if arguments to search is not good : BadArgumentsException
     */
    @DeleteMapping(value = "person")
    public ResponseEntity<Void> deletePerson(@RequestBody PersonsDto person) {
        log.debug("DELETE : person {}", person);
        if (person.getLastName().isBlank() || person.getFirstName().isBlank()) {
            log.error("DELETE : firstName and lastName are not null for create personDTO {}");
            throw new BadArgumentsException("DELETE : firstName and lastName are not null for create personDTO");
        }
        Boolean retour = personsService.deletePerson(person);
        if (!retour) {
            log.error("Delete person not found : {}");
            throw new PersonsNotFoundException("The person with the name " + person.getLastName() + " could not be found.");
        }
        log.info("DELETE person : SUCCESS");
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * Person to update information
     *
     * @param person to update
     * @return ResponseEntity<PersonsDto> if exist update person else PersonsNotFoundException, if arguments to search is not good : BadArgumentsException
     */
    @PutMapping(value = "person")
    public ResponseEntity<PersonsDto> updatePerson(@RequestBody PersonsDto person) {
        log.debug("PUT update person : {}", person);
        if (person.getLastName().isBlank() || person.getFirstName().isBlank()) {
            log.error("UPDATE : firstName and lastName are not null for create personDTO");
            throw new BadArgumentsException("UPDATE : firstName and lastName are not null for create personDTO");
        }
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(personsService.updatePerson(person)
                .orElseThrow(PersonsNotFoundException::new));
    }


    /**
     * List all the people with the same names
     *
     * @param firstName
     * @param lastName
     * @return List PersonsMedicationAdresseDTO else PersonsNotFoundException, if arguments to search is not good : BadArgumentsException
     */
    @GetMapping(value = "personInfo")
    public ResponseEntity<List<PersonsMedicationAdresseDTO>> personInfo(@RequestParam String firstName, @RequestParam String lastName) {
        log.debug("GET list info person by name : {}, {} ", firstName, lastName);
        if (firstName.isBlank() || lastName.isBlank()) {
            log.error("UPDATE : firstName and lastName are not null for create personDTO");
            throw new BadArgumentsException("GET : firstName and lastName are not null");
        }

        return ResponseEntity.status(HttpStatus.OK.value())
                .body(personsService.getPersonInfo(firstName, lastName)
                        .orElseThrow(PersonsNotFoundException::new));
    }

    /**
     * List the email addresses of all the inhabitants of the city.
     * @param city
     * @return List email
     */
    @GetMapping(value = "communityEmail")
    public ResponseEntity<List<PersonsEmailDTO>> personsEmail(@RequestParam String city) {
        log.debug("GET search all email by city : " + city);
        if (city.isBlank()) {
            log.error("GET : city not null");
            throw new BadArgumentsException("GET : city not null");
        }

        return ResponseEntity.status(HttpStatus.OK.value())
                .body(personsService.getPersonsEmail(city)
                        .orElseThrow(PersonsNotFoundException::new));
    }
}
