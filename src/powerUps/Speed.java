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
    public void paint(Graphics g) {
        if (this.obrazok != null) {
            super.paint(g);
            return;
        }
        g.setColor(new Color(50, 100, 220));
        g.fillRoundRect(this.x, this.y, this.sirka, this.vyska, 6, 6);
        g.setColor(Color.WHITE);
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        g.drawString(">>", this.x + this.sirka / 2 - 8, this.y + this.vyska / 2 + 5);
    }
}
