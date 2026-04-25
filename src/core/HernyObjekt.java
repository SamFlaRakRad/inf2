package core;

import java.awt.*;

public abstract class HernyObjekt {
    protected int x;
    protected int y;
    protected int sirka;
    protected int vyska;
    protected Image obrazok;

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

    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public int getSirka() { return this.sirka; }
    public int getVyska() { return this.vyska; }
    public Image getObrazok() { return this.obrazok; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setObrazok(Image obrazok) { this.obrazok = obrazok; }

    /**
     * Abstraktná metóda vykresľovania - každý podtyp sa nakreslí inak.
     * Stena nakreslí textúru, hráč nakreslí sprite, PowerUp nakreslí ikonu.
     * Toto je jadro polymorfizmu cez paint().
     */
    public abstract void paint(Graphics g);

    /**
     * AABB kolízia - zdieľaná logika pre všetkých potomkov.
     * Axis-Aligned Bounding Box collision detection.
     */
    public boolean koliduje(HernyObjekt iný) {
        return this.x < iný.x + iný.sirka &&
                this.x + this.sirka > iný.x &&
                this.y < iný.y + iný.vyska &&
                this.y + this.vyska > iný.y;
    }

    public abstract boolean dotyk(HernyObjekt objekt);
}
