package sm.clagenna.plotge.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

import lombok.Getter;
import lombok.Setter;
import sm.clagenna.plotge.dati.Bordo;
import sm.clagenna.plotge.dati.Punto;
import sm.clagenna.plotge.dati.TrasponiFinestra;

public class PlotBordo {

  private static Color                      s_defaulColor     = Color.pink;
  private static Color                      s_shortestColor   = Color.red;
  private static Color                      s_labelColor      = new Color(32, 128, 32);

  private static int                        s_defaultSpessore = 3;
  private Bordo                             m_bordo;

  private PlotVertice                       m_vDa;
  private PlotVertice                       m_vA;

  @Getter
  @Setter private transient Color           color;
  @Getter
  @Setter private int                       spessore;
  @Getter
  @Setter private transient boolean         selected;

  private Polygon                           m_freccia;
  private EquazLineare                      m_eq;

  public PlotBordo(Bordo p_bo) {
    m_bordo = p_bo;
    m_eq = new EquazLineare(this);
    setColor(PlotBordo.s_defaulColor);
    setSpessore(s_defaultSpessore);
    initialize();
  }

  private void initialize() {
    m_freccia = new Polygon();
    m_freccia.addPoint(0, 5);
    m_freccia.addPoint( -5, -5);
    m_freccia.addPoint(5, -5);

    m_vDa = new PlotVertice(m_bordo.getVerticeDa());
    m_vA = new PlotVertice(m_bordo.getVerticeA());
  }

  public void paintComponent(Graphics2D p_g2, TrasponiFinestra p_trasp) {
    Graphics2D g2 = (Graphics2D) p_g2.create();
    stampaBordo(g2, p_trasp);
    stampaPeso(g2, p_trasp);
    stampaFreccia(g2, p_trasp);
    g2.dispose();
  }

  private void stampaBordo(Graphics2D p_g2, TrasponiFinestra p_trasp) {
    Graphics2D g2 = p_g2;
    Stroke stk = new BasicStroke(getSpessore());
    if (isSelected())
      g2.setColor(Color.red);
    else if (m_bordo.getShortNo() > 0) {
      g2.setColor(PlotBordo.s_shortestColor);
      stk = new BasicStroke(getSpessore() * 2);
    } else
      g2.setColor(getColor());
    g2.setStroke(stk);
    Punto p1 = p_trasp.convertiW(m_vDa.getPunto());
    Punto p2 = p_trasp.convertiW(m_vA.getPunto());
    var li = new Line2D.Double(p1.x, p1.y, //
        p2.x, p2.y);
    g2.draw(li);
  }

  private void stampaPeso(Graphics2D g2, TrasponiFinestra p_trasp) {
    int nFsize = (int) (1. * p_trasp.getZoom());
    String sz = String.valueOf(m_bordo.getPeso());
    Punto vDa0 = m_bordo.getVerticeDa().getPunto();
    Punto vA0 = m_bordo.getVerticeA().getPunto();
    Punto vDa = p_trasp.convertiW(vDa0);
    Punto vA = p_trasp.convertiW(vA0);
    var metax = Math.abs(vDa.x - vA.x) / 2;
    var metay = Math.abs(vDa.y - vA.y) / 2;
    var px = (vDa.x < vA.x ? vDa.x : vA.x) + metax;
    var py = (vDa.y < vA.y ? vDa.y : vA.y) + metay;
    g2.setColor(PlotBordo.s_labelColor);
    Font font = new Font("SanSerif", Font.BOLD, nFsize);
    FontRenderContext frc = g2.getFontRenderContext();
    GlyphVector gv = font.createGlyphVector(frc, sz);

    g2.drawGlyphVector(gv, px - (nFsize / 2) * (sz.length() - 1), py + nFsize / 2);
  }

  private void stampaFreccia(Graphics2D p_g2, TrasponiFinestra p_trasp) {
    boolean bCreaGraph = true;
    Graphics2D g2 = p_g2;
    if (bCreaGraph)
      g2 = (Graphics2D) p_g2.create();
    AffineTransform savTras = g2.getTransform();

    g2.setColor(PlotBordo.s_labelColor);

    Punto pDa = p_trasp.convertiW(m_vDa.getPunto());
    Punto pA = p_trasp.convertiW(m_vA.getPunto());

    // AffineTransform trasf = new AffineTransform();
    AffineTransform trasf = null;
    trasf = (AffineTransform) savTras.clone();
    // trasf.setToIdentity();

    double angolo = Math.atan2(pDa.y - pA.y, pDa.x - pA.x) + Math.PI;
    double raggio = m_vDa.getRaggio() * p_trasp.getZoom();
    if (m_vA.isStart() || m_vA.isEnd())
      raggio = m_vA.getRaggio() * 2;
    double fTraslX = pA.x - Math.cos(angolo) * raggio;
    double fTraslY = pA.y - Math.sin(angolo) * raggio;

    trasf.translate(fTraslX, fTraslY);
    trasf.rotate(angolo - Math.PI / 2.0);

    g2.setTransform(trasf);
    g2.fill(m_freccia);

    g2.setTransform(savTras);

    if (bCreaGraph)
      g2.dispose();
  }

  public boolean checkBersaglio(Punto p_pu) {
    boolean bRet = false;
    if (m_eq == null)
      return bRet;

    bRet = m_eq.inLine(p_pu);
    return bRet;
  }

  public Bordo getBordo() {
    return m_bordo;
  }

  @Override
  public String toString() {
    String sz = "*NULL*";
    if (m_bordo == null)
      return sz;
    sz = m_bordo.toString();
    return sz;
  }
}
