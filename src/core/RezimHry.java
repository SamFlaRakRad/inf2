package core;

import weapons.Strela;
import powerUps.Zberatelny;
import powerUps.Heal;
import powerUps.Speed;

//...existing imports...
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

/**

 * @author (Samuel Ďuriš)
 * @version (V5)
 */
public abstract class RezimHry extends JPanel implements ActionListener, KeyListener {

    protected static final int RIADKY    = 20;
    protected static final int STLPCE    = 25;
    protected static final int VELKOST_S = 32;

    // ── Zdieľané kolekcie ────────────────────────────────────────────────
    protected ArrayList<Stvorec> steny;
    protected ArrayList<Strela>      strely;
    protected ArrayList<Zberatelny>  pickupy;

    protected boolean[] stlaceneKlavesy = new boolean[256];
    protected boolean stopnutaHra = false;

    protected String[]    mapa;
    protected Obtiaznost  obtiaznost;
    protected Timer       gameLoop;

    protected Image obrazokSteny;

    /**
     * Konštruktor - inicializuje zdieľané kolekcie, načíta mapu, spustí loop.
     */
    public RezimHry(Obtiaznost obtiaznost) {
        this.obtiaznost = obtiaznost;
        this.steny   = new ArrayList<>();
        this.strely  = new ArrayList<>();
        this.pickupy = new ArrayList<>();

        this.addKeyListener(this);
        this.setFocusable(true);

        this.obrazokSteny = new ImageIcon(getClass().getResource("/images/stena.png")).getImage();

        Generator gen = new Generator();
        this.mapa = gen.vytvorMapu(obtiaznost);

        this.nacitajSteny();
        this.nacitajPostavy();   // abstraktné - každý mód inak
        this.pridajPickupy();

        this.gameLoop = new Timer(35, this);
        this.gameLoop.start();
    }


    protected abstract void nacitajPostavy();

    /** Pohybuje postavami - Multiplayer: pohybSa() na hráčoch, Single: + boss AI */
    protected abstract void pohybPostavami();

    /** Spracuje útok postáv - Multiplayer: obaja hráči, Single: hráč + boss fázy */
    protected abstract void spracujUtok();

    /** Kolízie striel s postavami - líši sa kto koho môže trafiť */
    protected abstract void skontrolujPostaveKolizie(Iterator<Strela> it, Strela strela);


    /** Vykreslí HUD - Multiplayer: ammo, Single: HP bar, boss fáza */
    protected abstract void kresliHUD(Graphics g);

    /** Skontroluje podmienky konca hry */
    protected abstract void skontrolujKoniec();

    /** Reštart konkrétneho módu */
    protected abstract void restart();

    // ── Zdieľaný herný cyklus ────────────────────────────────────────────

    /**
     * Herný cyklus - rovnaký pre oba módy.
     * Abstraktné metódy sa vykonajú polymorfne podľa toho, či je to Multi alebo Single.
     */
    @Override
    public final void actionPerformed(ActionEvent e) {
        if (!this.stopnutaHra) {
            this.pohybPostavami();            // abstract
            this.pohybStriel();               // zdieľané
            this.spracujUtok();               // abstract
            this.skontrolujStenoveKolizie();  // zdieľané
            this.skontrolujKoniec();          // abstract
            this.skontrolujPickupy();         // zdieľané
        }
        this.repaint();
        if (this.stopnutaHra) this.gameLoop.stop();
    }

    // ── Zdieľaná logika ─────────────────────────────────────────────────

    /**
     * Načíta steny z mapy - rovnaké pre oba módy.
     */
    protected void nacitajSteny() {
        this.steny.clear();
        for (int r = 0; r < RIADKY; r++) {
            for (int s = 0; s < STLPCE; s++) {
                if (this.mapa[r].charAt(s) == 'X') {
                    int x = s * VELKOST_S;
                    int y = r * VELKOST_S;
                    this.steny.add(new Stvorec(this.obrazokSteny, x, y, VELKOST_S, VELKOST_S));
                }
            }
        }
    }

    /**
     * Posunie všetky strely - rovnaké pre oba módy.
     */
    protected void pohybStriel() {
        for (Strela s : this.strely) s.pohyb();
    }

    /**
     * Kontrola kolízie striel so stenami - rovnaká pre oba módy.
     * Kolízie s postavami sú abstraktné (iné pre každý mód).
     */
    protected void skontrolujStenoveKolizie() {
        Iterator<Strela> it = this.strely.iterator();
        while (it.hasNext()) {
            Strela strela = it.next();

            // Kolízia so stenou - zdieľané
            boolean trafenaStena = false;
            for (HernyObjekt stena : this.steny) {
                if (strela.koliduje(stena)) { trafenaStena = true; break; }
            }
            if (trafenaStena) { it.remove(); continue; }

            // Kolízia s postavami - abstraktné (každý mód rozhodne kto koho trafí)
            this.skontrolujPostaveKolizie(it, strela);
        }
    }

    /**
     * Spracovanie pickupov - rovnaké pre oba módy.
     * Polymorfizmus cez Zberatelny: pouzi() je Heal alebo Speed, hra nerozlišuje.
     * pouzi() berie Postava - môže teda použiť hráč aj boss.
     */
    protected void skontrolujPickupy() {
        // Konkrétne postavy poskytnú podtriedy, tu len definujeme vzor
        // - zavolá sa z podtried s konkrétnym zoznamom postáv
    }

    /**
     * Pridá pickupy na mapu - rovnaké pre oba módy.
     * Podtriedy môžu override-núť pre iné rozmiestnenie.
     */
    protected void pridajPickupy() {
        this.pickupy.clear();
        // Predvolené pozície - podtriedy môžu override-núť
        int[][] pos = {{3, 5}, {15, 18}, {8, 12}, {12, 3}};
        for (int i = 0; i < pos.length; i++) {
            int x = pos[i][0] * VELKOST_S;
            int y = pos[i][1] * VELKOST_S;
            // Heal a Speed sa striedajú
            this.pickupy.add(i % 2 == 0
                    ? new Heal(null, x, y, VELKOST_S)
                    : new Speed(null, x, y, VELKOST_S));
        }
    }

    // ── Zdieľané vykresľovanie ───────────────────────────────────────────

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Steny - rovnaké pre oba módy
        for (HernyObjekt s : this.steny)  s.paint(g);

        // Pickupy - polymorfné: Heal nakreslí "+", Speed nakreslí ">>"
        for (Zberatelny z : this.pickupy) {
            if (z instanceof HernyObjekt) ((HernyObjekt) z).paint(g);
        }

        // Strely - polymorfné: NORMALNA/BOSS_SPREAD/RAKETA vyzerajú inak
        for (Strela s : this.strely) s.paint(g);

        // HUD - abstraktné (každý mód kreslí iné info)
        this.kresliHUD(g);
    }

    // ── Zdieľaná klávesnica ──────────────────────────────────────────────

    @Override
    public void keyPressed(KeyEvent e) {
        int kod = e.getKeyCode();
        if (kod < 256) this.stlaceneKlavesy[kod] = true;
        if (this.stopnutaHra && kod == KeyEvent.VK_ESCAPE) this.restart();
        this.keyPressedExtra(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int kod = e.getKeyCode();
        if (kod < 256) this.stlaceneKlavesy[kod] = false;
    }

    @Override public void keyTyped(KeyEvent e) {}

    /** Podtriedy môžu pridať vlastné key handling (napr. streľba hráča) */
    protected void keyPressedExtra(KeyEvent e) {}
}