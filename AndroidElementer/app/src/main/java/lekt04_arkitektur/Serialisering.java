package lekt04_arkitektur;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Serialisering {

  public static void gem(Serializable obj, String filnavn) throws IOException {
    FileOutputStream datastrøm = new FileOutputStream(filnavn);
    ObjectOutputStream objektstrøm = new ObjectOutputStream(datastrøm);
    objektstrøm.writeObject(obj);
    objektstrøm.close();
  }

  public static Serializable hent(String filnavn) throws Exception {
    FileInputStream datastrøm = new FileInputStream(filnavn);
    ObjectInputStream objektstrøm = new ObjectInputStream(datastrøm);
    Object obj = objektstrøm.readObject();
    objektstrøm.close();
    return (Serializable) obj;
  }
}