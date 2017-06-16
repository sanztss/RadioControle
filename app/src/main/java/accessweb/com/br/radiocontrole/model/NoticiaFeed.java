package accessweb.com.br.radiocontrole.model;

import java.io.Serializable;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Des. Android on 16/06/2017.
 */

@Root(name = "rss", strict = false)
public class NoticiaFeed implements Serializable {
    /*@Attribute
    private
    String version;*/

    @Element
    private
    NoticiaChannel channel;

    public NoticiaChannel getChannel() {
        return channel;
    }

   /* public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }*/

    public void NoticiaChannel(NoticiaChannel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "RSS{" +
                //"version='" + version + '\'' +
                ", channel=" + channel +
                '}';
    }
}
