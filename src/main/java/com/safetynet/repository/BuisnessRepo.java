package com.safetynet.repository;

import java.util.List;
import java.util.Optional;

public interface BuisnessRepo<T> {

    public List<T> findAllInit();

    public List<T> findAll();

    public Optional<T> add(T objet);

    public boolean delete(T objet);

    public boolean exist(T objet);

    public Optional<T> update(T objet);

}
