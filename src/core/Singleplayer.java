package core;

import players.Boss;
import players.Bot;
import players.Hrac;
import players.Ai;
import powerUps.Heal;
import powerUps.Speed;
import powerUps.Zberatelny;
import weapons.Strela;

import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;

/**
 * Herný režim Singleplayer - hra proti AI nepriateľovi (Bot alebo Boss).
 * Hráč sa pohybuje a strieľa na AI, ktorá používa pathfinding na sledovanie hráča.
 *
 * @author Samuel Ďuriš
 * @version V3
 */
public class Singleplayer extends RezimHry {
    private Hrac hrac;
    private Ai protivnik;
    private boolean jeBoss;

    private int zacinajuceNaboje;
    private int zasobnikHraca;
    private int poskodenieHraca;
    private int poskodenieProtivnika;

    private Image hrac1Vpravo;
    private Image hrac1Vlavo;
    private Image botVpravo;
    private Image botVlavo;
    private Image bossVpravo;
    private Image bossVlavo;
    private Image healObr;
    private Image speedObr;

    private boolean hracVyhral = false;
    private boolean protivnikVyhral = false;

    private int pocetPickupov;
    private int speedBoostTrvanie;
    private int healKoef;
    private ArrayList<Stvorec> nabojeNaMape;
    private Image nabojObr;

    public Singleplayer(Obtiaznost obtiaznost, TypEnemaka typEnemaka) {
        super(obtiaznost);
        switch (obtiaznost) {
            case LAHKA:
                this.zacinajuceNaboje = 20;
                this.pocetPickupov = 10;
                this.speedBoostTrvanie = 300;
                this.healKoef = 50;
                this.poskodenieHraca = 25;
                this.poskodenieProtivnika = 5;
                break;
            case STREDNA:
                this.zacinajuceNaboje = 10;
                this.pocetPickupov = 7;
                this.speedBoostTrvanie = 150;
                this.healKoef = 25;
                this.poskodenieHraca = 10;
                this.poskodenieProtivnika = 10;
                break;
            case TAZKA:
                this.zacinajuceNaboje =  5;
                this.pocetPickupov = 5;
                this.speedBoostTrvanie =  75;
                this.healKoef = 10;
                this.poskodenieHraca = 5;
                this.poskodenieProtivnika = 25;
                break;
            default:
                this.zacinajuceNaboje = 100;
        }
        this.jeBoss = (typEnemaka == TypEnemaka.BOSS);
        this.zasobnikHraca = this.zacinajuceNaboje;
        this.nacitajPostavy();
        this.pridajPickupy();
    }

    @Override
    public void nacitajPostavy() {
        this.nabojeNaMape = new ArrayList<>();
        this.hrac1Vpravo = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/hrac1Vpravo.png"))).getImage();
        this.hrac1Vlavo = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/hrac1Vlavo.png"))).getImage();
        this.botVlavo = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/botVlavo.png"))).getImage();
        this.botVpravo = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/botVpravo.png"))).getImage();
        this.bossVlavo = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/bossVlavo.png"))).getImage();
        this.bossVpravo = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/bossVpravo.png"))).getImage();
        this.healObr = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/heal.png"))).getImage();
        this.speedObr = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/speed.png"))).getImage();
        this.nabojObr = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/naboje.png"))).getImage();

        for (int r = 0; r < super.getRiadky(); r++) {
            for (int s = 0; s < super.getStlpce(); s++) {
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
                        Boss boss = new Boss(this.bossVlavo, x, y, super.getVelkostS());
                        boss.setObrazky(this.bossVlavo, this.bossVpravo);
                        this.protivnik = boss;
                    } else {
                        Bot bot = new Bot(this.botVlavo, x, y, super.getVelkostS());
                        bot.setObrazky(this.botVlavo, this.botVpravo);
                        this.protivnik = bot;
                    }
                }
                if (ch == 'A') {
                    this.nabojeNaMape.add(new Stvorec(this.nabojObr, x, y, super.getVelkostS(), super.getVelkostS()));
                }
            }
        }

        if (this.hrac != null && this.protivnik != null) {
            this.protivnik.sledujCiel(this.hrac);
        }

        if (this.protivnik != null) {
            this.protivnik.nastavMapu(this.getMapa(), super.getVelkostS());
        }
    }

    @Override
    public void pohybPostavami() {
        this.hrac.pohybSa();
        if (this.hrac.getSmerObr() == 'L') {
            this.hrac.setObrazok(this.hrac1Vlavo);
        } else {
            this.hrac.setObrazok(this.hrac1Vpravo);
        }
        super.pohybVSmere(this.hrac, this.hrac.getPohybX(), this.hrac.getPohybY());

        this.protivnik.pohybSa();
        if (this.protivnik.getSmerObr() == 'L') {
            this.protivnik.setObrazok(this.protivnik.getVlavo());
        } else {
            this.protivnik.setObrazok(this.protivnik.getVpravo());
        }
        super.pohybVSmere(this.protivnik, this.protivnik.getPohybX(), this.protivnik.getPohybY());


    }

    @Override
    public void spracujUtok() {
        if (this.hrac.getVystrelena() && this.zasobnikHraca > 0) {
            int pred = this.getStrely().size();
            this.hrac.utoc(this.protivnik.getX(), this.protivnik.getY(), this.getStrely());
            if (this.getStrely().size() > pred) {
                this.zasobnikHraca--;
            }
        }
        if (this.protivnik.mozeUtocit()) {
            this.protivnik.utoc(this.hrac.getX(), this.hrac.getY(), this.getStrely());
        }
    }

    @Override
    public void spracujKoliziu(Iterator<Strela> it, Strela strela) {
        if (strela.getStrana() == 0 && strela.koliduje(this.protivnik)) {
            this.protivnik.dostanZasah(this.poskodenieHraca);
            it.remove();
        } else if (strela.getStrana() == 1 && strela.koliduje(this.hrac)) {
            this.hrac.dostanZasah(this.poskodenieProtivnika);
            it.remove();
        }
    }

    @Override
    public void koniec() {
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
    public void skontrolujPickupy() {
        Stvorec zobrany = null;
        for (Stvorec naboj : this.nabojeNaMape) {
            if (this.hrac.koliduje(naboj)) {
                this.zasobnikHraca += this.zacinajuceNaboje;
                zobrany = naboj;
                break;
            }
        }
        if (zobrany != null) {
            this.nabojeNaMape.remove(zobrany);
        }

        for (Zberatelny z : this.getPickupy()) {
            if (!z.jeZobrany() && this.hrac.koliduje((HernyObjekt)z)) {
                if (z instanceof Heal && this.hrac.getHP() == 100) {
                    continue;
                }
                z.pouzi(this.hrac);
            }
        }
        this.getPickupy().removeIf(Zberatelny::jeZobrany);
    }

    @Override
    public void kresliPostavy(Graphics g) {
        this.hrac.paint(g);
        this.protivnik.paint(g);
        for (Stvorec naboj : this.nabojeNaMape) {
            naboj.paint(g);
        }
    }

    @Override
    public void pridajPickupy() {
        this.getPickupy().clear();
        if (this.pocetPickupov <= 0) {
            return;
        }
        Random rand = new Random();
        int counter = 0;
        int maxPokusov = 500;
        int pokus = 0;

        while (counter < this.pocetPickupov && pokus < maxPokusov) {
            pokus++;
            int row = rand.nextInt(super.getRiadky());
            int col = rand.nextInt(super.getStlpce());
            if (this.getMapa()[row].charAt(col) != '.') {
                continue;
            }
            int x = col * super.getVelkostS();
            int y = row * super.getVelkostS();

            if (counter % 2 == 0) {
                this.getPickupy().add(new Heal(this.healObr, x, y, super.getVelkostS(), this.healKoef));
            } else {
                this.getPickupy().add(new Speed(this.speedObr, x, y, super.getVelkostS(), this.speedBoostTrvanie));
            }
            counter++;
        }
    }

    @Override
    public void hud(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("ammo: " + this.zasobnikHraca, 10, 670);
        g.drawString("HP: " + this.hrac.getHP(), 130, 670);

        String text;
        if (this.jeBoss) {
            text = "Boss HP: ";
        } else {
            text = "Bot HP: ";
        }
        g.drawString(text + this.protivnik.getHP(), this.getWidth() - 160, 670);

        if (this.isStopnutaHra()) {
            String msg;
            if (this.hracVyhral) {
                msg = "Vyhral si";
            } else {
                msg = "Prehral si";
            }
            g.setFont(new Font("Arial", Font.BOLD, 40));
            FontMetrics fm = g.getFontMetrics();
            int x = (this.getWidth()  - fm.stringWidth(msg)) / 2;
            int y = (this.getHeight() + fm.getAscent())       / 2;
            g.setColor(Color.RED);
            g.drawString(msg, x, y);
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Stlac ESC pre restart", x - 50, y + 35);
        }
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
        this.nabojeNaMape.clear();
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


}
