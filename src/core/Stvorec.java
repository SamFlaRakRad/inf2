package core;

import java.awt.*;

/**
 * Táto trieda vytvára štvorce z vlastnými obrazkami ktoré umiestňuje na plátno 
 * 
 * @author (Samuel Ďuriš) 
 * @version (V3)
 */
public class Stvorec extends HernyObjekt {

    /**
     * Konštruktor triedy Stvorec
     */
    public Stvorec(Image obrazok, int x, int y, int stranaA, int stranaB) {
        super(obrazok, x, y, stranaA, stranaA);
    }

    /**
     * Getter pre stranu (zachovaná kompatibilita s pôvodným kódom)
     */
    public int getStranaA() {
        return this.sirka;
    }

    /**
     * Setter pre obrázok (zachovaná kompatibilita)
     */
    public void setImage(Image obrazok) {
        this.obrazok = obrazok;
    }

    /**
     * Polymorfné vykresľovanie - Stvorec nakreslí svoj obrázok.
     * Rovnaké volanie paint(g) na Hrac nakreslí sprite hráča,
     * tu nakreslí stenu alebo náboj.
     */
    @Override
    public void paint(Graphics g) {
        g.drawImage(this.obrazok, this.x, this.y, this.sirka, this.vyska, null);
    }

    /**
     * Implementácia rozhrania Kolizie.
     * Polymorfizmus: Hra môže zavolať dotyk() na hocakom Kolizie objekte.
     */
    @Override
    public boolean dotyk(HernyObjekt objekt) {
        return this.koliduje(objekt);
    }
}
