package pica.Dekoratori;
import pica.DodaciDekorator;
import pica.Pica;

public class Pelat extends DodaciDekorator
{
    public static double cena = 40;
    public static String ime = "Pelat";
    public Pelat (Pica novaPica)
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
