package pica;

public abstract class DodaciDekorator implements Pica
{
    protected Pica pomocna;
    public DodaciDekorator(Pica novaPica)
    {
        this.pomocna = novaPica;
    }
    public String vratiOpis ()
    {
       return pomocna.vratiOpis();
    }

    @Override
    public double vratiCenu ()
    {
        return pomocna.vratiCenu();
    }
}
