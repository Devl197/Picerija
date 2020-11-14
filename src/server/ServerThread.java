package server;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class ServerThread extends Thread
{
    //Soket koji uzima od servera
    private Socket s;
    //Bafere za pisanje i citanje
    private BufferedReader in;
    private PrintWriter out;
    public ServerThread(Socket s)
    {
        this.s = s;
        try
        {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())),true);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        start();
    }
    @Override
    public void run ()
    {
        try
        {
            String zahtev = in.readLine();
            System.out.println(zahtev + " primljena!");
            if(zahtev.contains("registracija"))
            {
                String []nizZaOsobu = zahtev.split(":");
                String []zaPoljaR = nizZaOsobu[1].split(" ");
                if(!Server.daLiListaSadrziKlijenta(zaPoljaR[2]))
                {
                    Server.klijenti.add(new Klijent(zaPoljaR[0], zaPoljaR[1], zaPoljaR[2],
                                                  zaPoljaR[3], zaPoljaR[4]));
                    out.println("Uspesno ste se registrovali!");
                    Server.upisiListuUDat("klijenti",Server.klijenti);
                }
                else out.println("Ovaj nalog vec postoji!");
                out.close();
                in.close();
                s.close();
            }
            else if(zahtev.contains("prijava"))
            {
                String []nizZaOsobu = zahtev.split(":");
                String []zaPoljaR = nizZaOsobu[1].split(" ");
                switch (Server.autentifikuj(zaPoljaR[0],zaPoljaR[1]))
                {
                    case -1: out.println("Nepostojaca email adresa, pokusajte ponovo!");break;
                    case -2: out.println("Pogresna lozinka, pokusajte ponovo!");break;
                    case -3: System.out.println("Lista je prazna!");break;
                    case 0: out.println("Uspesno ste se prijavili: " + zaPoljaR[0]);break;
                }
                out.close();
                in.close();
                s.close();
            }
            else if(zahtev.contains("porudzbina"))
            {
                String []nizZaOsobu = zahtev.split(":");
                String []zaPoljaR = nizZaOsobu[1].split(" ");
                //Server.izlistajOsobe();
                //Sakuplja stvari za porudzbinu od klijenta
                String email = zaPoljaR[0];
                String pica = zaPoljaR[1];
                String precnik = zaPoljaR[2];
                String dodaci = zaPoljaR[3];
                double cena = Double.parseDouble(zaPoljaR[4]);
                String adresa = zaPoljaR[5].replaceAll("\\|"," ");
                System.out.println(adresa);
                String napomena = zaPoljaR[6];

                //Pravljenje narudzbine i stavljanje u mapu
                //Sluzi za odredjivanja vremena kada ce pica biti isporucena
                Random rd = new Random();
                int max = 2;
                int min = 1;
                int vreme = rd.nextInt((max + 1) - min) + min;
                Porudzbina p = new Porudzbina(Server.vratiKlijenta(email),cena,pica,precnik,dodaci,adresa,napomena,
                        vreme);
                Server.porudzbineNeisporucene.put(p.getID(),p);//Stavlja porudzbinu u mapu neisporucenih porudzbina
                Server.upisiHashMapuUDat(Server.porudzbineNeisporucene,Server.datNeisporucene);//Upisuje mapu u datoteku
                System.out.println(p);//Ispisuje porudzbinu
                new Brojac(p.getID(),(double)vreme);//Pravi novu nit koja ce odbrojavati vreme dok se porudzbina ne
                // zavrsi
                out.println("Vaša pica će biti isporučena za: " + vreme + " min");//Salje odg. klijentu
                in.close();
                out.close();
                s.close();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
