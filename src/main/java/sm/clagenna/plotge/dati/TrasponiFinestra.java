package sm.clagenna.plotge.dati;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JComponent;

import lombok.Getter;
import lombok.Setter;
import sm.clagenna.plotge.enumerati.EPropChange;
import sm.clagenna.plotge.swing.PlotVertice;
import sm.clagenna.plotge.sys.PropertyChangeBroadcaster;

public class TrasponiFinestra implements PropertyChangeListener {

  public static double           s_zoomDefault = 10F;

  @Getter
  @Setter private double         width;
  @Getter
  @Setter private double         height;
  @Getter private double         zoom;

  private double                 m_maxX;
  private double                 m_maxY;

  private transient ModelloDati  m_dati;

  public TrasponiFinestra(ModelloDati p_tr) {
    m_dati = p_tr;
    setZoom(s_zoomDefault);
    resetGeometry(p_tr);
    inizializza();
  }

  //  public TrasponiFinestra(JPanel p_pan) {
  //    setZoom(s_zoomDefault);
  //    resetGeometry(p_pan);
  //    inizializza();
  //  }

  public TrasponiFinestra(Rectangle p_r) {
    setZoom(s_zoomDefault);
    setRect(p_r);
    inizializza();
  }

  public TrasponiFinestra(Dimension p_size) {
    setZoom(s_zoomDefault);
    setRect(p_size);
    inizializza();
  }

  private void inizializza() {
    PropertyChangeBroadcaster.getInst().addPropertyChangeListener(this);
  }

  public void setRect(Dimension p_size) {
    setWidth(p_size.getWidth());
    setHeight(p_size.getHeight());
  }

  public void setRect(Rectangle p_r) {
    setWidth(p_r.getWidth());
    setHeight(p_r.getHeight());
  }

  /**
   * Converte dalle coordinate window a quelle cartesiane
   *
   * @deprecated meglio usare {@link #convertiX(Punto)}
   * @param p
   * @return
   */
  //  @Deprecated
  //  public Point convertiX(Point p) {
  //    double lx = p.getX() / zoom;
  //    double ly = (height - p.getY()) / zoom;
  //    Point ret = new Point();
  //    ret.setLocation(lx, ly);
  //    return ret;
  //  }

  /**
   * Converte dalle coordinate window a quelle cartesiane
   *
   * @param p
   * @return
   */
  public Punto convertiX(Punto p) {
    Punto ret = (Punto) p.clone();
    double lx = p.getWx() / zoom;
    double ly = (m_maxY - p.getWy()) / zoom;
    ret.setPx(lx);
    ret.setPy(ly);
    return ret;
  }

  /**
   * Converte dalle coordinate cartesiane a quelle window
   *
   * @deprecated meglio usare la {@link #convertiW(Punto)}
   * @param p
   * @return
   */
  //  @Deprecated
  //  public Point convertiW(Point p) {
  //    double lx = p.getX() * zoom;
  //    double ly = height - p.getY() * zoom;
  //    Point ret = new Point();
  //    ret.setLocation(lx, ly);
  //    return ret;
  //  }

  /**
   * Converte dalle coordinate cartesiane a quelle window
   *
   * @param p
   * @return
   */
  public Punto convertiW(Punto p) {
    Punto ret = (Punto) p.clone();
    double lx = p.getPx() * zoom;
    // double ly = height - p.getPy() * zoom;
    double ly = m_maxY - p.getPy() * zoom;
    ret.setWx((int) lx);
    ret.setWy((int) ly);
    return ret;
  }

  @Deprecated
  public boolean isGeometryChanged(Component p_pan) {
    Rectangle rect = p_pan.getBounds();
    int wi = (rect.width);
    int he = (rect.height);
    return wi != width || he != height;
  }

  //  @Deprecated
  //  public void resetGeometry(JPanel p_pan) {
  //    if (p_pan == null)
  //      return;
  //    Rectangle rect = p_pan.getBounds();
  //    width = (rect.width);
  //    height = (rect.height);
  //    inizializza();
  //  }
  
  public void resetGeometry(ModelloDati p_dati) {
    m_dati = p_dati;
    resetGeometry(p_dati.getPlotVertici());
  }

  public void resetGeometry(ModelloDati p_dati, JComponent p_co) {
    m_dati = p_dati;
    List<PlotVertice> li = p_dati.getPlotVertici();
    if (li == null || li.size() == 0) {
      if (p_co != null) {
        Dimension di = p_co.getSize();
        m_maxX = di.getWidth();
        m_maxY = di.getHeight();
      }
      return;
    }
    resetGeometry(p_dati.getPlotVertici());
  }

  public void resetGeometry(List<PlotVertice> p_li) {
    m_maxX = 40;
    m_maxY = 30;
    if (p_li == null)
      return;
    for (PlotVertice pve : p_li) {
      Punto p = pve.getPunto();
      double lx = (p.getPx() + pve.getRaggio()) * zoom;
      double ly = (p.getPy() + pve.getRaggio()) * zoom;
      m_maxX = m_maxX < lx ? lx : m_maxX;
      m_maxY = m_maxY < ly ? ly : m_maxY;
    }
    m_maxX += 40;
    m_maxY += 30;
  }

  public double getMaxX() {
    // return width / zoom;
    return m_maxX;
  }

  public double getMaxY() {
    // return height / zoom;
    return m_maxY;
  }

  public void setZoom(double pv) {
    double delta = pv - zoom;
    if ( (Math.abs(delta) > 25 || Math.abs(pv) > 25))
      System.out.println("TrasponiFinestra.setZoom():" + pv);
    zoom = pv;
  }

  public void incrZoom(int pv) {
    setZoom(zoom + pv / 70.);
    if (m_dati != null)
      resetGeometry(m_dati);
  }

  @Override
  public void propertyChange(PropertyChangeEvent p_evt) {
    Object obj = p_evt.getOldValue();
    if ( ! (obj instanceof EPropChange))
      return;
    EPropChange pch = (EPropChange) obj;
    switch (pch) {

      case panelRezized:
        Dimension dim = (Dimension) p_evt.getNewValue();
        System.out.println("TrasponiFinestra.propertyChange():" + dim.toString());
        setWidth(dim.getWidth());
        setHeight(dim.getHeight());
        break;

      default:
        break;
    }

  }

}
