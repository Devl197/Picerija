package server;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

public class Server extends JFrame implements Serializable
{
    private ServerSocket ss;
    //Port na kome radi server.Server
    private final int TCP_IP = 9000;
    //Lista u kojoj ce se cuvati klijenti
    static volatile List <Klijent> klijenti = new ArrayList<>();
    //Mape porudzbina
    static volatile HashMap<Integer,Porudzbina> porudzbineNeisporucene = new HashMap<>();
    static volatile HashMap<Integer,Porudzbina> porudzbineIsporucene = new HashMap<>();
    //TextArea za isporucene porudzbine
    private static JTextArea zaIsporucene = new JTextArea();
    //Panel za neisporucene porudzbine
    private static JPanel zaNeisporucene = new JPanel();
    static String datIsporucene = "isporucene";
    static String datNeisporucene = "neisporucene";
    static boolean koristimMapu = false;
    public Server()
    {
        try
        {
            ss = new ServerSocket(TCP_IP);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        setTitle("Server");
        setSize(800,650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        Container cp = getContentPane();
        cp.setLayout(new GridLayout(1,2));
        //Pane za isporucene porudzbine
        JScrollPane pane1 = new JScrollPane();
        //Pane za neisporucene porudzbine
        JScrollPane pane2 = new JScrollPane();
        pane1.getViewport().add(zaIsporucene);
        zaNeisporucene.setLayout(new BoxLayout(zaNeisporucene,BoxLayout.Y_AXIS));
        pane2.getViewport().add(zaNeisporucene);
        zaIsporucene.setEditable(false);
        cp.add(pane1);
        cp.add(pane2);
        procitajListuIzDat("klijenti");
        JLabel neisporucene = new JLabel("Neisporučene porudžbine: ");
        zaNeisporucene.add(neisporucene);
        porudzbineIsporucene = citajHashMapuIzDat(datIsporucene);
        porudzbineNeisporucene = citajHashMapuIzDat(datNeisporucene);
        nastavi();
        setujTextAreu();
        setVisible(true);
    }
    //Metoda koja dodaje labele
    static void dodajLabelu(JLabel labela)
    {
        zaNeisporucene.add(labela);
    }
    //Metoda koja uporedjuje dati email sa listom i utvrdjuje da li u listi postoji vec takav email
    static boolean daLiListaSadrziKlijenta(String email)
    {
        for(Klijent o : klijenti)
        {
            if(o != null && o.getEmail().equals(email))
                return true;
        }
        return false;
    }
    //Metoda koja izlistava osobe
    static void izlistajOsobe(){
        System.out.println("Pocetak izlistavanja: ");
        for(Klijent o : klijenti)
        {
            System.out.println(o);
        }
    }
    //Metoda za autentifikaciju klijenata
    static int autentifikuj(String email, String lozinka)
    {
        if(klijenti.size() > 0)
        {
            for(Klijent o : klijenti)
            {
                if(o.getEmail().equals(email))
                {
                    if(o.getLozinka().equals(lozinka))
                        return 0;//Vraca 0 ako je sve uredu
                    else return -2;//Vraca -2 ako je nasao email, a sifra je pogresna
                }
            }
            return -1; //Vraca -1 ako nije nasao email
        }
        else return -3;//Vraca -3 kada je lista prazna
    }
    //Metoda koja vraca osobu ako je nadje u liste klijanata, u slucaju da ne nadje osobu vraca null
    static Klijent vratiKlijenta(String email)
    {
        for(Klijent klijent : klijenti)
        {
            if(klijent.getEmail().equals(email))
                return klijent;
        }
        return null;
    }
    //Metoda koja upisuje u datoteku liste
    static void upisiListuUDat(String imeDat, List lista)
    {
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream(imeDat + ".bin"));
            //Posto je objekat klijenti statican ne moze da se upise u datoteku, zato je potrebno prekopirati ga u novi
            // objekat koji moze da se upise
            List lista2 = lista;
            oos.writeObject(lista2);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    //Cita listu iz datoteke
    static void procitajListuIzDat(String imeDat)
    {
        try
        {
            File datoteka = new File(imeDat + ".bin");
            if(datoteka.exists())
            {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(datoteka));
                klijenti = (List<Klijent>) ois.readObject();
            }
            else throw new Exception("Datoteka nepostoji!");
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //Metoda koja revalidira i repaintuje panel
    static void revalidacija()
    {
        zaNeisporucene.revalidate();
        zaNeisporucene.repaint();
    }
    //Metoda koja sklanja labelu sa panela
    static void skloniLabelu(JLabel label)
    {
        zaNeisporucene.remove(label);
        revalidacija();
    }
    //Metoda koja dodaje na text-areu
    static void dodajNaTextArea(String string)
    {
        zaIsporucene.setText(zaIsporucene.getText() + "\n" + string);
        revalidacija();
    }
    //Metoda za cuvanje hashmapa
    static void upisiHashMapuUDat(HashMap <Integer, Porudzbina> mapa,String imeDat)
    {
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream(imeDat + ".bin"));
            HashMap<Integer,Porudzbina> pom = mapa;
            System.out.println("Upisao mapu!");
            oos.writeObject(mapa);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    //Namesti textAreu
    private void setujTextAreu()
    {
        String tekst = "Isporučene narudžbine: ";
        zaIsporucene.setText(tekst + vratiVrednostHashMape(porudzbineIsporucene));
    }
    //Metoda koja cita hashmapu iza dat
    private static HashMap<Integer,Porudzbina> citajHashMapuIzDat(String imeDat)
    {
        try
        {
            Path path = FileSystems.getDefault().getPath("."); //absolutna ruta od roditeljskog direktorijuma
            File datoteka = new File(path + "\\" + imeDat + ".bin");
            if(datoteka.exists())
            {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(datoteka));
                HashMap<Integer,Porudzbina> mapa = (HashMap<Integer,Porudzbina>) ois.readObject();
                System.out.println("Uspesno ucitana mapa!");
                return mapa;
            }
            else throw new Exception("Datoteka  " + datoteka +  "nepostoji!");
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new HashMap<Integer, Porudzbina>();
    }
    //Metoda koja ispisuje hashmapu
    static void ispisiMapu(HashMap<Integer,Porudzbina> mapa)
    {
        Iterator it = mapa.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry par = (Map.Entry) it.next();
            System.out.println(par.getKey() + " = " + par.getValue());
        }
    }
    //Metoda koja obnavlja thredove i porudzbine ukoliko dodje do njihovog prekida usled nestanka struje ili neke
    // druge prilike
    private void nastavi()
    {
        Date trenutno = new Date();
        long lTrenutno = trenutno.getTime(); //trenutno vreme u milisekundama
        //Prolazak kroz mapu da bi se utvrdilo vreme koje je bilo predvidjeno za isporuku
        HashMap<Integer, Porudzbina> kopija = new HashMap<>(porudzbineNeisporucene);
        Iterator it = kopija.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry par = (Map.Entry) it.next();
            Porudzbina p = (Porudzbina)par.getValue();
            long lVreme = p.predvidjenoVreme() - lTrenutno; // ako je lvreme > 0 treba da se nastavi u suprotnom se
            // porudzbina zavrsila
            if(lVreme <= 0)//Porudzbina se zavrsila
            {
                Server.obradi(p.getID(),null);
            }
            else
            {
                new Brojac(p.getID(),(double)lVreme / 60000);
            }
        }
    }
    //Metoda koja vraca vrednost hashmape
    static String vratiVrednostHashMape(HashMap<Integer,Porudzbina> mapa)
    {
        StringBuilder vrednost = new StringBuilder();
        Iterator it = mapa.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry par = (Map.Entry) it.next();
            vrednost.append("\n" + par.getValue());
        }
        return vrednost.toString();
    }
    //Obradi porudzbinu
    static synchronized void obradi(int idPorudzbine, JLabel labela)
    {
        Server.koristimMapu = true;
        Porudzbina p = Server.porudzbineNeisporucene.remove(idPorudzbine);//Izbacuje porudzbinu koja se izvrsila
        Server.upisiHashMapuUDat(Server.porudzbineNeisporucene,Server.datNeisporucene);//Upisuje promenjenu mapu
        // porudzbina u datoteku
        p.setDatumIsporuke(new Date());//Stavlja porudzbinu trenutno vreme isporuke
        Server.porudzbineIsporucene.put(idPorudzbine,p);//Stavlja porudzbinu u mapu za isporucene
        if(labela != null)
        {
            Server.skloniLabelu(labela);//Uklanja labelu za panela
        }
        Server.dodajNaTextArea(p.toString());//Dodaje na textareu
        Server.upisiHashMapuUDat(Server.porudzbineIsporucene,Server.datIsporucene);//Upisuje mapu isporucenih
        // porudzbina u fajl
        System.out.println("Isporucena: " + p);
    }
    //Metoda u kojoj se delegiraju klijenti serverskim tredovima
    public void primajKlijente()
    {
        System.out.println("Pokrenuto primanje klijenata");
        while (true)
        {
            try
            {
                Socket s = ss.accept();
                System.out.println("Klijent primljen");
                ServerThread st = new ServerThread(s);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    public static void main (String[] args)
    {
        Server s = new Server();
        s.primajKlijente();
    }
}
