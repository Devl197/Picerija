package server;

import javax.swing.*;
import java.util.Date;

public class Brojac extends Thread
{
    private int ID;
    private double vreme;
    private JLabel labela;
    public Brojac(int ID, double vreme)
    {
        this.vreme = vreme;
        this.ID = ID;
        this.labela = new JLabel("ID porudzbine: " + ID);
        Server.dodajLabelu(labela);
        setDaemon(true);
        start();
    }

    @Override
    public void run ()
    {
        String sLabela = labela.getText();
        System.out.println("Krenula porudzbina koja ima ID: " + ID);
        for(double i = vreme * 1000 * 60; i > 0; i -= 1000)
        {
            try
            {
                labela.setText(sLabela + " vreme: " + i/1000 + " sec");
                Server.revalidacija();
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        Server.obradi(ID,labela);
    }
}
