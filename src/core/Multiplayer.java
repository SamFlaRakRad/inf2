package core;

import players.Hrac;
import powerUps.Heal;
import powerUps.Speed;
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

/**
 * Trieda Multiplayer - hernný režim pre dvoch hráčov hrajúcich proti sebe.
 * Každý hráč ovláda svoju postavu a pokúša sa poraziť druhého hráča.
 * Hra obsahuje náboje na mape, powerupy a systém cooldownu pre strelbou.
 * Hra skončí, keď jeden z hráčov stratí všetko zdravie alebo sa skončia náboje a strely.
 *
 * @author Samuel Ďuriš
 * @version V3
 */
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
    private Image healObr;
    private Image speedObr;

    // Náboje zo mapy
    private ArrayList<Stvorec> nabojeNaMape;
    private Image nabojObr;

    private boolean remiza   = false;
    private boolean vyhralH1 = false;
    private boolean vyhralH2 = false;
    private int pocetPickupov;
    private int speedBoostTrvanie;
    private int healKoef;


    public Multiplayer(Obtiaznost obtiaznost) {
        super(obtiaznost);
        switch (obtiaznost) {
            case LAHKA:
                this.zacinajuceNaboje = 20;
                this.pocetPickupov = 4;
                this.speedBoostTrvanie = 300;
                this.healKoef = 50;
                break;

            case STREDNA:
                this.zacinajuceNaboje = 10;
                this.pocetPickupov = 2;
                this.speedBoostTrvanie = 150;
                this.healKoef = 25;
                break;

            case TAZKA:
                this.zacinajuceNaboje =  5;
                this.pocetPickupov = 1;
                this.speedBoostTrvanie =  75;
                this.healKoef = 10;
                break;

            default:
                this.zacinajuceNaboje = 100;
        }
        this.zasobnikH1 = this.zacinajuceNaboje;
        this.zasobnikH2 = this.zacinajuceNaboje;
        this.hrac1HP = this.hrac1.getHP();
        this.hrac2HP = this.hrac2.getHP();
        this.velkostS = super.getVelkostS();
        this.pridajPickupy();
    }

    @Override
    public void nacitajPostavy() {
        this.hraci = new ArrayList<>();
        this.nabojeNaMape = new ArrayList<>();

        this.nabojObr = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/naboje.png"))).getImage();
        this.hrac1Vpravo = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/hrac1Vpravo.png"))).getImage();
        this.hrac1Vlavo = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/hrac1Vlavo.png"))).getImage();
        this.hrac2Vpravo = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/hrac2Vpravo.png"))).getImage();
        this.hrac2Vlavo = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/hrac2Vlavo.png"))).getImage();
        this.healObr = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/heal.png"))).getImage();
        this.speedObr = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/speed.png"))).getImage();

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
        for (Stvorec naboj : this.nabojeNaMape) {
            naboj.paint(g);
        }
        for (Hrac h : this.hraci) {
            h.paint(g);
        }
    }

    @Override
    public void pridajPickupy() {
        this.getPickupy().clear();
        if (this.pocetPickupov <= 0) {
            return;
        }
        Random rand = new Random();
        int umiestnene = 0;
        int maxPokusov = 500;
        int pokus = 0;

        while (umiestnene < this.pocetPickupov && pokus < maxPokusov) {
            pokus++;
            int row = rand.nextInt(super.getRIADKY());
            int col = rand.nextInt(super.getSTLPCE());
            if (this.getMapa()[row].charAt(col) != '.') {
                continue;
            }
            int x = col * super.getVelkostS();
            int y = row * super.getVelkostS();

            if (umiestnene % 2 == 0) {
                this.getPickupy().add(new Heal(this.healObr, x, y, super.getVelkostS(), this.healKoef));
            } else {
                this.getPickupy().add(new Speed(this.speedObr, x, y,
                        super.getVelkostS(), this.speedBoostTrvanie));
            }
            umiestnene++;
        }
    }

    @Override
    public void pohybPostavami() {
        for (Hrac h : this.hraci) {
            h.pohybSa();
            Image vpravo;
            Image vlavo;
            if (h == this.hrac1) {
                vpravo = this.hrac1Vpravo;
                vlavo  = this.hrac1Vlavo;
            } else {
                vpravo = this.hrac2Vpravo;
                vlavo  = this.hrac2Vlavo;
            }
            if (h.getSmerObr() == 'L') {
                h.setObrazok(vlavo);
            } else {
                h.setObrazok(vpravo);
            }
            super.pohybVSmere(h, h.getPohybX(), h.getPohybY());
        }
    }


    @Override
    public void spracujUtok() {
        for (int i = 0; i < this.hraci.size(); i++) {
            Hrac h = this.hraci.get(i);
            int zasobnik;
            if (h == this.hrac1) {
                zasobnik = this.zasobnikH1;
            } else {
                zasobnik = this.zasobnikH2;
            }
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
    public void spracujKoliziu(Iterator<Strela> it, Strela strela) {
        for (int i = 0; i < this.hraci.size(); i++) {
            Hrac h = this.hraci.get(i);
            if (strela.getStrana() != i && strela.koliduje(h)) {
                h.dostanZasah(25);
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
    public void koniec() {
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
                if (z instanceof Heal && this.hrac1.getHP() == 100) {
                    continue;
                }
                z.pouzi(this.hrac1);
            }
        }
        for (Zberatelny z : this.getPickupy()) {
            if (!z.jeZobrany() && this.hrac2.koliduje((HernyObjekt)z)) {
                if (z instanceof Heal && this.hrac2.getHP() == 100) {
                    continue;
                }
                z.pouzi(this.hrac2);
            }
        }

        this.getPickupy().removeIf(Zberatelny::jeZobrany);
    }

    @Override
    public void hud(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("P1 ammo: " + this.zasobnikH1, 10, 670);
        g.drawString("P2 ammo: " + this.zasobnikH2, this.getWidth() - 220, 670);
        g.drawString("HP: " + this.hrac1.getHP(), 150, 670);
        g.drawString("HP: " + this.hrac2.getHP(), this.getWidth() - 90, 670);

        if (this.isStopnutaHra()) {
            String text;
            if (this.remiza) {
                text = "REMÍZA!";
            } else if (this.vyhralH1) {
                text = "Hráč 1 vyhral!";
            } else {
                text = "Hráč 2 vyhral!";
            }
            g.setFont(new Font("Arial", Font.BOLD, 40));
            FontMetrics fm = g.getFontMetrics();
            int x = (this.getWidth()  - fm.stringWidth(text)) / 2;
            int y = (this.getHeight() + fm.getAscent())       / 2;
            g.setColor(Color.RED);
            g.drawString(text, x, y);
            g.setFont(new Font("Arial", Font.PLAIN, 30));
            g.drawString("Stlac ESC pre restart", x + 20, y + 30);
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
        this.zasobnikH1 = this.zacinajuceNaboje;
        this.zasobnikH2 = this.zacinajuceNaboje;
        this.remiza = false;
        this.vyhralH1 = false;
        this.vyhralH2 = false;
        this.setStopnutaHra(false);
        this.startGameLoop();
    }

}
