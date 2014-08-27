package dk.andreas.tabvejrny.dataklasser;

import android.provider.BaseColumns;

public interface Constants extends BaseColumns {
    public static final String DATABASE_NAME = "tabs_db";
    public static final String TABLE_NAME_DEFAULT = "tabs_default";

    public static final String ID = "id";
    public static final String TAB_NAVN = "tab_navn";
    public static final String TAB_KLASSE = "tab_klasse";
    public static final String TAB_AKTIV = "tab_aktiv";
    public static final String TAB_PRIORITET = "tab_prioritet";
    public static final String TAB_PRELOAD = "tab_preload";

    public static final String BILLEDDIRECTORY ="data/data/dk.andreas.tabvejrny/billeder";
    public static final String XMLDIRECTORY ="data/data/dk.andreas.tabvejrny/xml";
    public static final int ANTALRADARBILLEDER = 10;
}