package pica.Dekoratori;
import pica.DodaciDekorator;
import pica.Pica;

public class Sampinjoni extends DodaciDekorator
{
    public static double cena = 40;
    public static String ime = "Šampinjoni";
    public Sampinjoni (Pica novaPica)
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
