/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lekt10_livscyklus;

import lekt04_arkitektur.Programdata;

public class SingletonSimpel {
  public Programdata programdata = new Programdata();

  public static SingletonSimpel instans = new SingletonSimpel();
}
