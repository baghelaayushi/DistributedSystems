package helpers;

public class Site {
    private String ipAddress;
    private String uDPStartAddress;
    private String uDPEndAddress;

    public Site(String ipAddress, String uDPStartAddress, String uDPEndAddress){
        this.ipAddress = ipAddress;
        this.uDPStartAddress = uDPStartAddress;
        this.uDPEndAddress = uDPEndAddress;
    }

    public String getIpAddress(){
        return this.ipAddress;
    }
}
