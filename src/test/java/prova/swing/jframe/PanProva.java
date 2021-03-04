package prova.swing.jframe;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import sm.clagenna.plotge.dati.Bordo;
import sm.clagenna.plotge.dati.Punto;
import sm.clagenna.plotge.dati.TrasponiFinestra;
import sm.clagenna.plotge.dati.Vertice;
import sm.clagenna.plotge.swing.PlotBordo;
import sm.clagenna.plotge.swing.PlotVertice;
import sm.clagenna.plotge.sys.TimeThis;

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
    for (PlotBordo bo : m_liPBord)
      if (bo.checkBersaglio(pu))
        pBo = bo;
    System.out.printf("MousePress (%s)  (%.2f, %.2f) sel=%s\n", //
        pu.toString(), //
        lx1, ly1, //
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
      int inc = p_e.getWheelRotation();
      if (p_e.isShiftDown())
        inc *= 10;
      m_trasp.incrZoom(inc);
      repaint();
    }
  }

  @Override
  public void paint(Graphics p_g) {
    TimeThis tt = new TimeThis("paint");
    Graphics2D g2 = (Graphics2D) p_g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    if (m_trasp.isGeometryChanged(this))
      m_trasp.resetGeometry(this);

    g2.clearRect(0, 0, (int) m_trasp.getWidth(), (int) m_trasp.getHeight());
    disegnaGriglia(g2);
    // disegnaLinea(g2);
    disegnaBordi(g2);
    disegnaVertici(g2);

    tt.stop(true);
  }

  private void disegnaBordi(Graphics2D p_g2) {
    for (PlotBordo pbo : m_liPBord)
      pbo.paintComponent(p_g2, m_trasp);
  }

  private void disegnaVertici(Graphics2D p_g2) {
    for (PlotVertice pve : m_liPVert)
      pve.paintComponent(p_g2, m_trasp, false);
  }

  private void disegnaGriglia(Graphics2D p_g2) {
    Graphics2D g2 = (Graphics2D) p_g2.create();
    g2.setColor(Color.LIGHT_GRAY);
    int wi = (int) m_trasp.getWidth();
    int he = (int) m_trasp.getHeight();

    for (double lx = 0; lx < m_trasp.getMaxX(); lx++) {
      Punto p1 = new Punto((int) lx, 0);
      Punto p2 = new Punto((int) lx, he);
      Punto pp1 = m_trasp.convertiW(p1);
      Punto pp2 = m_trasp.convertiW(p2);
      boolean rott = ((int) lx % 5 == 0);
      Color co = Color.LIGHT_GRAY;
      if (rott)
        co = new Color(192, 64, 192);
      disegnaRetta(g2, pp1, pp2, 1, co);
      if (rott) {
        Punto pu = m_trasp.convertiW(new Punto(lx + 0.5, 1.5));
        stampaNome(g2, pu, String.valueOf((int) lx));
      }
    }
    for (double ly = 0; ly < m_trasp.getMaxY(); ly++) {
      Punto p1 = new Punto(0, (int) ly);
      Punto p2 = new Punto(wi, (int) ly);
      Punto pp1 = m_trasp.convertiW(p1);
      Punto pp2 = m_trasp.convertiW(p2);
      Color co = Color.LIGHT_GRAY;
      boolean rott = ((int) ly % 5 == 0);
      if (rott)
        co = new Color(192, 64, 192);
      disegnaRetta(g2, pp1, pp2, 1, co);
      if (rott) {
        Punto pu = m_trasp.convertiW(new Punto(1.5, ly + 0.5));
        stampaNome(g2, pu, String.valueOf((int) ly));
      }
    }
    g2.dispose();
  }

  private void stampaNome(Graphics2D g2, Punto p_pu, String p_tx) {
    double nFsize = m_trasp.getZoom();

    Font font = new Font("SanSerif", Font.PLAIN, (int) (nFsize));
    FontRenderContext frc = g2.getFontRenderContext();
    GlyphVector gv = font.createGlyphVector(frc, p_tx);
    // Rectangle2D rec = gv.getVisualBounds();

    g2.setColor(Color.DARK_GRAY);

    float px = (float) (p_pu.getWx() - (nFsize * 1F) /* * (sz.length() - 1) */);
    float py = (float) (p_pu.getWy() + nFsize / 1F);
    // System.out.printf("PlotVertice.stampaNome(\"%s\", %.2f, %.2f) zo=%.2f\n", sz, px, py, nFsize);
    g2.drawGlyphVector(gv, px, py);
  }

  private void caricaDati() {
    m_liPVert = new ArrayList<>();

    Vertice ve1 = new Vertice("An");
    ve1.setPunto(new Punto(3, 6));

    m_liPVert.add(new PlotVertice(ve1));

    Vertice ve2 = new Vertice("due");
    ve2.setPunto(new Point(7, 2));
    m_liPVert.add(new PlotVertice(ve2));

    m_liPBord = new ArrayList<>();
    Bordo bo = new Bordo(ve1, ve2, 23);
    ve1.addBordo(bo);
    m_liPBord.add(new PlotBordo(bo));

  }

  protected void disegnaRetta(Graphics2D p_g2, Point p_p1, Point p_p2, int spess, Color p_col) {
    p_g2.setColor(p_col);
    Stroke stk = new BasicStroke(spess);
    p_g2.setStroke(stk);
    Line2D.Double li = new Line2D.Double(p_p1.getX(), p_p1.getY(), p_p2.getX(), p_p2.getY());
    p_g2.draw(li);
  }

}
