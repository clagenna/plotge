package sm.clagenna.plotge.swing;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

import sm.clagenna.plotge.dati.ModelloDati;
import sm.clagenna.plotge.enumerati.EPropChange;
import sm.clagenna.plotge.sys.AppProperties;
import sm.clagenna.plotge.sys.PropertyChangeBroadcaster;

public class SwingApp extends MainJFrame implements PropertyChangeListener {

  /** serialVersionUID long */
  private static final long         serialVersionUID = -3372037517230384176L;
  private JSplitPane                m_splitPane;
  private JSplitPane                m_splitParams;
  private PanParmGeneral            m_panVertici;
  private PanelBase                 m_panRight;
  private JMenuBar                  m_menuBar;
  private JMenu                     m_menu;
  private JMenuItem                 m_mnuLeggi;
  private JMenuItem                 m_mnuSalva;
  private JMenuItem                 m_mnuEsci;
  private JMenu                     m_mnuCalcolo;
  private JMenuItem                 m_mnuShortestPath;
  private JMenuItem                 m_mnuRevertPaths;
  private PropertyChangeBroadcaster m_bcst;

  public SwingApp(String p_tit) {
    super(p_tit);
  }

  @Override
  protected void creaComponents() {

    m_bcst = PropertyChangeBroadcaster.getInst();
    m_bcst.addPropertyChangeListener(this);

    m_splitPane = new JSplitPane();
    m_splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    getContentPane().add(m_splitPane, BorderLayout.CENTER);

    m_splitParams = new JSplitPane();
    m_splitParams.setOrientation(JSplitPane.VERTICAL_SPLIT);
    m_splitPane.setLeftComponent(m_splitParams);

    m_panVertici = new PanParmGeneral();
    m_splitParams.setLeftComponent(m_panVertici);

    m_panRight = new PanelBase();
    m_panRight.setPreferredSize(new Dimension(400, 300));
    m_splitPane.setRightComponent(m_panRight);

    m_menuBar = new JMenuBar();
    setJMenuBar(m_menuBar);

    m_menu = new JMenu("File");
    m_menu.setMnemonic(KeyEvent.VK_M);
    m_menuBar.add(m_menu);

    m_mnuLeggi = new JMenuItem("Leggi", createImageIcon("open.png", "Apri un file di dati"));
    m_mnuLeggi.setMnemonic('L');
    m_mnuLeggi.setMnemonic(KeyEvent.VK_L);
    m_mnuLeggi.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuLeggiClick();
      }
    });
    m_menu.add(m_mnuLeggi);

    m_mnuSalva = new JMenuItem("Salva", createImageIcon("save.png", "Salva il file di dati"));
    m_mnuSalva.setMnemonic('s');
    m_mnuSalva.setMnemonic(KeyEvent.VK_S);
    m_mnuSalva.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuSalvaClick();
      }
    });
    m_menu.add(m_mnuSalva);

    m_mnuEsci = new JMenuItem("Esci", createImageIcon("exit.png", "Esci dal applicazione"));
    m_mnuEsci.setMnemonic('E');
    m_mnuEsci.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuEsciClick();
      }
    });
    m_menu.add(m_mnuEsci);

    m_mnuCalcolo = new JMenu("Calcolo");
    m_menuBar.add(m_mnuCalcolo);

    m_mnuShortestPath = new JMenuItem("Sh.Path", createImageIcon("shpath.png", "Esci dal applicazione"));
    m_mnuShortestPath.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuShortestPathClick();
      }
    });
    m_mnuCalcolo.add(m_mnuShortestPath);

    m_mnuRevertPaths = new JMenuItem("Inverti Path");
    m_mnuRevertPaths.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuInvertiIPaths();
      }
    });
    m_mnuCalcolo.add(m_mnuRevertPaths);

  }

  private ImageIcon createImageIcon(String path, String description) {
    URL imgURL = getClass().getResource(path);
    ImageIcon ico = null;
    if (imgURL != null)
      ico = new ImageIcon(imgURL, description);
    else
      System.err.println("Couldn't find file: " + path);
    return ico;
  }

  protected void mnuLeggiClick() {

    try {
      System.out.println("SwingApp.mnuLeggiClick()");
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      m_panRight.leggiFile();
    } finally {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
  }

  protected void mnuSalvaClick() {
    try {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      m_panRight.salvaFile();
    } finally {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
  }

  protected void mnuEsciClick() {
    AppProperties prop = AppProperties.getInst();
    if (prop == null)
      return;
    var siz = getSize();
    var pos = getLocation();

    prop.setPropVal(AppProperties.CSZ_PROP_DIMFRAME_X, siz.width);
    prop.setPropVal(AppProperties.CSZ_PROP_DIMFRAME_Y, siz.height);
    prop.setPropVal(AppProperties.CSZ_PROP_POSFRAME_X, pos.x);
    prop.setPropVal(AppProperties.CSZ_PROP_POSFRAME_Y, pos.y);
    prop.saveProperties();
    dispose();
  }

  protected void mnuShortestPathClick() {
    System.out.println("SwingApp.mnuShortestPathClick()");

  }

  protected void mnuInvertiIPaths() {
    System.out.println("SwingApp.mnuInvertiIPaths()");

  }

  public static void main(String[] args) {
    JFrame frame = new SwingApp("Trova lo shortest path con Dijkstra");
    MainJFrame.lanciaApp(frame);
  }

  @Override
  public void propertyChange(PropertyChangeEvent p_evt) {
    String szNam = p_evt.getPropertyName();
    String szTit = getTitle();
    System.out.println("SwingApp.propertyChange():" + p_evt.toString());
    if (szNam == null)
      return;
    EPropChange pch = EPropChange.valueOf(p_evt.getPropertyName());
    switch (pch) {
      
      case leggiFile:
        szNam=p_evt.getSource().toString();
        szTit = String.format("Shortest Path file: %s", szNam);
        setTitle(szTit);
        break;
        
      default:
        break;
    }
  }

  @Override
  public String getTitle() {
    String sz = super.getTitle();
    ModelloDati dat = getDati();
    File fi = null;
    if (dat != null)
      fi = dat.getFileDati();
    if (fi != null)
      sz = String.format("Shortest path file:" + fi.getAbsolutePath());
    return sz;
  }

}
