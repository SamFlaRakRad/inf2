package players;

import core.HernyObjekt;
import weapons.Strela;
import weapons.TypStrely;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

/**
 * Trieda Boss
 * Boss má dva fázy
 * Fáza sa mení, keď Boss stratí viac ako 50% zdravia.
 * Boss používa rôzne typy útokov: normálny, shotgun, full-auto a sniper.
 *
 * @author Samuel Ďuriš
 * @version V3
 */
public class Boss extends Ai {

    private int hp;
    private static final int MAX_HP = 100;
    private int rychlost = 3;

    private Hrac ciel;
    private int faza = 1;
    private static final int POLHP = MAX_HP / 2;
    private int startCooldown = 30;
    private int cooldownNormalny = 30;
    private int fullAuto = 5;

    private char smerObr = 'L';
    private Image vlavo;
    private Image vpravo;

    public Boss(Image obrazok, int x, int y, int velkost) {
        super(obrazok, x, y, velkost);
        this.hp = MAX_HP;
        this.vlavo = obrazok;
        this.vpravo = obrazok;
    }

    @Override
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

        if (this.getMapa() != null) {
            int bossX = (this.getX() + this.getSirka() / 2) / this.getVelkostS();
            int bossY  = (this.getY() + this.getVyska() / 2) / this.getVelkostS();
            int cielGX = (this.ciel.getX() + this.ciel.getSirka() / 2) / this.getVelkostS();
            int cielGY = (this.ciel.getY() + this.ciel.getVyska() / 2) / this.getVelkostS();

            int[] smer = this.pathfinding( bossX, bossY, cielGX, cielGY, this.ciel.getPohybX(), this.ciel.getPohybY());

            super.korekcia(smer, bossX, bossY, this.rychlost);
        }
        if (this.cooldownNormalny > 0) {
            this.cooldownNormalny--;
        }
        if (Math.abs(x) < 200 && Math.abs(y) < 200) {
            this.vystrel();
        }
    }


    @Override
    public void utoc(int cielX, int cielY, ArrayList<Strela> strely) {
        if (this.faza == 1) {
            this.normal(strely);
            this.resetCooldown();
        } else {
            Random rand = new Random();
            switch (rand.nextInt(4)) {
                case 0:
                    this.normal(strely);
                    this.resetCooldown();
                    break;
                case 1:
                    this.broka(strely);
                    this.resetCooldown();
                    break;
                case 2:
                    this.fullAuto(strely);
                    break;
                case 3:
                    this.snipa(strely);
                    this.resetCooldown();
                    break;

            }

        }

    }

    public void normal(ArrayList<Strela> strely) {
        double speedX;
        if (this.smerObr == 'L') {
            speedX = -9.0;
        } else {
            speedX = 9.0;
        }
        strely.add(new Strela(this.getX() + this.getSirka() / 2, this.getY() + this.getVyska() / 2,
                            speedX, 0.0, 1, TypStrely.NORMALNA));
    }

    public void broka(ArrayList<Strela> strely) {
        double speedX;
        if (this.smerObr == 'L') {
            speedX = -8.0;
        } else {
            speedX = 8.0;
        }
        int[]  spread = {-12, 0, 12};
        for (int s : spread) {
            strely.add(new Strela(this.getX() + this.getSirka() / 2,
                    this.getY() + this.getVyska() / 2 + s,
                    speedX, 0.0, 1, TypStrely.SHOTGUN));
        }
    }

    public void fullAuto(ArrayList<Strela> strely) {
        double speedX;
        if (this.smerObr == 'L') {
            speedX = -7.0;
        } else {
            speedX = 7.0;
        }
        for (int i = 0; i < 6; i++) {
            strely.add(new Strela(
                    this.getX() + this.getSirka() / 2,
                    this.getY() + this.getVyska() / 2,
                    speedX, 0.0, 1, TypStrely.NORMALNA));
        }
        this.cooldownNormalny = this.fullAuto;
    }

    public void snipa(ArrayList<Strela> strely) {
        double speedX;
        if (this.smerObr == 'L') {
            speedX = -20.0;
        } else {
            speedX = 20.0;
        }
        strely.add(new Strela(this.getX() + this.getSirka() / 2, this.getY() + this.getVyska() / 2,
                speedX, 0.0, 1, TypStrely.NORMALNA));
    }

    @Override
    public Image getVlavo() {
        return this.vlavo;
    }

    @Override
    public Image getVpravo() {
        return this.vpravo;
    }

    public void setObrazky(Image vlavo, Image vpravo) {
        this.vlavo = vlavo;
        this.vpravo = vpravo;
    }

    @Override
    public boolean mozeUtocit() {
        return this.cooldownNormalny <= 0;
    }
    @Override
    public void vystrel() {

    }
    @Override
    public void resetCooldown() {
        this.cooldownNormalny = this.startCooldown;
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
    public void dostanZasah(int p) {
        this.hp = Math.max(0, this.hp - p);
        if (this.faza == 1 && this.hp <= POLHP) {
            this.faza = 2;
            this.rychlost = 5;
        }
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
    public void activateSpeedBoost(int tiky) {

    }

    public int getFaza() {
        return this.faza;
    }
    @Override
    public void pohyb() {

    }
    @Override
    public void paint(Graphics g) {
        g.drawImage(this.getObrazok(), this.getX(), this.getY(), this.getSirka(), this.getVyska(), null);
    }

    @Override
    public boolean dotyk(HernyObjekt o) {
        return this.koliduje(o);
    }
}