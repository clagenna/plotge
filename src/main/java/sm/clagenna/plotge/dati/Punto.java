package sm.clagenna.plotge.dati;

import java.awt.Point;

public class Punto extends Point implements Cloneable {

  /** serialVersionUID long */
  private static final long serialVersionUID = -4469124977835459607L;
  /** posizione X sul asse cartesiano */
  private double            px;
  /** posizione Y sul asse cartesiano */
  private double            py;

  public Punto(Point p_point) {
    setPx(p_point.x);
    setPy(p_point.y);
    setWx(p_point.x);
    setWy(p_point.y);
  }

  public Punto(double p_lx, double p_ly) {
    setPx(p_lx);
    setPy(p_ly);
    setWx((int) p_lx);
    setWy((int) p_ly);
  }

  /** Posizione X sul asse cartesiano, in trasposizione */
  public double getPx() {
    return px;
  }

  public void setPx(double p_px) {
    px = p_px;
  }

  /** Posizione Y sul asse cartesiano, in trasposizione */
  public double getPy() {
    return py;
  }

  public void setPy(double p_py) {
    py = p_py;
  }

  /** Posizione X in Windows nel JFrame */
  public int getWx() {
    return x;
  }

  public void setWx(int p_wx) {
    x = p_wx;
  }

  /** Posizione Y in Windows nel JFrame */
  public int getWy() {
    return y;
  }

  public void setWy(int p_wy) {
    y = p_wy;
  }

  public Punto trasponi(TrasponiFinestra p_t) {
    Punto p = p_t.convertiX(this);
    setPx(p.px);
    setPy(p.py);
    return p;
  }

  @Override
  public Object clone() {
    Punto ret = new Punto(px, py);
    ret.setWx(x);
    ret.setWy(y);
    return ret;
  }

  @Override
  public String toString() {
    String sz = String.format("W(%d,%d)-C(%.2f:%.2f)", x, y, px, py);
    return sz;
  }

}
