package players;

import core.HernyObjekt;
import weapons.Strela;

import java.awt.*;
import java.util.List;

public class Boss extends HernyObjekt implements Postava {
    private int hp;
    private static final int MAX_HP = 10;
    private int rychlost = 3;

    private HernyObjekt ciel;

    private int faza = 1;
    private static final int FAZA2_PRAH = MAX_HP / 2;

    private int cooldownNormalny  = 0;
    private int cooldownSpecialny = 0;
    private static final int COOLDOWN_NORMALNY  = 50;
    private static final int COOLDOWN_SPECIALNY = 200;

    private int pohybX = 0;
    private int pohybY = 0;

    public Boss(Image obrazok, int x, int y, int velkost) {
        super(obrazok, x, y, velkost, velkost);
        this.hp = MAX_HP;
    }

    public void setCiel(HernyObjekt ciel) {
        this.ciel = ciel;
    }


    @Override
    public void pohybSa() {
        if (this.ciel == null) return;
        int dx = this.ciel.getX() - this.getX();
        int dy = this.ciel.getY() - this.getY();
        double vzd = Math.sqrt(dx * dx + dy * dy);
        if (vzd > 0) {
            this.pohybX = (int) ((dx / vzd) * this.rychlost);
            this.pohybY = (int) ((dy / vzd) * this.rychlost);
        }
        if (this.cooldownNormalny  > 0) this.cooldownNormalny--;
        if (this.cooldownSpecialny > 0) this.cooldownSpecialny--;
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
    public void utoc(int cielX, int cielY, List<Strela> strely) {
        if (this.cooldownSpecialny <= 0) {
            this.vypalisRaketu(cielX, cielY, strely);
            this.cooldownSpecialny = COOLDOWN_SPECIALNY;
            return;
        }
        if (this.cooldownNormalny > 0) return;
        if (this.faza == 1) this.vypalisNormalnu(cielX, cielY, strely);
        else                this.vypalisSpread(cielX, cielY, strely);
        this.cooldownNormalny = COOLDOWN_NORMALNY;
    }

    private void vypalisNormalnu(int cx, int cy, List<Strela> st) {
        double[] s = this.smer(cx, cy, 7.0);
        st.add(new Strela(this.getX() + this.getSirka() / 2,
                this.getY() + this.getVyska() / 2,
                s[0], s[1], 1, Strela.TypStrely.NORMALNA));
    }

    private void vypalisSpread(int cx, int cy, List<Strela> st) {
        double[] uhly = {-0.35, 0, 0.35};
        for (double u : uhly) {
            double[] s  = this.smer(cx, cy, 6.0);
            double rx   = s[0] * Math.cos(u) - s[1] * Math.sin(u);
            double ry   = s[0] * Math.sin(u) + s[1] * Math.cos(u);
            st.add(new Strela(this.getX() + this.getSirka() / 2,
                    this.getY() + this.getVyska() / 2,
                    rx, ry, 1, Strela.TypStrely.BOSS_SPREAD));
        }
    }

    private void vypalisRaketu(int cx, int cy, List<Strela> st) {
        double[] s  = this.smer(cx, cy, 4.0);
        Strela r = new Strela(this.getX() + this.getSirka() / 2,
                this.getY() + this.getVyska() / 2,
                s[0], s[1], 1, Strela.TypStrely.RAKETA);
        r.setCiel(cx, cy);
        st.add(r);
    }

    private double[] smer(int cx, int cy, double spd) {
        double dx = cx - this.getX();
        double dy = cy - this.getY();
        double d  = Math.sqrt(dx * dx + dy * dy);
        if (d == 0) return new double[]{spd, 0};
        return new double[]{(dx / d) * spd, (dy / d) * spd};
    }

    @Override
    public boolean mozeUtocit() {
        return this.cooldownNormalny <= 0 || this.cooldownSpecialny <= 0;
    }

    @Override
    public void vystrel() {

    }

    @Override
    public void resetCooldown() {
        this.cooldownNormalny = COOLDOWN_NORMALNY;
    }

    @Override
    public int  getHP() {
        return this.hp;
    }

    @Override
    public int  getMaxHP() {
        return MAX_HP;
    }

    @Override
    public void setHP(int hp) {
        this.hp = Math.min(Math.max(hp, 0), MAX_HP);
    }


    @Override
    public void dostaZasah(int p) {
        this.hp -= p;
        if (this.hp < 0) this.hp = 0;
        if (this.faza == 1 && this.hp <= FAZA2_PRAH) {
            this.faza    = 2;
            this.rychlost = 5;
        }
    }

    @Override
    public void dostanZasah(int p) {
        this.dostaZasah(p);
    }

    @Override
    public boolean jeZiva() {
        return this.hp > 0;
    }

    @Override
    public int dealDmg() {
        return 20;
    }
    @Override
    public int getRychlost() {
        return this.rychlost;
    }

    @Override
    public int getX() {
        return super.getX();
    }

    @Override
    public int getY() {
        return super.getY();
    }

    public int getFaza() {
        return this.faza;
    }

    @Override public void pohyb() {

    }

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public boolean dotyk(HernyObjekt o) {
        return this.koliduje(o);
    }
}
