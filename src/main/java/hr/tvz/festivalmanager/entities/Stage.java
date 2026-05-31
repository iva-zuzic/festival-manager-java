package hr.tvz.festivalmanager.entities;

import java.io.Serializable;

public record Stage(String name, int capacity) implements Serializable {
}
