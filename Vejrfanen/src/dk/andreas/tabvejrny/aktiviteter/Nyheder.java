package dk.andreas.tabvejrny.aktiviteter;

import dk.andreas.tabvejrny.dataklasser.Message;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import dk.andreas.tabvejrny.FeedParser;
import dk.andreas.tabvejrny.FeedParserFactory;
import dk.andreas.tabvejrny.dataklasser.ParserType;
import dk.andreas.tabvejrny.R;

public class Nyheder extends ListActivity {

    private List<Message> messages;
    private ArrayList<Message> nyheder = null;
    private NyhedsAdapter n_adapter;
    String TAG = "MessageList";
    private float x;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.nyheder);
        Log.i("Messagelist", "ContentView set");
        TextView tv = (TextView)findViewById(R.id.NyhederTextView);
        //tv.setText("Henter RSS");
        Log.i("Messagelist", "ViewFound");
        loadFeed(ParserType.XML_PULL);
        Log.i("Messagelist", "Slut");
    }

	@SuppressWarnings("unchecked")
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);
		ParserType type = ParserType.values()[item.getItemId()];
		ArrayAdapter<String> adapter =
			(ArrayAdapter<String>) this.getListAdapter();
		if (adapter.getCount() > 0){
			adapter.clear();
		}
		this.loadFeed(type);
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent viewMessage = new Intent(Intent.ACTION_VIEW,
				Uri.parse(messages.get(position).getLink().toExternalForm()));
		this.startActivity(viewMessage);
	}

	private void loadFeed(ParserType type){
    	try{
    		Log.i("AndroidNews", "ParserType="+type.name());
	    	FeedParser parser = FeedParserFactory.getParser(type);
	    	long start = System.currentTimeMillis();
	    	messages = parser.parse();
	    	long duration = System.currentTimeMillis() - start;
	    	Log.i("AndroidNews", "Parser duration=" + duration);
	    	String xml = writeXml();
	    	Log.i("AndroidNews", xml);
	    	List<String> titles = new ArrayList<String>(messages.size());

                nyheder = new ArrayList<Message>();

	    	for (Message msg : messages){
                        titles.add(msg.getMessage());
                        nyheder.add(msg);
	    	}

                this.n_adapter = new NyhedsAdapter(this, R.layout.row, nyheder);
                setListAdapter(this.n_adapter);

  	} catch (Throwable t){
    		Log.e("AndroidNews",t.getMessage(),t);
    	}
    }

	private String writeXml(){
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "messages");
			serializer.attribute("", "number", String.valueOf(messages.size()));
			for (Message msg: messages){
				serializer.startTag("", "message");
				serializer.attribute("", "date", msg.getDate());
				serializer.startTag("", "title");
				serializer.text(msg.getTitle());
				serializer.endTag("", "title");
				serializer.startTag("", "url");
				serializer.text(msg.getLink().toExternalForm());
				serializer.endTag("", "url");
				serializer.startTag("", "body");
				serializer.text(msg.getDescription());
				serializer.endTag("", "body");
				serializer.endTag("", "message");
			}
			serializer.endTag("", "messages");
			serializer.endDocument();
                        
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

    private class NyhedsAdapter extends ArrayAdapter<Message> {

            private ArrayList<Message> items;

            public NyhedsAdapter(Context context, int textViewResourceId, ArrayList<Message> items) {
                    super(context, textViewResourceId, items);
                    this.items = items;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                    View v = convertView;
                    if (v == null) {
                        LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        v = vi.inflate(R.layout.row, null);
                    }
                    Message o = items.get(position);
                    if (o != null) {
                            TextView tt = (TextView) v.findViewById(R.id.NyhederOverskrift);
                            TextView mt = (TextView) v.findViewById(R.id.NyhederDato);
                            TextView bt = (TextView) v.findViewById(R.id.NyhederBeskrivelse);
                            if (tt != null) {
                                  tt.setText(o.getTitle());
                                  Log.i("Message", "overskrift" + o.getTitle());
                            }
                            if (mt != null) {
                                  mt.setText(o.getDate().toString());
                                  Log.i("Message", "dato"+o.getDate().toString());
                            }
                            if(bt != null){
                                  bt.setText(o.getDescription());
                                  Log.i("Message", "beskrivelse"+o.getDescription());
                            }
                    }
                    return v;
            }
    }

    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }

    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

}
