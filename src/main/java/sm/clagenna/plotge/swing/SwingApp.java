package sm.clagenna.plotge.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sm.clagenna.plotge.dati.MenuFiles;
import sm.clagenna.plotge.dati.MioDijkstra;
import sm.clagenna.plotge.dati.ModelloDati;
import sm.clagenna.plotge.dati.Vertice;
import sm.clagenna.plotge.enumerati.EPropChange;
import sm.clagenna.plotge.interf.IGestFile;
import sm.clagenna.plotge.sys.AppProperties;
import sm.clagenna.plotge.sys.PropertyChangeBroadcaster;

public class SwingApp extends MainJFrame implements PropertyChangeListener, IGestFile {

  /** serialVersionUID long */
  private static final long         serialVersionUID = -3372037517230384176L;
  private static final Logger       s_log            = LogManager.getLogger(SwingApp.class);

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
  private MenuFiles                 m_filesMenu;
  private JMenu                     m_mnUltimiFiles;
  private JPanel                    m_statusBar;
  private JLabel                    m_lbStatus;
  private JPanel m_panel;

  public SwingApp(String p_tit) {
    super(p_tit);
  }

  @Override
  protected void creaComponents() {
    String sz = null;
    m_filesMenu = MenuFiles.getInst();
    AppProperties pr = getProp();
    sz = pr.getPropVal(AppProperties.CSZ_PROP_BO_TOLLER);
    if (sz != null)
      EquazLineare.setTolleranza(Double.parseDouble(sz));

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
    
    m_panel = new JPanel();
    m_splitParams.setRightComponent(m_panel);

    m_panRight = new PanelBase();
    m_panRight.setPreferredSize(new Dimension(400, 300));

    JScrollPane panScroll = new JScrollPane(m_panRight, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    m_splitPane.setRightComponent(panScroll);

    m_statusBar = new JPanel();
    getContentPane().add(m_statusBar, BorderLayout.SOUTH);
    m_statusBar.setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY), new EmptyBorder(4, 4, 4, 4)));

    m_lbStatus = new JLabel("Last Status");
    m_lbStatus.setHorizontalAlignment(SwingConstants.LEFT);
    m_statusBar.add(m_lbStatus);
    
    
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent p_e) {
        mnuEsciClick();
      }
    });

    m_menuBar = new JMenuBar();
    setJMenuBar(m_menuBar);

    m_menu = new JMenu("File");
    m_menu.setMnemonic(KeyEvent.VK_M);
    m_menuBar.add(m_menu);

    m_mnuLeggi = new JMenuItem("Leggi", createImageIcon("open.png", "Apri un file di dati"));
    m_mnuLeggi.setMnemonic('L');
    m_mnuLeggi.setMnemonic(KeyEvent.VK_L);
    KeyStroke shK = KeyStroke.getKeyStroke('L', java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
    m_mnuLeggi.setAccelerator(shK);
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
    shK = KeyStroke.getKeyStroke('S', java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
    m_mnuSalva.setAccelerator(shK);
    m_mnuSalva.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuSalvaClick();
      }
    });
    m_menu.add(m_mnuSalva);

    m_mnUltimiFiles = new JMenu("Ultimi Files");
    m_menu.add(m_mnUltimiFiles);

    m_filesMenu.creaElenco(this, m_mnUltimiFiles);

    JSeparator separator = new JSeparator();
    m_menu.add(separator);

    m_mnuEsci = new JMenuItem("Esci", createImageIcon("exit.png", "Esci dal applicazione"));
    m_mnuEsci.setMnemonic('E');
    shK = KeyStroke.getKeyStroke('E', java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
    m_mnuEsci.setAccelerator(shK);
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
    shK = KeyStroke.getKeyStroke('H', java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
    m_mnuShortestPath.setAccelerator(shK);
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
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      m_panRight.leggiFile(null);
    } finally {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
  }

  @Override
  public void apriFile(File p_fi) {
    try {
      System.out.println("SwingApp.mnuLeggiClick()");
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      m_panRight.leggiFile(p_fi);
    } finally {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

  }

  protected void mnuSalvaClick() {
    try {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      m_panRight.salvaFile(null);
    } finally {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
  }

  protected void mnuEsciClick() {
    if ( !getDati().canIDispose(m_panRight))
      return;
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
    try {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      ModelloDati dati = MainJFrame.getInstance().getDati();
      dati.resetShortestPath();
      Vertice primo = dati.getStartVert();
      Vertice finale = dati.getEndVert();
      MioDijkstra dij = new MioDijkstra();
      dij.analizzaTutti(primo, finale);
      s_log.debug("MainFrame.mnuShortestPathClick():{}", dij.getNodoFine());
      m_panRight.repaint();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

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
    String szNam = null;
    String szTit = getTitle();
    // System.out.println("SwingApp.propertyChange():" + p_evt.toString());

    EPropChange pch = (EPropChange) p_evt.getOldValue();
    switch (pch) {
      case scriviFile:
      case leggiFile:
        szNam = p_evt.getNewValue().toString();
        szTit = String.format("Shortest Path file: %s", szNam);
        setTitle(szTit);
        m_filesMenu.creaElenco(this, m_mnUltimiFiles);
        break;

      case notificaStatus:
        szTit = (String) p_evt.getNewValue();
        m_lbStatus.setText(szTit);
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
