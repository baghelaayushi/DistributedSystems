package helpers;

public class Site {
    private String ipAddress;
    private int uDPStartAddress;
    private int uDPEndAddress;

    public Site(String ipAddress, String uDPStartAddress, String uDPEndAddress){
        this.ipAddress = ipAddress;
        this.uDPStartAddress = Integer.parseInt(uDPStartAddress);
        this.uDPEndAddress = Integer.parseInt(uDPEndAddress);
    }

    public String getIpAddress(){
        return this.ipAddress;
    }

    public int getRandomPort(){
        return (int) (Math.random() * (uDPEndAddress - uDPStartAddress)) + uDPStartAddress;
    }
}
