package powerUps;

import core.HernyObjekt;
import players.Postava;

import java.awt.Graphics;
import java.awt.Image;

public abstract class PowerUp extends HernyObjekt implements Zberatelny {

    private boolean zobrany = false;

    public PowerUp(Image obrazok, int x, int y, int velkost) {
        super(obrazok, x, y, velkost, velkost);
    }

    @Override
    public abstract void pouzi(Postava postava);

    @Override
    public boolean jeZobrany() {
        return this.zobrany;
    }

    @Override
    public void pohyb() {

    }

    @Override
    public void oznacZobrany() {
        this.zobrany = true;
    }

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
