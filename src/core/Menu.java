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
 * Trieda Menu ktorá pri spustení aplikacie vytvorí GUI kde si hráči vyberú jednu z ponukaných obtiažností.
 * 
 * @author (Samuel Ďuriš) 
 * @version (V3)
 */
public class Menu extends JFrame {
    private Obtiaznost vybrata;
    /**
     * Konštruktor triedy Menu
     */
    public Menu() {
        this.setTitle("Výber obtiažnosti");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        
        
        JPanel hlavnyPanel = new JPanel(new BorderLayout(10, 10));
        JLabel nadpis = new JLabel("Vyberte obtiažnosť hry", JLabel.CENTER);
        nadpis.setFont(new Font("Arial", Font.BOLD, 24));
        hlavnyPanel.add(nadpis, BorderLayout.NORTH);
        JPanel panelTlacidiel = new JPanel(new GridLayout(3, 1, 10, 10));
        
        //ľahka obtiažnosť
        JButton lahka = new JButton(Obtiaznost.LAHKA.getNazov());
        lahka.setFont(new Font("Arial", Font.PLAIN, 18));
        //vnorena trieda spôsob implementacie z tutorialu 
        lahka.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Menu.this.vyberObtaznost(Obtiaznost.LAHKA);
            }
        });
        
        //stredná obtiažnosť
        JButton stredna = new JButton(Obtiaznost.STREDNA.getNazov());
        stredna.setFont(new Font("Arial", Font.PLAIN, 18));
        //vnorena trieda spôsob implementacie z tutorialu
        stredna.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Menu.this.vyberObtaznost(Obtiaznost.STREDNA);
            }
        });
        
        //ťažká obtiažnosť spôsob implementacie z tutorialu
        JButton tazka = new JButton(Obtiaznost.TAZKA.getNazov());
        tazka.setFont(new Font("Arial", Font.PLAIN, 18));
        //vnorena trieda 
        tazka.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Menu.this.vyberObtaznost(Obtiaznost.TAZKA);
            }
        });
        
        panelTlacidiel.add(lahka);
        panelTlacidiel.add(stredna);
        panelTlacidiel.add(tazka);
        
        hlavnyPanel.add(panelTlacidiel, BorderLayout.CENTER);
        
        this.add(hlavnyPanel);
        this.setVisible(true);
    }
    
    /**
     * Metóda ktorá sa zavolá po výbere obtiažnosti a vytvorý plátno s touto obtiažnosťou 
     */
    private void vyberObtaznost(Obtiaznost obtaznost) {
        this.vybrata = obtaznost;
        this.dispose();
        new Platno(this.vybrata);
    }
    
    /**
     * getter pre vybranú obtiažnosť 
     */
    public Obtiaznost getVybrataObtaznost() {
        return this.vybrata;
    }
}
