package com.safetynet.dto.factory;

import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ServiceFactoryImpl implements ServiceFactory {

    @Bean
    @Override
    public PersonsMedicalsFactory getPersonsMedicalsFactory() {
        return new PersonsMedicalsFactory();
    }

    @Bean
    @Override
    public ChildStationFactory getChildStationFactory() {
        return new ChildStationFactory();
    }

    @Bean
    @Override
    public PersonalBelongFirestationFactory getPersonalBelongFirestationFactory() {
        return new PersonalBelongFirestationFactory();
    }

    @Bean
    @Override
    public PersonsFirestationFactory getPersonsFirestationFactory() {
        return new PersonsFirestationFactory();
    }

    @Bean
    @Override
    public PersonShortFactory getPersonShortFactory() {
        return new PersonShortFactory();
    }

    @Bean
    @Override
    public PersonsMedicalsStationFactory getPersonsMedicalsStationFactory() {
        return new PersonsMedicalsStationFactory();
    }

    @Bean
    @Override
    public PersonsPhoneFactory getPersonsPhoneFactory() {
        return new PersonsPhoneFactory();
    }

    @Bean
    @Override
    public PersonsMedicationAdresseFactory getPersonsMedicationAdresseFactory() {
        return new PersonsMedicationAdresseFactory();
    }

    @Bean
    @Override
    public PersonEmailFactory getPersonEmailFactory() {
        return  new PersonEmailFactory();
    }
}