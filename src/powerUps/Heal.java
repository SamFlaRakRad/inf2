package powerUps;

import core.HernyObjekt;
import players.Postava;

import java.awt.*;

public class Heal extends PowerUp {
    public Heal(Image obrazok, int x, int y, int velkost) {
        super(obrazok, x, y, velkost);
    }

    @Override
    public void pouzi(Postava postava) {
        postava.setHP(postava.getHP() + 1);
        this.oznacZobrany();
    }

    @Override
    public void paint(Graphics g) {
        if (this.getObrazok() != null) g.drawImage(this.getObrazok(), this.getX(), this.getY(), this.getSirka(), this.getVyska(), null);
    }
}
