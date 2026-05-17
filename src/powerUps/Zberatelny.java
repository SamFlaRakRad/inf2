package powerUps;

import players.Postava;

import java.awt.*;

/**
 * Interface Zberatelny - definuje rozhranie pre zberateľné objekty v hre.
 * Rozširujú ho powerupy (Heal, Speed) a iné objekty, ktoré môžu byť zbierané hráčom.
 *
 * @author Samuel Ďuriš
 * @version V3
 */
public interface Zberatelny {

    /**
     * Aplikuje efekt powerupu na postavu, ktorá ho zbrala.
     */
    void pouzi(Postava postava);

    /**
     * Skontroluje, či bol powerup už zbratý a mal by byť odstránený.
     */
    boolean jeZobrany();

    /**
     * Označí powerup ako zbratý - signalizuje, že by mal byť odstránený z mapy.
     */
    void oznacZobrany();

    /**
     *Vykresluje zberatelne itemi
     */
    void paint(Graphics g);
}
