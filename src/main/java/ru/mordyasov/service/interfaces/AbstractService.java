package ru.mordyasov.service.interfaces;

import java.util.List;
import java.util.Optional;

public interface AbstractService<T, ID> {
    Optional<T> find(ID id);
    void add(T t);
    void delete(T t);
    void update(T t);
    List<T> findAll();
}
