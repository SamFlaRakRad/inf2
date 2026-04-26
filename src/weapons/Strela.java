package weapons;

import core.HernyObjekt;
import java.awt.Graphics;
import java.awt.Color;

public class Strela extends HernyObjekt {
    private int x;
    private int y;
    private double rychlostX;
    private double rychlostY;
    private static final int VELKOST_STRELY = 8;
    private int strana = 0;  // 0 = hráč, 1 = bot/boss

    public enum TypStrely {
        NORMALNA, BOSS_SPREAD, RAKETA
    }

    private TypStrely typ = TypStrely.NORMALNA;
    private int cielX = 0;
    private int cielY = 0;

    public Strela() {
        super(null, 0, 0, VELKOST_STRELY, VELKOST_STRELY);
    }


    public Strela(int x, int y, double rx, double ry, int strana, TypStrely typ) {
        super(null, x, y, VELKOST_STRELY, VELKOST_STRELY);
        this.x = x;
        this.y = y;
        this.rychlostX = rx;
        this.rychlostY = ry;
        this.strana = strana;
        this.typ = typ;
    }


    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    public void setRychlostX(int rychlost) {
        this.rychlostX = rychlost;
    }

    public void setRychlostY(int rychlost) {
        this.rychlostY = rychlost;
    }


    public int getStranaStrely() {
        return this.VELKOST_STRELY;
    }


    @Override
    public void pohyb() {
        this.x += (int) this.rychlostX;
        this.y += (int) this.rychlostY;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillOval(this.x, this.y, 8, 8);
    }

    public int getStrana() {
        return this.strana;
    }

    public TypStrely getTyp() {
        return this.typ;
    }

    public void setCiel(int cx, int cy) {
        this.cielX = cx;
        this.cielY = cy;
    }

    public void aktualizujCiel(int cx, int cy) {
        this.cielX = cx;
        this.cielY = cy;
        // Aktualizovať smer ak je to raketa
        if (this.typ == TypStrely.RAKETA && this.x >= 0 && this.y >= 0) {
            double dx = cx - this.x;
            double dy = cy - this.y;
            double d = Math.sqrt(dx * dx + dy * dy);
            if (d > 0) {
                this.rychlostX = (dx / d) * 4;
                this.rychlostY = (dy / d) * 4;
            }
        }
    }

    @Override
    public boolean dotyk(HernyObjekt objekt) {
        return this.koliduje(objekt);
    }
}
