package me.chanjar.weixin.cp.bean.external.msg;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 图片消息.
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 * @date 2020-08-16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image implements Serializable {
  private static final long serialVersionUID = -606286372867787121L;

  @SerializedName("media_id")
  private String mediaId;

  @SerializedName("pic_url")
  private String picUrl;
}
