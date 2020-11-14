package pica.Dekoratori;
import pica.DodaciDekorator;
import pica.Pica;

public class Kulen extends DodaciDekorator
{
    public static double cena = 140;
    public static String ime = "Kulen";
    public Kulen (Pica novaPica)
    {
        super(novaPica);
    }
    public String vratiOpis ()
    {
        return pomocna.vratiOpis()+ ", " + ime.toLowerCase();
    }

    @Override
    public double vratiCenu ()
    {
        return pomocna.vratiCenu() + cena;
    }
}
