package weapons;

import core.HernyObjekt;
import java.awt.Graphics;
import java.awt.Color;

/**
 * Trieda Strela - predstavuje strelu vypúšťanú postávou.
 * Strela sa pohybuje v priamej línií s danou rýchlosťou a smerom.
 *
 * @author Samuel Ďuriš
 * @version V3
 */
public class Strela extends HernyObjekt {
    private int x;
    private int y;
    private double rychlostX;
    private double rychlostY;
    private static final int VELKOST_STRELY = 8;
    private int strana = 0;
    private TypStrely typ;

    /**
     * Konštruktor - vytvorí strelu s danou pozíciou a rýchlosťou.
     */
    public Strela(int x, int y, double rychlostX, double rychlostY, int strana, TypStrely typ) {
        super(null, x, y, VELKOST_STRELY, VELKOST_STRELY);
        this.rychlostX = rychlostX;
        this.rychlostY = rychlostY;
        this.strana = strana;
        this.typ = typ;
    }

    /**
     * Pohyb - aktualizuje pozíciu strely na základe jej rýchlosti.
     */
    @Override
    public void pohyb() {
        this.setX(this.getX() + (int)this.rychlostX);
        this.setY(this.getY() + (int)this.rychlostY);
    }

    /**
     * Vykreslí strelu ako čierny kruh.
     */
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillOval(this.getX(), this.getY(), VELKOST_STRELY, VELKOST_STRELY);
    }

    /**
     * Getter pre stranu, ktorá strelu vytvorila.
     */
    public int getStrana() {
        return this.strana;
    }


    /**
     * Skontroluje, či strela koliduje s iným objektom.
     */
    @Override
    public boolean dotyk(HernyObjekt objekt) {
        return this.koliduje(objekt);
    }
}
