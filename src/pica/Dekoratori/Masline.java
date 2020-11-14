package pica.Dekoratori;
import pica.DodaciDekorator;
import pica.Pica;

public class Masline extends DodaciDekorator
{
    public static double cena = 50;
    public static String ime = "Masline";
    public Masline (Pica novaPica)
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