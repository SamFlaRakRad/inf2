package Core;

import java.awt.Image;
/**
 * Táto trieda vytvára štvorce z vlastnými obrazkami ktoré umiestňuje na plátno 
 * 
 * @author (Samuel Ďuriš) 
 * @version (V3)
 */
public class Stvorec {
    private int x;
    private int y;
    private int stranaA;
    private int stranaB;
    private Image obrazok;
    /**
     * Konštruktor triedy Stvorec
     */
    public Stvorec(Image obrazok, int x, int y, int stranaA, int stranaB) {
        this.obrazok = obrazok;
        this.x = x;
        this.y = y;
        this.stranaA = stranaA;
    }
    
    /**
     * Getter pre stranu štvorca
     */
    public int getStranaA() {
        return this.stranaA;
    }
    
    /**
     * Getter pre x-ovu pozíciu štvorca
     */
    public int getX() {
        return this.x;
    }
    
    /**
     * Getter pre y-ovu pozíciu štvorca
     */
    public int getY() {
        return this.y;
    }
    
    /**
     * Getter pre obrázok štvorca
     */
    public Image getObrazok() {
        return this.obrazok;
    }
    
    /**
     * Setter pre obrázok štvorca
     */
    public void setImage(Image obrazok) {
        this.obrazok = obrazok;
    }
    
    /**
     * Setter pre x-ovu pozíciu štvorca
     */
    public void setX(int x) {
        this.x = x;
    }
    
    /**
     * Setter pre y-ovu pozíciu štvorca
     */
    public void setY(int y) {
        this.y = y;
    }
}
