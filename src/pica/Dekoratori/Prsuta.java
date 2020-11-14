package pica.Dekoratori;
import pica.DodaciDekorator;
import pica.Pica;

public class Prsuta extends DodaciDekorator
{
    public static double cena = 190;
    public static String ime = "Pršuta";
    public Prsuta (Pica novaPica)
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
