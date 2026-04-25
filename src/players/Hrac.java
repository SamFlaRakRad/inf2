package players;

import weapons.Strela;
import core.HernyObjekt;
import java.awt.*;
import java.util.List;

/**
 * Táto trieda vytvára hráčov.
 * 
 * @author (Samuel Ďuriš) 
 * @version (V3)
 */
public class Hrac extends HernyObjekt implements Postava {
    private char smerObr;
    private int pohybX = 0;
    private int pohybY = 0;
    private boolean vystrelenaStrela = false;

    protected int x = 0;
    protected int y = 0;
    protected int sirka = 0;
    protected int vyska = 0;
    protected Image obrazok = null;

    protected int hp = 100;
    protected int rychlost = 8;
    protected int cooldown = 0;

    private final int klavesHore;
    private final int klavesDole;
    private final int klavesVlavo;
    private final int klavesVpravo;
    private final int klavesStrelba;
    protected boolean[] stlaceneKlavesy;

    /**
     * Konštruktor triedy Hrac (starý formát)
     */
    public Hrac(char pociatocnySmer, int hore, int dole, int vlavo, int vpravo, int strelba) {
        super(null, 0, 0, 32, 32);
        this.smerObr = pociatocnySmer;
        this.klavesHore = hore;
        this.klavesDole = dole;
        this.klavesVlavo = vlavo;
        this.klavesVpravo = vpravo;
        this.klavesStrelba = strelba;
        this.stlaceneKlavesy = new boolean[256];
    }

    /**
     * Konštruktor triedy Hrac (nový formát s pozíciou a klávesami)
     */
    public Hrac(Image obrazok, int x, int y, int velkost, char smer,
            int hore, int dole, int vlavo, int vpravo, int strelba, boolean[] stlaceneKlavesy) {
        super(obrazok, x, y, velkost, velkost);
        this.smerObr = smer;
        this.klavesHore = hore;
        this.klavesDole = dole;
        this.klavesVlavo = vlavo;
        this.klavesVpravo = vpravo;
        this.klavesStrelba = strelba;
        this.stlaceneKlavesy = stlaceneKlavesy;
        this.hp = 100;
        this.rychlost = 8;
    }

    /**
     * metoda spracovania pohybu
     */
    public void spracujPohyb(boolean[] stlaceneKlavesy) {
        this.pohybX = 0;
        this.pohybY = 0;

        if (stlaceneKlavesy[this.klavesHore]) {
            this.pohybY = -this.rychlost;
        }
        if (stlaceneKlavesy[this.klavesDole]) {
            this.pohybY = this.rychlost;
        }
        if (stlaceneKlavesy[this.klavesVlavo]) {
            this.pohybX = -this.rychlost;
            this.smerObr = 'L';
        }
        if (stlaceneKlavesy[this.klavesVpravo]) {
            this.pohybX = this.rychlost;
            this.smerObr = 'R';
        }
    }

    /**
     * Pohyb postery (nový formát)
     */
    @Override
    public void pohybSa() {
        if (this.stlaceneKlavesy == null) return;
        this.pohybX = 0;
        this.pohybY = 0;

        if (this.stlaceneKlavesy[this.klavesHore]) {
            this.pohybY = -this.rychlost;
        }
        if (this.stlaceneKlavesy[this.klavesDole]) {
            this.pohybY = this.rychlost;
        }
        if (this.stlaceneKlavesy[this.klavesVlavo]) {
            this.pohybX = -this.rychlost;
            this.smerObr = 'L';
        }
        if (this.stlaceneKlavesy[this.klavesVpravo]) {
            this.pohybX = this.rychlost;
            this.smerObr = 'R';
        }

        if (this.cooldown > 0) this.cooldown--;
    }

    /**
     * metoda ktorá oznamuje že hrac vystrelil strelu
     */
    @Override
    public void vystrel() {
        this.vystrelenaStrela = true;
    }

    @Override
    public int getHP() {
        return this.hp;
    }

    @Override
    public int getMaxHP() {
        return 100;
    }

    @Override
    public void setHP(int hp) {
        this.hp = Math.min(hp, 100);
    }

    @Override
    public void dostaZasah(int poskodenie) {
        this.hp -= poskodenie;
        if (this.hp < 0) this.hp = 0;
    }

    @Override
    public void dostanZasah(int poskodenie) {
        this.hp -= poskodenie;
        if (this.hp < 0) this.hp = 0;
    }

    @Override
    public boolean jeZiva() {
        return this.hp > 0;
    }

    @Override
    public void utoc(int cielX, int cielY, List<Strela> strely) {
        if (!this.mozeUtocit() || !this.getVystrelena()) return;
        double dx = cielX - this.x;
        double dy = cielY - this.y;
        double d = Math.sqrt(dx * dx + dy * dy);
        if (d == 0) return;
        double rx = (dx / d) * 8;
        double ry = (dy / d) * 8;
        strely.add(new Strela(this.x + this.sirka / 2, this.y + this.sirka / 2,
                rx, ry, 0, Strela.TypStrely.NORMALNA));
        this.resetVystrelena();
        this.cooldown = 15;
    }

    @Override
    public boolean mozeUtocit() {
        return this.cooldown <= 0;
    }

    @Override
    public int dealDmg() {
        return 1;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    /**
     * metoda ktorá po vystreleni vráti povodny nevystrelený stav hráčovy 
     */
    public void resetVystrelena() {
        this.vystrelenaStrela = false;
    }

    /**
     * geter pre pohyb x-ovej osi
     */
    public int getPohybX() {
        return this.pohybX;
    }

    /**
     * geter pre pohyb y-ovej osi
     */
    public int getPohybY() {
        return this.pohybY;
    }

    /**
     * seter pre pohyb x-ovej osi
     */
    @Override
    public void setX(int x) {
        this.x = x;
    }

    /**
     * seter pre pohyb y-ovej osi
     */
    @Override
    public void setY(int y) {
        this.y = y;
    }

    /**
     * getter pre zaciatocny smer hracov
     */
    public char getSmerObr() {
        return this.smerObr;
    }

    /**
     * getter pre stav hraca 
     */
    public boolean getVystrelena() {
        return this.vystrelenaStrela;
    }

    /**
     * setter pre zaciatocny smer hracov
     */
    public void setSmerObr(char smer) {
        this.smerObr = smer;
    }

    public void setObrazok(Image img) {
        this.obrazok = img;
    }

    public Image getObrazok() {
        return this.obrazok;
    }

    public int getRychlost() {
        return this.rychlost;
    }

    public void setRychlost(int rych) {
        this.rychlost = rych;
    }

    @Override
    public void paint(Graphics g) {
        if (this.obrazok != null) {
            g.drawImage(this.obrazok, this.x, this.y, this.sirka, this.vyska, null);
        }
    }

    @Override
    public boolean dotyk(HernyObjekt objekt) {
        return this.koliduje(objekt);
    }
}
