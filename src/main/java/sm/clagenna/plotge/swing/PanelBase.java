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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sm.clagenna.plotge.dati.Bordo;
import sm.clagenna.plotge.dati.ModelloDati;
import sm.clagenna.plotge.dati.Punto;
import sm.clagenna.plotge.dati.TrasponiFinestra;
import sm.clagenna.plotge.dati.Vertice;
import sm.clagenna.plotge.enumerati.EMouseGesture;
import sm.clagenna.plotge.enumerati.EPropChange;
import sm.clagenna.plotge.sys.AppProperties;
import sm.clagenna.plotge.sys.MioFileFilter;
import sm.clagenna.plotge.sys.PropertyChangeBroadcaster;

public class PanelBase extends JPanel implements PropertyChangeListener {

  /** serialVersionUID long */
  private static final long         serialVersionUID = 5653594504599665379L;
  private static final Logger       s_log            = LogManager.getLogger(PanelBase.class);

  private ModelloDati               m_dati;
  // private TrasponiFinestra          m_trasp;
  private PropertyChangeBroadcaster m_broadc;
  /** vertice selezionato sul grafo */
  private PlotVertice               m_selVert;
  /** il bordo selezionato sul grafo */
  private PlotBordo                 m_selBordo;
  private int                       m_nTasto;

  private PlotVertice               m_primoCerchio;
  private PlotVertice               m_secondoCerchio;
  private Punto                     m_secondoPunto;
  // private Punto                     m_maxp;

  public PanelBase() {
    initialize();
  }

  public PanelBase(LayoutManager p_layout) {
    super(p_layout);
  }

  private void initialize() {
    // rinnovo completamente il Model
    m_dati = MainJFrame.getInstance().nuovoModelloDati();
    m_broadc = PropertyChangeBroadcaster.getInst();
    m_broadc.addPropertyChangeListener(this);
    // m_trasp = new TrasponiFinestra(getSize());
    TrasponiFinestra trasp = m_dati.getTraspondiFinestra();
    trasp.resetGeometry(m_dati);

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
    PropertyChangeBroadcaster.getInst().broadCast(this, EPropChange.panelRezized, dim);
  }

  protected void locMousePress(MouseEvent p_e) {
    // solo se creo nuovo bordo
    m_primoCerchio = null;
    m_secondoCerchio = null;
    // ------------------------
    TrasponiFinestra trasp = m_dati.getTraspondiFinestra();
    Punto pu = trasp.convertiX(new Punto(p_e.getPoint()));
    double lx1 = pu.getX();
    double ly1 = pu.getY();
    PropertyChangeBroadcaster bcst = PropertyChangeBroadcaster.getInst();

    if (m_selBordo != null)
      m_selBordo.setSelected(false);
    if (m_selVert != null)
      m_selVert.setSelected(false);
    m_selBordo = null;
    m_selVert = null;
    // 31 = tasto destro + singolo click
    m_nTasto = p_e.getButton() * 10 + p_e.getClickCount();
    EMouseGesture eg = EMouseGesture.valueOf(m_nTasto);

    switch (eg) {

      case SingClickSinistro: {
        m_selVert = m_dati.checkBersaglioVertice(pu);
        if (m_selVert == null)
          m_selBordo = m_dati.checkBersaglioBordo(pu);
      }
        break;
      case DoppClickSinistro:
        break;

      case SingClickCentrale:
        break;
      case DoppClickCentrale:
        break;

      case SingClickDestro:
        m_primoCerchio = m_dati.checkBersaglioVertice(pu);
        //        if (m_primoCerchio != null)
        //          System.out.println("Clck3:" + m_primoCerchio.toString());
        break;
      case DoppClickDestro:
        if (m_selVert != null)
          m_selVert.setSelected(false);
        m_selVert = m_dati.nuovoVertice(pu);
        break;

      default:
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
      bcst.broadCast(this, EPropChange.selectBordo, m_selBordo);
    }
    if (m_selVert != null) {
      m_selVert.setSelected(true);
      bRepaint = true;
      bcst.broadCast(this, EPropChange.selectVertice, m_selVert);
    }
    if (bRepaint)
      repaint();

  }

  protected void locMouseDragged(MouseEvent p_e) {
    boolean bRepaint = false;
    // vale il tasto di #locMousePressed
    // m_nTasto = p_e.getButton() * 10 + p_e.getClickCount();
    EMouseGesture eg = EMouseGesture.valueOf(m_nTasto);
    TrasponiFinestra trasp = m_dati.getTraspondiFinestra();
    Punto pu = trasp.convertiX(new Punto(p_e.getPoint()));

    switch (eg) {

      case SingClickSinistro:
        if (m_selVert != null)
          m_selVert.setPunto(pu);
        break;
      case DoppClickSinistro:
        break;

      case SingClickCentrale:
        break;
      case DoppClickCentrale:
        break;

      case SingClickDestro:
        if (m_primoCerchio != null) {
          m_secondoCerchio = m_dati.checkBersaglioVertice(pu);
          m_secondoPunto = pu;
          // System.out.println("dragP:" + m_secondoPunto.toString());
        }
        if (m_secondoCerchio != null) {
          if (m_secondoCerchio.equals(m_primoCerchio))
            m_secondoCerchio = null;
          //          else
          //            System.out.println("Drag3:" + m_secondoCerchio.toString());
        }
        break;
      case DoppClickDestro:
        break;

      default:
        break;

    }
    bRepaint = m_selVert != null;
    bRepaint |= m_primoCerchio != null;

    if (bRepaint)
      repaint();
  }

  protected void locMouseRelease(MouseEvent p_e) {
    boolean bRepaint = false;
    m_nTasto = p_e.getButton() * 10 + p_e.getClickCount();
    EMouseGesture eg = EMouseGesture.valueOf(m_nTasto);
    TrasponiFinestra trasp = m_dati.getTraspondiFinestra();
    Punto pu = trasp.convertiX(new Punto(p_e.getPoint()));
    m_secondoPunto = null;

    switch (eg) {

      case SingClickSinistro:
        if (m_selVert != null) {
          m_selVert.setPunto(pu);
          m_dati.recalcEquazLineare(m_selVert);
        }
        break;
      case DoppClickSinistro:
        break;

      case SingClickCentrale:
        break;
      case DoppClickCentrale:
        break;

      case SingClickDestro:
        m_secondoCerchio = m_dati.checkBersaglioVertice(pu);
        break;
      case DoppClickDestro:
        break;

      default:
        break;
    }
    if (m_primoCerchio != null && m_secondoCerchio != null) {
      Vertice v1 = m_dati.findVertice(m_primoCerchio.getId());
      Vertice v2 = m_dati.findVertice(m_secondoCerchio.getId());
      PlotBordo bo = new PlotBordo(new Bordo(v1, v2, 1));
      m_dati.addPlotBordo(bo);
      bRepaint = true;
    }
    if (bRepaint)
      repaint();
  }

  protected void locMouseWheelMoved(MouseWheelEvent p_e) {
    if (p_e.isControlDown()) {
      int inc = p_e.getWheelRotation() * 30;
      if (p_e.isShiftDown())
        inc *= 8;
      TrasponiFinestra trasp = m_dati.getTraspondiFinestra();
      trasp.incrZoom(inc);
      setPreferredSize(new Dimension((int) trasp.getMaxX(), (int) trasp.getMaxY()));
      repaint();
    }
  }

  public void leggiFile(File p_fi) {
    File retFi = p_fi;
    if (retFi == null)
      retFi = apriFileChooser("Dammi il nome file JSON da leggere", true);
    if (retFi == null)
      return;
    m_broadc.removePropertyChangeListener(m_dati);
    // rinnovo completamente il Model
    m_dati = MainJFrame.getInstance().nuovoModelloDati();
    m_dati.leggiFile(retFi);
    repaint();

  }

  public void salvaFile(File p_fi) {
    File retFi = p_fi;
    if (retFi == null)
      retFi = apriFileChooser("Dammi il nome file JSON da leggere", false);
    if (retFi == null)
      return;

    System.out.println("PanelBase.salvaFile():" + retFi.getAbsolutePath());
    m_dati.salvaFile(retFi);

  }

  private File apriFileChooser(String p_titolo, boolean bRead) {
    File retFi = null;
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
    int returnValue = 0;
    if (bRead)
      returnValue = jfc.showOpenDialog(this);
    else
      returnValue = jfc.showSaveDialog(this);
    if (returnValue == JFileChooser.APPROVE_OPTION) {
      retFi = jfc.getSelectedFile();
      props.setLastFile(retFi.getAbsolutePath());
      File fi2 = jfc.getCurrentDirectory();
      props.setLastDir(fi2.getAbsolutePath());
      s_log.info("Scelto file: {}", retFi.getAbsolutePath());
    }
    return retFi;
  }

  @Override
  public void propertyChange(PropertyChangeEvent p_evt) {
    Object obj = p_evt.getOldValue();
    // Object lv = p_evt.getNewValue();
    if ( ! (obj instanceof EPropChange))
      return;
    EPropChange pch = (EPropChange) obj;

    switch (pch) {
      case valNomeVertChanged:
      case valPesoChanged:
        s_log.debug("Evento {} su {}", pch.toString(), p_evt.getSource().getClass().getSimpleName());
        repaint();
        break;
      default:
        break;
    }

  }

  @Override
  public void paint(Graphics p_g) {
    //    TimeThis tt = new TimeThis("paint");
    TrasponiFinestra trasp = m_dati.getTraspondiFinestra();
    Graphics2D g2 = (Graphics2D) p_g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//    if (trasp.isGeometryChanged(this))
//      trasp.resetGeometry(this);
    trasp.resetGeometry(m_dati, this);

    g2.clearRect(0, 0, (int) trasp.getWidth(), (int) trasp.getHeight());
    PlotGriglia pg = new PlotGriglia();
    pg.disegnaGriglia(g2, trasp);
    disegnaBordi(g2);
    disegnaVertici(g2);
    // imposto la pref.size al massimo toccato dai grafi
    // System.out.println("PanelBase.setPreferredSize" + m_maxp);
    setPreferredSize(new Dimension((int) trasp.getMaxX(), (int) trasp.getMaxY()));
    //    tt.stop(true);
  }

  private void disegnaBordi(Graphics2D p_g2) {
    TrasponiFinestra trasp = m_dati.getTraspondiFinestra();
    for (PlotBordo bo : m_dati.getPlotBordi())
      bo.paintComponent(p_g2, trasp);
    //    System.out.printf("disb: primo(%s) \tsecon(%s)\n", //
    //        (m_primoCerchio == null ? "*NULL*" : m_primoCerchio.getPunto().toString()), //
    //        (m_secondoPunto == null ? "*NULL*" : m_secondoPunto.toString()) //
    //    );
    if (m_primoCerchio != null && m_secondoPunto != null) {
      Punto p1 = trasp.convertiW(m_primoCerchio.getPunto());
      Punto p2 = m_secondoPunto;
      p_g2.drawLine(p1.getWx(), p1.getWy(), p2.getWx(), p2.getWy());
      //      System.out.printf("paint line (%d, %d) - (%d, %d)\n", //
      //          p1.getWx(), p1.getWy(), p2.getWx(), p2.getWy());
    }
  }

  private void disegnaVertici(Graphics2D p_g2) {
    TrasponiFinestra trasp = m_dati.getTraspondiFinestra();
    for (PlotVertice pve : m_dati.getPlotVertici())
      pve.paintComponent(p_g2, trasp);
  }

}
