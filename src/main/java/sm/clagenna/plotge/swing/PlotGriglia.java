package sm.clagenna.plotge.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Line2D;

import sm.clagenna.plotge.dati.Punto;
import sm.clagenna.plotge.dati.TrasponiFinestra;

public class PlotGriglia {
  private TrasponiFinestra m_trasp;

  public PlotGriglia() {
    // m_pan = p_pan;
  }

  public void disegnaGriglia(Graphics2D p_g2, TrasponiFinestra p_tr) {
    Graphics2D g2 = (Graphics2D) p_g2.create();
    m_trasp = p_tr;
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

  private void disegnaRetta(Graphics2D p_g2, Point p_p1, Point p_p2, int spess, Color p_col) {
    p_g2.setColor(p_col);
    Stroke stk = new BasicStroke(spess);
    p_g2.setStroke(stk);
    Line2D.Double li = new Line2D.Double(p_p1.getX(), p_p1.getY(), p_p2.getX(), p_p2.getY());
    p_g2.draw(li);
  }

}
