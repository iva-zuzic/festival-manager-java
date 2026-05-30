package hr.tvz.festivalmanager.repository;

import java.util.List;

public interface Repository<T> {
    void inputAllEntities();

    List<T> getAllEntities();
}
