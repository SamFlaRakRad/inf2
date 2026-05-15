package core;

import players.Hrac;
import powerUps.Heal;
import powerUps.Zberatelny;
import weapons.Strela;

import java.util.Random;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Multiplayer extends RezimHry {
    private Hrac hrac1;
    private Hrac hrac2;
    private ArrayList<Hrac> hraci;
    private int hrac1HP;
    private int hrac2HP;

    private int zasobnikH1;
    private int zasobnikH2;
    private int zacinajuceNaboje;
    private int velkostS;

    private Image hrac1Vpravo;
    private Image hrac1Vlavo;
    private Image hrac2Vpravo;
    private Image hrac2Vlavo;

    // Náboje zo mapy
    private ArrayList<Stvorec> nabojeNaMape;
    private Image nabojObr;

    private boolean remiza   = false;
    private boolean vyhralH1 = false;
    private boolean vyhralH2 = false;

    public Multiplayer(Obtiaznost obtiaznost) {
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
        this.zasobnikH1 = this.zacinajuceNaboje;
        this.zasobnikH2 = this.zacinajuceNaboje;
        this.hrac1HP = this.hrac1.getHP();
        this.hrac2HP = this.hrac2.getHP();
        this.velkostS = super.getVelkostS();
    }

    @Override
    public void nacitajPostavy() {
        this.hraci = new ArrayList<>();
        this.nabojeNaMape = new ArrayList<>();

        this.nabojObr    = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/naboje.png"))).getImage();
        this.hrac1Vpravo = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/hrac1Vpravo.png"))).getImage();
        this.hrac1Vlavo  = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/hrac1Vlavo.png"))).getImage();
        this.hrac2Vpravo = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/hrac2Vpravo.png"))).getImage();
        this.hrac2Vlavo  = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/hrac2Vlavo.png"))).getImage();

        for (int r = 0; r < super.getRIADKY(); r++) {
            for (int s = 0; s < super.getSTLPCE(); s++) {
                char ch = this.getMapa()[r].charAt(s);
                int x = s * super.getVelkostS();
                int y = r * super.getVelkostS();

                if (ch == 'H') {
                    // Hráč 1
                    this.hrac1 = new Hrac(this.hrac1Vpravo, x, y, super.getVelkostS(), 'R',
                            KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D,
                            KeyEvent.VK_SPACE, this.getStlaceneKlavesy(), 0);
                    this.hraci.add(this.hrac1);
                }
                if (ch == 'P') {
                    // Hráč 2
                    this.hrac2 = new Hrac(this.hrac2Vlavo, x, y, super.getVelkostS(), 'L',
                            KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
                            KeyEvent.VK_ENTER, this.getStlaceneKlavesy(), 1);
                    this.hraci.add(this.hrac2);
                }
                if (ch == 'A') {
                    this.nabojeNaMape.add(new Stvorec(this.nabojObr, x, y, super.getVelkostS(), super.getVelkostS()));
                }
            }
        }
    }

    @Override
    public void kresliPostavy(Graphics g) {
        // draw ammo pickups on map
        for (Stvorec naboj : this.nabojeNaMape) {
            naboj.paint(g);
        }
        // draw both players
        for (Hrac h : this.hraci) {
            h.paint(g);
        }
    }

    @Override
    public void pridajPickupy() {
        Random rand = new Random();
        int x;
        int y;
        do {
            x = rand.nextInt(20);
            y = rand.nextInt(25);
        } while (this.getMapa()[x].charAt(y) != '.');

        this.getPickupy().add(new Heal(
                new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/heal.png"))).getImage(),
                y * super.getVelkostS(), x * super.getVelkostS(), super.getVelkostS()));
    }

    @Override
    public void pohybPostavami() {
        for (Hrac h : this.hraci) {
            h.pohybSa();
            Image vpravo = (h == this.hrac1) ? this.hrac1Vpravo : this.hrac2Vpravo;
            Image vlavo  = (h == this.hrac1) ? this.hrac1Vlavo  : this.hrac2Vlavo;
            h.setObrazok(h.getSmerObr() == 'L' ? vlavo : vpravo);
            this.aplikujPohybSKolizou(h);
        }
    }



    @Override
    public void spracujUtok() {
        for (int i = 0; i < this.hraci.size(); i++) {
            Hrac h       = this.hraci.get(i);
            int zasobnik = (h == this.hrac1) ? this.zasobnikH1 : this.zasobnikH2;
            if (zasobnik <= 0 || !h.getVystrelena()) {
                continue;
            }

            Hrac ciel = this.hraci.get(1 - i);
            int pred  = this.getStrely().size();
            h.utoc(ciel.getX(), ciel.getY(), this.getStrely());
            if (this.getStrely().size() > pred) {
                if (h == this.hrac1) {
                    this.zasobnikH1--;
                } else {
                    this.zasobnikH2--;
                }
            }
        }
    }

    @Override
    public void skontrolujPostaveKolizie(Iterator<Strela> it, Strela strela) {
        for (int i = 0; i < this.hraci.size(); i++) {
            Hrac h = this.hraci.get(i);
            if (strela.getStrana() != i && strela.koliduje(h)) {
                h.dostaZasah(25);   // 4 zásahy = koniec
                if (!h.jeZiva()) {
                    if (i == 0) {
                        this.vyhralH2 = true;
                    } else {
                        this.vyhralH1 = true;
                    }
                    this.setStopnutaHra(true);
                }
                it.remove();
                return;
            }
        }
    }

    @Override
    public void skontrolujKoniec() {
        if (this.zasobnikH1 == 0 && this.zasobnikH2 == 0
                && this.getStrely().isEmpty() && this.nabojeNaMape.isEmpty()) {
            this.remiza = true;
            this.setStopnutaHra(true);
        }
    }

    @Override
    public void skontrolujPickupy() {
        Stvorec zobrany = null;
        for (Stvorec naboj : this.nabojeNaMape) {
            for (Hrac h : this.hraci) {
                if (h.koliduje(naboj)) {
                    if (h == this.hrac1) {
                        this.zasobnikH1 += this.zacinajuceNaboje;
                    } else {
                        this.zasobnikH2 += this.zacinajuceNaboje;
                    }
                    zobrany = naboj;
                    break;
                }
            }
            if (zobrany != null) {
                break;
            }
        }
        if (zobrany != null) {
            this.nabojeNaMape.remove(zobrany);
        }

        if (this.hrac1 == null || this.hrac2 == null) {
            return;
        }
        for (Zberatelny z : this.getPickupy()) {
            if (!z.jeZobrany() && this.hrac1.koliduje((HernyObjekt)z)) {
                z.pouzi(this.hrac1);
            }
        }
        for (Zberatelny z : this.getPickupy()) {
            if (!z.jeZobrany() && this.hrac2.koliduje((HernyObjekt)z)) {
                z.pouzi(this.hrac2);
            }
        }

        this.getPickupy().removeIf(Zberatelny::jeZobrany);
    }

    @Override
    public void kresliHUD(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("P1 ammo: " + this.zasobnikH1, 10, 670);
        g.drawString("P2 ammo: " + this.zasobnikH2, this.getWidth() - 220, 670);
        g.drawString("HP: " + this.hrac1.getHP(), 150, 670);
        g.drawString("HP: " + this.hrac2.getHP(), this.getWidth() - 90, 670);

        if (this.isStopnutaHra()) {
            String msg = this.remiza   ? "REMÍZA!"
                    : this.vyhralH1 ? "Hráč 1 vyhral!"
                      :                 "Hráč 2 vyhral!";
            g.setFont(new Font("Arial", Font.BOLD, 36));
            FontMetrics fm = g.getFontMetrics();
            int x = (getWidth()  - fm.stringWidth(msg)) / 2;
            int y = (getHeight() + fm.getAscent())       / 2;
            g.setColor(Color.RED);
            g.drawString(msg, x, y);
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("ESC = reštart", x + 20, y + 30);
        }
    }

    @Override
    public void keyPressedExtra(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && this.hrac1 != null) {
            this.hrac1.vystrel();
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER && this.hrac2 != null) {
            this.hrac2.vystrel();
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
        this.zasobnikH1  = this.zacinajuceNaboje;
        this.zasobnikH2  = this.zacinajuceNaboje;
        this.remiza      = false;
        this.vyhralH1    = false;
        this.vyhralH2    = false;
        this.setStopnutaHra(false);
        this.startGameLoop();
    }

    private void aplikujPohybSKolizou(Hrac h) {
        int nx = h.getX() + h.getPohybX();
        int ny = h.getY() + h.getPohybY();
        h.setX(nx);
        h.setY(ny);
        for (HernyObjekt stena : this.getSteny()) {
            if (h.koliduje(stena)) {
                h.setX(nx - h.getPohybX());
                h.setY(ny - h.getPohybY());
                break;
            }
        }
    }
}
