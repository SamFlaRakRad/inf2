package core;

import weapons.Strela;
import powerUps.Zberatelny;
import powerUps.Heal;
import powerUps.Speed;

import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.Objects;

/**

 * @author (Samuel Ďuriš)
 * @version (V5)
 */
public abstract class RezimHry extends JPanel implements ActionListener, KeyListener {

    private static final int RIADKY = 20;
    private static final int STLPCE = 25;
    private static final int VELKOST_S = 32;

    private ArrayList<Stvorec> steny;
    private ArrayList<Strela> strely;
    private ArrayList<Zberatelny> pickupy;

    private boolean[] stlaceneKlavesy = new boolean[256];
    private boolean   stopnutaHra     = false;

    private String[]   mapa;
    private Obtiaznost obtiaznost;
    private Timer      gameLoop;
    private Image      obrazokSteny;

    public RezimHry(Obtiaznost obtiaznost) {
        this.obtiaznost  = obtiaznost;
        this.steny   = new ArrayList<>();
        this.strely  = new ArrayList<>();
        this.pickupy = new ArrayList<>();

        this.addKeyListener(this);
        this.setFocusable(true);

        this.obrazokSteny = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images/stena.png"))).getImage();

        Generator gen = new Generator();
        this.mapa = gen.vytvorMapu(obtiaznost);

        this.nacitajSteny();
        this.nacitajPostavy();
        this.pridajPickupy();

        this.gameLoop = new Timer(35, this);
        this.gameLoop.start();
    }


    public ArrayList<Stvorec> getSteny() {
        return this.steny;
    }
    public ArrayList<Strela> getStrely() {
        return this.strely;
    }
    public ArrayList<Zberatelny> getPickupy() {
        return this.pickupy;
    }
    public boolean[] getStlaceneKlavesy() {
        return this.stlaceneKlavesy;
    }
    public String[] getMapa() {
        return this.mapa; }
    public Obtiaznost getObtiaznost() {
        return this.obtiaznost;
    }
    public boolean isStopnutaHra() {
        return this.stopnutaHra;
    }

    public int getRIADKY() {
        return this.RIADKY;
    }

    public int getSTLPCE() {
        return this.STLPCE;
    }

    public int getVelkostS() {
        return this.VELKOST_S;
    }

    public void setStopnutaHra(boolean v) {
        this.stopnutaHra = v; }
    public void setMapa(String[] m) {
        this.mapa = m;
    }
    public void startGameLoop() {
        this.gameLoop.start();
    }
    public void stopGameLoop() {
        this.gameLoop.stop();
    }

    public abstract void nacitajPostavy();
    public abstract void pohybPostavami();
    public abstract void spracujUtok();
    public abstract void skontrolujPostaveKolizie(Iterator<Strela> it, Strela strela);
    public abstract void kresliHUD(Graphics g);
    public abstract void skontrolujKoniec();
    public abstract void restart();

    public abstract void kresliPostavy(Graphics g);



    @Override
    public final void actionPerformed(ActionEvent e) {
        if (!this.stopnutaHra) {
            this.pohybPostavami();
            this.pohybStriel();
            this.spracujUtok();
            this.skontrolujStenoveKolizie();
            this.skontrolujKoniec();
            this.skontrolujPickupy();
        }
        this.repaint();
        if (this.stopnutaHra) {
            this.gameLoop.stop();
        }
    }

    public void nacitajSteny() {
        this.steny.clear();
        for (int r = 0; r < RIADKY; r++) {
            for (int s = 0; s < STLPCE; s++) {
                if (this.mapa[r].charAt(s) == 'X') {
                    this.steny.add(new Stvorec(this.obrazokSteny,
                            s * VELKOST_S, r * VELKOST_S, VELKOST_S, VELKOST_S));
                }
            }
        }
    }

    public void pohybStriel() {
        for (Strela s : this.strely) {
            s.pohyb();
        }
    }

    public void skontrolujStenoveKolizie() {
        Iterator<Strela> it = this.strely.iterator();
        while (it.hasNext()) {
            Strela strela = it.next();
            boolean trafenaStena = false;
            for (HernyObjekt stena : this.steny) {
                if (strela.koliduje(stena)) {
                    trafenaStena = true;
                    break;
                }
            }
            if (trafenaStena) {
                it.remove();
                continue;
            }
            this.skontrolujPostaveKolizie(it, strela);
        }
    }

    public void skontrolujPickupy() { /* override v podtriedach */ }

    public void pridajPickupy() {
        this.pickupy.clear();
        int[][] pos = {{3, 5}, {15, 18}, {8, 12}, {12, 3}};
        for (int i = 0; i < pos.length; i++) {
            int x = pos[i][0] * VELKOST_S;
            int y = pos[i][1] * VELKOST_S;
            this.pickupy.add(i % 2 == 0
                    ? new Heal(null, x, y, VELKOST_S)
                    : new Speed(null, x, y, VELKOST_S));
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (HernyObjekt s : this.steny) {
            s.paint(g);
        }
        for (Zberatelny  z : this.pickupy) {
            if (z instanceof HernyObjekt) {
                ((HernyObjekt)z).paint(g);
            }
        }
        for (Strela s : this.strely) {
            s.paint(g);
        }
        this.kresliHUD(g);
        this.kresliPostavy(g);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int kod = e.getKeyCode();
        if (kod < 256) {
            this.stlaceneKlavesy[kod] = true;
        }
        if (this.stopnutaHra && kod == KeyEvent.VK_ESCAPE) {
            this.restart();
        }
        this.keyPressedExtra(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int kod = e.getKeyCode();
        if (kod < 256) {
            this.stlaceneKlavesy[kod] = false;
        }
    }

    @Override public void keyTyped(KeyEvent e) {

    }

    public void keyPressedExtra(KeyEvent e) {

    }
}