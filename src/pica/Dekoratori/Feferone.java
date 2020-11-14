package pica.Dekoratori;
import pica.DodaciDekorator;
import pica.Pica;

public class Feferone extends DodaciDekorator
{
    public static double cena = 60;
    public static String ime = "Feferone";
    public Feferone (Pica novaPica)
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
