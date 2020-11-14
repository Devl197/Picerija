package klijent;

import javafx.scene.control.RadioButton;
import pica.FabrikaPica;
import pica.ObicnaPica;
import pica.PrecnikPice;
import pica.Pica;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Klijent extends JFrame
{
    private Container cp;
    private CardLayout cl = new CardLayout();
    //Pocetni panel
    private JPanel pocetni = new JPanel();
    //Panel za formu
    private JPanel porudzbina = new JPanel();
    //Panel za prikaz dostave
    private JPanel dostava = new JPanel();
    //Panel za registraciju
    private JPanel registracija = new JPanel();
    //Panel za prijavu
    private JPanel prijava = new JPanel();
    //Objekat koji se bavi eventovima koje dugmici izazivaju
    private Klik listener = new Klik();
    //Bafer za citanje
    private BufferedReader in;
    //Bafer za slanje
    private PrintWriter out;
    //Adresa servera
    private InetAddress adresaServera;
    private Socket soket;
    //Port na serveru
    private static final int TCP_IP = 9000;
    //Sirina ekrana
    private final int SIRINA = 800;
    //Visina ekrana
    private final  int VISINA = 650;
    //Font za naslove
    private Font veliki = new Font("Avro",Font.ITALIC,25);
    //Font za labele
    private Font mali = new Font("Avro",Font.PLAIN,16);
    //Objekat za dimenzije
    private Dimension d = new Dimension();
    //Visina i sirina input polja
    private final int duzinaInputa = 200;
    private final int visinaInputa = 25;
    //Duzina dugmeta, a za visinu se cesto koristi visina inputa
    private final int duzinaDugmeta = 90;
    //Lista za polja iz registracije
    private ArrayList rLista = new ArrayList();
    //LIsta za polje iz prijave
    private ArrayList pLista = new ArrayList();
    //Nadalje stvari za poudzbenicu
    private ButtonGroup grupa;
    private JComboBox pice;
    private JList dodaci;
    private JTextArea napomena;
    private JLabel medjuzbirPica;
    private JLabel medjuzbirDodaci;
    private JLabel ukupno;
    private JLabel opisPice;
    private JTextField adresa;
    private Promena p;
    private JRadioButton[] precnici = new JRadioButton[3];
    //Cena dodataka
    private double cenaD = 0.0;
    //Cena pice
    private double cenaP = 0.0;
    //Ukupna cena
    private double uCena = 0.0;
    private Pica pica;
    //Email koje ce se koristiti za porudzbine
    private String lokalniEmail;

    public Klijent(){
        setSize(SIRINA,VISINA);
        setTitle("Picerija");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cp = getContentPane();
        cp.setLayout(cl);
        setujPorudzbinu();
        setujIDodajPocetniEkran();
        setujIDodajPrijavu();
        setujIDodajRegistraciju();
        cl.show(cp,"pocetna");
        setVisible(true);
    }

    //Metoda za menjanje kartica
    private void promeniNaKarticu(String kartica)
    {
        cl.show(cp,kartica);
        System.out.println("Promena na " + kartica + " karticu");
    }
    private void setujIDodajPocetniEkran()
    {
        int xPoz = 300, yPoz = 400;
        pocetni.setLayout(null);
        JButton prijava = setujINapraviDugme("Prijava",listener,duzinaDugmeta + 15,visinaInputa,xPoz,yPoz,0);
        JButton registracija = setujINapraviDugme("Registracija",listener,duzinaDugmeta + 15,visinaInputa,xPoz + 115,
                yPoz,
                0);
        try
        {
            Path path = FileSystems.getDefault().getPath(".");
            BufferedImage slika = ImageIO.read(new File(path + "\\" + "LOGO.png"));
            JLabel jSlika = new JLabel(new ImageIcon(slika));
            jSlika.setLocation(xPoz - 40,100);
            jSlika.setSize(300,300);
            pocetni.add(jSlika);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        pocetni.add(prijava);
        pocetni.add(registracija);
        cp.add(pocetni,"pocetna");
    }
    //Metoda za setovanje konekcije
    private void setujKonekciju(){
        try
        {
            adresaServera = InetAddress.getByName("127.0.0.1");
            soket = new Socket(adresaServera,TCP_IP);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    //Metoda za setovanje bafera
    private void setujBafere(){
        try
        {
            in = new BufferedReader(new InputStreamReader(soket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soket.getOutputStream())),true);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    //Metoda za pravljenje stranice za registraciju
    private void setujIDodajRegistraciju(){
        registracija.setLayout(null);
        //Razmak izmedju elemenata
        int razmak = 10;
        //Horizontalna pozcija elemenata
        int xPoz = 300;
        //Sluzi da pamti vertikalne pozicije elemenata
        int yPoz = 100;

        //Naslov
        JLabel reg = setujINapraviLabelu("Registracija",xPoz,yPoz, veliki, 0);
        yPoz += d.height;
        registracija.add(reg);

        //Input za ime i njegova labela
        JLabel lIme = setujINapraviLabelu("Vaše ime",xPoz,yPoz,mali,razmak);
        yPoz += d.height;
        registracija.add(lIme);
        JTextField ime = setujINapraviTF(xPoz,yPoz,razmak,"Ime");
        yPoz += visinaInputa;
        rLista.add(ime);
        registracija.add(ime);

        //Input za prezime i njegova labela
        JLabel lPrezime = setujINapraviLabelu("Vaše prezime",xPoz,yPoz,mali,razmak);
        yPoz += d.height;
        registracija.add(lPrezime);
        JTextField prezime = setujINapraviTF(xPoz,yPoz,razmak,"Prezime");
        yPoz += visinaInputa;
        rLista.add(prezime);
        registracija.add(prezime);

        //Input za e-mail i njegova labela
        JLabel lEmail = setujINapraviLabelu("Vaš e-mail",xPoz,yPoz,mali,razmak);
        yPoz += d.height;
        registracija.add(lEmail);
        JTextField email = setujINapraviTF(xPoz,yPoz,razmak,"Email");
        yPoz += visinaInputa;
        rLista.add(email);
        registracija.add(email);

        //Input za telefon i njegova labela
        JLabel lTelefon = setujINapraviLabelu("Vaš kontakt telefon",xPoz,yPoz,mali,
                razmak);
        yPoz += d.height;
        registracija.add(lTelefon);
        JTextField telefon = setujINapraviTF(xPoz,yPoz,razmak,"Telefon");
        yPoz += visinaInputa;
        rLista.add(telefon);
        registracija.add(telefon);

        //Input za lozinku i njegova labela
        JLabel lLozinka = setujINapraviLabelu("Vaša lozinka",xPoz,yPoz,mali,razmak);
        yPoz += d.height;
        registracija.add(lLozinka);
        JPasswordField lozinka = setujINapraviPF(xPoz,yPoz,razmak,"Lozinka");
        yPoz += visinaInputa;
        rLista.add(lozinka);
        registracija.add(lozinka);

        //Dugmici za potvrdu i nazad
        JButton potvrdi = setujINapraviDugme("Potvrdi",listener,duzinaDugmeta,visinaInputa,xPoz,yPoz,razmak + 20,
                "RPotvrdi");
        JButton nazad = setujINapraviDugme("Nazad",listener,duzinaDugmeta,visinaInputa,xPoz + 110,yPoz,razmak + 20);
        registracija.add(nazad);
        registracija.add(potvrdi);
        cp.add(registracija,"registracija");
    }
    //Metoda za pravljenje stranice za prijavu
    private void setujIDodajPrijavu(){
        prijava.setLayout(null);
        int xPoz = 300, yPoz = 150;
        int razmak = 10;
        //Naslov
        JLabel jPrijava = setujINapraviLabelu("Prijava",xPoz,yPoz, veliki,0);
        yPoz += d.height;
        prijava.add(jPrijava);

        //Input za e-mail i njegova labela
        JLabel lEmail = setujINapraviLabelu("Vaš e-mail",xPoz,yPoz,mali,razmak);
        yPoz += d.height;
        prijava.add(lEmail);
        JTextField email = setujINapraviTF(xPoz,yPoz,razmak,"Email");
        yPoz += visinaInputa;
        pLista.add(email);
        prijava.add(email);

        //Input za lozinku i njegova labela
        JLabel lLozinka = setujINapraviLabelu("Vaša lozinka",xPoz,yPoz,mali,razmak);
        yPoz += d.height;
        prijava.add(lLozinka);
        JPasswordField lozinka = setujINapraviPF(xPoz,yPoz,razmak,"Lozinka");
        yPoz += visinaInputa;
        pLista.add(lozinka);
        prijava.add(lozinka);

        //Dugmici za potvrdu i nazad
        JButton potvrdi = setujINapraviDugme("Potvrdi",listener,duzinaDugmeta,visinaInputa,xPoz,yPoz,razmak + 20,
                "PPotvrdi");
        JButton nazad = setujINapraviDugme("Nazad",listener,duzinaDugmeta,visinaInputa,xPoz + 110,yPoz,razmak + 20);
        prijava.add(nazad);
        prijava.add(potvrdi);
        cp.add(prijava,"prijava");
    }
    //Metoda za pravljenje porudzbine
    private void setujPorudzbinu(){
        porudzbina.setLayout(null);
        p = new Promena();
        int xPoz = 300;
        int yPoz = 25;
        int duzina = 75;
        int visina = 18;

        //Naslov
        JLabel naslov = setujINapraviLabelu("Naručite picu",xPoz,yPoz,veliki,10);
        yPoz += d.height;
        porudzbina.add(naslov);

        //Precnik pice
        JLabel precnik = setujINapraviLabelu("Precnik pice: ",xPoz,yPoz,mali,15);
        yPoz += d.height;
        porudzbina.add(precnik);

        //Radio dugmici za precnik bice
        JRadioButton m = setujRadioDugme("25cm",duzina,visina,xPoz,yPoz,20);
        m.addItemListener(p);
        m.setActionCommand("25cm");
        precnici[0] = m;
        JRadioButton l = setujRadioDugme("32cm",duzina,visina,xPoz + 85,yPoz,20);
        l.addItemListener(p);
        l.setActionCommand("32cm");
        precnici[1] = l;
        JRadioButton xl =setujRadioDugme("50cm",duzina,visina,xPoz + 170,yPoz,20);
        xl.setActionCommand("50cm");
        xl.addItemListener(p);
        precnici[2] = xl;

        grupa = new ButtonGroup();
        grupa.add(m);
        grupa.add(l);
        grupa.add(xl);
        yPoz += 2 * visina;
        porudzbina.add(m);
        porudzbina.add(l);
        porudzbina.add(xl);

        //ComboBox za pice
        pice = new JComboBox();
        napuniJCB(pice);
        pice.setLocation(xPoz,yPoz + 10);
        pice.setSize(3 * 75,25);
        pice.setSelectedIndex(2);
        pice.addItemListener(p);
        yPoz += 25;
        porudzbina.add(pice);

        //Opis pice
        opisPice = setujINapraviLabelu("Opis: ",xPoz, yPoz, new Font("Avro",Font.PLAIN,13),10);
        opisPice.setSize(opisPice.getWidth() + 400, opisPice.getHeight());
        yPoz += d.height;
        porudzbina.add(opisPice);

        //Labela za dodatke
        JLabel jDodaci = setujINapraviLabelu("Dodaci: ", xPoz, yPoz,mali, 15);
        yPoz += d.height;
        porudzbina.add(jDodaci);

        //Lista dodataka
        JScrollPane pane = new JScrollPane();
        DefaultListModel model = new DefaultListModel();
        dodaci = new JList(model);
        napuniJL(model);
        pane.setSize(3 * 75, 100);
        pane.setLocation(xPoz,yPoz + 15);
        yPoz += 100;
        pane.getViewport().add(dodaci);
        dodaci.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged (ListSelectionEvent e)
            {
                boolean adjust = e.getValueIsAdjusting(); //False je kada se selektuje
                if(!adjust)
                {
                    List  lista = dodaci.getSelectedValuesList();
                    List <String> cene  = new ArrayList();
                    for(Object str : lista)
                    {
                        String []niz = str.toString().split("\\|");
                        cene.add(niz[1].replaceAll("[A-Z]","").trim());
                    }
                    cenaD = saberiCenuDodataka(cene);
                    medjuzbirDodaci.setText("Medjuzbir dodataka: " + cenaD +  " RSD");
                    osveziUkupno();
                }
            }
        });
        porudzbina.add(pane);

        //Medjuzbir za pice
        medjuzbirPica = setujINapraviLabelu("Medjuzbir pice: " + cenaP + " RSD", xPoz, yPoz, mali,20);
        medjuzbirPica.setSize(medjuzbirPica.getWidth() + 100, medjuzbirPica.getHeight());
        yPoz += d.height;
        porudzbina.add(medjuzbirPica);
        //Medjuzbir za dodatke
        medjuzbirDodaci = setujINapraviLabelu("Medjuzbir dodataka: " + cenaD + " RSD", xPoz, yPoz, mali,15);
        medjuzbirDodaci.setSize(medjuzbirDodaci.getWidth() + 100,medjuzbirDodaci.getHeight());
        yPoz += d.height;
        porudzbina.add(medjuzbirDodaci);
        //Ukupno
        ukupno = setujINapraviLabelu("Ukupno: " + uCena + " RSD", xPoz, yPoz, mali,10);
        ukupno.setSize(ukupno.getWidth() + 100, ukupno.getHeight());
        yPoz += d.height;
        porudzbina.add(ukupno);

        //Labela za napomenu
        JLabel jNapomena = setujINapraviLabelu("Napomena: ",xPoz,yPoz,mali,10);
        yPoz += d.height;
        porudzbina.add(jNapomena);

        //Napomena
        napomena = new JTextArea();
        napomena.setLineWrap(true);
        napomena.setRows(5);
        napomena.setColumns(5);
        napomena.setSize(3 * 75,100);
        napomena.setLocation(xPoz,yPoz + 15);
        yPoz += 100;
        porudzbina.add(napomena);

        //Labela za adresu
        JLabel jAdresa = setujINapraviLabelu("Adresa dostave: ", xPoz, yPoz, mali,15);
        yPoz += d.height;
        porudzbina.add(jAdresa);
        //Adresa
        adresa = setujINapraviTF(xPoz,yPoz,15,"Adresa");
        adresa.setSize(3*75,adresa.getHeight());
        yPoz += visinaInputa;
        porudzbina.add(adresa);

        //Labela za napomenu za plaćanje
        JLabel placanje = setujINapraviLabelu("Napomena: porudžbina se plaća pouzećem!",xPoz,yPoz,mali,20);
        yPoz += d.height;
        porudzbina.add(placanje);

        //Dugme za slanje
        JButton posalji = setujINapraviDugme("Naruči",listener,3 * 75, 25, xPoz,yPoz,25);
        porudzbina.add(posalji);

        m.setSelected(true);
        cp.add(porudzbina,"porudzbina");
    }
    //Osvezi ukupno
    private void osveziUkupno()
    {
        uCena = cenaD + cenaP;
        ukupno.setText("Ukupno: " + uCena + " RSD");
    }
    //Metoda za sabiranje dodataka
    private double saberiCenuDodataka(List <String> cene)
    {
        double ukupno = 0;
        for(int i = 0; i < cene.size(); i++)
        {
            ukupno += Double.parseDouble(cene.get(i));
        }
        return ukupno;
    }
    //Napuni JCB
    private void napuniJCB(JComboBox jcb)
    {
        for(String pica : FabrikaPica.listaPica)
        {
            jcb.addItem(pica);
        }
    }
    //Napuni JL
    private void napuniJL(DefaultListModel model)
    {
        for(String dodatak : FabrikaPica.dodaci)
        {
            model.addElement(dodatak);
        }
    }
    //Metoda za setovanje radioDugmadi
    private JRadioButton setujRadioDugme(String ime, int duzina, int visina,int xPoz, int yPoz, int razmak)
    {
        JRadioButton dugme = new JRadioButton(ime);
        dugme.setSize(duzina,visina);
        dugme.setLocation(xPoz,yPoz + razmak);
        return dugme;
    }
    //Metoda za pravljenje labela
    private JLabel setujINapraviLabelu(String naziv,int xPoz, int yPoz, Font font, int razmak)
    {
        JLabel labela = new JLabel(naziv);
        labela.setFont(font);
        //Objekat koji uzima dimenzije od labela
        d = labela.getMaximumSize();
        labela.setSize(d.width,d.height);
        labela.setLocation(xPoz,yPoz + razmak);
        return labela;
    }
    //Metoda za pravljenje input polja
    private JTextField setujINapraviTF(int xPoz, int yPoz,int razmak, String ime)
    {
        JTextField jtf = new JTextField();
        jtf.setSize(duzinaInputa,visinaInputa);
        jtf.setName(ime);
        jtf.setLocation(xPoz,yPoz + razmak);
        return jtf;
    }
    //Metoda za pravljenje inputa lozinke
    private JPasswordField setujINapraviPF(int xPoz, int yPoz,int razmak, String ime)
    {
        JPasswordField jpf = new JPasswordField();
        jpf.setSize(duzinaInputa,visinaInputa);
        jpf.setName(ime);
        jpf.setLocation(xPoz,yPoz + razmak);
        return jpf;
    }
    //Metoda za pravljenje dugmeta
    private JButton setujINapraviDugme(String ime, ActionListener listener, int duzina, int visina, int xPoz,
                                       int yPoz, int razmak, String akcionaKomanda)
    {
        JButton dugme = new JButton(ime);
        dugme.setActionCommand(akcionaKomanda);
        dugme.addActionListener(listener);
        dugme.setSize(duzina,visina);
        dugme.setLocation(xPoz,yPoz + razmak);
        return dugme;
    }
    //Preklopljena metoda za prevljenje dugmeta koja nema navedenu akcionu komandu
    private JButton setujINapraviDugme(String ime, ActionListener listener, int duzina, int visina, int xPoz,
                                       int yPoz, int razmak)
    {
        JButton dugme = new JButton(ime);
        dugme.setActionCommand(ime);
        dugme.addActionListener(listener);
        dugme.setSize(duzina,visina);
        dugme.setLocation(xPoz,yPoz + razmak);
        return dugme;
    }
    //Proverava formu
    private boolean proveriFormu(ArrayList rLista)
    {
        for(Object jtf : rLista)
        {
            JTextField text = (JTextField)jtf;
            switch (text.getName())
            {
                case "Ime": if(daLiJePrazno(text.getText()) || !daLiJeOdgDuzine(text.getText())) return false; else break;
                case "Prezime": if(daLiJePrazno(text.getText()) || !daLiJeOdgDuzine(text.getText())) return false; else break;
                case "Telefon": if(daLiJePrazno(text.getText()) || !daLiJeOdgDuzine(text.getText()) || !(daLiJeTelefon(text.getText()))) return false; else break;
                case "Email":   if(daLiJePrazno(text.getText()) || !daLiJeOdgDuzine(text.getText()) || !(text.getText().contains("@"))) return false; else break;
                case "Lozinka": if(daLiJePrazno(text.getText()) || !daLiJeOdgDuzine(text.getText())) return false; else break;
            }
        }
        return true;
    }
    //Provera da li string zadovoljava oblik telefona
    private boolean daLiJeTelefon(String polje)
    {
        polje = polje.trim();
        return polje.matches("\\d{9,10}");
    }
    //Provera da li je string prazan
    private boolean daLiJePrazno(String polje)
    {
        polje = polje.trim();
        return polje.equals("");
    }
    //Provera da li je string odg duzine
    private boolean daLiJeOdgDuzine(String polje)
    {
        polje = polje.trim();
        return polje.length() >= 3 && polje.length() <= 45;
    }
    //Metoda koja pravi zahtev koji se salje serveru
    private String napraviZahtev(ArrayList rLista, String pocetakZahteva)
    {
        StringBuilder zahtev = new StringBuilder();
        zahtev.append(pocetakZahteva + ":");
        for(Object jtf : rLista)
        {
            JTextField text = (JTextField) jtf;
            zahtev.append(text.getText().trim());
            zahtev.append(" ");
        }
        return zahtev.toString();
    }
    private String napraviZahtevZaPorudzbinu()
    {
        StringBuilder zahtev = new StringBuilder("porudzbina:" + lokalniEmail);
        String sDodaci = "nema";
        //Model sadrzi precnik pice
        ButtonModel model = grupa.getSelection();
        String sPice = pice.getSelectedItem().toString().replaceAll(" ","");
        zahtev.append(" " + sPice + " " + model.getActionCommand() + " ");

        List dodataka = dodaci.getSelectedValuesList();
        if(dodataka.size() > 0)
        {
            for (Object dodatak : dodataka)
            {
                String []pom = dodatak.toString().split("\\|");
                zahtev.append(pom[0].trim().replaceAll(" ","") + ",");
            }
        }
        else zahtev.append(sDodaci);
        zahtev.append(" " + uCena);
        if(adresa.getText().trim().equals(""))
            return "adresa"; //Vraca poruku adresa, kao signal da adresa nije popunjena
        String sAdresa = adresa.getText().trim().replaceAll(" ", "|");
        zahtev.append(" " + sAdresa);
        zahtev.append(napomena.getText().equals("") ? " nema" : " " + napomena.getText());
        //System.out.println(zahtev);
        return zahtev.toString();
    }
    //Unutrasnja klasa koja ce se baviti dugmicima
    class Klik implements ActionListener{

        @Override
        public void actionPerformed (ActionEvent e)
        {
            switch (e.getActionCommand())
            {
                case "Prijava": promeniNaKarticu("prijava"); break;
                case "Registracija": promeniNaKarticu("registracija"); break;
                case "Nazad": promeniNaKarticu("pocetna"); break;
                case "PPotvrdi": if(proveriFormu(pLista)) {
                    setujKonekciju();
                    setujBafere();
                    out.println(napraviZahtev(pLista,"prijava"));
                    System.out.println("Zahtev poslat");
                    try {
                        String odgovor = in.readLine();
                        JOptionPane.showMessageDialog(null,odgovor);
                        if(odgovor.contains("Uspesno"))
                        {
                            String []privremeni = odgovor.split(":");
                            //Cuva se email od prijavljenog klijenta kako bi se koristio pri slanju porudzbine
                            lokalniEmail = privremeni[1].trim();
                            cl.show(cp,"porudzbina");
                        }
                        in.close();
                        out.close();
                        soket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                else {JOptionPane.showMessageDialog(null,"Neko polje je prazno!");}
                break;
                case "RPotvrdi": if(proveriFormu(rLista)) {
                    setujKonekciju();
                    setujBafere();
                    out.println(napraviZahtev(rLista,"registracija"));
                    System.out.println("Zahtev poslat!");
                    try {
                        String odgovor = in.readLine();
                        JOptionPane.showMessageDialog(null,odgovor);
                        in.close();
                        out.close();
                        soket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                else {JOptionPane.showMessageDialog(null,"Niste dobro popunili formu!");}
                break;
                case "Naruči":
                    if(!(napraviZahtevZaPorudzbinu().equals("adresa")))
                    {
                        setujKonekciju();
                        setujBafere();
                        out.println(napraviZahtevZaPorudzbinu());
                        System.out.println("Porudzbina poslata!");
                        try
                        {
                            String odgovor = in.readLine();
                            JOptionPane.showMessageDialog(null,odgovor);
                            int iOdgovor = JOptionPane.showConfirmDialog(null,"Želite li još da poručujete ?","Odluka",
                                    JOptionPane.YES_NO_OPTION);
                            if(iOdgovor == 1)
                            {
                                cl.show(cp,"pocetna");
                            }
                            in.close();
                            out.close();
                            soket.close();
                        } catch (IOException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                    else JOptionPane.showMessageDialog(null,"Polje za adresu mora biti popunjeno!");
                    break;
            }
        }
    }
    private PrecnikPice vratiPrecnikPice(String text)
    {
        switch (text)
        {
            case "25cm":
                return PrecnikPice.M;
            case "32cm":
               return  PrecnikPice.L;
            case "50cm":
               return PrecnikPice.XL;
            default:
                return PrecnikPice.M;
        }
    }
    //Unutrasnja klasa koja ce se baviti radio dugmadima i porudzbinom generalno
    class Promena implements ItemListener
    {
        @Override
        public void itemStateChanged (ItemEvent e)
        {
            PrecnikPice precnik = PrecnikPice.M;
            String imePice = pice.getSelectedItem().toString();
            if (e.getItem() instanceof JRadioButton)
            {
                precnik = vratiPrecnikPice(((AbstractButton) e.getItem()).getText());
            }
            else
            {
                for(JRadioButton r : precnici)
                {
                    if(r.isSelected())
                    {
                        precnik = vratiPrecnikPice(r.getText());
                        break;
                    }
                }
            }
            if (e.getSource() instanceof JComboBox)
            {
                imePice = pice.getSelectedItem().toString();
            }
            pica = FabrikaPica.napraviPicu(new ObicnaPica(precnik),imePice);
            opisPice.setText("Opis: " + pica.vratiOpis());
            cenaP = pica.vratiCenu();
            medjuzbirPica.setText("Medjuzbir za pice: " + cenaP + " RSD");
            osveziUkupno();
        }
    }
    public static void main (String[] args)
    {
        Klijent k = new Klijent();
    }

}
