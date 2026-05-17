package players;

import core.HernyObjekt;
import weapons.Strela;
import weapons.TypStrely;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

/**
 * Trieda Bot - predstavuje AI nepriateľa
 *
 * @author Samuel Ďuriš
 * @version V3
 */
public class Bot extends Ai implements Postava {

    private static final int RYCHLOST_START = 3;
    private static final int MAX_HP = 100;

    private int hp = MAX_HP;
    private char smerObr = 'L';
    private int cooldown = 0;

    private boolean vystrelenaStrela = false;

    private Hrac ciel;

    public Bot(Image obrazok, int x, int y, int velkost) {
        super(obrazok, x, y, velkost);
    }

    public void sledujCiel(Hrac ciel) {
        this.ciel = ciel;
    }

    @Override
    public void pohybSa() {
        int x = this.ciel.getX() - this.getX();
        int y = this.ciel.getY() - this.getY();
        if (x >= 0) {
            this.smerObr = 'R';
        } else {
            this.smerObr = 'L';
        }

        int botX = (this.getX() + this.getSirka() / 2) / this.getVelkostS();
        int botY = (this.getY() + this.getVyska() / 2) / this.getVelkostS();
        int cielX = (this.ciel.getX() + this.ciel.getSirka() / 2) / this.getVelkostS();
        int cielY = (this.ciel.getY() + this.ciel.getVyska() / 2) / this.getVelkostS();

        int[] smer = this.pathfinding(botX, botY, cielX, cielY, this.ciel.getPohybX(), this.ciel.getPohybY());

        super.korekcia(smer, botX, botY, RYCHLOST_START);

        if (this.cooldown > 0) {
            this.cooldown--;
        }
        if (Math.abs(x) < 200 && Math.abs(y) < 200) {
            this.vystrel();
        }
    }

    @Override
    public void utoc(int cielX, int cielY, ArrayList<Strela> strely) {
        double speedX;
        if (this.smerObr == 'L') {
            speedX = -8.0;
        } else {
            speedX = 8.0;
        }
        strely.add(new Strela(
                this.getX() + this.getSirka() / 2,
                this.getY() + this.getVyska() / 2,
                speedX, 0.0, 1, TypStrely.NORMALNA));
        this.vystrelenaStrela = false;
        this.cooldown = 30;
    }

    @Override
    public void dostanZasah(int p) {
        this.hp = Math.max(0, this.hp - p);
    }
    @Override
    public void vystrel() {
        this.vystrelenaStrela = true;
    }
    @Override
    public boolean mozeUtocit() {
        return this.cooldown <= 0;
    }
    @Override
    public void resetCooldown() {
        this.cooldown = 0;
    }
    @Override
    public int getHP() {
        return this.hp;
    }
    @Override
    public int getMaxHP() {
        return MAX_HP;
    }
    @Override
    public void setHP(int hp) {
        this.hp = Math.clamp(hp, 0, MAX_HP);
    }
    @Override
    public boolean jeZiva() {
        return this.hp > 0;
    }

    @Override
    public int getX() {
        return super.getX();
    }
    @Override
    public int getY() {
        return super.getY();
    }
    @Override
    public char getSmerObr() {
        return this.smerObr;
    }
    @Override
    public void pohyb() {

    }
    @Override
    public void paint(Graphics g) {

    }
    @Override
    public boolean dotyk(HernyObjekt o) {
        return this.koliduje(o);
    }
}