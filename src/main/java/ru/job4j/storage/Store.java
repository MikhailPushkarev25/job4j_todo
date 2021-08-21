package ru.job4j.storage;

import ru.job4j.model.Items;

import java.util.List;

public interface Store {

    Items add(Items items);

    void replace(Items items);

    void update(Items items);

    List<Items> findAll();

    Items findById(int id);
}
