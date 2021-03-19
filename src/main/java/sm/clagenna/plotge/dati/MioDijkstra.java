package sm.clagenna.plotge.dati;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sm.clagenna.plotge.enumerati.EPropChange;
import sm.clagenna.plotge.sys.PropertyChangeBroadcaster;

public class MioDijkstra {

  private static final Logger   s_log = LogManager.getLogger(MioDijkstra.class);

  private String                nodoInizio;
  private String                nodoFine;
  private Set<Vertice>          m_nodiSistemati;
  private Set<Vertice>          m_nodiIgnoti;
  /** i nodi di destinazione con il peso minore */
  private Map<Vertice, Integer> m_cortissima;
  private Map<Vertice, Vertice> m_predecess;

  public MioDijkstra() {
    init();
  }

  public void analizzaTutti(Vertice p_vePrimo, Vertice p_veUltimo) {
    init();
    if (p_vePrimo == null || p_veUltimo == null) {
      String szMsg = "Mancano i vertici estremi";
      s_log.error(szMsg);
      PropertyChangeBroadcaster bcst = PropertyChangeBroadcaster.getInst();
      bcst.broadCast(this, EPropChange.notificaStatus, szMsg);
      return;
    }
    nodoInizio = p_vePrimo.getId();
    nodoFine = p_veUltimo.getId();
    m_nodiIgnoti.add(p_vePrimo);
    m_cortissima.put(p_vePrimo, 0);
    while (m_nodiIgnoti.size() > 0) {
      Vertice veVicino = getPiuVicino();
      nodoSistemato(veVicino);
      studiaVertIgnotoEDistanzaMinima(veVicino);
    }
    stampaNodi();
    String szFreccia = "";
    Vertice finale = p_veUltimo;
    String szLog = String.format("Piu corto da %s verso %s \t", p_vePrimo.getId(), finale.getId());
    Vertice corrente = finale;
    int totale = 0;
    while (finale != null) {
      String szId = finale.getId();
      System.out.printf("%s %s ", szFreccia, szId);
      finale = m_predecess.get(finale);
      if (finale != null) {
        totale += finale.marchiaBordo(corrente);
        corrente = finale;
      }
      szFreccia = "->";
    }
    szLog += String.format("  peso Totale = %s", totale);
    PropertyChangeBroadcaster.getInst().broadCast(this, EPropChange.notificaStatus, szLog);
  }

  /**
   * Torna il Vertice piu vicino di tutti (se esiste!) fra quelli ignoti
   *
   * @param p_nodiIgnoti
   * @return
   */
  private Vertice getPiuVicino() {
    Vertice veRet = null;
    int distMin = Integer.MAX_VALUE;

    for (Vertice veIgnoto : m_nodiIgnoti) {
      if (veRet == null) {
        veRet = veIgnoto;
        distMin = getMinoreDistanzaCalcolata(veIgnoto);
      } else {
        int dist = getMinoreDistanzaCalcolata(veIgnoto);
        if (dist < distMin) {
          veRet = veIgnoto;
          distMin = dist;
        }
      }
    }
    return veRet;
  }

  /** torna la distanza dal origine al nodo fornito ( <b>se esiste</b>) */
  private int getMinoreDistanzaCalcolata(Vertice p_ve) {
    int nRet = Integer.MAX_VALUE;
    if (m_cortissima.containsKey(p_ve))
      nRet = m_cortissima.get(p_ve);
    return nRet;
  }

  /**
   * Studia il Vertice (per ora ignoto/non calcolato) cerca la distanza minima
   * degli <b>adiacienti</b> a <code>p_veA</code>
   *
   * @param p_veA
   *          il nodo da studiare i suoi adiacenti
   */
  private void studiaVertIgnotoEDistanzaMinima(Vertice p_veA) {
    Map<Vertice, Bordo> mapBordi = p_veA.getBordi();
    if (mapBordi == null)
      return;
    int dist2A = getMinoreDistanzaCalcolata(p_veA);
    for (Bordo bo : mapBordi.values()) {
      Vertice p_VeB = bo.getVerticeA();
      int distO2B = getMinoreDistanzaCalcolata(p_VeB);
      int distA2B = bo.getPeso();
      int distO2Bnew = dist2A + distA2B;
      if (distO2Bnew < distO2B) {
        m_cortissima.put(p_VeB, distO2Bnew);
        m_predecess.put(p_VeB, p_veA);
        m_nodiIgnoti.add(p_VeB);
      }
    }
    stampaVieBrevi(p_veA);
  }

  private void stampaVieBrevi(Vertice p_ve) {
    String sz = "cortissima da " + p_ve.getId();
    System.out.println(sz);
    //    for (Vertice veDa : m_cortissima.keySet()) {
    //      sz = String.format("da %s -(%d)-> %s", veDa.getId(), m_cortissima.get(veDa), nodoInizio);
    //      System.out.println(sz);
    //    }

    List<Vertice> li = m_cortissima.keySet().stream().sorted().collect(Collectors.toList());
    for (Vertice veDa : li) {
      String predec = m_predecess.containsKey(veDa) ? m_predecess.get(veDa).getId() : "*no*";
      sz = String.format("da %s -(%d)[pp:%s]-> %s", veDa.getId(), m_cortissima.get(veDa), predec, nodoInizio);
      System.out.println(sz);
    }

    System.out.println("-----------------------");
  }

  private void nodoSistemato(Vertice p_ve) {
    m_nodiSistemati.add(p_ve);
    m_nodiIgnoti.remove(p_ve);
  }

  private void init() {
    m_nodiIgnoti = new HashSet<>();
    m_nodiSistemati = new HashSet<>();
    m_cortissima = new HashMap<>();
    m_predecess = new HashMap<>();
  }

  private void stampaNodi() {
    m_cortissima //
        .keySet() //
        .stream() //
        .sorted() //
        .forEach(s -> System.out.printf("%10s %d\n", s.getId(), m_cortissima.get(s)));

  }

  public String getNodoInizio() {
    return nodoInizio;
  }

  public void setNodoInizio(String p_nodoInizio) {
    nodoInizio = p_nodoInizio;
  }

  public String getNodoFine() {
    return nodoFine;
  }

  public void setNodoFine(String p_nodoFine) {
    nodoFine = p_nodoFine;
  }
}
