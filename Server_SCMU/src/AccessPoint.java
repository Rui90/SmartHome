public class AccessPoint {

    private String scanResult;
    private double distance;

    public AccessPoint(String scanR, double distance){
        this.scanResult = scanR;
        this.distance = distance;
    }

    public String getScanResult(){
        return scanResult;
    }

    public double getDistance(){
        return distance;
    }

    public void setScanResult(String scan){
        scanResult = scan;
    }

    public void setDistance(double dist){
        distance = dist;
    }
}
