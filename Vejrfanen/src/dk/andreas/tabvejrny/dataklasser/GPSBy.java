package dk.andreas.tabvejrny.dataklasser;

public class GPSBy {

    private String postnr, bynavn;
    private double lat, lon, afstand;

    public GPSBy(String pn, String bn, double lt, double ln)
    {
        postnr = pn;
        bynavn = bn;
        lat=lt;
        lon=ln;
    }

    public void SætAfstand(double af)
    {
        afstand = af;
    }

    public double LæsAfstand()
    {
        return afstand;
    }

    public Double Læslat(){
        return lat;
    }

    public Double Læslon(){
        return lon;
    }

    public String LæsBynavn() {
        return bynavn;
    }

    public String LæsPostnr() {
        return postnr;
    }
}
