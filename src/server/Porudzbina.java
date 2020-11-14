package server;

import java.io.*;
import java.util.Date;

public class Porudzbina implements Serializable
{
    static int brojac = 0;
    private int ID;
    private Klijent narucilac;
    private double cena;
    private String dodaci;
    private String napomena;
    private String pica;
    private String precnik;
    private String adresa;
    private Date datumNarudzbine;
    private int vreme;
    private Date datumIsporuke;//Kada je ovo polje popunjeno porudzbina je isporucena

    public Porudzbina (Klijent narucilac, double cena, String pica, String precnik, String dodaci, String adresa,
                       String napomena, int vreme)
    {
        ucitajBrojac();
        this.ID = brojac++;
        upisiBrojac();
        this.narucilac = narucilac;
        this.cena = cena;
        this.dodaci = dodaci;
        this.precnik = precnik;
        this.pica = pica;
        this.napomena = napomena;
        this.adresa = adresa;
        this.vreme = vreme;
        datumNarudzbine = new Date();
    }

    public int getID ()
    {
        return ID;
    }

    public Klijent getNarucilac ()
    {
        return narucilac;
    }

    public double getCena ()
    {
        return cena;
    }

    public String getDodaci ()
    {
        return dodaci;
    }

    public String getNapomena ()
    {
        return napomena;
    }

    public Date getDatumNarudzbine ()
    {
        return datumNarudzbine;
    }

    public Date getDatumIsporuke ()
    {
        return datumIsporuke;
    }

    public String getPica ()
    {
        return pica;
    }

    public String getPrecnik ()
    {
        return precnik;
    }

    public String getAdresa() {
        return adresa;
    }

    public int getVreme ()
    {
        return vreme;
    }

    public void setDatumIsporuke (Date datumIsporuke)
    {
        this.datumIsporuke = datumIsporuke;
    }
    private static void upisiBrojac()
    {
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream("brojac.bin"));
            int pom = brojac;
            oos.writeObject(pom);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    private static void ucitajBrojac(){
        File datoteka = new File("brojac.bin");
        try
        {
            if(datoteka.exists())
            {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(datoteka));
                brojac = (int)ois.readObject();

            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    //Vrati predvidjeno vreme zavrsetka
   public long predvidjenoVreme()
   {
       long dNarudzbine = datumNarudzbine.getTime();
       return (vreme * 1000 * 60) + dNarudzbine;
   }

    @Override
    public String toString ()
    {
        return "Porudzbina{" +
                "ID=" + ID +
                ", narucilac=" + narucilac +
                ", cena=" + cena +
                ", dodaci='" + dodaci + '\'' +
                ", napomena='" + napomena + '\'' +
                ", pica='" + pica + '\'' +
                ", precnik='" + precnik + '\'' +
                ", adresa='" + adresa + '\'' +
                ", datumNarudzbine=" + datumNarudzbine +
                ", vreme=" + vreme +
                ", datumIsporuke=" + datumIsporuke +
                '}';
    }
}
