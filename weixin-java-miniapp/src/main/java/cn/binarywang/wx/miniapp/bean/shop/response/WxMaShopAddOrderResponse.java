package cn.binarywang.wx.miniapp.bean.shop.response;

import cn.binarywang.wx.miniapp.bean.shop.WxMaShopAddOrderResult;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author leiin
 * @date 2021/3/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WxMaShopAddOrderResponse extends WxMaShopBaseResponse implements Serializable {
  private static final long serialVersionUID = -8923439859095040010L;

  @SerializedName("data")
  private WxMaShopAddOrderResult date;
}
