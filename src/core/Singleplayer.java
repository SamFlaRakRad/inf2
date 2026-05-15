package core;

import players.Boss;
import players.Bot;
import players.Hrac;
import players.Postava;
import powerUps.Zberatelny;
import weapons.Strela;

import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Objects;

public class Singleplayer extends RezimHry {
    private Hrac hrac;
    private Postava protivnik;
    private boolean jeBoss;

    private int zacinajuceNaboje;
    private int zasobnikHraca;


    private Image hrac1Vpravo;
    private Image hrac1Vlavo;

    private boolean hracVyhral      = false;
    private boolean protivnikVyhral = false;

    public Singleplayer(Obtiaznost obtiaznost, TypEnemaka typEnemaka) {
        super(obtiaznost);
        switch (obtiaznost) {
            case LAHKA:
                this.zacinajuceNaboje = 20;
                break;
            case STREDNA:
                this.zacinajuceNaboje = 10;
                break;
            case TAZKA:
                this.zacinajuceNaboje =  5;
                break;
            default:
                this.zacinajuceNaboje = 100;
        }
        this.jeBoss = (typEnemaka == TypEnemaka.BOSS);
        this.zasobnikHraca = this.zacinajuceNaboje;
        this.nacitajPostavy();
    }

    @Override
    public void nacitajPostavy() {
        this.hrac1Vpravo = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/hrac1Vpravo.png"))).getImage();
        this.hrac1Vlavo  = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/hrac1Vlavo.png"))).getImage();
        Image hrac2Img   = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/hrac2Vlavo.png"))).getImage();

        for (int r = 0; r < super.getRIADKY(); r++) {
            for (int s = 0; s < super.getSTLPCE(); s++) {
                char ch = this.getMapa()[r].charAt(s);
                int x = s * super.getVelkostS();
                int y = r * super.getVelkostS();

                if (ch == 'H') {
                    this.hrac = new Hrac(this.hrac1Vpravo, x, y, super.getVelkostS(), 'R',
                            KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D,
                            KeyEvent.VK_SPACE, this.getStlaceneKlavesy(), 0);
                }

                if (ch == 'P') {
                    if (this.jeBoss) {
                        this.protivnik = new Boss(null, x, y, super.getVelkostS() * 2);
                    } else {
                        Bot bot = new Bot(hrac2Img, x, y, super.getVelkostS(), this.getStlaceneKlavesy());
                        this.protivnik = bot;
                    }
                }
            }
        }

        if (this.hrac != null && this.protivnik != null) {
            if (this.protivnik instanceof Boss) {
                ((Boss)this.protivnik).setCiel(this.hrac);
            } else if (this.protivnik instanceof Bot) {
                ((Bot)this.protivnik).sledujCiel(this.hrac);
            }
        }
    }

    @Override
    public void pohybPostavami() {
        if (this.hrac == null || this.protivnik == null) {
            return;
        }

        // Hráč
        this.hrac.pohybSa();
        this.hrac.setObrazok(this.hrac.getSmerObr() == 'L' ? this.hrac1Vlavo : this.hrac1Vpravo);
        this.aplikujPohybSKolizou(this.hrac, this.hrac.getPohybX(), this.hrac.getPohybY());

        // enemy
        this.protivnik.pohybSa();
        this.aplikujPohybSKolizou((HernyObjekt)this.protivnik,
                this.protivnik.getPohybX(), this.protivnik.getPohybY());

        for (Strela s : this.getStrely()) {
            if (s.getTyp() == Strela.TypStrely.RAKETA) {
                s.aktualizujCiel(this.hrac.getX(), this.hrac.getY());
            }
        }
    }

    @Override
    public void spracujUtok() {
        if (this.hrac == null || this.protivnik == null) {
            return;
        }

        // Hráč
        if (this.hrac.getVystrelena() && this.zasobnikHraca > 0) {
            int pred = this.getStrely().size();
            this.hrac.utoc(this.protivnik.getX(), this.protivnik.getY(), this.getStrely());
            if (this.getStrely().size() > pred) {
                this.zasobnikHraca--;
            }
        }

        // enemy
        if (this.protivnik.mozeUtocit()) {
            this.protivnik.utoc(this.hrac.getX(), this.hrac.getY(), this.getStrely());
        }
    }

    @Override
    public void skontrolujPostaveKolizie(Iterator<Strela> it, Strela strela) {
        HernyObjekt protObj = (HernyObjekt)this.protivnik;
        if (strela.getStrana() == 0 && strela.koliduje(protObj)) {
            this.protivnik.dostaZasah(1);   // polymorfné – Boss mení fázu
            it.remove();
        } else if (strela.getStrana() == 1 && strela.koliduje(this.hrac)) {
            this.hrac.dostaZasah(20);
            it.remove();
        }
    }

    @Override
    public void skontrolujKoniec() {
        if (this.protivnik != null && !this.protivnik.jeZiva()) {
            this.hracVyhral = true;
            this.setStopnutaHra(true);
        }
        if (this.hrac != null && !this.hrac.jeZiva()) {
            this.protivnikVyhral = true;
            this.setStopnutaHra(true);
        }
    }

    @Override
    public void kresliHUD(Graphics g) {

    }

    @Override
    public void skontrolujPickupy() {
        if (this.hrac == null) {
            return;
        }
        for (Zberatelny z : this.getPickupy()) {
            if (!z.jeZobrany() && this.hrac.koliduje((HernyObjekt)z)) {
                z.pouzi(this.hrac);
            }
        }
        this.getPickupy().removeIf(Zberatelny::jeZobrany);
    }

    @Override
    public void keyPressedExtra(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && this.hrac != null) {
            this.hrac.vystrel();
        }
    }

    @Override
    public void restart() {
        Generator gen = new Generator();
        this.setMapa(gen.vytvorMapu(this.getObtiaznost()));
        this.nacitajSteny();
        this.getStrely().clear();
        this.nacitajPostavy();
        this.pridajPickupy();
        this.zasobnikHraca = this.zacinajuceNaboje;
        this.hracVyhral = false;
        this.protivnikVyhral = false;
        this.setStopnutaHra(false);
        this.startGameLoop();
    }

    @Override
    public void kresliPostavy(Graphics g) {

    }

    private void aplikujPohybSKolizou(HernyObjekt obj, int dx, int dy) {
        obj.setX(obj.getX() + dx);
        obj.setY(obj.getY() + dy);
        for (HernyObjekt stena : this.getSteny()) {
            if (obj.koliduje(stena)) {
                obj.setX(obj.getX() - dx);
                obj.setY(obj.getY() - dy);
                break;
            }
        }
    }
}
