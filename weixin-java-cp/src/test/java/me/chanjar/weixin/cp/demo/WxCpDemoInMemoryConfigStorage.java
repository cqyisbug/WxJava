package me.chanjar.weixin.cp.demo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.ToString;
import me.chanjar.weixin.common.util.xml.XmlBeanUtil;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;

import java.io.InputStream;

/**
 * @author Daniel Qian
 */
@XStreamAlias("xml")
@ToString
public class WxCpDemoInMemoryConfigStorage extends WxCpDefaultConfigImpl {
  public static WxCpDemoInMemoryConfigStorage fromXml(InputStream is) {
    return XmlBeanUtil.toBean(is, WxCpDemoInMemoryConfigStorage.class);
  }

}
