package core;

import java.awt.*;

public abstract class HernyObjekt {
    private int x;
    private int y;
    private int sirka;
    private int vyska;
    private Image obrazok;

    /**
     * Konštruktor
     */
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
    public void setObrazok(Image obrazok) {
        this.obrazok = obrazok;
    }

    public abstract void pohyb();

    public abstract void paint(Graphics g);

    /**
     * Axis-Aligned Bounding Box collision detection.
     */
    public boolean koliduje(HernyObjekt volaco) {
        return this.x < volaco.x + volaco.sirka &&
                this.x + this.sirka > volaco.x &&
                this.y < volaco.y + volaco.vyska &&
                this.y + this.vyska > volaco.y;
    }

    public boolean dotyk(HernyObjekt objekt) {
        return this.koliduje(objekt);
    }
}
