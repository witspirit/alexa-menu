package be.witspirit.alexamenu;

/**
 * Hardcoded version of the Menu
 */
public class FixedMenuRepository extends MapMenuRepository {

    public FixedMenuRepository() {
        currentWeekMenu();
    }

    private void currentWeekMenu() {
        set("20170410", "Macaroni");
        set("20170411", "Toscaanse Kip");
        set("20170412", "Pannenkoeken");
        set("20170413", "Diepvriespizza");
        set("20170414", "Spaghetti");
    }
}
