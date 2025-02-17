package me.chanjar.weixin.cp.api.impl;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.json.GsonParser;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.WxCpUserService;
import me.chanjar.weixin.cp.bean.WxCpInviteResult;
import me.chanjar.weixin.cp.bean.WxCpUser;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactInfo;
import me.chanjar.weixin.cp.util.json.WxCpGsonBuilder;

import java.util.List;
import java.util.Map;

import static me.chanjar.weixin.cp.constant.WxCpApiPathConsts.User.*;

/**
 * <pre>
 *  Created by BinaryWang on 2017/6/24.
 * </pre>
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@RequiredArgsConstructor
public class WxCpUserServiceImpl implements WxCpUserService {
  private final WxCpService mainService;

  @Override
  public void authenticate(String userId) throws WxErrorException {
    this.mainService.get(this.mainService.getWxCpConfigStorage().getApiUrl(USER_AUTHENTICATE + userId), null);
  }

  @Override
  public void create(WxCpUser user) throws WxErrorException {
    String url = this.mainService.getWxCpConfigStorage().getApiUrl(USER_CREATE);
    this.mainService.post(url, user.toJson());
  }

  @Override
  public void update(WxCpUser user) throws WxErrorException {
    String url = this.mainService.getWxCpConfigStorage().getApiUrl(USER_UPDATE);
    this.mainService.post(url, user.toJson());
  }

  @Override
  public void delete(String... userIds) throws WxErrorException {
    if (userIds.length == 1) {
      String url = this.mainService.getWxCpConfigStorage().getApiUrl(USER_DELETE + userIds[0]);
      this.mainService.get(url, null);
      return;
    }

    JsonObject jsonObject = new JsonObject();
    JsonArray jsonArray = new JsonArray();
    for (String userId : userIds) {
      jsonArray.add(new JsonPrimitive(userId));
    }

    jsonObject.add("useridlist", jsonArray);
    this.mainService.post(this.mainService.getWxCpConfigStorage().getApiUrl(USER_BATCH_DELETE), jsonObject.toString());
  }

  @Override
  public WxCpUser getById(String userid) throws WxErrorException {
    String url = this.mainService.getWxCpConfigStorage().getApiUrl(USER_GET + userid);
    String responseContent = this.mainService.get(url, null);
    return WxCpUser.fromJson(responseContent);
  }

  @Override
  public List<WxCpUser> listByDepartment(Long departId, Boolean fetchChild, Integer status) throws WxErrorException {
    String params = "";
    if (fetchChild != null) {
      params += "&fetch_child=" + (fetchChild ? "1" : "0");
    }
    if (status != null) {
      params += "&status=" + status;
    } else {
      params += "&status=0";
    }

    String url = this.mainService.getWxCpConfigStorage().getApiUrl(USER_LIST + departId);
    String responseContent = this.mainService.get(url, params);
    JsonObject jsonObject = GsonParser.parse(responseContent);
    return WxCpGsonBuilder.create()
      .fromJson(jsonObject.get("userlist"),
        new TypeToken<List<WxCpUser>>() {
        }.getType()
      );
  }

  @Override
  public List<WxCpUser> listSimpleByDepartment(Long departId, Boolean fetchChild, Integer status)
    throws WxErrorException {
    String params = "";
    if (fetchChild != null) {
      params += "&fetch_child=" + (fetchChild ? "1" : "0");
    }
    if (status != null) {
      params += "&status=" + status;
    } else {
      params += "&status=0";
    }

    String url = this.mainService.getWxCpConfigStorage().getApiUrl(USER_SIMPLE_LIST + departId);
    String responseContent = this.mainService.get(url, params);
    JsonObject tmpJson = GsonParser.parse(responseContent);
    return WxCpGsonBuilder.create()
      .fromJson(
        tmpJson.get("userlist"),
        new TypeToken<List<WxCpUser>>() {
        }.getType()
      );
  }

  @Override
  public WxCpInviteResult invite(List<String> userIds, List<String> partyIds, List<String> tagIds)
    throws WxErrorException {
    JsonObject jsonObject = new JsonObject();
    if (userIds != null) {
      JsonArray jsonArray = new JsonArray();
      for (String userId : userIds) {
        jsonArray.add(new JsonPrimitive(userId));
      }
      jsonObject.add("user", jsonArray);
    }

    if (partyIds != null) {
      JsonArray jsonArray = new JsonArray();
      for (String userId : partyIds) {
        jsonArray.add(new JsonPrimitive(userId));
      }
      jsonObject.add("party", jsonArray);
    }

    if (tagIds != null) {
      JsonArray jsonArray = new JsonArray();
      for (String tagId : tagIds) {
        jsonArray.add(new JsonPrimitive(tagId));
      }
      jsonObject.add("tag", jsonArray);
    }

    String url = this.mainService.getWxCpConfigStorage().getApiUrl(BATCH_INVITE);
    return WxCpInviteResult.fromJson(this.mainService.post(url, jsonObject.toString()));
  }

  @Override
  public Map<String, String> userId2Openid(String userId, Integer agentId) throws WxErrorException {
    String url = this.mainService.getWxCpConfigStorage().getApiUrl(USER_CONVERT_TO_OPENID);
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("userid", userId);
    if (agentId != null) {
      jsonObject.addProperty("agentid", agentId);
    }

    String responseContent = this.mainService.post(url, jsonObject.toString());
    JsonObject tmpJson = GsonParser.parse(responseContent);
    Map<String, String> result = Maps.newHashMap();
    if (tmpJson.get("openid") != null) {
      result.put("openid", tmpJson.get("openid").getAsString());
    }

    if (tmpJson.get("appid") != null) {
      result.put("appid", tmpJson.get("appid").getAsString());
    }

    return result;
  }

  @Override
  public String openid2UserId(String openid) throws WxErrorException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("openid", openid);
    String url = this.mainService.getWxCpConfigStorage().getApiUrl(USER_CONVERT_TO_USERID);
    String responseContent = this.mainService.post(url, jsonObject.toString());
    JsonObject tmpJson = GsonParser.parse(responseContent);
    return tmpJson.get("userid").getAsString();
  }

  @Override
  public String getUserId(String mobile) throws WxErrorException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("mobile", mobile);
    String url = this.mainService.getWxCpConfigStorage().getApiUrl(GET_USER_ID);
    String responseContent = this.mainService.post(url, jsonObject.toString());
    JsonObject tmpJson = GsonParser.parse(responseContent);
    return tmpJson.get("userid").getAsString();
  }

  @Override
  public WxCpExternalContactInfo getExternalContact(String externalUserId) throws WxErrorException {
    String url = this.mainService.getWxCpConfigStorage().getApiUrl(GET_EXTERNAL_CONTACT + externalUserId);
    String responseContent = this.mainService.get(url, null);
    return WxCpExternalContactInfo.fromJson(responseContent);
  }

  @Override
  public String getJoinQrCode(int sizeType) throws WxErrorException {
    String url = this.mainService.getWxCpConfigStorage().getApiUrl(GET_JOIN_QR_CODE + sizeType);
    String responseContent = this.mainService.get(url, null);
    JsonObject tmpJson = GsonParser.parse(responseContent);
    return tmpJson.get("join_qrcode").getAsString();
  }
}
