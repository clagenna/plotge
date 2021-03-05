package prova.swing.jframe;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import sm.clagenna.plotge.dati.Bordo;
import sm.clagenna.plotge.dati.Punto;
import sm.clagenna.plotge.dati.TrasponiFinestra;
import sm.clagenna.plotge.dati.Vertice;
import sm.clagenna.plotge.swing.PlotBordo;
import sm.clagenna.plotge.swing.PlotGriglia;
import sm.clagenna.plotge.swing.PlotVertice;

public class PanProva extends JPanel {
  /** serialVersionUID long */
  private static final long serialVersionUID = -8278183255370594859L;
  // private int               zoom;
  private TrasponiFinestra  m_trasp;

  private List<PlotVertice> m_liPVert;
  private List<PlotBordo>   m_liPBord;
  private PlotBordo         m_plBo;

  public PanProva() {
    inizializza();
  }

  public PanProva(LayoutManager p_layout) {
    super(p_layout);
    inizializza();
  }

  public void inizializza() {
    m_trasp = new TrasponiFinestra(this);

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

    addMouseWheelListener(new MouseAdapter() {
      @Override
      public void mouseWheelMoved(MouseWheelEvent p_e) {
        locMouseWheelMoved(p_e);
      }
    });

    caricaDati();
  }

  protected void locMousePress(MouseEvent p_e) {
    Punto pu = m_trasp.convertiX(new Punto(p_e.getPoint()));
    double lx1 = pu.getX();
    double ly1 = pu.getY();
    // boolean bSel = false;
    PlotBordo pBo = null;
    PlotVertice pVe = null;
    for (PlotVertice ve : m_liPVert)
      if (ve.checkBersaglio(pu)) {
        pVe = ve;
        break;
      }
    for (PlotBordo bo : m_liPBord)
      if (bo.checkBersaglio(pu)) {
        pBo = bo;
        break;
      }
    System.out.printf("MousePress (%s)  (%.2f, %.2f) V=%s , B=%s\n", //
        pu.toString(), //
        lx1, ly1, //
        (pVe != null ? pVe.getId() : "-" ), //
        (pBo != null ? pBo.toString() : "-"));
    boolean bRepaint = false;
    if (m_plBo != null) {
      m_plBo.getBordo().setShortNo(0);
      m_plBo = null;
      bRepaint = true;
    }
    if (pBo != null) {
      m_plBo = pBo;
      m_plBo.getBordo().setShortNo(1);
      bRepaint = true;
    }
    if (bRepaint)
      repaint();

  }

  protected void locMouseRelease(MouseEvent p_e) {
    // TODO Auto-generated method stub

  }

  protected void locMouseWheelMoved(MouseWheelEvent p_e) {
    String szWhe = p_e.isControlDown() ? "Ctrl-Down" : "";
    System.out.printf("ProvaSwingBase.locMouseWheelMoved(%d, %s)\n", p_e.getWheelRotation(), szWhe);
    if (p_e.isControlDown()) {
      int inc = p_e.getWheelRotation() * 30;
      if (p_e.isShiftDown())
        inc *= 8;
      m_trasp.incrZoom(inc);
      repaint();
    }
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
    for (PlotBordo pbo : m_liPBord)
      pbo.paintComponent(p_g2, m_trasp);
  }

  private void disegnaVertici(Graphics2D p_g2) {
    for (PlotVertice pve : m_liPVert)
      pve.paintComponent(p_g2, m_trasp, false);
  }

  private void caricaDati() {
    m_liPVert = new ArrayList<>();

    Vertice ve1 = new Vertice("A");
    ve1.setPunto(new Punto(3, 6));

    m_liPVert.add(new PlotVertice(ve1));

    Vertice ve2 = new Vertice("due");
    ve2.setPunto(new Point(7, 2));
    m_liPVert.add(new PlotVertice(ve2));

    Vertice ve3 = new Vertice("Lungo");
    ve3.setPunto(new Point(7, 8));
    m_liPVert.add(new PlotVertice(ve3));

    Vertice ve4 = new Vertice("steso");
    ve4.setPunto(new Point(15, 2));
    m_liPVert.add(new PlotVertice(ve4));

    m_liPBord = new ArrayList<>();
    
    Bordo bo = new Bordo(ve1, ve2, 23);
    ve1.addBordo(bo);
    m_liPBord.add(new PlotBordo(bo));
    
    bo = new Bordo(ve2, ve3, 13);
    ve2.addBordo(bo);
    m_liPBord.add(new PlotBordo(bo));
    
    bo = new Bordo(ve2, ve4, 17);
    ve2.addBordo(bo);
    m_liPBord.add(new PlotBordo(bo));

  }

}
