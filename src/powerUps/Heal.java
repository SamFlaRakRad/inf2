package powerUps;

import core.HernyObjekt;
import players.Postava;

import java.awt.*;

public class Heal extends PowerUp {
    public Heal(Image obrazok, int x, int y, int velkost) {
        super(obrazok, x, y, velkost);
    }

    /**
     * OVERRIDE pouzi() - obnoví 1 HP.
     * Berie Postava - funguje pre hráča aj bossa.
     */
    @Override
    public void pouzi(Postava postava) {
        postava.setHP(postava.getHP() + 1);
        this.oznacZobrany();
    }

    @Override
    public void paint(Graphics g) {
        if (this.obrazok != null) {
            super.paint(g);
            return;
        }
        g.setColor(new Color(50, 200, 50));
        g.fillRoundRect(this.x, this.y, this.sirka, this.vyska, 6, 6);
        g.setColor(Color.WHITE);
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        g.drawString("+", this.x + this.sirka / 2 - 5, this.y + this.vyska / 2 + 6);
    }
}
