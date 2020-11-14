package pica.Dekoratori;
import pica.DodaciDekorator;
import pica.Pica;

public class Kecap extends DodaciDekorator
{
    public static double cena = 110;
    public static String ime = "Kečap";
    public Kecap (Pica novaPica)
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