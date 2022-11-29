package domain;

import java.lang.annotation.Target;
import java.time.LocalDateTime;

public class Room {

    private int locationId;
    private int totalSeatNumber;
    private int useSeatNumber;
    private int availableSeatNUmber;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
}
