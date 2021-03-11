package sm.clagenna.plotge.sys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.Setter;

public class AppProperties {

  private static final Logger        s_log               = LogManager.getLogger(AppProperties.class);
  private static final String        CSZ_PROP_FILE       = "plotge.properties";
  public static final String         CSZ_PROP_LASTDIR    = "last.dir";
  public static final String         CSZ_PROP_LASTFIL    = "last.fil";
  public static final String         CSZ_PROP_DIMFRAME_X = "frame.dimx";
  public static final String         CSZ_PROP_DIMFRAME_Y = "frame.dimy";
  public static final String         CSZ_PROP_POSFRAME_X = "frame.posx";
  public static final String         CSZ_PROP_POSFRAME_Y = "frame.posy";
  public static final String         CSZ_PROP_MENUFILE   = "menu.%d";

  private static AppProperties       s_inst;

  @Getter
  @Setter private Properties         properties;
  @Getter
  @Setter private File               propertyFile;

  public AppProperties() {
    if (s_inst != null)
      throw new UnsupportedOperationException("AppProperties gia' istanziato");
    s_inst = this;
  }

  public void openProperties() {
    propertyFile = new File(CSZ_PROP_FILE);

    s_log.info("Apro il file properties {}", propertyFile.getAbsolutePath());
    properties = new Properties();
    if ( !propertyFile.exists()) {
      s_log.error("Il file di properties {} non esiste", CSZ_PROP_FILE);
      return;
    }
    try (InputStream is = new FileInputStream(propertyFile)) {
      properties = new Properties();
      properties.load(is);
      setPropertyFile(propertyFile);
    } catch (IOException e) {
      e.printStackTrace();
      s_log.error("Errore apertura property file: {}", propertyFile.getAbsolutePath(), e);
    }

  }

  public void saveProperties() {
    try (OutputStream output = new FileOutputStream(propertyFile)) {
      properties.store(output, null);
      s_log.info("Salvo property file {}", propertyFile.getAbsolutePath());
    } catch (IOException e) {
      e.printStackTrace();
      s_log.error("Errore scrittura property file: {}", propertyFile.getAbsolutePath(), e);
    }

  }

  public static AppProperties getInst() {
    return s_inst;
  }

  public String getLastDir() {
    String szRet = null;
    if (properties != null)
      szRet = properties.getProperty(CSZ_PROP_LASTFIL);
    return szRet;
  }

  public void setLastDir(String p_lastDir) {
    if (properties != null)
      if (p_lastDir != null)
        properties.setProperty(CSZ_PROP_LASTDIR, p_lastDir);
  }

  public String getLastFile() {
    String szRet = null;
    if (properties != null)
      szRet = properties.getProperty(CSZ_PROP_LASTFIL);
    return szRet;
  }

  public void setLastFile(String p_lastFile) {
    if (properties != null)
      if (p_lastFile != null)
        properties.setProperty(CSZ_PROP_LASTFIL, p_lastFile);
  }

  public String getPropVal(String p_key) {
    String szRet = null;
    if (properties != null)
      szRet = properties.getProperty(p_key);
    return szRet;
  }

  public void setPropVal(String p_key, String p_val) {
    if (properties != null)
      if (p_val != null)
        properties.setProperty(p_key, p_val);
  }

  public void setPropVal(String p_key, int p_val) {
    setPropVal(p_key, String.valueOf(p_val));
  }

  public int getPropIntVal(String p_key) {
    Integer ii = Integer.valueOf(0);
    String sz = getPropVal(p_key);
    if (sz != null)
      ii = Integer.decode(sz);
    return ii.intValue();
  }

}
