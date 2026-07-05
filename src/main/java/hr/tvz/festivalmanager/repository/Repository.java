package hr.tvz.festivalmanager.repository;

import java.io.IOException;
import java.util.List;

public interface Repository<T> {
    void inputAllEntities() throws IOException;

    List<T> getAllEntities();
}
