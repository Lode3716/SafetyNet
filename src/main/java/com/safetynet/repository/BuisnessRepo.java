package com.safetynet.repository;

import java.util.List;
import java.util.Optional;

public interface BuisnessRepo<T> {

    public List<T> findAllInit();

    public List<T> findAll();

    public Optional<T> add(T objet);

    public void remove(T objet);

}
