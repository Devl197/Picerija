package server;

import java.io.Serializable;

public class Klijent implements Serializable
{
    private String ime;
    private String prezime;
    private String telefon;
    private String email;
    private String lozinka;

    public Klijent (String ime, String prezime, String email, String telefon, String lozinka)
    {
        this.ime = ime;
        this.prezime = prezime;
        this.telefon = telefon;
        this.email = email;
        this.lozinka = lozinka;
    }

    public String getIme ()
    {
        return ime;
    }

    public String getPrezime ()
    {
        return prezime;
    }

    public String getTelefon ()
    {
        return telefon;
    }

    public String getEmail ()
    {
        return email;
    }

    public String getLozinka ()
    {
        return lozinka;
    }

    @Override
    public String toString ()
    {
        return "Klijent{" +
                "ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                ", telefon='" + telefon + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
