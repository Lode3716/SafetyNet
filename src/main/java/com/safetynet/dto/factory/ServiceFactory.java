package com.safetynet.dto.factory;

public interface ServiceFactory {

    PersonsMedicalsFactory getPersonsMedicalsFactory();

    ChildStationFactory getChildStationFactory();

    PersonalBelongFirestationFactory getPersonalBelongFirestationFactory();

    PersonsFirestationFactory getPersonsFirestationFactory();

    PersonShortFactory getPersonShortFactory();

    PersonsMedicalsStationFactory getPersonsMedicalsStationFactory();

    PersonsPhoneFactory getPersonsPhoneFactory();

}
