package dk.nordfalk.aktivitetsliste;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.util.Linkify;
import android.text.util.Linkify.TransformFilter;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VisKildekode extends Activity {
  String filnavn;
  TextView tv;
  ScrollView sv;

  private static int onCreateTæller = 0;
  public final static String KILDEKODE_FILNAVN = "filen der skal vises";
  final static String LOKAL_PRÆFIX = "file:///android_asset/";

  // Subversion - gammel
  //static String HS_PRÆFIX = "http://code.google.com/p/android-eksempler/source/browse/trunk/AndroidElementer/";

  // GIT
  static String HS_PRÆFIX = "https://github.com/nordfalk/android-eksempler/tree/master/AndroidElementer/app/src/main/";

  static void find_HS_PRÆFIX(Context ctx) {
    try {
      Bundle metaData = ctx.getPackageManager().
              getActivityInfo(new ComponentName(ctx, VisKildekode.class), PackageManager.GET_META_DATA).metaData;
      HS_PRÆFIX = metaData.getString("HS_PRÆFIX");
    } catch (Exception ex) {
      ex.printStackTrace();
      Toast.makeText(ctx, "Kunne ikke læse HS_PRÆFIX fra manifestet. Eksterne henvisninger er muligvis forkerte", Toast.LENGTH_LONG).show();
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    find_HS_PRÆFIX(this);

    tv = new TextView(this);

    // Genskab foretrukken skriftstørrelse
    float skriftstørrelse = getPreferences(MODE_PRIVATE).getFloat("skriftstørrelse", 0);
    if (skriftstørrelse > 0) tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, skriftstørrelse);

    sv = new ScrollView(this);
    sv.addView(tv);
    setContentView(sv);

    // Genskab scrollpos
    if (savedInstanceState != null) {
      final int position = savedInstanceState.getInt("scrollpos");
      sv.post(new Runnable() {
        public void run() {
          sv.scrollTo(0, position);
        }
      });
    }


    Intent kaldtMedIntent = getIntent();
    if (kaldtMedIntent.getExtras() != null) {
      filnavn = kaldtMedIntent.getExtras().getString(KILDEKODE_FILNAVN);
    }

    if (filnavn == null) {
      tv.setText("Manglede ekstradata med filen der skal vises.\n"
              + "Du kan lave et 'langt tryk' på aktivitetslisten for at vise kildekoden til en aktivitet");
    } else {
      try {
        byte[] b = læsAssetFil(filnavn);
        String str = new String(b, "UTF-8");

        tv.setText(str);


        TransformFilter dokFilter = new TransformFilter() {
          public final String transformUrl(final Matcher match, String url) {
            String klassenavn = match.group(1);
            return "http://developer.android.com/reference/" + klassenavn.replace('.', '/') + ".html";
          }
        };
        Linkify.addLinks(tv, Pattern.compile("import (android.*?);"), null, null, dokFilter);
        Linkify.addLinks(tv, Pattern.compile("import (java.*?);"), null, null, dokFilter);


        TransformFilter andResFilter = new TransformFilter() {
          public final String transformUrl(final Matcher match, String url) {
            // http://android.git.kernel.org/?p=platform/frameworks/base.git;a=blob;f=core/res/res/layout/simple_list_item_1.xml"
            // http://android.git.kernel.org/?p=platform/frameworks/base.git;a=blob;f=core/res/res/" + match.group(1).replace('.', '/') + ".xml";

            //        https://github.com/android/platform_frameworks_base/blob/master/core/res/res/
            //return "https://github.com/android/platform_frameworks_base/blob/master/core/res/res/" + match.group(1).replace('.', '/') + ".xml";

            //        https://android.googlesource.com/platform/frameworks/base.git/+/master/core/res/res/
            //return "https://android.googlesource.com/platform/frameworks/base.git/+/master/core/res/res/" + match.group(1).replace('.', '/') + ".xml";

            //      https://github.com/android/platform_frameworks_base/blob/master/core/res/res/
            return "https://github.com/android/platform_frameworks_base/blob/master/core/res/res/" + match.group(1).replace('.', '/') + ".xml";
          }
        };
        Linkify.addLinks(tv, Pattern.compile("android.R.(layout.[a-z0-9_]+)"), null, null, andResFilter);
        Linkify.addLinks(tv, Pattern.compile("android.R.(xml.[a-z0-9_]+)"), null, null, andResFilter);
        Linkify.addLinks(tv, Pattern.compile("android.R.(raw.[a-z0-9_]+)"), null, null, andResFilter);
        Linkify.addLinks(tv, Pattern.compile("android.R.(drawable.[a-z0-9_]+)"), null, null, andResFilter);
        Linkify.addLinks(tv, Pattern.compile("android.R.(anim.[a-z0-9_]+)"), null, null, andResFilter);


        TransformFilter resFilter = new TransformFilter() {
          public final String transformUrl(final Matcher match, String url) {
            // LOKAL_PRÆFIX dur ikke her da vi starter et webbrowserintent
            return HS_PRÆFIX + "res/" + match.group(1).replace('.', '/') + ".xml";
          }
        };
        Linkify.addLinks(tv, Pattern.compile("R.(layout.[a-z0-9_]+)"), null, null, resFilter);
        Linkify.addLinks(tv, Pattern.compile("R.(xml.[a-z0-9_]+)"), null, null, resFilter);
        Linkify.addLinks(tv, Pattern.compile("R.(anim.[a-z0-9_]+)"), null, null, resFilter);

        //
        TransformFilter javaFilter = new TransformFilter() {
          public final String transformUrl(final Matcher match, String url) {
            // LOKAL_PRÆFIX dur ikke her da vi starter et webbrowserintent
            return HS_PRÆFIX + "java/" + match.group(1).replace('.', '/') + ".java";
          }
        };
        Linkify.addLinks(tv, Pattern.compile("import ([a-zA-Z0-9_\\.]+)"), null, null, javaFilter);
        //


        // Almindelig HTTP
        TransformFilter httpFlter = new TransformFilter() {
          public final String transformUrl(final Matcher match, String url) {
            return match.group(1);
          }
        };
        Linkify.addLinks(tv, Pattern.compile("(http[a-zA-Z0-9_/:\\.\\?\\&\\=\\-]+)"), null, null, httpFlter);

      } catch (FileNotFoundException ex) {
        Toast.makeText(this, "Filen mangler i assets/.\nViser " + filnavn + " fra nettet i stedet.", Toast.LENGTH_LONG).show();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(HS_PRÆFIX + filnavn)));
        finish(); // Afslut denne aktivitet
      } catch (IOException ex) {
        Logger.getLogger(VisKildekode.class.getName()).log(Level.SEVERE, null, ex);
        tv.setText("Kunne ikke åbne " + filnavn + ":\n" + ex);
      }
    }


    if (onCreateTæller++ == 2) {
      Toast.makeText(this, "Tryk MENU for andre visninger", Toast.LENGTH_LONG).show();
    }

  }

  private byte[] læsAssetFil(String filnavn) throws IOException {
    InputStream is = getAssets().open(filnavn);
    byte b[] = new byte[is.available()]; // kun små filer
    is.read(b);
    is.close();
    return b;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, 105, 0, "Skriftstørrelse...");
    menu.add(0, 100, 0, "Vis i WebView");
    menu.add(0, 102, 0, "Vælg fil");
    menu.add(0, 103, 0, "Vis i browser");
    menu.add(0, 104, 0, "Vis i andet...");
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == 100) {
      startActivity(new Intent(this, VisKildekodeIWebView.class).putExtra(KILDEKODE_FILNAVN, filnavn));
    } else if (item.getItemId() == 102) {
      vælgFil();
    } else if (item.getItemId() == 103) {
      startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(HS_PRÆFIX + filnavn)));
    } else if (item.getItemId() == 104) {
      try {
        byte[] b = læsAssetFil(filnavn);

        File tmp = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "tmp");
        tmp.mkdirs();
        File f = new File(tmp, new File(filnavn).getName());
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(b);
        fos.close();
        Intent i = new Intent(Intent.ACTION_VIEW);//, Uri.fromFile(f.getAbsoluteFile()));
        i.setDataAndType(Uri.fromFile(f), "application/text");
//				i.setDataAndType(Uri.fromFile(f), "text/plain");
        //startActivity(Intent.createChooser(i, filnavn));
        System.out.println("Gemmer fil i " + f);
        System.out.println("Starter " + i.toURI());
/*
                           Uri path = Uri.fromFile(file);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    try {
                        startActivity(intent);
                    }
	*/
        startActivity(i);
      } catch (Exception ex) {
        ex.printStackTrace();
        Toast.makeText(this, "Fejl : " + ex, Toast.LENGTH_LONG).show();
      }
    } else if (item.getItemId() == 105) {
      final EditText input = new EditText(this);
      input.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
      input.setText(String.valueOf((int) tv.getTextSize()));
      new AlertDialog.Builder(this).setTitle("Vælg skriftstørrelse").
              setView(input).
              setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                  float str = Float.parseFloat(input.getText().toString());
                  getPreferences(MODE_PRIVATE).edit().putFloat("skriftstørrelse", str).commit();
                  tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, str);
                }
              }).show();
    }

    return true;
  }

  private void vælgFil() {
    try {
      final String sti = filnavn.substring(0, filnavn.lastIndexOf("/"));
      ;
      final String[] filer = getAssets().list(sti);
      new AlertDialog.Builder(this).setTitle("Filer i " + sti).setItems(filer, new Dialog.OnClickListener() {
        public void onClick(DialogInterface arg0, int hvilken) {
          filnavn = sti + "/" + filer[hvilken];
          Toast.makeText(VisKildekode.this, "Viser " + filnavn, Toast.LENGTH_SHORT).show();
          startActivity(new Intent(VisKildekode.this, VisKildekode.class).putExtra(KILDEKODE_FILNAVN, filnavn));
        }
      }).show();
    } catch (IOException ex) {
      Logger.getLogger(VisKildekode.class.getName()).log(Level.SEVERE, null, ex);
    }

  }


  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt("scrollpos", sv.getScrollY());
  }
}
