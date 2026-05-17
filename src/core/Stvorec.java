package core;

import java.awt.Image;
import java.awt.Graphics;

/**
 * Trieda Stvorec - predstavuje štvorcový herný objekt s vlastným obrázkom.
 * 
 * @author Samuel Ďuriš
 * @version V3
 */
public class Stvorec extends HernyObjekt {

    /**
     * Konštruktor - vytvorí štvorcový objekt.
     */
    public Stvorec(Image obrazok, int x, int y, int stranaA, int stranaB) {
        super(obrazok, x, y, stranaA, stranaA);
    }

    /**
     * Getter pre stranu štvorca (šírka).
     */
    public int getStranaA() {
        return this.getSirka();
    }

    /**
     * Setter pre obrázok štvorca.
     */
    public void setImage(Image obrazok) {
        this.setObrazok(obrazok);
    }

    /**
     * Pohyb - steny sa nepohybujú, takže metóda ostáva prázdna.
     */
    @Override
    public void pohyb() {

    }

    /**
     * Vykreslí štvorec na canvas s jeho obrázkom.
     */
    @Override
    public void paint(Graphics g) {
        g.drawImage(this.getObrazok(), this.getX(), this.getY(),
                this.getSirka(), this.getVyska(), null);
    }

    /**
     * Detekuje dotyky - ktoí sa dotýka steny.
     */
    @Override
    public boolean dotyk(HernyObjekt objekt) {
        return this.koliduje(objekt);
    }
}
