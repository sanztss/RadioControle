package accessweb.com.br.radiocontrole.model;


public class ChannelItem {
    public String title;
    public int icon;


    public ChannelItem() {

    }

    public ChannelItem(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

}
