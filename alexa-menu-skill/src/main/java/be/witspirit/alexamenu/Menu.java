package be.witspirit.alexamenu;

/**
 * Menu representation as provided by the MenuStore API
 */
public class Menu {
    private String date;
    private String menu;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }
}
