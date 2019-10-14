package helpers;

import helpers.ClientInfo;

import java.sql.Timestamp;

public class Event {
    private String operationType;
    private ClientInfo operation;
    private Timestamp time;
    private int NodeId;

    public Event(String operationType,ClientInfo operation,int NodeId){
        this.operationType = operationType;
        this.operation = operation;
        this.NodeId = NodeId;
        this.time = new Timestamp(System.currentTimeMillis());
    }
}
