package powerUps;

import core.HernyObjekt;
import players.Postava;

import java.awt.Graphics;
import java.awt.Image;

/**
 * Abstraktná trieda PowerUp - predstavuje zberateľný objekt v hre.
 * Rozširuje triedu HernyObjekt a implementuje rozhranie Zberatelny.
 * PowerUp môže byť použitý hráčom (napr. Heal, Speed).
 *
 * @author Samuel Ďuriš
 * @version V3
 */
public abstract class PowerUp extends HernyObjekt implements Zberatelny {

    private boolean zobrany = false;

    /**
     * Konštruktor - inicializuje powerup s obrázkom a pozíciou.
     */
    public PowerUp(Image obrazok, int x, int y, int velkost) {
        super(obrazok, x, y, velkost, velkost);
    }

    /**
     * Abstraktná metóda - aplikuje efekt powerupuu na postavu.
     */
    @Override
    public abstract void pouzi(Postava postava);

    /**
     * Skontroluje, či bol powerup už zobraný hráčom.
     */
    @Override
    public boolean jeZobrany() {
        return this.zobrany;
    }

    @Override
    public void pohyb() {

    }

    /**
     * Označí powerup ako zobraný - signalizuje, že by mal byť odstránený z mapy.
     */
    @Override
    public void oznacZobrany() {
        this.zobrany = true;
    }

    @Override
    public boolean dotyk(HernyObjekt objekt) {
        return false;
    }

    /**
     * Vykreslí powerup na canvas.
     */
    @Override
    public void paint(Graphics g) {
        if (this.getObrazok() != null) {
            g.drawImage(this.getObrazok(), this.getX(), this.getY(), this.getSirka(), this.getVyska(), null);
        }
    }
}
