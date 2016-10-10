package lekt04_arkitektur;

import java.io.Serializable;
import java.util.ArrayList;

public class Programdata implements Serializable {
  // Sæt versionsnummer så objekt kan læses selvom klassen er ændret
  private static final long serialVersionUID = 12345; // bare et eller andet nummer

  public int alder = 0;
  public String navn = "(ukendt)";
  public ArrayList<String> noter = new ArrayList<>();
  public transient ArrayList<Runnable> observatører = new ArrayList<>();

  public String toString() {
    return navn + " på " + alder + " år med noter: " + noter;
  }

  public void kaldObservatører() {
    for (Runnable r : observatører) r.run();
    // Hvis metoden bliver kaldt fra baggrundstråde så brug i stedet
    //for (Runnable r : observatører) MinApp.forgrundstråd.post(r);
  }
}