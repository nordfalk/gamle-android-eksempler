package dk.andreas.tabvejrny;
import dk.andreas.tabvejrny.dataklasser.Message;
import java.util.List;

public interface FeedParser {
    List<Message> parse();
}
