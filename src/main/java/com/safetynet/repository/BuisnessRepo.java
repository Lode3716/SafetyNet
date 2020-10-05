package com.safetynet.repository;

public interface BuisnessRepo {

    public void read(byte[] jsonData);

    public void add(byte[] jsonData);

    public void remove(byte[] jsonData);

}
