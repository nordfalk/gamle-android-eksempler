
package dk.andreas.tabvejrny.dataklasser;

public class Tab {

    private String tabNavn, tabKlasse;
    private int tabId, tabPrioritet;
    private boolean tabAktiv, tabPreLoad;

    public Tab(int ti, String tn, String tk, boolean ta, int tp, boolean tpl)
    {
        tabId = ti;
        tabNavn = tn;
        tabKlasse = tk;
        tabAktiv = ta;
        tabPrioritet = tp;
        tabPreLoad = tpl;
    }

    public void SætNavn(String tn)
    {
        tabNavn = tn;
    }

    public String LæsNavn()
    {
        return tabNavn;
    }

    public int LæsId(){
        return tabId;
    }

    public int LæsPrioritet(){
        return tabPrioritet;
    }

    public void SætPrioritet(int tp) {
        tabPrioritet = tp;
    }

    public String LæsKlasse() {
        return tabKlasse;
    }

    public boolean LæsAktiv() {
        return tabAktiv;
    }

    public boolean LæsPreLoad() {
        return tabPreLoad;
    }

    public void SætAktiv(boolean ta) {
        tabAktiv = ta;
    }
}
