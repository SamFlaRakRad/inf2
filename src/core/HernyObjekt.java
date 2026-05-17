package core;

import java.awt.Graphics;
import java.awt.Image;

/**
 * Abstraktná trieda reprezentujúca herný objekt so základnými vlastnosťami.
 * Každý objekt má pozíciu (x, y), rozmery (šírku a výšku) a obrázok.
 * Poskytuje základné metódy na manipuláciu s objektom a detekciu kolízií.
 * Všetky hernné objekty (steny, postavy, strely, powerupy) rozširujú túto triedu.
 *
 * @author Samuel Ďuriš
 * @version V3
 */
public abstract class HernyObjekt {
    private int x;
    private int y;
    private int sirka;
    private int vyska;
    private Image obrazok;

    /**
     * Konštruktor - inicializuje herný objekt s pozíciou, veľkosťou a obrázkom.
     */
    public HernyObjekt(Image obrazok, int x, int y, int sirka, int vyska) {
        this.obrazok = obrazok;
        this.x = x;
        this.y = y;
        this.sirka = sirka;
        this.vyska = vyska;
    }

    /**
     * Getter pre x-ovú súradnicu
     */
    public int getX() {
        return this.x;
    }

    /**
     * Getter pre y-ovú súradnicu
     */
    public int getY() {
        return this.y;
    }

    /**
     * Getter pre šírku
     */
    public int getSirka() {
        return this.sirka;
    }

    /**
     * Getter pre výšku
     */
    public int getVyska() {
        return this.vyska;
    }

    /**
     * Getter pre obrázok
     */
    public Image getObrazok() {
        return this.obrazok;
    }

    /**
     * Setter pre x-ovú súradnicu
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Setter pre y-ovú súradnicu
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Setter pre obrázok
     */
    public void setObrazok(Image obrazok) {
        this.obrazok = obrazok;
    }

    /**
     * Abstraktná metóda - pohyb objektu. Konkrétne implementácie sa líšia podľa typu objektu.
     */
    public abstract void pohyb();

    /**
     * Abstraktná metóda - vykreslenie objektu na canvas.
     */
    public abstract void paint(Graphics g);

    /**
     * Detekuje AABB (Axis-Aligned Bounding Box) kolíziu medzi dvoma objektami.
     * Používa sa na detekciu kolízií medzi všetkými hernými objektami.
     */
    public boolean koliduje(HernyObjekt iny) {
        return this.getX() < iny.getX() + iny.getSirka()
                && this.getX() + this.getSirka() > iny.getX()
                && this.getY() < iny.getY() + iny.getVyska()
                && this.getY() + this.getVyska() > iny.getY();
    }

    /**
     * Abstraktná metóda - detekuje dotyky/kolízie. Konkrétna implementácia sa líši podľa typu objektu.
     */
    public abstract boolean dotyk(HernyObjekt objekt);
}
