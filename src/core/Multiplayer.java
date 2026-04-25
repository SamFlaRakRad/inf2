package core;

import players.Bot;
import players.Hrac;
import powerUps.Zberatelny;
import weapons.Strela;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

public class Multiplayer extends RezimHry {
    private ArrayList<Hrac> hraci;
    private Hrac hrac1;
    private Hrac hrac2;

    private int zasobnikH1, zasobnikH2, zacinajuceNaboje;

    private Image hrac1Vpravo, hrac1Vlavo;
    private Image hrac2Vpravo, hrac2Vlavo;

    private boolean remiza   = false;
    private boolean vyhralH1 = false;
    private boolean vyhralH2 = false;

    public Multiplayer(Obtiaznost obtiaznost) {
        super(obtiaznost);
        switch (obtiaznost) {
            case LAHKA:   this.zacinajuceNaboje = 20; break;
            case STREDNA: this.zacinajuceNaboje = 10; break;
            case TAZKA:   this.zacinajuceNaboje = 5;  break;
            default:      this.zacinajuceNaboje = 10;
        }
        this.zasobnikH1 = this.zacinajuceNaboje;
        this.zasobnikH2 = this.zacinajuceNaboje;
    }

    @Override
    protected void nacitajPostavy() {
        this.hraci = new ArrayList<>();

        this.hrac1Vpravo = new ImageIcon(getClass().getResource("/images/hrac1Vpravo.png")).getImage();
        this.hrac1Vlavo  = new ImageIcon(getClass().getResource("/images/hrac1Vlavo.png")).getImage();
        this.hrac2Vpravo = new ImageIcon(getClass().getResource("/images/hrac2Vpravo.png")).getImage();
        this.hrac2Vlavo  = new ImageIcon(getClass().getResource("/images/hrac2Vlavo.png")).getImage();

        for (int r = 0; r < RIADKY; r++) {
            for (int s = 0; s < STLPCE; s++) {
                char ch = this.mapa[r].charAt(s);
                int x = s * VELKOST_S, y = r * VELKOST_S;
                if (ch == 'H') {
                    this.hrac1 = new Hrac(this.hrac1Vpravo, x, y, VELKOST_S, 'R',
                            KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D,
                            KeyEvent.VK_SPACE, this.stlaceneKlavesy);
                    this.hraci.add(this.hrac1);
                }
                if (ch == 'P') {
                    Bot bot = new Bot(this.hrac2Vlavo, x, y, VELKOST_S, this.stlaceneKlavesy);
                    this.hrac2 = bot;
                    this.hraci.add(bot);
                }
            }
        }
        if (this.hrac2 instanceof Bot && this.hrac1 != null) {
            ((Bot) this.hrac2).sledujCiel(this.hrac1);
        }
    }

    /**
     * POLYMORFIZMUS: pohybSa() na Hrac aj Bot.
     * Hrac.pohybSa() → klávesy, Bot.pohybSa() → AI (override v Bot).
     */
    @Override
    protected void pohybPostavami() {
        for (Hrac h : this.hraci) {
            h.pohybSa(); // runtime dispatch
            Image vpravo = (h == this.hrac1) ? this.hrac1Vpravo : this.hrac2Vpravo;
            Image vlavo  = (h == this.hrac1) ? this.hrac1Vlavo  : this.hrac2Vlavo;
            h.setObrazok(h.getSmerObr() == 'L' ? vlavo : vpravo);
            this.aplikujPohybSKolizou(h);
        }
    }

    /**
     * POLYMORFIZMUS: utoc() na Hrac aj Bot (cez Postava interface).
     * Hrac.utoc() → 1 strela smerom k cieľu (strana 0).
     * Bot.utoc()  → 1 strela smerom k cieľu (strana 1, override).
     */
    @Override
    protected void spracujUtok() {
        for (int i = 0; i < this.hraci.size(); i++) {
            Hrac h    = this.hraci.get(i);
            int zasobnik = (i == 0) ? this.zasobnikH1 : this.zasobnikH2;
            if (zasobnik <= 0) continue;
            Hrac ciel = this.hraci.get(1 - i);
            int pred  = this.strely.size();
            h.utoc(ciel.getX(), ciel.getY(), this.strely);
            if (this.strely.size() > pred) {
                if (i == 0) this.zasobnikH1--; else this.zasobnikH2--;
            }
        }
    }

    /**
     * Strela trafí opačného hráča.
     * dostaZasah() je Postava.dostaZasah() — polymorfne.
     */
    @Override
    protected void skontrolujPostaveKolizie(Iterator<Strela> it, Strela strela) {
        for (int i = 0; i < this.hraci.size(); i++) {
            Hrac h = this.hraci.get(i);
            if (strela.getStrana() != i && strela.koliduje(h)) {
                h.dostaZasah(1);
                if (!h.jeZiva()) {
                    if (i == 0) this.vyhralH2 = true; else this.vyhralH1 = true;
                    this.stopnutaHra = true;
                }
                it.remove();
                return;
            }
        }
    }

    @Override
    protected void skontrolujKoniec() {
        if (this.zasobnikH1 == 0 && this.zasobnikH2 == 0 && this.strely.isEmpty()) {
            this.remiza = true;
            this.stopnutaHra = true;
        }
    }

    /**
     * POLYMORFIZMUS: Zberatelny.pouzi(Postava).
     * Heal.pouzi() → obnoví HP, Speed.pouzi() → zrýchli.
     */
    @Override
    protected void skontrolujPickupy() {
        for (Zberatelny z : this.pickupy) {
            if (z.jeZobrany()) continue;
            for (Hrac h : this.hraci) {
                if (h.koliduje((HernyObjekt) z)) z.pouzi(h);
            }
        }
        this.pickupy.removeIf(Zberatelny::jeZobrany);
    }

    @Override
    protected void kresliHUD(Graphics g) {
        for (Hrac h : this.hraci) {
            h.paint(g);
            g.setColor(java.awt.Color.RED);
            for (int i = 0; i < h.getHP(); i++)
                g.fillRect(h.getX() + i * 10, h.getY() - 10, 8, 6);
        }

        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 25));
        g.setColor(java.awt.Color.BLACK);
        if      (this.vyhralH1) g.drawString("Hrac1 vyhral!",     310, 670);
        else if (this.vyhralH2) g.drawString("Bot vyhral!",        310, 670);
        else if (this.remiza)   g.drawString("Remíza!",            340, 670);
        else {
            g.drawString("Ammo P1: " + this.zasobnikH1, 30,  670);
            g.drawString("Ammo P2: " + this.zasobnikH2, 620, 670);
        }
        if (this.stopnutaHra) {
            g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 32));
            g.setColor(java.awt.Color.RED);
            g.drawString("ESC = restart", 270, 400);
        }
    }

    @Override
    protected void keyPressedExtra(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && this.hrac1 != null) this.hrac1.vystrel();
    }

    @Override
    protected void restart() {
        Generator gen = new Generator();
        this.mapa = gen.vytvorMapu(this.obtiaznost);
        this.nacitajSteny();
        this.strely.clear();
        this.nacitajPostavy();
        this.pridajPickupy();
        this.zasobnikH1  = this.zacinajuceNaboje;
        this.zasobnikH2  = this.zacinajuceNaboje;
        this.remiza      = false;
        this.vyhralH1    = false;
        this.vyhralH2    = false;
        this.stopnutaHra = false;
        this.gameLoop.start();
    }

    private void aplikujPohybSKolizou(Hrac h) {
        int nx = h.getX() + h.getPohybX();
        int ny = h.getY() + h.getPohybY();
        h.setX(nx); h.setY(ny);
        for (Stvorec stena : this.steny) {
            if (h.koliduje(stena)) {
                h.setX(nx - h.getPohybX());
                h.setY(ny - h.getPohybY());
                break;
            }
        }
    }
}
