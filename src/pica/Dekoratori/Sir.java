package pica.Dekoratori;
import pica.DodaciDekorator;
import pica.Pica;

public class Sir extends DodaciDekorator
{
    public static double cena = 130;
    public static String ime = "Sir";
    public Sir (Pica novaPica)
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
