package sm.clagenna.plotge.dati;

import lombok.Getter;
import lombok.Setter;

public class Bordo implements Comparable<Bordo> {
  @Getter private transient Vertice verticeDa;

  @Getter private transient Vertice verticeA;

  @Getter
  @Setter private int               peso;

  /** se rappresenta la via breve e quale */
  private int                       shortNo;

  public Bordo() {
    //
  }

  public Bordo(Vertice p_ve1, Vertice p_ve2, int p_i) {
    verticeDa = p_ve1;
    verticeA = p_ve2;
    peso = p_i;
  }

  /**
   * @return the shortNo
   */
  public int getShortNo() {
    return shortNo;
  }

  /**
   * @param p_v
   *          the shortNo to set
   */
  public void setShortNo(int p_v) {
    shortNo = p_v;
  }

  @Override
  public int compareTo(Bordo p_o) {
    Vertice lvDa = null;
    Vertice lvA = null;
    if (p_o != null) {
      lvDa = p_o.getVerticeDa();
      lvA = p_o.getVerticeA();
    }
    // se non ho vertici -1
    if (verticeDa == null || lvDa == null)
      return -1;
    if (verticeA == null || lvA == null)
      return -1;

    int v = lvDa.compareTo(verticeDa);
    if (v != 0)
      return v;
    return v = lvA.compareTo(verticeA);
  }

  @Override
  public boolean equals(Object p_obj) {
    if ( ! (p_obj instanceof Bordo))
      return false;
    Bordo bo = (Bordo) p_obj;
    var idDa1 = getVerticeDa().getId();
    var idA1 = getVerticeA().getId();
    var idDa2 = bo.getVerticeDa().getId();
    var idA2 = bo.getVerticeA().getId();

    // boolean bRet = idDa1.equals(idDa2) || idDa1.equals(idA2);
    // bRet &= idA1.equals(idDa2) || idA1.equals(idA2);
    boolean bRet = idDa1.equals(idDa2);
    bRet &= idA1.equals(idA2);
    return bRet;
  }

}
