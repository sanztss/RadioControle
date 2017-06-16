package accessweb.com.br.radiocontrole.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Des. Android on 16/06/2017.
 */

@Root(strict = false)
public class NoticiaChannel implements Serializable {

    @ElementList(name = "item", required = true, inline = true)
    private List<Noticia> itemList;

    public NoticiaChannel(List<Noticia> mFeedItems) {
        this.itemList = mFeedItems;
    }

    public NoticiaChannel() {
    }

    public List<Noticia> getItemList() {
        return itemList;
    }

    public void setItemList(List<Noticia> itemList) {
        this.itemList = itemList;
    }
}
