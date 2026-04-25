package players;

import weapons.Strela;

import java.util.List;

public interface Postava {
    void vystrel();

    int getHP();
    int getMaxHP();
    void setHP(int hp);

    /**
     * Zoberie poškodenie.
     * Hrac.dostaZasah() - odráta HP.
     * Boss.dostaZasah() - odráta HP, pri prechode <50% sa zrýchli a zmení fázu útoku.
     */
    void dostanZasah(int poskodenie);

    boolean jeZiva();

    /**
     * Vykoná útok a pridá strely do zdieľaného zoznamu.
     * Hrac.utoc() - vystrelí 1 normálnu strelu podľa smerObr
     * Bot.utoc()  - vystrelí 1 strelu priamo k súperovi (override)
     * Boss.utoc() - volí medzi normal / spread / navádzacia raketa podľa fázy
     */
    void utoc(int cielX, int cielY, List<Strela> strely);

    /** True ak cooldown vypršal a postava môže strieľať */
    boolean mozeUtocit();


    int dealDmg();

    int getX();
    int getY();
}
