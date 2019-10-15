package helpers;

import java.util.ArrayList;
import java.util.List;

public class Message {
    private static List<Event> log = new ArrayList<>();
    private static int[][] matrixClock;

    public Message(List<Event> log, int[][] matrixClock){
        this.log = log;
        this.matrixClock = matrixClock;
    }
}
