package prova.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileSystemView;

import sm.clagenna.plotge.dati.MenuFiles;
import sm.clagenna.plotge.interf.IGestFile;
import sm.clagenna.plotge.sys.AppProperties;
import sm.clagenna.plotge.sys.MioFileFilter;

public class ProvaMenuFile extends JFrame implements IGestFile {

  /** serialVersionUID long */
  private static final long serialVersionUID = -7202914775847935434L;
  // private AppProperties     m_prop;
  private JMenuBar          m_menuBar;
  private JMenu             m_mnUltimiFiles;
  private MenuFiles         m_menuf;

  public ProvaMenuFile() {
    super("Prova elenco files");
    inizializza();
  }

  private void inizializza() {

    try {
      // Set cross-platform Java L&F (also called "Metal")
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception l_e) {
      System.out.println("Set Look and Feel:" + l_e.toString());
      System.exit(1);
    }

    setSize(400, 600);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    AppProperties prop = new AppProperties();
    prop.openProperties();
    m_menuf = new MenuFiles();

    m_menuBar = new JMenuBar();
    setJMenuBar(m_menuBar);

    JMenu mnFile = new JMenu("File");
    m_menuBar.add(mnFile);

    JMenuItem mnApriFile = new JMenuItem("Apri");
    mnApriFile.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        locApriFile();
      }
    });
    mnFile.add(mnApriFile);

    JMenuItem mnSalvaFile = new JMenuItem("Salva");
    mnFile.add(mnSalvaFile);
    mnSalvaFile.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        locSalvaFile();
      }
    });

    m_mnUltimiFiles = new JMenu("Ultimi Files");
    mnFile.add(m_mnUltimiFiles);
    m_menuf.creaElenco(this, m_mnUltimiFiles);

    JSeparator separator = new JSeparator();
    mnFile.add(separator);

    JMenuItem mnEsci = new JMenuItem("Esci");
    mnEsci.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        locEsci();
      }
    });
    mnFile.add(mnEsci);
  }

  protected void locApriFile() {
    JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    jfc.setDialogTitle("Dammi il nome file JSON da leggere");
    jfc.setMultiSelectionEnabled(false);
    jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

    MioFileFilter json = new MioFileFilter("json file", ".json");
    jfc.addChoosableFileFilter(json);
    AppProperties props = AppProperties.getInst();
    String sz = props.getLastDir();
    if (sz != null)
      jfc.setCurrentDirectory(new File(sz));

    int returnValue = jfc.showOpenDialog(this);
    if (returnValue == JFileChooser.APPROVE_OPTION) {
      File fi = jfc.getSelectedFile();
      props.setLastFile(fi.getAbsolutePath());
      File fi2 = jfc.getCurrentDirectory();
      props.setLastDir(fi2.getAbsolutePath());
      m_menuf.add(fi);
      m_menuf.creaElenco(this, m_mnUltimiFiles);
    }

  }

  protected void locSalvaFile() {
    JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    jfc.setDialogTitle("Dammi il nome file JSON su cui salvare");
    jfc.setMultiSelectionEnabled(false);
    jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

    MioFileFilter json = new MioFileFilter("json file", ".json");
    jfc.addChoosableFileFilter(json);

    AppProperties props = AppProperties.getInst();
    String sz = props.getLastDir();
    if (sz != null)
      jfc.setCurrentDirectory(new File(sz));
    int returnValue = jfc.showSaveDialog(this);
    if (returnValue == JFileChooser.APPROVE_OPTION) {
      File fi = jfc.getSelectedFile();
      props.setLastFile(fi.getAbsolutePath());
      File fi2 = jfc.getCurrentDirectory();
      props.setLastDir(fi2.getAbsolutePath());
      m_menuf.add(fi);
    }

  }

  protected void locEsci() {
    AppProperties prop = AppProperties.getInst();
    prop.saveProperties();
    dispose();
  }

  @Override
  public void apriFile(File p_fi) {
    System.out.println("Apri file:" + p_fi.getAbsolutePath());
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        ProvaMenuFile fram = new ProvaMenuFile();
        fram.setVisible(true);
      }
    });
  }

}
