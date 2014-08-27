package dk.andreas.tabvejrny.dataklasser;

import android.graphics.Bitmap;
import java.util.Date;

public class Billede {

    public  String billedNavn, billedURL, XML;
    private Bitmap billedBillede;
    private Date billedDato;
    private int billedType, billedCacheTid;

    public Billede(String bn, String bu, int bt, int bc)
    {
        billedNavn = bn;
        billedURL = bu;
        billedType = bt;
        billedCacheTid = bc;
    }

    public void SætNavn(String bn)
    {
        billedNavn = bn;
    }

    public String LæsNavn()
    {
        return billedNavn;
    }

    public void SætURL(String bu)
    {
        billedURL = bu;
    }
    public String LæsURL(){
        return billedURL;
    }

    public Date LæsDato(){
        return billedDato;
    }

    public void SætDato(Date bd) {
        billedDato = bd;
    }

    public Bitmap LæsBillede() {
        return billedBillede;
    }

    public void SætBillede(Bitmap bb) {
        billedBillede = bb;
    }

    public String LæsXML() {
        return XML;
    }

    public void SætXML(String xml) {
        XML = xml;
    }

    public int LæsType() {
        return billedType;
    }

    public void SætType(int bt) {
        billedType = bt;
    }

    public int LæsCacheTid() {
        return billedCacheTid;
    }

    void frigiv() {
        billedBillede.recycle();
    }
}
