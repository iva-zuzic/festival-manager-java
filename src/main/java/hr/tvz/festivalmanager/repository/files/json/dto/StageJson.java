package hr.tvz.festivalmanager.repository.files.json.dto;

public class StageJson {

    private String name;
    private int capacity;

    public StageJson() {
    }

    public StageJson(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
