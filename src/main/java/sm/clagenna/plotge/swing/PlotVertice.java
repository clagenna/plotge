package sm.clagenna.plotge.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import lombok.Getter;
import lombok.Setter;
import sm.clagenna.plotge.dati.Punto;
import sm.clagenna.plotge.dati.TrasponiFinestra;
import sm.clagenna.plotge.dati.Vertice;

/**
 * Classe preposta a disegnare il vertice su di una Frame con il metoto
 * {@link #paintComponent(Graphics2D, TrasponiFinestra)}
 *
 * @author claudio
 *
 */
public class PlotVertice {

  private static Color                      s_Vert          = new Color(0, 0, 0);
  private static Color                      s_Vert_Selected = new Color(92, 128, 92);
  private static Color                      s_Vert_Start    = new Color(0, 128, 0);
  private static Color                      s_Vert_End      = Color.magenta;

  private static int                        s_raggioDefault = 1;

  @Getter
  @Setter private int                       raggio;

  private Vertice                           m_vert;
  private Shape                             m_cerchio;
  private transient Color                   m_colore;
  @Getter
  @Setter private transient boolean         selected;

  public PlotVertice() {
    m_colore = s_Vert;
    setRaggio(s_raggioDefault);
  }

  public PlotVertice(Vertice p_v) {
    m_colore = s_Vert;
    m_vert = p_v;
    setRaggio(s_raggioDefault);
  }

  public void paintComponent(Graphics2D p_g2, TrasponiFinestra p_trasp) {
    double zoo = p_trasp.getZoom();
    int ragW = (int) (raggio * zoo);
    Graphics2D g2 = (Graphics2D) p_g2.create();
    Punto pVert = p_trasp.convertiW(m_vert.getPunto());
    int px = pVert.getWx();
    int py = pVert.getWy();
    Color bkg = g2.getBackground();
    if (m_vert.isStart() || m_vert.isEnd()) {
      int ragWSE = ragW + 5;
      Shape lcerchio = new Ellipse2D.Double(px - ragWSE, py - ragWSE, ragWSE * 2.0, ragWSE * 2.0);
      g2.setColor(bkg);
      g2.fill(lcerchio);
      if (m_vert.isStart())
        g2.setColor(s_Vert_Start);
      else
        g2.setColor(s_Vert_End);
      g2.draw(lcerchio);
    }
    // il piu stretto
    var l_rag = (int) (getRaggio() * zoo);
    m_cerchio = new Ellipse2D.Double(px - l_rag, py - l_rag, l_rag * 2.0, l_rag * 2.0);
    if (m_vert.isCieco())
      bkg = Color.gray;
    g2.setColor(bkg);
    g2.fill(m_cerchio);
    if (isSelected()) {
      g2.setColor(s_Vert_Selected);
      g2.setStroke(new BasicStroke(3));
    } else
      g2.setColor(m_colore);
    g2.draw(m_cerchio);
    stampaNome(g2, p_trasp);
    g2.dispose();
  }

  private void stampaNome(Graphics2D g2, TrasponiFinestra p_trasp) {
    double nFsize = p_trasp.getZoom();
    String sz = m_vert.getId();
    Font font = new Font("SanSerif", Font.PLAIN, (int) (nFsize));
    FontRenderContext frc = g2.getFontRenderContext();
    GlyphVector gv = font.createGlyphVector(frc, sz);
    Rectangle2D rec = gv.getVisualBounds();
    Punto pVert = p_trasp.convertiW(m_vert.getPunto());
    int px = pVert.getWx();
    int py = pVert.getWy();
    g2.setColor(s_Vert_End);
    // float px = (pu.x - (nFsize / +5F) * (sz.length() - 1));
    // float pfx = (float) (px - (nFsize * 0.6F));
    float pfx = (float) (px - (rec.getWidth() / 2));
    // float pfy = (float) (py + nFsize / 2.5F);
    float pfy = (float) (py + (rec.getHeight() / 2));
    // System.out.printf("PlotVertice.stampaNome(\"%s\", %.2f, %.2f) zo=%.2f\n", sz, px, py, nFsize);
    g2.drawGlyphVector(gv, pfx, pfy);
  }

  public String getId() {
    String ret = "?";
    if (m_vert != null)
      ret = m_vert.getId();
    return ret;
  }

  public void setPunto(Punto p_pu) {
    if (m_vert == null)
      return;
    m_vert.setPunto(p_pu);
  }

  public Punto getPunto() {
    if (m_vert == null)
      return null;
    return m_vert.getPunto();
  }

  public boolean isStart() {
    if (m_vert == null)
      return false;
    return m_vert.isStart();
  }

  public boolean isEnd() {
    if (m_vert == null)
      return false;
    return m_vert.isEnd();
  }

  public boolean checkBersaglio(Punto p_pu) {
    Punto lPu = m_vert.getPunto();
    double dx = Math.abs(p_pu.getPx() - lPu.getPx());
    if (dx > raggio)
      return false;
    double dy = Math.abs(p_pu.getPy() - lPu.getPy());
    return dy <= raggio;
  }

  @Override
  public String toString() {
    Punto pu = m_vert.getPunto();
    String sz = String.format("Vert.%s,Pu={%s}", //
        m_vert.getId(), //
        pu.toString());
    sz += "\n\t" + m_vert.toString();
    return sz;
  }

}
