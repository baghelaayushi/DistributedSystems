package helpers;

import java.util.List;

public class smallMessage extends Message{
    private  int[] matrixRow;
    public smallMessage(List<Event> logD, int[] matrixRowD, int siteId){
        this.siteId = siteId;
        log = logD;
        this.matrixRow = matrixRowD;
        marker = false;
    }
}

