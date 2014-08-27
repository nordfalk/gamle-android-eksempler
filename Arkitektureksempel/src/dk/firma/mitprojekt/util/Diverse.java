/**
DR Radio 2 is developed by Jacob Nordfalk, Hanafi Mughrabi and Frederik Aagaard.
Some parts of the code are loosely based on Sveriges Radio Play for Android.

DR Radio 2 for Android is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 2 as published by
the Free Software Foundation.

DR Radio 2 for Android is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
DR Radio 2 for Android.  If not, see <http://www.gnu.org/licenses/>.

*/

package dk.firma.mitprojekt.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings.Secure;
import dk.firma.mitprojekt.MinApp;

/**
 *
 * @author j
 */
public class Diverse {



  public static void kontakt(Activity akt, String emne, String txt) {

    String[] modtagere = null;
    try {
      modtagere = MinApp.stamdata.json.getString("kontakt_modtagere").split(",");
    } catch (Exception ex) {
      Log.e("JSONParsning - fallback", ex);
      modtagere = new String[] { "jacob.nordfalk@gmail.com"};
    }

    Intent i = new Intent(android.content.Intent.ACTION_SEND);
    i.setType("plain/text");
    i.putExtra(android.content.Intent.EXTRA_EMAIL, modtagere);
    i.putExtra(android.content.Intent.EXTRA_SUBJECT, emne);
    i.putExtra(android.content.Intent.EXTRA_TEXT, txt);
    akt.startActivity(Intent.createChooser(i, "Send feedback..."));
  }


  public static String lavTelefoninfo(Activity a) {

    PackageManager pm = a.getPackageManager();
		String version = lavAppVersion(a);

    String ret ="Program: "+a.getPackageName()+" version "+version
        +"\nTelefonmodel: "+Build.MODEL +" "+Build.PRODUCT
        +"\nAndroid v"+Build.VERSION.RELEASE
        +"\nsdk: "+Build.VERSION.SDK
        +"\nAndroid_ID: "+Secure.getString(a.getContentResolver(), Secure.ANDROID_ID);

    return ret;
  }

	public static String lavAppVersion(Context a) {
		String version="(ukendt)";
		PackageManager pm = a.getPackageManager();
		try {
			PackageInfo pi=pm.getPackageInfo(a.getPackageName(), 0);
			version=pi.versionName;
		} catch (Exception e) {
			version=e.toString();
			e.printStackTrace();
		}
		return version;
	}

}
