package helpers;

import helpers.ClientInfo;

import java.sql.Timestamp;

public class Event {

    private String operationType;
    private ClientInfo operation;
    private int time;
    private int NodeId;

    public Event(String operationType,ClientInfo operation,int NodeId, int time){
        this.operationType = operationType;
        this.operation = operation;
        this.NodeId = NodeId;
        this.time = time;
    }

    public String getOperationType(){
        return this.operationType;
    }

    public ClientInfo getOperation() {
        return this.operation;
    }

    public int getTime() {

        return this.time;
    }

    public int getNodeId() {
        return this.NodeId;
    }

}
