package core;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
 * Trieda Menu - vytvorí GUI menu na začiatku aplikácie.
 * @author Samuel Ďuriš
 * @version V3
 */
public class Menu extends JFrame {
    private Obtiaznost vybrata;
    private TypHry typ;
    private TypEnemaka enemak;

    /**
     * Konštruktor triedy Menu
     */
    public Menu() {
        this.setTitle("Menu");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        
        JPanel typHryPanel = new JPanel(new BorderLayout(10, 10));
        JLabel typHryNadpis = new JLabel("Vyberte typ hry", JLabel.CENTER);
        typHryNadpis.setFont(new Font("Arial", Font.BOLD, 24));
        typHryPanel.add(typHryNadpis, BorderLayout.NORTH);
        JPanel vyberHier = new JPanel(new GridLayout(2, 1, 10, 10));
        
        JButton single = new JButton(TypHry.SINGLEPLAYER.getTypHry());
        single.setFont(new Font("Arial", Font.PLAIN, 20));
        single.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Menu.this.setTyp(TypHry.SINGLEPLAYER);
                Menu.this.remove(typHryPanel);
                Menu.this.vyberEnemaka();

            }
        });

        JButton multi = new JButton(TypHry.MULTIPLAYER.getTypHry());
        multi.setFont(new Font("Arial", Font.PLAIN, 20));
        multi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Menu.this.setTyp(TypHry.MULTIPLAYER);
                Menu.this.setEnemak(TypEnemaka.HRAC);
                Menu.this.vyberObtiaznosti();
                Menu.this.remove(typHryPanel);
            }
        });

        vyberHier.add(single);
        vyberHier.add(multi);
        typHryPanel.add(vyberHier, BorderLayout.CENTER);
        this.add(typHryPanel);
        this.setVisible(true);

    }


    /**
     * Zobrazí panel pre výber obtiažnosti hry.
     */
    public void vyberObtiaznosti() {
        JPanel obtaiaznostPanel = new JPanel(new BorderLayout(10, 10));
        JLabel nadpis = new JLabel("Vyberte obtiažnosť hry", JLabel.CENTER);
        nadpis.setFont(new Font("Arial", Font.BOLD, 24));
        obtaiaznostPanel.add(nadpis, BorderLayout.NORTH);
        JPanel panelTlacidiel = new JPanel(new GridLayout(3, 1, 10, 10));

        //ľahka obtiažnosť
        JButton lahka = new JButton(Obtiaznost.LAHKA.getNazov());
        lahka.setFont(new Font("Arial", Font.PLAIN, 18));
        //vnorena trieda spôsob implementacie z tutorialu
        lahka.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Menu.this.vyberObtaznost(Obtiaznost.LAHKA, Menu.this.getTyp(), Menu.this.getEnemak());
            }
        });

        //stredná obtiažnosť
        JButton stredna = new JButton(Obtiaznost.STREDNA.getNazov());
        stredna.setFont(new Font("Arial", Font.PLAIN, 18));
        //vnorena trieda spôsob implementacie z tutorialu
        stredna.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Menu.this.vyberObtaznost(Obtiaznost.STREDNA,  Menu.this.getTyp(), Menu.this.getEnemak());
            }
        });

        //ťažká obtiažnosť spôsob implementacie z tutorialu
        JButton tazka = new JButton(Obtiaznost.TAZKA.getNazov());
        tazka.setFont(new Font("Arial", Font.PLAIN, 18));
        //vnorena trieda
        tazka.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Menu.this.vyberObtaznost(Obtiaznost.TAZKA,  Menu.this.getTyp(), Menu.this.getEnemak());
            }
        });

        panelTlacidiel.add(lahka);
        panelTlacidiel.add(stredna);
        panelTlacidiel.add(tazka);

        obtaiaznostPanel.add(panelTlacidiel, BorderLayout.CENTER);


        this.add(obtaiaznostPanel);
        this.setVisible(true);
    }

    /**
     * Zobrazí panel pre výber typu nepriateľa (len pre Singleplayer).
     */
    public void vyberEnemaka() {
        JPanel vyberEnemaka = new JPanel(new BorderLayout(10, 10));
        JLabel enemyNadpis = new JLabel("Vyberte nepriateľa", JLabel.CENTER);
        enemyNadpis.setFont(new Font("Arial", Font.BOLD, 24));
        vyberEnemaka.add(enemyNadpis, BorderLayout.NORTH);
        JPanel enemy = new JPanel(new GridLayout(2, 1, 10, 10));

        JButton bot = new JButton(TypEnemaka.BOT.getEnemak());
        bot.setFont(new Font("Arial", Font.PLAIN, 20));
        bot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Menu.this.setEnemak(TypEnemaka.BOT);
                Menu.this.vyberObtiaznosti();
                Menu.this.remove(vyberEnemaka);
            }
        });

        JButton boss = new JButton(TypEnemaka.BOSS.getEnemak());
        boss.setFont(new Font("Arial", Font.PLAIN, 20));
        boss.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Menu.this.setEnemak(TypEnemaka.BOSS);
                Menu.this.vyberObtiaznosti();
                Menu.this.remove(vyberEnemaka);
            }
        });

        enemy.add(boss);
        enemy.add(bot);

        vyberEnemaka.add(enemy, BorderLayout.CENTER);
        this.add(vyberEnemaka);
        this.setVisible(true);

    }

    /**
     * Spustí hernú aplikáciu s vybratými parametrami.
     */
    private void vyberObtaznost(Obtiaznost obtaznost, TypHry typ, TypEnemaka typEnemaka) {
        this.vybrata = obtaznost;
        this.typ = typ;
        this.enemak = typEnemaka;
        this.dispose();
        new Platno(this.vybrata, this.typ, this.enemak);
    }

    /**
     * Getter pre vybranú obtiažnosť.
     */
    public Obtiaznost getVybrataObtaznost() {
        return this.vybrata;
    }

    /**
     * Getter pre vybratý typ hry.
     */
    public TypHry getTyp() {
        return this.typ;
    }

    /**
     * Setter pre typ hry.
     */
    public void setTyp(TypHry typ) {
        this.typ = typ;
    }

    /**
     * Setter pre typ nepriateľa.
     */
    public void setEnemak(TypEnemaka enemak) {
        this.enemak = enemak;
    }

    /**
     * Getter pre typ nepriateľa.
     */
    public TypEnemaka getEnemak() {
        return this.enemak;
    }
}
