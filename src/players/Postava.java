package players;

import weapons.Strela;

import java.util.ArrayList;

/**
 * Interface reprezentujúci hernú postavu.
 * Definuje základné operácie pre všetky postavy: pohyb, strelbom správu, zdravia a kolízií.
 * Implementujú ho triedy Hrac, Bot a Boss.
 *
 * @author Samuel Ďuriš
 * @version V3
 */
public interface Postava {

    /**
     * Vykonáva pohyb postavy - aktualizuje pohybové vektory na základe vstupu alebo AI.
     */
    void pohybSa();

    /**
     * Getter pre pohybový vektor v smere X.
     */
    int getPohybX();

    /**
     * Getter pre pohybový vektor v smere Y.
     */
    int getPohybY();

    /**
     * Signalizuje zámer výstrelu.
     */
    void vystrel();

    /**
     * Skontroluje, či postava môže nyní strieľať (cooldown skončil).
     */
    boolean mozeUtocit();

    /**
     * Resetuje cooldown strelbou - postava bude opäť schopná strieľať.
     */
    void resetCooldown();

    /**
     * Vykonáva útok na daný cieľ - vytvorí streľu smerujúcu na cieľ.
     */
    void utoc(int cielX, int cielY, ArrayList<Strela> strely);

    /**
     * Getter pre zdravie postavi.
     */
    int getHP();

    /**
     * Getter pre maximálne zdravie postavi.
     */
    int getMaxHP();

    /**
     * Setter pre zdravie postavi.
     */
    void setHP(int hp);

    /**
     * Postava dostane poškodenie.
     */
    void dostanZasah(int poskodenie);

    /**
     * Skontroluje, či je postava živá.
     */
    boolean jeZiva();

    /**
     * Getter pre x-ovú súradnicu postavi.
     */
    int getX();

    /**
     * Getter pre y-ovú súradnicu postavi.
     */
    int getY();

    /**
     * Getter pre smer orientácie postavi ('L' = vľavo, 'R' = vpravo)
     */
    char getSmerObr();

    /**
     * Aktivuje booster pick-up u hráča
     */
    void activateSpeedBoost(int tiky);
}
