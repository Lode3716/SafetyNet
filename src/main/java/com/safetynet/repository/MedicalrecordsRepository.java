package com.safetynet.repository;

import com.safetynet.dao.Database;
import com.safetynet.model.Medicalrecords;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Log4j2
@Repository
public class MedicalRecordsRepository implements BuisnessRepo<Medicalrecords> {

    @Autowired
    Database database;


    @Override
    public List<Medicalrecords> findAll() {
        return database.getMedicalrecordsList();
    }

    @Override
    public Optional<Medicalrecords> add(Medicalrecords medicalrecords) {
        if (exist(medicalrecords) == Boolean.FALSE) {
            database.getMedicalrecordsList().add(medicalrecords);
        }
        return Optional.of(medicalrecords);
    }

    @Override
    public boolean delete(Medicalrecords medicalrecords) {
        return false;
    }

    @Override
    public boolean exist(Medicalrecords medicalrecords) {
        return database.getMedicalrecordsList()
                .stream()
                .anyMatch(search -> medicalrecords.getLastName().equals(search.getLastName()) && medicalrecords.getFirstName().equals(search.getFirstName()));
    }


    @Override
    public Optional<Medicalrecords> update(Medicalrecords medicalrecords) {
        if (exist(medicalrecords)) {
            database.getMedicalrecordsList()
                    .stream()
                    .filter(search -> medicalrecords.getLastName().equals(search.getLastName()) && medicalrecords.getFirstName().equals(search.getFirstName()))
                    .findFirst()
                    .ifPresent(maj ->
                    {
                        maj.setBirthdate(medicalrecords.getBirthdate());
                        Optional.ofNullable(maj.getAllergies())
                                .ifPresentOrElse(allergie->
                                        {
                                            if(allergie.size()>0 && medicalrecords.getAllergies().size()>0) {
                                                log.info("Passe 1 "+allergie.size());
                                                Optional.ofNullable(compareListe(allergie, medicalrecords.getAllergies()))
                                                        .ifPresent(s -> maj.getAllergies().add(s));
                                            }else{
                                                log.info("Passe "+allergie.size());
                                                maj.setAllergies(medicalrecords.getAllergies());
                                            }

                                        },()-> maj.setAllergies(medicalrecords.getAllergies())
                                        );

                        Optional.ofNullable(maj.getMedications())
                                .ifPresentOrElse(medicaments->
                                        {
                                            Optional.ofNullable(compareListe(medicaments,medicalrecords.getMedications()))
                                                    .ifPresent(s->maj.getMedications().add(s));

                                        },()-> maj.setMedications(medicalrecords.getMedications())
                                );

                    });
            return Optional.of(medicalrecords);
        }
        return Optional.empty();
    }

    private String compareListe(List<String> oldList,List<String> newList)
    {
        AtomicReference<String> retour=new AtomicReference<>();
        oldList.stream().forEach(allOld->
        {
            newList.stream()
                    .filter(allNew-> !allNew.equals(allOld))
                    .limit(1)
                    .forEach(allNew->retour.set(allNew));
        });
        return retour.get();
    }
}
