package core;

import java.awt.*;

/**
 * Táto trieda vytvára štvorce z vlastnými obrazkami ktoré umiestňuje na plátno 
 * 
 * @author (Samuel Ďuriš) 
 * @version (V3)
 */
public class Stvorec extends HernyObjekt {

    public Stvorec(Image obrazok, int x, int y, int stranaA, int stranaB) {
        super(obrazok, x, y, stranaA, stranaA);
    }

    public int getStranaA() {
        return this.getSirka();
    }

    public void setImage(Image obrazok) {
        this.setObrazok(obrazok);
    }

    @Override
    public void pohyb() {

    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(this.getObrazok(), this.getX(), this.getY(),
                this.getSirka(), this.getVyska(), null);
    }

    @Override
    public boolean dotyk(HernyObjekt objekt) {
        return this.koliduje(objekt);
    }
}
