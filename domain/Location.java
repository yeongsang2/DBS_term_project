package domain;

public class Location {
    private String name;
    private String sector;
    private String floor;

    public Location(String name, String sector, String floor) {
        this.name = name;
        this.sector = sector;
        this.floor = floor;
    }

    public String getName() {
        return name;
    }

    public String getSector() {
        return sector;
    }

    public String getFloor() {
        return floor;
    }
}
