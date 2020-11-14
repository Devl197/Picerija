package pica;
import pica.Dekoratori.*;
import java.util.Arrays;
import java.util.List;

public abstract class FabrikaPica
{
    //Punjenje liste nazivima pica
    public static List<String> listaPica = Arrays.asList("Capricciosa","Pepperoni Piccante","Kulen","Margarita",
                                                        "Pršuta","Feferoni");
    //Punjenje liste nazivima dodataka
    public static List<String> dodaci = Arrays.asList(napraviDodatakSaCenom(Feferone.ime,Feferone.cena),
            napraviDodatakSaCenom(FetaSir.ime, FetaSir.cena),napraviDodatakSaCenom(Kecap.ime,Kecap.cena),
            napraviDodatakSaCenom(Kulen.ime,Kulen.cena),napraviDodatakSaCenom(Masline.ime,Masline.cena),
            napraviDodatakSaCenom(Pelat.ime,Pelat.cena),napraviDodatakSaCenom(Prsuta.ime,Prsuta.cena),
            napraviDodatakSaCenom(Sampinjoni.ime,Sampinjoni.cena),napraviDodatakSaCenom(Sir.ime,Sir.cena),
            napraviDodatakSaCenom(Slanina.ime,Slanina.cena),napraviDodatakSaCenom(Sunka.ime,Sunka.cena));
    public static Pica napraviPicu(ObicnaPica op, String tipPice)
    {
        switch (tipPice)
        {
            case "Capricciosa": return napraviCapricciosu(op);
            case "Pepperoni Piccante": return napraviPepperoniPiccante(op);
            case "Kulen": return napraviKulen(op);
            case "Margarita": return napraviMargaritu(op);
            case "Pršuta": return napraviPrsutu(op);
            case "Feferoni": return napraviFeferoni(op);
            default: return null;
        }
    }
    //Pravljenje razlicitih pica
    private static Pica napraviCapricciosu(ObicnaPica op)
    {
        return new Pelat(new Sunka(new Sir(new Sampinjoni(op))));
    }
    private static Pica napraviPepperoniPiccante(ObicnaPica op)
    {
        return new Pelat(new Sir(new Kulen(op)));
    }
    private static Pica napraviKulen(ObicnaPica op)
    {
        return new Pelat(new Sunka(new Sir(new Sampinjoni(op))));
    }
    private static Pica napraviMargaritu(ObicnaPica op)
    {
        return new Pelat(new Sir(op));
    }
    private static Pica napraviPrsutu(ObicnaPica op)
    {
        return new Kecap(new Sir(new Prsuta(op)));
    }
    private static Pica napraviFeferoni(ObicnaPica op)
    {
        return new Pelat(new Sunka(new Feferone(new FetaSir(new Sir(new Sampinjoni(new Kulen(op)))))));
    }
    public static void main (String[] args)
    {
        System.out.println(FabrikaPica.napraviPicu(new ObicnaPica(PrecnikPice.M),"Capricciosa").vratiCenu());
        System.out.println(FabrikaPica.napraviPicu(new ObicnaPica(PrecnikPice.M),"Pepperoni Piccante").vratiCenu());
        System.out.println(FabrikaPica.napraviPicu(new ObicnaPica(PrecnikPice.M),"Kulen").vratiCenu());
        System.out.println(FabrikaPica.napraviPicu(new ObicnaPica(PrecnikPice.M),"Margarita").vratiCenu());
        System.out.println(FabrikaPica.napraviPicu(new ObicnaPica(PrecnikPice.M),"Pršuta").vratiCenu());
        System.out.println(FabrikaPica.napraviPicu(new ObicnaPica(PrecnikPice.M),"Feferoni").vratiCenu());
    }
    private static String napraviDodatakSaCenom(String ime, double cena)
    {
        return ime + " | " + cena + " RSD";
    }
}
