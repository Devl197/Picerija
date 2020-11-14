package pica.Dekoratori;
import pica.DodaciDekorator;
import pica.Pica;

public class FetaSir extends DodaciDekorator
{
    public static double cena = 80;
    public static String ime = "Feta sir";
    public FetaSir (Pica novaPica)
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