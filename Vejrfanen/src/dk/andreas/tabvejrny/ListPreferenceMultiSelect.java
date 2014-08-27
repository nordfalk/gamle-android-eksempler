package dk.andreas.tabvejrny;

import dk.andreas.tabvejrny.dataklasser.Tabs;
import dk.andreas.tabvejrny.dataklasser.Tab;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.util.AttributeSet;
import java.util.ArrayList;

public class ListPreferenceMultiSelect extends ListPreference {

    private static final String SEPARATOR = "OV=I=XseparatorX=I=VO";
    private boolean[] mClickedDialogEntryIndices;

    public ListPreferenceMultiSelect(Context context, AttributeSet attrs) {
        super(context, attrs);

//        mClickedDialogEntryIndices = new boolean[getEntries().length];
    }

    @Override
    public void setEntries(CharSequence[] entries) {
    	super.setEntries(entries);
        mClickedDialogEntryIndices = new boolean[entries.length];
    }

    public ListPreferenceMultiSelect(Context context) {
        this(context, null);
    }

    @Override
    protected void onPrepareDialogBuilder(Builder builder) {
    	CharSequence[] entries = getEntries();
    	CharSequence[] entryValues = getEntryValues();

        if (entries == null || entryValues == null || entries.length != entryValues.length ) {
            throw new IllegalStateException(
                    "ListPreference requires an entries array and an entryValues array which are both the same length");
        }

        restoreCheckedEntries();
        builder.setMultiChoiceItems(entries, mClickedDialogEntryIndices,
                new DialogInterface.OnMultiChoiceClickListener() {
					public void onClick(DialogInterface dialog, int which, boolean val) {
                    	mClickedDialogEntryIndices[which] = val;
					}
        });
    }

    public static String[] parseStoredValue(CharSequence val) {
		if ( "".equals(val) )
			return null;
		else
			return ((String)val).split(SEPARATOR);
    }

    private void restoreCheckedEntries() {
    	CharSequence[] entryValues = getEntryValues();

    	String[] vals = parseStoredValue(getValue());
    	if ( vals != null ) {
        	for ( int j=0; j<vals.length; j++ ) {
        		String val = vals[j].trim();
            	for ( int i=0; i<entryValues.length; i++ ) {
            		CharSequence entry = entryValues[i];
                	if ( entry.equals(val) ) {
            			mClickedDialogEntryIndices[i] = true;
            			break;
            		}
            	}
        	}
        }
        try{
        if(vals[0].equals("#ALL#")) {
            	for ( int i=0; i<entryValues.length; i++ ) {
                    mClickedDialogEntryIndices[i] = true;
            	}
        }
        } catch(Exception e) {
            mClickedDialogEntryIndices[0] = true;
        }

    }

	@Override
    protected void onDialogClosed(boolean positiveResult) {
        ArrayList<Tab> al = Tabs.LæsAl();

    	CharSequence[] entryValues = getEntryValues();
        if (positiveResult && entryValues != null) {
        	StringBuffer value = new StringBuffer();
        	for ( int i=0; i<entryValues.length; i++ ) {
                    Tab tab = al.get(i);
                    if ( mClickedDialogEntryIndices[i] ) {
        			value.append(entryValues[i]).append(SEPARATOR);
                                tab.SætAktiv(true);
        		}
                    else {
                        tab.SætAktiv(false);
                    }
        	}

            if (callChangeListener(value)) {
            	String val = value.toString();
            	if ( val.length() > 0 )
            		val = val.substring(0, val.length()-SEPARATOR.length());
            	setValue(val);
            }
                Database db = Database.hentInstans();
                db.OpdaterAktive(al);

        }
    }
}
