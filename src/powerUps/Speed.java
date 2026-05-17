package powerUps;

import players.Postava;

import java.awt.Graphics;
import java.awt.Image;

/**
 * Trieda Speed - powerup, ktorý zvyšuje rýchlosť hráča na čas.
 * Keď hráč zberie Speed powerup, jeho útok sa zrýchli na určené trvanie.
 *
 * @author Samuel Ďuriš
 * @version V3
 */
public class Speed extends PowerUp {

    private final int trvanie;

    /**
     * Konštruktor - inicializuje Speed powerup.
     */
    public Speed(Image obrazok, int x, int y, int velkost, int trvanie) {
        super(obrazok, x, y, velkost);
        this.trvanie = trvanie;
    }

    /**
     * Aplikuje efekt zrýchlenia - zvýši rýchlosť hráča na čas.
     */
    @Override
    public void pouzi(Postava postava) {
        postava.activateSpeedBoost(this.trvanie);
        this.oznacZobrany();
    }

    @Override
    public void paint(Graphics g) {
        if (this.getObrazok() != null) {
            g.drawImage(this.getObrazok(), this.getX(), this.getY(), this.getSirka(), this.getVyska(), null);
        }
    }

}
