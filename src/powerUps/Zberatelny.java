package powerUps;

import players.Postava;

/**
 * Rozhranie Zberatelny - zmluva pre predmety, ktoré sa dajú zobrať.
 *
 * Implementujú: Heal, Speed (a akýkoľvek nový PowerUp v budúcnosti)
 * Neimplementujú: steny, hráči, strely - tie sa nedajú zobrať
 *
 * Prečo interface:
 *   Pickup je výnimka, nie pravidlo. Väčšina herných objektov sa nedá zobrať.
 *   Interface opisuje špeciálnu schopnosť minority tried, nie majoritnú vlastnosť.
 *   Porovnaj: Pohyblivy by implementovalo 90% tried - to nedáva zmysel ako interface.
 *             Zberatelny implementujú len Heal a Speed - to zmysel dáva.
 *
 * Polymorfizmus:
 *   RezimHry ukladá List<Zberatelny> namiesto List<PowerUp>.
 *   zberatelny.pouzi(postava) - Heal obnoví HP, Speed zvýši rýchlosť.
 *   zberatelny.jeZobrany()    - RezimHry odstraňuje zobrané predmety.
 *   Hra nemusí vedieť, či ide o Heal alebo Speed.
 *
 * Pozn: pouzi() berie Postava, nie Hrac - takže aj Boss by mohol zobrať pickup.
 *
 * @author (Samuel Ďuriš)
 * @version (V5)
 */
public interface Zberatelny {

    /**
     * Aktivuje efekt predmetu na postavu, ktorá ho zobrala.
     * Heal.pouzi()  → postava.setHP(postava.getHP() + 1)
     * Speed.pouzi() → zvýši rýchlosť pohybu postavy
     */
    void pouzi(Postava postava);

    /** True ak predmet už bol zobraný a má byť odstránený z mapy */
    boolean jeZobrany();

    /** Označí predmet ako zobraný */
    void oznacZobrany();
}
