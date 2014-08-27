package dk.andreas.tabvejrny.dataklasser;

import android.util.Log;

public class BilledeOpdateret {

    String TAG="BilledeOpdateret";

    public interface OnImageAddedListener {
        public abstract void onImageAdded(Billede b);
        public void onCouldNotDownload(Billede tmpbillede);
    }

    // Listeners
    protected OnImageAddedListener mOnImageAddedListener;

    public void OnImageAdded(Billede b) {
        // Only call the listener if there is one set:
        Log.d(TAG,"OnImgeAdded 1" + b.LæsNavn());
        if (this.mOnImageAddedListener != null) {
            this.mOnImageAddedListener.onImageAdded(b);
            Log.d(TAG,"OnImgeAdded 3");
        }
    }

    public void BilledHåndtering(Billede b) {
        Log.d(TAG, "BilledHåndtering " + b.LæsNavn());
        OnImageAdded(b);

    }
}
