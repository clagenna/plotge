package prova.json;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import sm.clagenna.plotge.dati.Bordo;
import sm.clagenna.plotge.dati.ModelloDati;
import sm.clagenna.plotge.dati.Punto;
import sm.clagenna.plotge.dati.Vertice;
import sm.clagenna.plotge.swing.PlotBordo;
import sm.clagenna.plotge.swing.PlotVertice;

public class ProvaJSon {

  private static final String CSZ_JSONFILE  = "log/ProvaJSon.json";
  private static final String CSZ_JSONFILE2 = "log/ProvaJSonDue.json";

  private ModelloDati         m_dati;
  private ModelloDati         m_datiLetto;

  public ProvaJSon() {
    //
  }

  @Test
  public void doTheJob() {
    m_dati = new ModelloDati();
    caricaDati();
    serializza();
    deserializza();
    serializzaDue();
    confrontaHash();
  }

  private void caricaDati() {

    Vertice ve1 = new Vertice("A");
    ve1.setPunto(new Punto(3, 6));

    m_dati.addPlotVertice(new PlotVertice(ve1));

    Vertice ve2 = new Vertice("due");
    ve2.setPunto(new Point(7, 2));
    m_dati.addPlotVertice(new PlotVertice(ve2));

    Vertice ve3 = new Vertice("Lungo");
    ve3.setPunto(new Point(7, 8));
    m_dati.addPlotVertice(new PlotVertice(ve3));

    Vertice ve4 = new Vertice("steso");
    ve4.setPunto(new Point(15, 2));
    m_dati.addPlotVertice(new PlotVertice(ve4));

    Bordo bo = new Bordo(ve1, ve2, 23);
    ve1.addBordo(bo);
    m_dati.addPlotBordo(new PlotBordo(bo));

    bo = new Bordo(ve2, ve3, 13);
    ve2.addBordo(bo);
    m_dati.addPlotBordo(new PlotBordo(bo));

    bo = new Bordo(ve2, ve4, 17);
    ve2.addBordo(bo);
    m_dati.addPlotBordo(new PlotBordo(bo));

  }

  private void serializza() {
    File fi = new File(CSZ_JSONFILE);
    m_dati.salvaFile(fi);
  }

  private void deserializza() {
    File fi = new File(CSZ_JSONFILE);
    m_datiLetto = new ModelloDati();
    m_datiLetto.leggiFile(fi);
  }

  private void serializzaDue() {
    File fi = new File(CSZ_JSONFILE2);
    m_datiLetto.salvaFile(fi);
  }

  private void confrontaHash() {
    File fi1 = new File(CSZ_JSONFILE);
    File fi2 = new File(CSZ_JSONFILE2);

    try {
      MessageDigest shaDigest = MessageDigest.getInstance("SHA-256");
      String sha1 = ProvaJSon.getFileChecksum(shaDigest, fi1);
      String sha2 = ProvaJSon.getFileChecksum(shaDigest, fi2);
      System.out.println("---------------  Hash dei files generati    --------------");
      System.out.printf("%s\t%s\n", CSZ_JSONFILE, sha1);
      System.out.printf("%s\t%s\n", CSZ_JSONFILE2, sha2);
      if ( sha1.equals(sha2))
        System.out.println("I files sono IDENTICI !!");
      else
        System.out.println("????   Errore nella serializzazione  ?????");
        
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static String getFileChecksum(MessageDigest digest, File file) throws IOException {
    //Get file input stream for reading the file content
    FileInputStream fis = new FileInputStream(file);

    //Create byte array to read data in chunks
    byte[] byteArray = new byte[1024];
    int bytesCount = 0;

    //Read file data and update in message digest
    while ( (bytesCount = fis.read(byteArray)) != -1) {
      digest.update(byteArray, 0, bytesCount);
    }
    ;

    //close the stream; We don't need it now.
    fis.close();

    //Get the hash's bytes
    byte[] bytes = digest.digest();

    //This bytes[] has bytes in decimal format;
    //Convert it to hexadecimal format
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < bytes.length; i++) {
      sb.append(Integer.toString( (bytes[i] & 0xff) + 0x100, 16).substring(1));
    }

    //return complete hash
    return sb.toString();
  }

}
