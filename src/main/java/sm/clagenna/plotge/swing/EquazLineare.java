package sm.clagenna.plotge.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Line2D;

import sm.clagenna.plotge.dati.Bordo;
import sm.clagenna.plotge.dati.Punto;
import sm.clagenna.plotge.dati.Vertice;

public class EquazLineare {

  @SuppressWarnings("unused") private PlotBordo m_bo;
  private Punto                                 m_p1;
  private Punto                                 m_p2;
  private boolean                               m_bVerticale;
  private boolean                               m_bOrizontale;
  private static double                         sTolleranza = 1.;

  private double                                m_a;
  private double                                m_b;

  public EquazLineare(Point p1, Point p2) {
    m_p1 = new Punto(p1);
    m_p2 = new Punto(p2);
    calcolaEquazLineare();
  }

  public EquazLineare(double px1, double py1, double px2, double py2) {
    m_p1 = new Punto(px1, py1);
    m_p2 = new Punto(px2, py2);
    calcolaEquazLineare();
  }

  public EquazLineare(PlotBordo p_bo) {
    setPlotBordo(p_bo);
    calcolaEquazLineare();
  }

  public static void setTolleranza(double p_v) {
    sTolleranza = p_v;
  }

  public void setPlotBordo(PlotBordo p_bo) {
    m_bo = p_bo;
    Bordo bo = p_bo.getBordo();
    Vertice vDa = bo.getVerticeDa();
    Vertice vA = bo.getVerticeA();
    m_p1 = vDa.getPunto();
    m_p2 = vA.getPunto();
  }

  private void calcolaEquazLineare() {
    m_a = 0;
    m_b = 0;
    m_bVerticale = m_p2.getPx() == m_p1.getPx();
    m_bOrizontale = m_p2.getPy() == m_p1.getPy();
    if (m_bVerticale || m_bOrizontale)
      return;
    m_a = (m_p2.getPy() - m_p1.getPy()) / (m_p2.getPx() - m_p1.getPx());
    m_b = m_p1.getPy() - m_p1.getPx() * m_a;
  }

  public void disegnaRetta(Graphics2D p_g) {
    Graphics2D lg = (Graphics2D) p_g.create();
    p_g.setColor(Color.green);
    Stroke stk = new BasicStroke(2);
    p_g.setStroke(stk);
    Point pw1 = new Point(0, (int) retta(0));
    Point pw2 = new Point(500, (int) retta(500));
    Line2D.Double li = new Line2D.Double(pw1.getX(), pw1.getY(), pw2.getX(), pw2.getY());
    p_g.draw(li);
    lg.dispose();
  }

  public double retta(double p_x) {
    if (m_bOrizontale || m_bVerticale)
      return m_p1.getPy();
    return p_x * m_a + m_b;
  }

  //  public boolean inLine(Point p_pu) {
  //    return inLine(p_pu.getX(), p_pu.getY(), false);
  //  }

  //  public boolean inLine(Punto p_pu ) {
  //    return inLine(p_pu.getPx(), p_pu.getPy() );
  //  }
  public boolean inLine(Punto p_pu) {
    return inLine(p_pu.getPx(), p_pu.getPy());
  }

  //  public boolean inLine(double lx, double ly) {
  //    return inLine(lx, ly, false);
  //  }

  /**
   * Prova se giace sulla riga p1 - p2
   *
   * @param p_p1
   *          punto inizio riga
   * @param p_p2
   *          punto finale riga
   * @param p_px
   *          punto da investigare
   * @param p_bound
   *          se devo verificare che X sia interno ai punti
   * @return
   */
  public boolean inLine(double lx, double ly) {
    // se P1 P2 e' verticale
    if (m_p1.getPx() == lx)
      return m_p2.getPx() == lx;
    // se P1 P2 e' orizontale
    if (m_p1.getPy() == ly)
      return m_p2.getPy() == ly;

    // se PX e' fra P1 e P2
    double lx1 = Math.min(m_p1.getPx(), m_p2.getPx()) - 0.1;
    double lx2 = Math.max(m_p1.getPx(), m_p2.getPx()) + 0.1;
    if (lx < lx1 || lx > lx2)
      return false;
    // se PY e' fra P1 e P2
    double ly1 = Math.min(m_p1.getPy(), m_p2.getPy()) - 0.1;
    double ly2 = Math.max(m_p1.getPy(), m_p2.getPy()) + 0.1;
    if (ly < ly1 || ly > ly2)
      return false;

    // se verticale, sono gia nel range col test sopra
    if (m_bVerticale)
      return true;

    // test sul gradiente
    double k1 = (ly - m_p1.getPy()) * (m_p2.getPx() - m_p1.getPx());
    double k2 = (m_p2.getPy() - m_p1.getPy()) * (lx - m_p1.getPx());
    double diff = Math.abs(k1 - k2);
    
    // if ( diff < 10. ) 
      System.out.printf("equaz diff %.2f\n", diff);

    return diff < sTolleranza;
  }

  public Point getPunto1() {
    return m_p1;
  }

  public Point getPunto2() {
    return m_p2;
  }

}
