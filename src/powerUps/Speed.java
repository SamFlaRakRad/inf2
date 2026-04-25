package powerUps;

import players.Postava;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class Speed extends PowerUp {
    // Speed boost for power-ups

    public Speed(Image obrazok, int x, int y, int velkost) {
        super(obrazok, x, y, velkost);
    }

    /**
     * OVERRIDE pouzi() - Speed power-up effect.
     * Currently just marks the pickup as used.
     */
    @Override
    public void pouzi(Postava postava) {
        // Speed boost is a visual/gameplay effect only
        // Store in Postava if needed - for now just mark as used
        this.oznacZobrany();
    }

    @Override
    public void pohyb() {

    }

    @Override
    public void paint(Graphics g) {
        if (this.getObrazok() != null) {
            g.drawImage(this.getObrazok(), this.getX(), this.getY(), this.getSirka(), this.getVyska(), null);
        }
    }
}
