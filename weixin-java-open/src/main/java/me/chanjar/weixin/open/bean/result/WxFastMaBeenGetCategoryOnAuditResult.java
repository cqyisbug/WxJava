package me.chanjar.weixin.open.bean.result;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.chanjar.weixin.open.bean.ma.WxOpenMaCategory;

import java.util.List;

/**
 * @author caiqy
 * @date 2021/9/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WxFastMaBeenGetCategoryOnAuditResult extends WxOpenResult {

  /**
   * 类目信息列表
   */
  @SerializedName("category_list")
  private List<WxOpenMaCategory> categoryList;

}
