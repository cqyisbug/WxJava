package me.chanjar.weixin.common.util.xml;

import com.thoughtworks.xstream.XStream;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * @author caiqy
 */
public class XmlBeanUtil {

  public static String toXml(Object obj) {
    return toXml(obj, null);
  }

  public static String toXml(Object obj, Consumer<XStream> consumer) {
    return XStreamInitializer.getInstance(obj.getClass(), consumer).toXML(obj);
  }

  public static <T> T toBean(String xml, Class<T> clz) {
    return toBean(xml, clz, null);
  }

  @SuppressWarnings("unchecked")
  public static <T> T toBean(String xml, Class<T> clz, Consumer<XStream> consumer) {
    return (T) XStreamInitializer.getInstance(clz, consumer).fromXML(xml);
  }

  public static <T> T toBean(InputStream is, Class<T> clz) {
    return toBean(is, clz, null);
  }

  @SuppressWarnings("unchecked")
  public static <T> T toBean(InputStream is, Class<T> clz, Consumer<XStream> consumer) {
    return (T) XStreamInitializer.getInstance(clz, consumer).fromXML(is);
  }
}
