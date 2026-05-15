package core;

import java.awt.Graphics;
import java.awt.Image;

public abstract class HernyObjekt {
    private int x;
    private int y;
    private int sirka;
    private int vyska;
    private Image obrazok;

    public HernyObjekt(Image obrazok, int x, int y, int sirka, int vyska) {
        this.obrazok = obrazok;
        this.x = x;
        this.y = y;
        this.sirka = sirka;
        this.vyska = vyska;
    }

    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public int getSirka() {
        return this.sirka;
    }
    public int getVyska() {
        return this.vyska;
    }
    public Image getObrazok() {
        return this.obrazok;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setObrazok(Image img) {
        this.obrazok = img;
    }

    public abstract void pohyb();

    public abstract void paint(Graphics g);

    /**
     * AABB kolízia
     */
    public boolean koliduje(HernyObjekt iny) {
        return this.getX() < iny.getX() + iny.getSirka()
                && this.getX() + this.getSirka() > iny.getX()
                && this.getY() < iny.getY() + iny.getVyska()
                && this.getY() + this.getVyska() > iny.getY();
    }

    public abstract boolean dotyk(HernyObjekt objekt);
}
