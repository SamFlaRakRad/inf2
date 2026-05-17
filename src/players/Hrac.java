package players;

import weapons.Strela;
import core.HernyObjekt;
import weapons.TypStrely;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

/**
 * Trieda Hrac - predstavuje hráčovu postavu, ktorú ovláda hráč.
 * Hráč môž pohybovať postavou pomocou kláves, strieľať a zbierať powerupy.
 * Má zdravie, munitýu, rýchlosť pohybu a možnosť aktivovania speedupov.
 *
 * @author Samuel Ďuriš
 * @version V3
 */
public class Hrac extends HernyObjekt implements Postava {
    private char smerObr;
    private int pohybX = 0;
    private int pohybY = 0;
    private static final int RYCHLOST_ZAKLADNA = 8;
    private static final int RYCHLOST_BOOST    = 5;
    private int speedBoostTiky = 0;
    private boolean vystrelenaStrela = false;

    private int hp = 100;
    private int rychlost = 8;
    private int cooldown = 0;

    private int strana;

    private final int klavesHore;
    private final int klavesDole;
    private final int klavesVlavo;
    private final int klavesVpravo;
    private final int klavesStrelba;
    private boolean[] stlaceneKlavesy;

    public Hrac(Image obrazok, int x, int y, int velkost, char smer,
                int hore, int dole, int vlavo, int vpravo,
                int strelba, boolean[] stlaceneKlavesy, int strana) {
        super(obrazok, x, y, velkost, velkost);
        this.smerObr = smer;
        this.klavesHore = hore;
        this.klavesDole = dole;
        this.klavesVlavo = vlavo;
        this.klavesVpravo = vpravo;
        this.klavesStrelba = strelba;
        this.stlaceneKlavesy = stlaceneKlavesy;
        this.strana = strana;
    }

    @Override
    public void pohybSa() {
        if (this.speedBoostTiky > 0) {
            this.speedBoostTiky--;
            if (this.speedBoostTiky == 0) {
                this.rychlost = RYCHLOST_ZAKLADNA;
            }
        }

        this.pohybX = 0;
        this.pohybY = 0;
        if (this.stlaceneKlavesy[this.klavesHore]) {
            this.pohybY = -this.rychlost;
        }
        if (this.stlaceneKlavesy[this.klavesDole]) {
            this.pohybY =  this.rychlost;
        }
        if (this.stlaceneKlavesy[this.klavesVlavo]) {
            this.pohybX = -this.rychlost;
            this.smerObr = 'L';
        }
        if (this.stlaceneKlavesy[this.klavesVpravo]) {
            this.pohybX =  this.rychlost;
            this.smerObr = 'R';
        }
        if (this.cooldown > 0) {
            this.cooldown--;
        }
    }

    @Override
    public void vystrel() {
        this.vystrelenaStrela = true;
    }

    @Override
    public void utoc(int cielX, int cielY, ArrayList<Strela> strely) {
        if (!this.mozeUtocit() || !this.vystrelenaStrela) {
            return;
        }
        double speedX;
        if (this.smerObr == 'L') {
            speedX = -10.0;
        } else {
            speedX = 10.0;
        }
        strely.add(new Strela(this.getX() + this.getSirka() / 2,
                              this.getY() + this.getVyska() / 2,
                                speedX, 0, this.strana, TypStrely.NORMALNA));
        this.resetVystrelena();
        this.cooldown = 10;
    }

    public void activateSpeedBoost(int tiky) {
        this.rychlost = RYCHLOST_ZAKLADNA + RYCHLOST_BOOST;
        this.speedBoostTiky = tiky;
    }

    @Override
    public boolean mozeUtocit()   {
        return this.cooldown <= 0;
    }

    @Override
    public void resetCooldown() {
        this.cooldown = 0;
    }

    @Override
    public int  getHP() {
        return this.hp;
    }

    @Override
    public int getMaxHP() {
        return 100;
    }

    @Override
    public void setHP(int hp) {
        this.hp = Math.clamp(hp, 0, 100);
    }

    @Override
    public void dostanZasah(int p) {
        this.hp = Math.max(0, this.hp - p);
    }

    @Override
    public boolean jeZiva() {
        return this.hp > 0;
    }

    @Override
    public int getPohybX() {
        return this.pohybX;
    }
    @Override
    public int getPohybY() {
        return this.pohybY;
    }

    @Override
    public int getX() {
        return super.getX();
    }

    @Override
    public int getY() {
        return super.getY();
    }

    public void  resetVystrelena() {
        this.vystrelenaStrela = false;
    }

    public boolean getVystrelena() {
        return this.vystrelenaStrela;
    }

    public char getSmerObr() {
        return this.smerObr;
    }

    @Override
    public void pohyb() {

    }

    @Override
    public void paint(Graphics g) {
        Image img = this.getObrazok();
        if (img != null) {
            g.drawImage(img, this.getX(), this.getY(), this.getSirka(), this.getVyska(), null);
        } else {
            throw new NullPointerException("Nema obrazok");
        }
    }

    @Override
    public boolean dotyk(HernyObjekt o) {
        return this.koliduje(o);
    }
}
