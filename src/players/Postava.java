package players;

import weapons.Strela;

import java.util.List;

public interface Postava {
    void pohybSa();

    int getPohybX();
    int getPohybY();

    void vystrel();
    boolean mozeUtocit();
    void resetCooldown();

    void utoc(int cielX, int cielY, List<Strela> strely);

    int getHP();
    int getMaxHP();
    void setHP(int hp);

    void dostanZasah(int poskodenie);
    void dostaZasah(int poskodenie);

    boolean jeZiva();

    int getRychlost();
    int dealDmg();

    int getX();
    int getY();
}
