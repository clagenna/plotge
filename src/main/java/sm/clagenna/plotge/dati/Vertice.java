package sm.clagenna.plotge.dati;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class Vertice implements Comparable<Vertice> {

  private static int                                    s_nObj = 0;

  @Getter @Setter private String                        id;

  private Punto                                         punto;

  @Getter @Setter private boolean                       start;

  @Getter @Setter private boolean                       end;

  @Setter private transient boolean                     cieco;

  @Getter @Setter private transient Map<Vertice, Bordo> bordi;

  public Vertice() {
    setId(String.valueOf(++s_nObj));
  }

  public Vertice(String p_id) {
    ++s_nObj;
    setId(p_id);
  }

  @Deprecated
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

  /**
   * Cerca il bordo che ha come destinazione p_veA Imposta il bordo come
   * "analizzato" e lo marchia
   *
   * @param p_ve
   *          il vertice di destinazione
   * @return
   */
  public int marchiaBordo(Vertice p_veA) {
    Bordo bo = bordi.get(p_veA);
    bo.setShortNo(1);
    return bo.getPeso();
  }

  @Override
  public int compareTo(Vertice p_o) {
    if ((p_o == null) || (id == null) || (p_o.id == null))
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

  @Override
  public boolean equals(Object p_obj) {
    if (p_obj == null || ! (p_obj instanceof Vertice))
      return false;
    Vertice altro = (Vertice) p_obj;
    if (getId() == null || altro.getId() == null)
      return false;
    return getId().equals(altro.getId());
  }
}
