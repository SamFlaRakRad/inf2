package powerUps;

import core.HernyObjekt;
import players.Postava;

import java.awt.*;

public abstract class PowerUp extends HernyObjekt implements Zberatelny {

    protected boolean zobrany = false;

    public PowerUp(Image obrazok, int x, int y, int velkost) {
        super(obrazok, x, y, velkost, velkost);
    }

    /**
     * Abstraktná metóda - Heal a Speed implementujú inak.
     * Berie Postava (interface) nie Hrac (konkrétna trieda) -
     * vďaka tomu môže pickup zobrať hráč aj boss.
     */
    @Override
    public abstract void pouzi(Postava postava);

    @Override
    public boolean jeZobrany() { return this.zobrany; }

    @Override
    public void oznacZobrany() { this.zobrany = true; }

    @Override
    public boolean dotyk(HernyObjekt objekt) {
        return false;
    }

    @Override
    public void paint(Graphics g) {
        if (this.getObrazok() != null) {
            g.drawImage(this.getObrazok(), this.getX(), this.getY(), this.getSirka(), this.getVyska(), null);
        }
    }
}
