package lekt04_arkitektur;

import java.io.Serializable;
import java.util.ArrayList;

public class Programdata implements Serializable {
  // Vigtigt: Sæt versionsnummer så objekt kan læses selvom klassen er ændret!
  private static final long serialVersionUID = 12345; // bare et eller andet nr.

  public int alder = 0;
  public String navn = "(ukendt)";
  public ArrayList<String> noter = new ArrayList<String>();

  public String toString() {
    return navn + " på " + alder + " år med noter: " + noter;
  }
}