package pica.Dekoratori;
import pica.DodaciDekorator;
import pica.Pica;

public class Slanina extends DodaciDekorator
{
    public static double cena = 110;
    public static String ime = "Slanina";
    public Slanina (Pica novaPica)
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
