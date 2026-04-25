package players;

import core.HernyObjekt;
import weapons.Strela;

import java.awt.*;
import java.util.List;

public class Boss extends HernyObjekt implements Postava {

    private int hp;
    private static final int MAX_HP = 10;
    private int rychlost = 3;

    // Cieľ (hráč) - Boss sa pohybuje k nemu
    private HernyObjekt ciel;

    // Fázy
    private int faza = 1;
    private static final int FAZA2_PRAH = MAX_HP / 2; // pri <=5 HP

    // Cooldowny
    private int cooldownNormalny = 0;
    private int cooldownSpecialny = 0;
    private static final int COOLDOWN_NORMALNY  = 50;
    private static final int COOLDOWN_SPECIALNY = 200;

    // Pohyb
    private int pohybX = 0;
    private int pohybY = 0;

    public Boss(Image obrazok, int x, int y, int velkost) {
        super(obrazok, x, y, velkost, velkost);
        this.hp = MAX_HP;
    }

    public void setCiel(HernyObjekt ciel) { this.ciel = ciel; }

    // ── Pohyblivy ────────────────────────────────────────────────────────

    /**
     * pohybSa() - Boss sa pohybuje k hráčovi, nie podľa klávesnice.
     *
     * Polymorfizmus: Hrac.pohybSa() číta stlaceneKlavesy[].
     *                Boss.pohybSa() vypočíta smer k cieľu.
     * Hra zavolá pohyblivy.pohybSa() - nevie ani ho zaujíma, ktorý je ktorý.
     */
    @Override
    public void pohybSa() {
        if (this.ciel == null) return;

        int dx = this.ciel.getX() - this.x;
        int dy = this.ciel.getY() - this.y;
        double vzdialenost = Math.sqrt(dx * dx + dy * dy);

        if (vzdialenost > 0) {
            // Normalizovaný smer * rýchlosť
            this.pohybX = (int) ((dx / vzdialenost) * this.rychlost);
            this.pohybY = (int) ((dy / vzdialenost) * this.rychlost);
        }

        // Znižuj cooldowny každý tick
        if (this.cooldownNormalny > 0)  this.cooldownNormalny--;
        if (this.cooldownSpecialny > 0) this.cooldownSpecialny--;
    }

    @Override
    public int getRychlost() { return this.rychlost; }

    // ── Utocnik ──────────────────────────────────────────────────────────

    /**
     * utoc() - Boss volí typ útoku podľa fázy.
     *
     * Polymorfizmus: Hrac.utoc() vždy vystrelí 1 normálnu strelu.
     *                Boss.utoc() volí medzi normálnou, spread a raketou.
     * Rovnaká signatúra, úplne iná logika.
     */
    @Override
    public void utoc(int cielX, int cielY, List<Strela> strely) {
        // Špeciálny útok (raketa) - každých 200 tickov, bez ohľadu na fázu
        if (this.cooldownSpecialny <= 0) {
            this.vypalisRaketu(cielX, cielY, strely);
            this.cooldownSpecialny = COOLDOWN_SPECIALNY;
            return;
        }

        if (this.cooldownNormalny > 0) return;

        if (this.faza == 1) {
            // Fáza 1: jedna normálna strela k hráčovi
            this.vypalisNormalnu(cielX, cielY, strely);
        } else {
            // Fáza 2: spread shot - 3 strely do rozptylu
            this.vypalisSpread(cielX, cielY, strely);
        }
        this.cooldownNormalny = COOLDOWN_NORMALNY;
    }

    private void vypalisNormalnu(int cielX, int cielY, java.util.List<Strela> strely) {
        double[] smer = this.vypocitajSmer(cielX, cielY, 7.0);
        int cx = this.x + this.sirka / 2;
        int cy = this.y + this.sirka / 2;
        strely.add(new Strela(cx, cy, smer[0], smer[1], 1, Strela.TypStrely.NORMALNA));
    }

    private void vypalisSpread(int cielX, int cielY, java.util.List<Strela> strely) {
        // 3 strely - priamo, -20°, +20°
        double[] uhly = {-0.35, 0, 0.35};
        int cx = this.x + this.sirka / 2;
        int cy = this.y + this.sirka / 2;
        for (double uhol : uhly) {
            double[] smer = this.vypocitajSmer(cielX, cielY, 6.0);
            double cos = Math.cos(uhol);
            double sin = Math.sin(uhol);
            double rx = smer[0] * cos - smer[1] * sin;
            double ry = smer[0] * sin + smer[1] * cos;
            strely.add(new Strela(cx, cy, rx, ry, 1, Strela.TypStrely.BOSS_SPREAD));
        }
    }

    private void vypalisRaketu(int cielX, int cielY, java.util.List<Strela> strely) {
        double[] smer = this.vypocitajSmer(cielX, cielY, 4.0);
        int cx = this.x + this.sirka / 2;
        int cy = this.y + this.sirka / 2;
        Strela raketa = new Strela(cx, cy, smer[0], smer[1], 1, Strela.TypStrely.RAKETA);
        raketa.setCiel(cielX, cielY);
        strely.add(raketa);
    }

    private double[] vypocitajSmer(int cielX, int cielY, double rychlost) {
        double dx = cielX - this.x;
        double dy = cielY - this.y;
        double d = Math.sqrt(dx * dx + dy * dy);
        if (d == 0) return new double[]{rychlost, 0};
        return new double[]{(dx / d) * rychlost, (dy / d) * rychlost};
    }

    @Override
    public boolean mozeUtocit() {
        return this.cooldownNormalny <= 0 || this.cooldownSpecialny <= 0;
    }

    @Override
    public int dealDmg() {
        return 0;
    }

    @Override
    public void resetCooldown() { this.cooldownNormalny = COOLDOWN_NORMALNY; }

    // ── Postava ──────────────────────────────────────────────────────────

    /**
     * dostaZasah() - Boss sa pri prechode do fázy 2 rozzúri.
     *
     * Polymorfizmus: Hrac.dostaZasah() len odráta HP.
     *                Boss.dostaZasah() odráta HP A môže zmeniť fázu/rýchlosť.
     */
    @Override
    public void dostaZasah(int poskodenie) {
        this.hp -= poskodenie;
        // Prechod do fázy 2 - zrýchlenie pri prvom prechode
        if (this.faza == 1 && this.hp <= FAZA2_PRAH) {
            this.faza = 2;
            this.rychlost = 5; // Boss sa zrýchli
        }
    }

    @Override
    public void vystrel() {
        // Boss fires automatically in utoc()
    }

    @Override
    public int getHP() { return this.hp; }

    @Override
    public void setHP(int hp) { this.hp = Math.min(hp, MAX_HP); }

    @Override
    public void dostanZasah(int poskodenie) {
        this.dostaZasah(poskodenie);
    }

    @Override
    public boolean jeZiva() { return this.hp > 0; }

    @Override
    public int getMaxHP() { return MAX_HP; }

    // ── Paint ────────────────────────────────────────────────────────────

    /**
     * Boss sa nakreslí inak v každej fáze.
     * Fáza 1: červené telo.
     * Fáza 2: tmavočervené, väčší HP bar, "RAGE" nápis.
     */
    @Override
    public void paint(Graphics g) {
        if (this.obrazok != null) {
            g.drawImage(this.obrazok, this.x, this.y, this.sirka, this.vyska, null);
        } else {
            // Kreslíme bossa geometricky ak nie je sprite
            Color bossColor = (this.faza == 2) ? new Color(160, 0, 0) : new Color(200, 30, 30);
            g.setColor(bossColor);
            g.fillRect(this.x, this.y, this.sirka, this.vyska);
            g.setColor(Color.BLACK);
            g.drawRect(this.x, this.y, this.sirka, this.vyska);
        }

        // HP bar
        int barW = this.sirka;
        int filled = (int)((double) this.hp / MAX_HP * barW);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(this.x, this.y - 12, barW, 8);
        g.setColor(this.faza == 2 ? Color.ORANGE : Color.GREEN);
        g.fillRect(this.x, this.y - 12, filled, 8);

        // Fáza 2: "RAGE!" nápis nad bossom
        if (this.faza == 2) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString("RAGE!", this.x + 4, this.y - 16);
        }
    }

    @Override
    public boolean dotyk(HernyObjekt objekt) {
        return false;
    }

    // ── Pohyb aplikácia ──────────────────────────────────────────────────
    public int getPohybX() { return this.pohybX; }
    public int getPohybY() { return this.pohybY; }
    public int getFaza() { return this.faza; }
}
