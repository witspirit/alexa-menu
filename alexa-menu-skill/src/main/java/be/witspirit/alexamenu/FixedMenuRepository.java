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

        set("20170415", "Vogelnestjes");
        set("20170416", "Macaroni");
        set("20170417", "Pizza Hut / Koude rijst");
        set("20170418", "Biefstuk Frietjes");
        set("20170419", "Pitta");
        set("20170420", "Kippebillen met rijst en curry");
        set("20170421", "Spaghetti");
    }
}
