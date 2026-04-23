package Weapons;

import java.awt.Graphics;
import java.awt.Color;

/**
 * Vytvorenie striel ktorými sa budú hráči strielať.
 * 
 * @author (Samuel Ďuriš) 
 * @version (V3)
 */
public class Strela {
    private int x;
    private int y;
    private int rychlostX;
    private static final int VELKOST_STRELY = 8;
    /**
     * Konštruktor
     */
    public Strela() {
    }
    
    /**
     * Getter pre x pre metoduDotykStrely v triede hra
     */
    public int getX() {
        return this.x;
    }
    
    /**
     * Getter pre y pre metoduDotykStrely v triede hra
     */
    public int getY() {
        return this.y;
    }
    
    /**
     * Setter pre x pozíciu
     */
    public void setX(int x) {
        this.x = x;
    }
    
    /**
     * Setter pre y pozíciu
     */
    public void setY(int y) {
        this.y = y;
    } 
    
    /**
     * Setter pre rychlosť strely
     */
    public void setRychlostX(int rychlost) {
        this.rychlostX = rychlost;
    }
    
    /**
     * Setter pre y pozíciu
     */
    public int getStranaStrely() {
        return this.VELKOST_STRELY; 
    }

    /**
     * setovanie novej x-ovej pozície strely
     */
    public void pohyb() {
        this.x += this.rychlostX;
    }
   
    /**
     * vykreslovanie kruhu na danej pozícii
     */
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillOval(this.x, this.y, 8, 8);
    }
    
}
