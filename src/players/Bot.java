package players;

import core.HernyObjekt;
import weapons.Strela;

import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;

public class Bot extends Hrac implements Postava {
    private HernyObjekt ciel;
    private ImageIcon ikonka;

    public Bot(Image obrazok, int x, int y, int velkost, boolean[] stlaceneKlavesy) {
        super(obrazok, x, y, velkost, 'L', -1, -1, -1, -1, -1, stlaceneKlavesy, 1);
    }

    public void sledujCiel(HernyObjekt ciel) {
        this.ciel = ciel;
    }

    public ImageIcon getIkonka() {
        return this.ikonka;
    }

    public void setIkonka(ImageIcon ikonka) {
        this.ikonka = ikonka;
        if (ikonka != null) {
            this.setObrazok(ikonka.getImage());
        }
    }

    @Override
    public void pohybSa() {
        if (this.ciel == null) {
            return;
        }
        this.setPohybX(0);
        this.setPohybY(0);

        int dx = this.ciel.getX() - this.getX();
        int dy = this.ciel.getY() - this.getY();

        if (Math.abs(dx) > Math.abs(dy)) {
            this.setPohybX(dx > 0 ? this.getRychlost() : -this.getRychlost());
            this.setSmerObr(dx > 0 ? 'R' : 'L');
        } else {
            this.setPohybY(dy > 0 ? this.getRychlost() : -this.getRychlost());
        }

        this.decrementCooldown();

        if (Math.abs(dx) < 200 && Math.abs(dy) < 200) {
            this.vystrel();
        }
    }

    @Override
    public void utoc(int cielX, int cielY, List<Strela> strely) {
        if (!this.mozeUtocit() || !this.getVystrelena()) {
            return;
        }
        double dx = cielX - this.getX();
        double dy = cielY - this.getY();
        double d  = Math.sqrt(dx * dx + dy * dy);
        if (d == 0) {
            return;
        }
        strely.add(new Strela(
                this.getX() + this.getSirka() / 2,
                this.getY() + this.getVyska() / 2,
                (dx / d) * 8, (dy / d) * 8,
                1, Strela.TypStrely.NORMALNA));
        this.resetVystrelena();
        this.setCooldown(20);
    }

    @Override
    public void paint(Graphics g) {

    }
}
