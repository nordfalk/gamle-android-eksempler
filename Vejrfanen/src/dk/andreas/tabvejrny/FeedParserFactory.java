package dk.andreas.tabvejrny;

import dk.andreas.tabvejrny.dataklasser.ParserType;

public abstract class FeedParserFactory {
//	static String feedUrl = "http://www.androidster.com/android_news.rss";
        static String feedUrl = "http://www.dmi.dk/dmi/rss-nyheder.xml";

	public static FeedParser getParser(){
		return getParser(ParserType.SAX);
	}

	public static FeedParser getParser(ParserType type){
		switch (type){
//			case SAX:
//				return new SaxFeedParser(feedUrl);
//			case DOM:
//				return new DomFeedParser(feedUrl);
//			case ANDROID_SAX:
//				return new AndroidSaxFeedParser(feedUrl);
			case XML_PULL:
				return new XmlPullFeedParser(feedUrl);
			default: return null;
		}
	}
}
