package dk.nordfalk.aktivitetsliste;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by j on 07-08-15.
 */
public class Aktivitetsdata {
  public static final Aktivitetsdata instans = new Aktivitetsdata();
  /**
   * Programdata - static da de ikke fylder det store og vi dermed slipper for reinitialisering
   */
  ArrayList<String> alleAktiviteter = new ArrayList<String>();
  ArrayList<String> pakkenavne;
  ArrayList<String> pakkekategorier;
  ArrayList<ArrayList<String>> klasselister = new ArrayList<ArrayList<String>>();
  HashSet<Integer> manglerTjekForAndreFiler = new HashSet<Integer>();
  private Application app;


  public void init(Application application) {
    if (app != null) return;
    app = application;
  }
}
