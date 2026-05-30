package hr.tvz.festivalmanager.entities;

import java.time.LocalDateTime;

public interface Schedulable {
    LocalDateTime getStart();

    LocalDateTime getEnd();
}
