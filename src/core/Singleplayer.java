package core;

import players.Boss;
import players.Hrac;
import powerUps.Zberatelny;
import weapons.Strela;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Iterator;

public class Singleplayer extends RezimHry {
    private Hrac hrac;
    private Boss boss;

    private int zasobnikHraca;
    private static final int ZASOBNIK_MAX = 30;

    private Image hrac1Vpravo, hrac1Vlavo;

    private boolean hracVyhral = false;
    private boolean bossVyhral = false;

    public Singleplayer(Obtiaznost obtiaznost) {
        super(obtiaznost);
        this.zasobnikHraca = ZASOBNIK_MAX;
    }

    @Override
    protected void nacitajPostavy() {
        this.hrac1Vpravo = new ImageIcon(getClass().getResource("/images/hrac1Vpravo.png")).getImage();
        this.hrac1Vlavo  = new ImageIcon(getClass().getResource("/images/hrac1Vlavo.png")).getImage();

        for (int r = 0; r < RIADKY; r++) {
            for (int s = 0; s < STLPCE; s++) {
                char ch = this.mapa[r].charAt(s);
                int x = s * VELKOST_S, y = r * VELKOST_S;
                if (ch == 'H') {
                    this.hrac = new Hrac(this.hrac1Vpravo, x, y, VELKOST_S, 'R',
                            KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D,
                            KeyEvent.VK_SPACE, this.stlaceneKlavesy);
                }
                if (ch == 'P') {
                    this.boss = new Boss(null, x, y, VELKOST_S * 2);
                }
            }
        }
        if (this.boss != null && this.hrac != null) this.boss.setCiel(this.hrac);
    }

    @Override
    protected void pohybPostavami() {
        if (this.hrac == null || this.boss == null) return;

        this.hrac.pohybSa();
        this.hrac.setObrazok(this.hrac.getSmerObr() == 'L' ? this.hrac1Vlavo : this.hrac1Vpravo);
        this.aplikujPohybSKolizou(this.hrac, this.hrac.getPohybX(), this.hrac.getPohybY());

        this.boss.pohybSa();
        this.aplikujPohybSKolizou(this.boss, this.boss.getPohybX(), this.boss.getPohybY());

        // Navádzacie strely sledujú hráča každý tick
        for (Strela s : this.strely) {
            if (s.getTyp() == Strela.TypStrely.RAKETA)
                s.aktualizujCiel(this.hrac.getX(), this.hrac.getY());
        }
    }

    /**
     * POLYMORFIZMUS: utoc() cez Postava interface.
     * hrac.utoc()  → 1 normálna strela.
     * boss.utoc()  → normal / spread / raketa podľa HP fázy.
     */
    @Override
    protected void spracujUtok() {
        if (this.hrac == null || this.boss == null) return;

        if (this.hrac.getVystrelena() && this.zasobnikHraca > 0) {
            int pred = this.strely.size();
            this.hrac.utoc(this.boss.getX(), this.boss.getY(), this.strely);
            if (this.strely.size() > pred) this.zasobnikHraca--;
        }

        if (this.boss.mozeUtocit()) {
            this.boss.utoc(this.hrac.getX(), this.hrac.getY(), this.strely);
        }
    }

    /**
     * Strela hráča (strana 0) trafí bossa.
     * Bossova strela (strana 1) trafí hráča.
     * Boss.dostaZasah() môže zmeniť fázu — polymorfizmus.
     */
    @Override
    protected void skontrolujPostaveKolizie(Iterator<Strela> it, Strela strela) {
        if (this.boss == null || this.hrac == null) return;
        if (strela.getStrana() == 0 && strela.koliduje(this.boss)) {
            this.boss.dostaZasah(1);
            it.remove();
        } else if (strela.getStrana() == 1 && strela.koliduje(this.hrac)) {
            this.hrac.dostaZasah(1);
            it.remove();
        }
    }

    @Override
    protected void skontrolujKoniec() {
        if (this.boss != null && !this.boss.jeZiva()) { this.hracVyhral = true; this.stopnutaHra = true; }
        if (this.hrac != null && !this.hrac.jeZiva()) { this.bossVyhral = true; this.stopnutaHra = true; }
    }

    /**
     * POLYMORFIZMUS: Zberatelny.pouzi(Postava).
     * Heal.pouzi() → HP+1, Speed.pouzi() → rýchlosť+3.
     */
    @Override
    protected void skontrolujPickupy() {
        if (this.hrac == null) return;
        for (Zberatelny z : this.pickupy) {
            if (!z.jeZobrany() && this.hrac.koliduje((HernyObjekt) z)) z.pouzi(this.hrac);
        }
        this.pickupy.removeIf(Zberatelny::jeZobrany);
    }

    @Override
    protected void kresliHUD(Graphics g) {
        if (this.hrac != null) {
            this.hrac.paint(g);
            g.setColor(Color.RED);
            for (int i = 0; i < this.hrac.getHP(); i++)
                g.fillRect(20 + i * 14, 645, 10, 10);
        }
        if (this.boss != null) this.boss.paint(g);

        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.setColor(Color.BLACK);
        g.drawString("Ammo: " + this.zasobnikHraca, 20, 638);
        if (this.boss != null)
            g.drawString("Boss fáza: " + this.boss.getFaza(), 350, 638);

        if      (this.hracVyhral) { g.setFont(new Font("Arial", Font.BOLD, 50)); g.setColor(new Color(0,150,0)); g.drawString("VYHRAL SI!", 230, 350); }
        else if (this.bossVyhral) { g.setFont(new Font("Arial", Font.BOLD, 50)); g.setColor(Color.RED);          g.drawString("BOSS VYHRAL!", 180, 350); }
        if (this.stopnutaHra) {
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.setColor(Color.DARK_GRAY);
            g.drawString("ESC = restart", 270, 420);
        }
    }

    @Override
    protected void keyPressedExtra(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && this.hrac != null) this.hrac.vystrel();
    }

    @Override
    protected void restart() {
        Generator gen = new Generator();
        this.mapa = gen.vytvorMapu(this.obtiaznost);
        this.nacitajSteny();
        this.strely.clear();
        this.nacitajPostavy();
        this.pridajPickupy();
        this.zasobnikHraca = ZASOBNIK_MAX;
        this.hracVyhral    = false;
        this.bossVyhral    = false;
        this.stopnutaHra   = false;
        this.gameLoop.start();
    }

    private void aplikujPohybSKolizou(HernyObjekt obj, int dx, int dy) {
        int nx = obj.getX() + dx;
        int ny = obj.getY() + dy;
        obj.setX(nx); obj.setY(ny);
        for (Stvorec stena : this.steny) {
            if (obj.koliduje(stena)) { obj.setX(nx - dx); obj.setY(ny - dy); break; }
        }
    }
}
