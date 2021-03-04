package sm.clagenna.plotge.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

public class SwingApp extends MainJFrame implements PropertyChangeListener {

  /** serialVersionUID long */
  private static final long serialVersionUID = -3372037517230384176L;
  private JSplitPane        m_splitPane;
  private JSplitPane        m_splitParams;
  private PanParmGeneral    m_panVertici;
  private PanelBase         m_panRight;
  private JMenuBar          m_menuBar;
  private JMenu             m_menu;
  private JMenuItem         m_mnuLeggi;
  private JMenuItem         m_mnuSalva;
  private JMenuItem         m_mnuEsci;
  private JMenu             m_mnuCalcolo;
  private JMenuItem         m_mnuShortestPath;
  private JMenuItem         m_mnuRevertPaths;

  public SwingApp(String p_tit) {
    super(p_tit);
  }

  @Override
  protected void creaComponents() {

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

    m_mnuLeggi = new JMenuItem("Leggi");
    m_mnuLeggi.setMnemonic('L');
    m_mnuLeggi.setMnemonic(KeyEvent.VK_L);
    m_mnuLeggi.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuLeggiClick();
      }
    });
    m_menu.add(m_mnuLeggi);

    m_mnuSalva = new JMenuItem("Salva");
    m_mnuSalva.setMnemonic('s');
    m_mnuSalva.setMnemonic(KeyEvent.VK_S);
    m_mnuSalva.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuSalvaClick();
      }
    });
    m_menu.add(m_mnuSalva);

    m_mnuEsci = new JMenuItem("Esci");
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

    m_mnuShortestPath = new JMenuItem("Sh.Path");
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

  protected void mnuLeggiClick() {
    System.out.println("SwingApp.mnuLeggiClick()");

  }

  protected void mnuSalvaClick() {
    System.out.println("SwingApp.mnuSalvaClick()");

  }

  protected void mnuEsciClick() {
    System.out.println("SwingApp.mnuEsciClick()");

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
    
  }

}
