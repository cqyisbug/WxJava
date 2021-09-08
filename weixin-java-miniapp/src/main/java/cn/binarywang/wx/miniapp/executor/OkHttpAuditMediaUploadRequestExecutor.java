package cn.binarywang.wx.miniapp.executor;

import cn.binarywang.wx.miniapp.bean.WxMaAuditMediaUploadResult;
import me.chanjar.weixin.common.enums.WxType;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.http.RequestHttp;
import me.chanjar.weixin.common.util.http.okhttp.OkHttpProxyInfo;
import okhttp3.*;

import java.io.File;
import java.io.IOException;

/**
 * @author yangyh22
 * @since 2020/11/14
 */
public class OkHttpAuditMediaUploadRequestExecutor extends AuditMediaUploadRequestExecutor<OkHttpClient, OkHttpProxyInfo> {

  public OkHttpAuditMediaUploadRequestExecutor(RequestHttp requestHttp) {
    super(requestHttp);
  }

  @Override
  public WxMaAuditMediaUploadResult execute(String uri, File file, WxType wxType) throws WxErrorException, IOException {

    RequestBody body = new MultipartBody.Builder()
      .setType(MediaType.parse("multipart/form-data"))
      .addFormDataPart("media",
        file.getName(),
        RequestBody.create(MediaType.parse("application/octet-stream"), file))
      .build();
    Request request = new Request.Builder().url(uri).post(body).build();

    Response response = requestHttp.getRequestHttpClient().newCall(request).execute();
    String responseContent = response.body().string();
    WxError error = WxError.fromJson(responseContent, wxType);
    if (error.getErrorCode() != 0) {
      throw new WxErrorException(error);
    }
    return WxMaAuditMediaUploadResult.fromJson(responseContent);
  }

}
