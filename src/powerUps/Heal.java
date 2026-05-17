package powerUps;

import players.Postava;

import java.awt.Graphics;
import java.awt.Image;

/**
 * Trieda Heal - powerup, ktorý zvyšuje zdravie postavy.
 *
 * @author Samuel Ďuriš
 * @version V3
 */
public class Heal extends PowerUp {
    private int healKoef;

    /**
     * Konštruktor - inicializuje Heal powerup.
     */
    public Heal(Image obrazok, int x, int y, int velkost, int healKoef) {
        super(obrazok, x, y, velkost);
        this.healKoef = healKoef;
    }

    /**
     * Aplikuje efekt healu - zvýši zdravie postavy.
     */
    @Override
    public void pouzi(Postava postava) {
        postava.setHP(postava.getHP() + this.healKoef);
        this.oznacZobrany();
    }

    /**
     * Vykreslí heal powerup.
     */
    @Override
    public void paint(Graphics g) {
        if (this.getObrazok() != null) {
            g.drawImage(this.getObrazok(), this.getX(), this.getY(), this.getSirka(), this.getVyska(), null);
        }
    }
}
