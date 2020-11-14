package pica;

public class ObicnaPica implements Pica
{
    PrecnikPice precnik;
    public ObicnaPica(PrecnikPice precnik)
    {
        this.precnik = precnik;
    }
    @Override
    public String vratiOpis ()
    {
        return "Testo";
    }

    @Override
    public double vratiCenu ()
    {
       switch (precnik)
       {
           case M: return 80;
           case L: return 250;
           case XL: return 410;
           default: return 30;
       }
    }
}
