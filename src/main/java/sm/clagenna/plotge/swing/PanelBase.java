package sm.clagenna.plotge.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;

import sm.clagenna.plotge.dati.ModelloDati;
import sm.clagenna.plotge.dati.Punto;
import sm.clagenna.plotge.dati.TrasponiFinestra;
import sm.clagenna.plotge.enumerati.EPropChange;
import sm.clagenna.plotge.sys.AppProperties;
import sm.clagenna.plotge.sys.MioFileFilter;
import sm.clagenna.plotge.sys.PropertyChangeBroadcaster;

public class PanelBase extends JPanel implements PropertyChangeListener {

  /** serialVersionUID long */
  private static final long                            serialVersionUID = 5653594504599665379L;

  private ModelloDati                                  m_dati;
  @SuppressWarnings("unused") private TrasponiFinestra m_trasp;
  private PropertyChangeBroadcaster                    m_broadc;
  /** vertice selezionato sul grafo */
  private PlotVertice                                  m_selVert;
  /** il bordo selezionato sul grafo */
  private PlotBordo                                    m_selBordo;
  private int                                          m_nTasto;

  public PanelBase() {
    initialize();
  }

  public PanelBase(LayoutManager p_layout) {
    super(p_layout);
  }

  private void initialize() {
    m_dati = new ModelloDati();
    m_broadc = PropertyChangeBroadcaster.getInst();
    m_trasp = new TrasponiFinestra(getSize());

    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent p_e) {
        locComponentResized(p_e);
      }
    });

    addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent p_e) {
        locMousePress(p_e);
      }

      @Override
      public void mouseReleased(MouseEvent p_e) {
        locMouseRelease(p_e);
      }

    });

    addMouseMotionListener(new MouseMotionAdapter() {

      @Override
      public void mouseDragged(MouseEvent p_e) {
        locMouseDragged(p_e);
      }
    });

    addMouseWheelListener(new MouseAdapter() {
      @Override
      public void mouseWheelMoved(MouseWheelEvent p_e) {
        locMouseWheelMoved(p_e);
      }
    });

  }

  protected void locComponentResized(ComponentEvent p_e) {
    Dimension dim = p_e.getComponent().getSize();
    PropertyChangeEvent ev = new PropertyChangeEvent(this, null, EPropChange.panelRezized, dim);
    PropertyChangeBroadcaster.getInst().broadCast(ev);
  }

  protected void locMousePress(MouseEvent p_e) {
    Punto pu = m_trasp.convertiX(new Punto(p_e.getPoint()));
    double lx1 = pu.getX();
    double ly1 = pu.getY();
    if (m_selBordo != null)
      m_selBordo.setSelected(false);
    if (m_selVert != null)
      m_selVert.setSelected(false);
    m_selBordo = null;
    m_selVert = null;
    // 12 = tasto sin. + doppio click
    // 31 = tasto destro + singolo click
    m_nTasto = p_e.getButton() * 10 + p_e.getClickCount();

    switch (m_nTasto) {
      
      case 11: // sin + sing. click
      {
        for (PlotVertice ve : m_dati.getPlotVertici())
          if (ve.checkBersaglio(pu)) {
            m_selVert = ve;
            break;
          }
        if (m_selVert == null) {
          for (PlotBordo bo : m_dati.getPlotBordi())
            if (bo.checkBersaglio(pu)) {
              m_selBordo = bo;
              break;
            }
        }
      }
        break;
      case 31: // destro + sing. click
        break;
    }

    System.out.printf("MousePress (%s)  (%.2f, %.2f) V=%s , B=%s\n", //
        pu.toString(), //
        lx1, ly1, //
        (m_selVert != null ? m_selVert.getId() : "-"), //
        (m_selBordo != null ? m_selBordo.toString() : "-"));

    boolean bRepaint = false;
    if (m_selBordo != null) {
      m_selBordo.setSelected(true);
      bRepaint = true;
    }
    if (m_selVert != null) {
      m_selVert.setSelected(true);
      bRepaint = true;
    }
    if (bRepaint)
      repaint();

  }

  protected void locMouseDragged(MouseEvent p_e) {
    boolean bRepaint = false;
    Punto pu = m_trasp.convertiX(new Punto(p_e.getPoint()));
    if (m_selVert != null) {
      m_selVert.setPunto(pu);
      for (PlotBordo bo : m_dati.getPlotBordi())
        bo.recalculate();
      bRepaint = true;
    }
    if (bRepaint)
      repaint();
  }

  protected void locMouseRelease(MouseEvent p_e) {
    // TODO Auto-generated method stub

  }

  protected void locMouseWheelMoved(MouseWheelEvent p_e) {
    if (p_e.isControlDown()) {
      int inc = p_e.getWheelRotation() * 30;
      if (p_e.isShiftDown())
        inc *= 8;
      m_trasp.incrZoom(inc);
      repaint();
    }
  }

  public void leggiFile() {
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

      System.out.println("PanelBase.leggiFile():" + fi.getAbsolutePath());
      //      m_bDragging = false;
      //      m_cerchio = null;
      //      m_primoCerchio = null;
      //      m_secondoCerchio = null;
      //      m_nTasto = 0;
      m_broadc.removePropertyChangeListener(m_dati);
      m_dati = new ModelloDati();
      m_dati.leggiFile(fi);
      repaint();
    }

  }

  public void salvaFile() {
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
      System.out.println("PanelBase.salvaFile():" + fi.getAbsolutePath());
      m_dati.salvaFile(fi);
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent p_evt) {
    // TODO Auto-generated method stub

  }

  @Override
  public void paint(Graphics p_g) {
    //    TimeThis tt = new TimeThis("paint");

    Graphics2D g2 = (Graphics2D) p_g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    if (m_trasp.isGeometryChanged(this))
      m_trasp.resetGeometry(this);

    g2.clearRect(0, 0, (int) m_trasp.getWidth(), (int) m_trasp.getHeight());
    PlotGriglia pg = new PlotGriglia();
    pg.disegnaGriglia(g2, m_trasp);
    disegnaBordi(g2);
    disegnaVertici(g2);

    //    tt.stop(true);
  }

  private void disegnaBordi(Graphics2D p_g2) {
    for (PlotBordo bo : m_dati.getPlotBordi())
      bo.paintComponent(p_g2, m_trasp);
  }

  private void disegnaVertici(Graphics2D p_g2) {
    for (PlotVertice pve : m_dati.getPlotVertici())
      pve.paintComponent(p_g2, m_trasp);
  }

}
