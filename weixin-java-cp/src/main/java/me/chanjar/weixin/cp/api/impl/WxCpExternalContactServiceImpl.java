package me.chanjar.weixin.cp.api.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxCpErrorMsgEnum;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.error.WxRuntimeException;
import me.chanjar.weixin.common.service.WxService;
import me.chanjar.weixin.common.util.BeanUtils;
import me.chanjar.weixin.common.util.json.GsonParser;
import me.chanjar.weixin.cp.api.WxCpExternalContactService;
import me.chanjar.weixin.cp.bean.WxCpBaseResp;
import me.chanjar.weixin.cp.bean.external.*;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactBatchInfo;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static me.chanjar.weixin.cp.constant.WxCpApiPathConsts.ExternalContact.*;

/**
 * @author 曹祖鹏 & yuanqixun
 */
@RequiredArgsConstructor
public class WxCpExternalContactServiceImpl implements WxCpExternalContactService {
  private final WxService mainService;

  private final Function<String, String> urlGenerator;

  @Override
  public WxCpContactWayResult addContactWay(@NonNull WxCpContactWayInfo info) throws WxErrorException {

    if (info.getContactWay().getUsers() != null && info.getContactWay().getUsers().size() > 100) {
      throw new WxRuntimeException("「联系我」使用人数默认限制不超过100人(包括部门展开后的人数)");
    }

    final String url = urlGenerator.apply(ADD_CONTACT_WAY);
    String responseContent = this.mainService.post(url, info.getContactWay().toJson());

    return WxCpContactWayResult.fromJson(responseContent);
  }

  @Override
  public WxCpContactWayInfo getContactWay(@NonNull String configId) throws WxErrorException {
    JsonObject json = new JsonObject();
    json.addProperty("config_id", configId);

    final String url = urlGenerator.apply(GET_CONTACT_WAY);
    String responseContent = this.mainService.post(url, json.toString());
    return WxCpContactWayInfo.fromJson(responseContent);
  }

  @Override
  public WxCpBaseResp updateContactWay(@NonNull WxCpContactWayInfo info) throws WxErrorException {
    if (StringUtils.isBlank(info.getContactWay().getConfigId())) {
      throw new WxRuntimeException("更新「联系我」方式需要指定configId");
    }
    if (info.getContactWay().getUsers() != null && info.getContactWay().getUsers().size() > 100) {
      throw new WxRuntimeException("「联系我」使用人数默认限制不超过100人(包括部门展开后的人数)");
    }

    final String url = urlGenerator.apply(UPDATE_CONTACT_WAY);
    String responseContent = this.mainService.post(url, info.getContactWay().toJson());

    return WxCpBaseResp.fromJson(responseContent);
  }

  @Override
  public WxCpBaseResp deleteContactWay(@NonNull String configId) throws WxErrorException {
    JsonObject json = new JsonObject();
    json.addProperty("config_id", configId);

    final String url = urlGenerator.apply(DEL_CONTACT_WAY);
    String responseContent = this.mainService.post(url, json.toString());

    return WxCpBaseResp.fromJson(responseContent);
  }

  @Override
  public WxCpBaseResp closeTempChat(@NonNull String userId, @NonNull String externalUserId) throws WxErrorException {

    JsonObject json = new JsonObject();
    json.addProperty("userid", userId);
    json.addProperty("external_userid", externalUserId);


    final String url = urlGenerator.apply(CLOSE_TEMP_CHAT);
    String responseContent = this.mainService.post(url, json.toString());

    return WxCpBaseResp.fromJson(responseContent);
  }

  @Override
  public WxCpExternalContactInfo getExternalContact(String externalUserId) throws WxErrorException {
    final String url = urlGenerator.apply(GET_EXTERNAL_CONTACT + externalUserId);
    String responseContent = this.mainService.get(url, null);
    return WxCpExternalContactInfo.fromJson(responseContent);
  }

  @Override
  public WxCpExternalContactInfo getContactDetail(String externalUserId) throws WxErrorException {
    final String url = urlGenerator.apply(GET_CONTACT_DETAIL + externalUserId);
    String responseContent = this.mainService.get(url, null);
    return WxCpExternalContactInfo.fromJson(responseContent);
  }

  @Override
  public String convertToOpenid(@NotNull String externalUserId) throws WxErrorException {
    JsonObject json = new JsonObject();
    json.addProperty("external_userid", externalUserId);
    final String url = urlGenerator.apply(CONVERT_TO_OPENID);
    String responseContent = this.mainService.post(url, json.toString());
    JsonObject tmpJson = GsonParser.parse(responseContent);
    return tmpJson.get("openid").getAsString();
  }

  @Override
  public String unionidToExternalUserid(@NotNull String unionid) throws WxErrorException {
    JsonObject json = new JsonObject();
    json.addProperty("unionid", unionid);
    final String url = urlGenerator.apply(UNIONID_TO_EXTERNAL_USERID);
    String responseContent = this.mainService.post(url, json.toString());
    JsonObject tmpJson = GsonParser.parse(responseContent);
    return tmpJson.get("external_userid").getAsString();
  }

  @Override
  public WxCpExternalContactBatchInfo getContactDetailBatch(String[] userIdList,
                                                            String cursor,
                                                            Integer limit)
    throws WxErrorException {
    final String url = urlGenerator.apply(GET_CONTACT_DETAIL_BATCH);
    JsonObject json = new JsonObject();
    json.add("userid_list", new Gson().toJsonTree(userIdList).getAsJsonArray());
    if (StringUtils.isNotBlank(cursor)) {
      json.addProperty("cursor", cursor);
    }
    if (limit != null) {
      json.addProperty("limit", limit);
    }
    String responseContent = this.mainService.post(url, json.toString());
    return WxCpExternalContactBatchInfo.fromJson(responseContent);
  }

  @Override
  public void updateRemark(WxCpUpdateRemarkRequest request) throws WxErrorException {
    final String url = urlGenerator.apply(UPDATE_REMARK);
    this.mainService.post(url, request.toJson());
  }

  @Override
  public List<String> listExternalContacts(String userId) throws WxErrorException {
    final String url = urlGenerator.apply(LIST_EXTERNAL_CONTACT + userId);
    try {
      String responseContent = this.mainService.get(url, null);
      return WxCpUserExternalContactList.fromJson(responseContent).getExternalUserId();
    } catch (WxErrorException e) {
      // not external contact,无客户则返回空列表
      if (e.getError().getErrorCode() == WxCpErrorMsgEnum.CODE_84061.getCode()) {
        return Collections.emptyList();
      }
      throw e;
    }
  }

  @Override
  public List<String> listFollowers() throws WxErrorException {
    final String url = urlGenerator.apply(GET_FOLLOW_USER_LIST);
    String responseContent = this.mainService.get(url, null);
    return WxCpUserWithExternalPermission.fromJson(responseContent).getFollowers();
  }

  @Override
  public WxCpUserExternalUnassignList listUnassignedList(Integer pageIndex, Integer pageSize) throws WxErrorException {
    JsonObject json = new JsonObject();
    json.addProperty("page_id", pageIndex == null ? 0 : pageIndex);
    json.addProperty("page_size", pageSize == null ? 100 : pageSize);
    final String url = urlGenerator.apply(LIST_UNASSIGNED_CONTACT);
    final String result = this.mainService.post(url, json.toString());
    return WxCpUserExternalUnassignList.fromJson(result);
  }

  @Override
  public WxCpBaseResp transferExternalContact(String externalUserid, String handOverUserid, String takeOverUserid) throws WxErrorException {
    JsonObject json = new JsonObject();
    json.addProperty("external_userid", externalUserid);
    json.addProperty("handover_userid", handOverUserid);
    json.addProperty("takeover_userid", takeOverUserid);
    final String url = urlGenerator.apply(TRANSFER_UNASSIGNED_CONTACT);
    final String result = this.mainService.post(url, json.toString());
    return WxCpBaseResp.fromJson(result);
  }

  @Override
  public WxCpUserTransferCustomerResp transferCustomer(WxCpUserTransferCustomerReq req) throws WxErrorException {
    BeanUtils.checkRequiredFields(req);
    final String url = urlGenerator.apply(TRANSFER_CUSTOMER);
    final String result = this.mainService.post(url, req.toJson());
    return WxCpUserTransferCustomerResp.fromJson(result);
  }

  @Override
  public WxCpUserTransferResultResp transferResult(@NotNull String handOverUserid, @NotNull String takeOverUserid, String cursor) throws WxErrorException {
    JsonObject json = new JsonObject();
    json.addProperty("cursor", cursor);
    json.addProperty("handover_userid", handOverUserid);
    json.addProperty("takeover_userid", takeOverUserid);
    final String url = urlGenerator.apply(TRANSFER_RESULT);
    final String result = this.mainService.post(url, json.toString());
    return WxCpUserTransferResultResp.fromJson(result);
  }

  @Override
  public WxCpUserTransferCustomerResp resignedTransferCustomer(WxCpUserTransferCustomerReq req) throws WxErrorException {
    BeanUtils.checkRequiredFields(req);
    final String url = urlGenerator.apply(RESIGNED_TRANSFER_CUSTOMER);
    final String result = this.mainService.post(url, req.toJson());
    return WxCpUserTransferCustomerResp.fromJson(result);
  }

  @Override
  public WxCpUserTransferResultResp resignedTransferResult(@NotNull String handOverUserid, @NotNull String takeOverUserid, String cursor) throws WxErrorException {
    JsonObject json = new JsonObject();
    json.addProperty("cursor", cursor);
    json.addProperty("handover_userid", handOverUserid);
    json.addProperty("takeover_userid", takeOverUserid);
    final String url = urlGenerator.apply(RESIGNED_TRANSFER_RESULT);
    final String result = this.mainService.post(url, json.toString());
    return WxCpUserTransferResultResp.fromJson(result);
  }

  @Override
  public WxCpUserExternalGroupChatList listGroupChat(Integer limit, String cursor, int status, String[] userIds) throws WxErrorException {
    JsonObject json = new JsonObject();
    json.addProperty("cursor", cursor == null ? "" : cursor);
    json.addProperty("limit", limit == null ? 100 : limit);
    json.addProperty("status_filter", status);
    if (ArrayUtils.isNotEmpty(userIds)) {
      JsonObject ownerFilter = new JsonObject();
      if (ArrayUtils.isNotEmpty(userIds)) {
        ownerFilter.add("userid_list", new Gson().toJsonTree(userIds).getAsJsonArray());
      }
      json.add("owner_filter", ownerFilter);
    }
    final String url = urlGenerator.apply(GROUP_CHAT_LIST);
    final String result = this.mainService.post(url, json.toString());
    return WxCpUserExternalGroupChatList.fromJson(result);
  }

  @Override
  public WxCpUserExternalGroupChatInfo getGroupChat(String chatId, Integer needName) throws WxErrorException {
    JsonObject json = new JsonObject();
    json.addProperty("chat_id", chatId);
    json.addProperty("need_name", needName);
    final String url = urlGenerator.apply(GROUP_CHAT_INFO);
    final String result = this.mainService.post(url, json.toString());
    return WxCpUserExternalGroupChatInfo.fromJson(result);
  }

  @Override
  public WxCpUserExternalGroupChatTransferResp transferGroupChat(String[] chatIds, String newOwner) throws WxErrorException {
    JsonObject json = new JsonObject();
    if (ArrayUtils.isNotEmpty(chatIds)) {
      json.add("chat_id_list", new Gson().toJsonTree(chatIds).getAsJsonArray());
    }
    json.addProperty("new_owner", newOwner);
    final String url = urlGenerator.apply(GROUP_CHAT_TRANSFER);
    final String result = this.mainService.post(url, json.toString());
    return WxCpUserExternalGroupChatTransferResp.fromJson(result);
  }

  @Override
  public WxCpUserExternalUserBehaviorStatistic getUserBehaviorStatistic(Date startTime, Date endTime, String[] userIds, String[] partyIds) throws WxErrorException {
    JsonObject json = new JsonObject();
    json.addProperty("start_time", startTime.getTime() / 1000);
    json.addProperty("end_time", endTime.getTime() / 1000);
    if (ArrayUtils.isNotEmpty(userIds) || ArrayUtils.isNotEmpty(partyIds)) {
      if (ArrayUtils.isNotEmpty(userIds)) {
        json.add("userid", new Gson().toJsonTree(userIds).getAsJsonArray());
      }
      if (ArrayUtils.isNotEmpty(partyIds)) {
        json.add("partyid", new Gson().toJsonTree(partyIds).getAsJsonArray());
      }
    }
    final String url = urlGenerator.apply(LIST_USER_BEHAVIOR_DATA);
    final String result = this.mainService.post(url, json.toString());
    return WxCpUserExternalUserBehaviorStatistic.fromJson(result);
  }

  @Override
  public WxCpUserExternalGroupChatStatistic getGroupChatStatistic(Date startTime, Integer orderBy, Integer orderAsc, Integer pageIndex, Integer pageSize, String[] userIds, String[] partyIds) throws WxErrorException {
    JsonObject json = new JsonObject();
    json.addProperty("day_begin_time", startTime.getTime() / 1000);
    json.addProperty("order_by", orderBy == null ? 1 : orderBy);
    json.addProperty("order_asc", orderAsc == null ? 0 : orderAsc);
    json.addProperty("offset", pageIndex == null ? 0 : pageIndex);
    json.addProperty("limit", pageSize == null ? 500 : pageSize);
    if (ArrayUtils.isNotEmpty(userIds) || ArrayUtils.isNotEmpty(partyIds)) {
      JsonObject ownerFilter = new JsonObject();
      if (ArrayUtils.isNotEmpty(userIds)) {
        ownerFilter.add("userid_list", new Gson().toJsonTree(userIds).getAsJsonArray());
      }
      if (ArrayUtils.isNotEmpty(partyIds)) {
        ownerFilter.add("partyid_list", new Gson().toJsonTree(partyIds).getAsJsonArray());
      }
      json.add("owner_filter", ownerFilter);
    }
    final String url = urlGenerator.apply(LIST_GROUP_CHAT_DATA);
    final String result = this.mainService.post(url, json.toString());
    return WxCpUserExternalGroupChatStatistic.fromJson(result);
  }

  @Override
  public WxCpMsgTemplateAddResult addMsgTemplate(WxCpUserExternalMsgTemplate wxCpUserExternalMsgTemplate) throws WxErrorException {
    final String url = urlGenerator.apply(ADD_MSG_TEMPLATE);
    final String result = this.mainService.post(url, wxCpUserExternalMsgTemplate.toJson());
    return WxCpMsgTemplateAddResult.fromJson(result);
  }

  @Override
  public WxCpUserExternalContactGroupMsgListV2Result groupMsgListV2(WxCpUserExternalContactGroupMsgListV2Request wxCpUserExternalContactGroupMsgListV2Request) throws WxErrorException {
    final String url = urlGenerator.apply(GROUP_MSG_LIST_V2);
    final String result = this.mainService.post(url, wxCpUserExternalContactGroupMsgListV2Request.toJson());
    return WxCpUserExternalContactGroupMsgListV2Result.fromJson(result);
  }

  @Override
  public WxCpUserExternalContactGroupMsgTaskResult groupMsgTask(WxCpUserExternalContactGroupMsgTaskRequest wxCpUserExternalContactGroupMsgTaskRequest) throws WxErrorException {
    final String url = urlGenerator.apply(GROUP_MSG_TASK);
    final String result = this.mainService.post(url, wxCpUserExternalContactGroupMsgTaskRequest.toJson());
    return WxCpUserExternalContactGroupMsgTaskResult.fromJson(result);
  }

  @Override
  public WxCpUserExternalContactGroupMsgSendResult groupMsgSendResult(WxCpUserExternalContactGroupMsgSendRequest wxCpUserExternalContactGroupMsgSendRequest) throws WxErrorException {
    final String url = urlGenerator.apply(GROUP_MSG_SEND_RESULT);
    final String result = this.mainService.post(url, wxCpUserExternalContactGroupMsgSendRequest.toJson());
    return WxCpUserExternalContactGroupMsgSendResult.fromJson(result);
  }

  @Override
  public void sendWelcomeMsg(WxCpWelcomeMsg msg) throws WxErrorException {
    final String url = urlGenerator.apply(SEND_WELCOME_MSG);
    this.mainService.post(url, msg.toJson());
  }

  @Override
  public WxCpUserExternalTagGroupList getCorpTagList(String[] tagId) throws WxErrorException {
    JsonObject json = new JsonObject();
    if (ArrayUtils.isNotEmpty(tagId)) {
      json.add("tag_id", new Gson().toJsonTree(tagId).getAsJsonArray());
    }
    final String url = urlGenerator.apply(GET_CORP_TAG_LIST);
    final String result = this.mainService.post(url, json.toString());
    return WxCpUserExternalTagGroupList.fromJson(result);
  }

  @Override
  public WxCpUserExternalTagGroupList getCorpTagList(String[] tagId, String[] groupId) throws WxErrorException {
    JsonObject json = new JsonObject();
    if (ArrayUtils.isNotEmpty(tagId)) {
      json.add("tag_id", new Gson().toJsonTree(tagId).getAsJsonArray());
    }
    if (ArrayUtils.isNotEmpty(groupId)) {
      json.add("group_id", new Gson().toJsonTree(groupId).getAsJsonArray());
    }
    final String url = urlGenerator.apply(GET_CORP_TAG_LIST);
    final String result = this.mainService.post(url, json.toString());
    return WxCpUserExternalTagGroupList.fromJson(result);
  }

  @Override
  public WxCpUserExternalTagGroupInfo addCorpTag(WxCpUserExternalTagGroupInfo tagGroup) throws WxErrorException {

    final String url = urlGenerator.apply(ADD_CORP_TAG);
    final String result = this.mainService.post(url, tagGroup.getTagGroup().toJson());
    return WxCpUserExternalTagGroupInfo.fromJson(result);
  }

  @Override
  public WxCpBaseResp editCorpTag(String id, String name, Integer order) throws WxErrorException {

    JsonObject json = new JsonObject();
    json.addProperty("id", id);
    json.addProperty("name", name);
    json.addProperty("order", order);
    final String url = urlGenerator.apply(EDIT_CORP_TAG);
    final String result = this.mainService.post(url, json.toString());
    return WxCpBaseResp.fromJson(result);
  }

  @Override
  public WxCpBaseResp delCorpTag(String[] tagId, String[] groupId) throws WxErrorException {
    JsonObject json = new JsonObject();
    if (ArrayUtils.isNotEmpty(tagId)) {
      json.add("tag_id", new Gson().toJsonTree(tagId).getAsJsonArray());
    }
    if (ArrayUtils.isNotEmpty(groupId)) {
      json.add("group_id", new Gson().toJsonTree(groupId).getAsJsonArray());
    }

    final String url = urlGenerator.apply(DEL_CORP_TAG);
    final String result = this.mainService.post(url, json.toString());
    return WxCpBaseResp.fromJson(result);
  }

  @Override
  public WxCpBaseResp markTag(String userid, String externalUserid, String[] addTag, String[] removeTag) throws WxErrorException {


    JsonObject json = new JsonObject();
    json.addProperty("userid", userid);
    json.addProperty("external_userid", externalUserid);

    if (ArrayUtils.isNotEmpty(addTag)) {
      json.add("add_tag", new Gson().toJsonTree(addTag).getAsJsonArray());
    }
    if (ArrayUtils.isNotEmpty(removeTag)) {
      json.add("remove_tag", new Gson().toJsonTree(removeTag).getAsJsonArray());
    }

    final String url = urlGenerator.apply(MARK_TAG);
    final String result = this.mainService.post(url, json.toString());
    return WxCpBaseResp.fromJson(result);
  }
}
