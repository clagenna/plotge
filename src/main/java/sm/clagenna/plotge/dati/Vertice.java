package sm.clagenna.plotge.dati;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class Vertice implements Comparable<Vertice> {

  private static int                                    s_nObj = 0;

  @Getter
  @Setter private String                                id;

  private Punto                                         punto;

  @Getter
  @Setter private boolean                               start;

  @Getter
  @Setter private boolean                               end;

  @Setter private transient boolean                     cieco;

  @Getter
  @Setter private transient Map<Vertice, Bordo>         bordi;

  public Vertice() {
    setId(String.valueOf(++s_nObj));
  }

  public Vertice(String p_id) {
    ++s_nObj;
    setId(p_id);
  }

  public void setPunto(Point p) {
    punto = new Punto(p);

  }

  public void setPunto(Punto p) {
    punto = p;
  }

  public Punto getPunto() {
    return punto;
  }

  public boolean isCieco() {
    if (isEnd())
      return false;
    return cieco;
  }

  @Override
  public int compareTo(Vertice p_o) {
    if (p_o == null)
      return -1;
    if (id == null)
      return -1;
    if (p_o.id == null)
      return -1;
    return id.compareTo(p_o.id);
  }

  public void addBordo(Bordo p_bo) {
    if (bordi == null)
      bordi = new HashMap<>();
    Vertice vA = p_bo.getVerticeA();
    if (vA.equals(this))
      throw new UnsupportedOperationException("Non puoi aggiungere un vertice a se stesso!" + this.id);
    if (bordi.keySet().contains(vA))
      // nothing to do !
      return;
    bordi.put(vA, p_bo);

  }

  @Override
  public String toString() {
    String sz = "Ver: " + id;
    if (bordi != null) {
      for (Bordo bo : bordi.values())
        sz += "\n\t" + bo.toString();
    }
    return sz;
  }

}
