package me.chanjar.weixin.cp.tp.service;

import lombok.NonNull;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.WxCpBaseResp;
import me.chanjar.weixin.cp.bean.external.*;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactBatchInfo;
import me.chanjar.weixin.cp.bean.external.contact.WxCpExternalContactInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

/**
 * <pre>
 * 外部联系人管理接口，企业微信的外部联系人的接口和通讯录接口已经拆离
 *  Created by Joe Cao on 2019/6/14
 * </pre>
 *
 * @author <a href="https://github.com/JoeCao">JoeCao</a>
 */
public interface WxCpTpExternalContactService {

  /**
   * 配置客户联系「联系我」方式
   * <pre>
   * 企业可以在管理后台-客户联系中配置成员的「联系我」的二维码或者小程序按钮，客户通过扫描二维码或点击小程序上的按钮，即可获取成员联系方式，主动联系到成员。
   * 企业可通过此接口为具有客户联系功能的成员生成专属的「联系我」二维码或者「联系我」按钮。
   * 如果配置的是「联系我」按钮，需要开发者的小程序接入小程序插件。
   *
   * 注意:
   * 通过API添加的「联系我」不会在管理端进行展示，每个企业可通过API最多配置50万个「联系我」。
   * 用户需要妥善存储返回的config_id，config_id丢失可能导致用户无法编辑或删除「联系我」。
   * 临时会话模式不占用「联系我」数量，但每日最多添加10万个，并且仅支持单人。
   * 临时会话模式的二维码，添加好友完成后该二维码即刻失效。
   * </pre>
   *
   * @param corpId the corp id
   * @param info   客户联系「联系我」方式
   * @return wx cp contact way result
   * @throws WxErrorException the wx error exception
   */
  WxCpContactWayResult addContactWay(String corpId, @NonNull WxCpContactWayInfo info) throws WxErrorException;

  /**
   * 获取企业已配置的「联系我」方式
   *
   * <pre>
   * <b>批量</b>获取企业配置的「联系我」二维码和「联系我」小程序按钮。
   * </pre>
   *
   * @param corpId   the corp id
   * @param configId 联系方式的配置id,必填
   * @return contact way
   * @throws WxErrorException the wx error exception
   */
  WxCpContactWayInfo getContactWay(String corpId, @NonNull String configId) throws WxErrorException;

  /**
   * 更新企业已配置的「联系我」方式
   *
   * <pre>
   * 更新企业配置的「联系我」二维码和「联系我」小程序按钮中的信息，如使用人员和备注等。
   * </pre>
   *
   * @param corpId the corp id
   * @param info   客户联系「联系我」方式
   * @return wx cp base resp
   * @throws WxErrorException the wx error exception
   */
  WxCpBaseResp updateContactWay(String corpId, @NonNull WxCpContactWayInfo info) throws WxErrorException;

  /**
   * 删除企业已配置的「联系我」方式
   *
   * <pre>
   * 删除一个已配置的「联系我」二维码或者「联系我」小程序按钮。
   * </pre>
   *
   * @param corpId   the corp id
   * @param configId 企业联系方式的配置id,必填
   * @return wx cp base resp
   * @throws WxErrorException the wx error exception
   */
  WxCpBaseResp deleteContactWay(String corpId, @NonNull String configId) throws WxErrorException;

  /**
   * 结束临时会话
   *
   * <pre>
   * 将指定的企业成员和客户之前的临时会话断开，断开前会自动下发已配置的结束语。
   *
   * 注意：请保证传入的企业成员和客户之间有仍然有效的临时会话, 通过<b>其他方式的添加外部联系人无法通过此接口关闭会话</b>。
   * </pre>
   *
   * @param corpId         the corp id
   * @param userId         the user id
   * @param externalUserId the external user id
   * @return wx cp base resp
   * @throws WxErrorException the wx error exception
   */
  WxCpBaseResp closeTempChat(String corpId, @NonNull String userId, @NonNull String externalUserId) throws WxErrorException;


  /**
   * 获取外部联系人详情.
   * <pre>
   *   企业可通过此接口，根据外部联系人的userid，拉取外部联系人详情。权限说明：
   * 企业需要使用外部联系人管理secret所获取的accesstoken来调用
   * 第三方应用需拥有“企业客户”权限。
   * 第三方应用调用时，返回的跟进人follow_user仅包含应用可见范围之内的成员。
   * </pre>
   *
   * @param corpId         the corp id
   * @param externalUserId 外部联系人的userid
   * @return . external contact
   * @throws WxErrorException the wx error exception
   * @deprecated 建议使用 {@link #getContactDetail(String, String)}
   */
  @Deprecated
  WxCpExternalContactInfo getExternalContact(String corpId, String externalUserId) throws WxErrorException;

  /**
   * 获取客户详情.
   * <pre>
   *
   * 企业可通过此接口，根据外部联系人的userid（如何获取?），拉取客户详情。
   *
   * 请求方式：GET（HTTPS）
   * 请求地址：https://qyapi.weixin.qq.com/cgi-bin/externalcontact/get?access_token=ACCESS_TOKEN&external_userid=EXTERNAL_USERID
   *
   * 权限说明：
   *
   * 企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）；
   * 第三方/自建应用调用时，返回的跟进人follow_user仅包含应用可见范围之内的成员。
   * </pre>
   *
   * @param corpId         the corp id
   * @param externalUserId 外部联系人的userid，注意不是企业成员的帐号
   * @return . contact detail
   * @throws WxErrorException .
   */
  WxCpExternalContactInfo getContactDetail(String corpId, String externalUserId) throws WxErrorException;

  /**
   * 企业和服务商可通过此接口，将微信外部联系人的userid转为微信openid，用于调用支付相关接口。暂不支持企业微信外部联系人（ExternalUserid为wo开头）的userid转openid。
   *
   * @param corpId         the corp id
   * @param externalUserid 微信外部联系人的userid
   * @return 该企业的外部联系人openid string
   * @throws WxErrorException .
   */
  String convertToOpenid(String corpId, String externalUserid) throws WxErrorException;

  /**
   * 批量获取客户详情.
   * <pre>
   *
   * 企业/第三方可通过此接口获取指定成员添加的客户信息列表。
   *
   * 请求方式：POST（HTTPS）
   * 请求地址：https://qyapi.weixin.qq.com/cgi-bin/externalcontact/batch/get_by_user?access_token=ACCESS_TOKEN
   *
   * 权限说明：
   *
   * 企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）；
   * 第三方/自建应用调用时，返回的跟进人follow_user仅包含应用可见范围之内的成员。
   * </pre>
   *
   * @param corpId the corp id
   * @param userId 企业成员的userid，注意不是外部联系人的帐号
   * @param cursor the cursor
   * @param limit  the  limit
   * @return wx cp user external contact batch info
   * @throws WxErrorException .
   */
  WxCpExternalContactBatchInfo getContactDetailBatch(String corpId, String userId, String cursor,
                                                     Integer limit)
    throws WxErrorException;

  /**
   * 修改客户备注信息.
   * <pre>
   * 企业可通过此接口修改指定用户添加的客户的备注信息。
   * 请求方式: POST(HTTP)
   * 请求地址:https://qyapi.weixin.qq.com/cgi-bin/externalcontact/remark?access_token=ACCESS_TOKEN
   * 文档地址：https://work.weixin.qq.com/api/doc/90000/90135/92115
   * </pre>
   *
   * @param corpId  the corp id
   * @param request 备注信息请求
   * @throws WxErrorException .
   */
  void updateRemark(String corpId, WxCpUpdateRemarkRequest request) throws WxErrorException;

  /**
   * 获取客户列表.
   * <pre>
   *   企业可通过此接口获取指定成员添加的客户列表。客户是指配置了客户联系功能的成员所添加的外部联系人。没有配置客户联系功能的成员，所添加的外部联系人将不会作为客户返回。
   *
   * 请求方式：GET（HTTPS）
   * 请求地址：https://qyapi.weixin.qq.com/cgi-bin/externalcontact/list?access_token=ACCESS_TOKEN&userid=USERID
   *
   * 权限说明：
   *
   * 企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）；
   * 第三方应用需拥有“企业客户”权限。
   * 第三方/自建应用只能获取到可见范围内的配置了客户联系功能的成员。
   * </pre>
   *
   * @param corpId the corp id
   * @param userId 企业成员的userid
   * @return List of External wx id
   * @throws WxErrorException .
   */
  List<String> listExternalContacts(String corpId, String userId) throws WxErrorException;

  /**
   * 企业和第三方服务商可通过此接口获取配置了客户联系功能的成员(Customer Contact)列表。
   * <pre>
   *   企业需要使用外部联系人管理secret所获取的accesstoken来调用（accesstoken如何获取？）；
   *   第三方应用需拥有“企业客户”权限。
   *   第三方应用只能获取到可见范围内的配置了客户联系功能的成员
   * </pre>
   *
   * @param corpId the corp id
   * @return List of CpUser id
   * @throws WxErrorException .
   */
  List<String> listFollowers(String corpId) throws WxErrorException;

  /**
   * 企业和第三方可通过此接口，获取所有离职成员的客户列表，并可进一步调用离职成员的外部联系人再分配接口将这些客户重新分配给其他企业成员。
   *
   * @param corpId   the corp id
   * @param page     the page
   * @param pageSize the page size
   * @return wx cp user external unassign list
   * @throws WxErrorException the wx error exception
   */
  WxCpUserExternalUnassignList listUnassignedList(String corpId, Integer page, Integer pageSize) throws WxErrorException;

  /**
   * 企业可通过此接口，将已离职成员的外部联系人分配给另一个成员接替联系。
   *
   * @param corpId         the corp id
   * @param externalUserid the external userid
   * @param handOverUserid the hand over userid
   * @param takeOverUserid the take over userid
   * @return wx cp base resp
   * @throws WxErrorException the wx error exception
   * @deprecated 此后续将不再更新维护, 建议使用 {@link #transferCustomer(String, WxCpUserTransferCustomerReq)}
   */
  @Deprecated
  WxCpBaseResp transferExternalContact(String corpId, String externalUserid, String handOverUserid, String takeOverUserid) throws WxErrorException;

  /**
   * 企业可通过此接口，转接在职成员的客户给其他成员。
   * <per>
   * external_userid必须是handover_userid的客户（即配置了客户联系功能的成员所添加的联系人）。
   * 在职成员的每位客户最多被分配2次。客户被转接成功后，将有90个自然日的服务关系保护期，保护期内的客户无法再次被分配。
   * <p>
   * 权限说明：
   * * 企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
   * 第三方应用需拥有“企业客户权限->客户联系->在职继承”权限
   * 接替成员必须在此第三方应用或自建应用的可见范围内。
   * 接替成员需要配置了客户联系功能。
   * 接替成员需要在企业微信激活且已经过实名认证。
   * </per>
   *
   * @param corpId the corp id
   * @param req    转接在职成员的客户给其他成员请求实体
   * @return wx cp base resp
   * @throws WxErrorException the wx error exception
   */
  WxCpUserTransferCustomerResp transferCustomer(String corpId, WxCpUserTransferCustomerReq req) throws WxErrorException;

  /**
   * 企业和第三方可通过此接口查询在职成员的客户转接情况。
   * <per>
   * 权限说明：
   * <p>
   * 企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
   * 第三方应用需拥有“企业客户权限->客户联系->在职继承”权限
   * 接替成员必须在此第三方应用或自建应用的可见范围内。
   * </per>
   *
   * @param corpId         the corp id
   * @param handOverUserid 原添加成员的userid
   * @param takeOverUserid 接替成员的userid
   * @param cursor         分页查询的cursor，每个分页返回的数据不会超过1000条；不填或为空表示获取第一个分页；
   * @return 客户转接接口实体 wx cp user transfer result resp
   * @throws WxErrorException the wx error exception
   */
  WxCpUserTransferResultResp transferResult(String corpId, @NotNull String handOverUserid, @NotNull String takeOverUserid, String cursor) throws WxErrorException;

  /**
   * 企业可通过此接口，分配离职成员的客户给其他成员。
   * <per>
   * handover_userid必须是已离职用户。
   * external_userid必须是handover_userid的客户（即配置了客户联系功能的成员所添加的联系人）。
   * 在职成员的每位客户最多被分配2次。客户被转接成功后，将有90个自然日的服务关系保护期，保护期内的客户无法再次被分配。
   * <p>
   * 权限说明：
   * <p>
   * 企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
   * 第三方应用需拥有“企业客户权限->客户联系->离职分配”权限
   * 接替成员必须在此第三方应用或自建应用的可见范围内。
   * 接替成员需要配置了客户联系功能。
   * 接替成员需要在企业微信激活且已经过实名认证。
   * </per>
   *
   * @param corpId the corp id
   * @param req    转接在职成员的客户给其他成员请求实体
   * @return wx cp base resp
   * @throws WxErrorException the wx error exception
   */
  WxCpUserTransferCustomerResp resignedTransferCustomer(String corpId, WxCpUserTransferCustomerReq req) throws WxErrorException;

  /**
   * 企业和第三方可通过此接口查询离职成员的客户分配情况。
   * <per>
   * 权限说明：
   * <p>
   * 企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
   * 第三方应用需拥有“企业客户权限->客户联系->在职继承”权限
   * 接替成员必须在此第三方应用或自建应用的可见范围内。
   * </per>
   *
   * @param corpId         the corp id
   * @param handOverUserid 原添加成员的userid
   * @param takeOverUserid 接替成员的userid
   * @param cursor         分页查询的cursor，每个分页返回的数据不会超过1000条；不填或为空表示获取第一个分页；
   * @return 客户转接接口实体 wx cp user transfer result resp
   * @throws WxErrorException the wx error exception
   */
  WxCpUserTransferResultResp resignedTransferResult(String corpId, @NotNull String handOverUserid, @NotNull String takeOverUserid, String cursor) throws WxErrorException;

  /**
   * <pre>
   * 该接口用于获取配置过客户群管理的客户群列表。
   * 企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
   * 暂不支持第三方调用。
   * 微信文档：https://work.weixin.qq.com/api/doc/90000/90135/92119
   * </pre>
   *
   * @param corpId    the corp id
   * @param pageIndex the page index
   * @param pageSize  the page size
   * @param status    the status
   * @param userIds   the user ids
   * @param partyIds  the party ids
   * @return the wx cp user external group chat list
   * @throws WxErrorException the wx error exception
   * @deprecated 请使用 {@link WxCpTpExternalContactService#listGroupChat(String corpId, Integer, String, int, String[])}
   */
  @Deprecated
  WxCpUserExternalGroupChatList listGroupChat(String corpId, Integer pageIndex, Integer pageSize, int status, String[] userIds, String[] partyIds) throws WxErrorException;

  /**
   * <pre>
   * 该接口用于获取配置过客户群管理的客户群列表。
   * 企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
   * 暂不支持第三方调用。
   * 微信文档：https://work.weixin.qq.com/api/doc/90000/90135/92119
   * </pre>
   *
   * @param corpId  the corp id
   * @param limit   分页，预期请求的数据量，取值范围 1 ~ 1000
   * @param cursor  用于分页查询的游标，字符串类型，由上一次调用返回，首次调用不填
   * @param status  客户群跟进状态过滤。0 - 所有列表(即不过滤)  1 - 离职待继承  2 - 离职继承中  3 - 离职继承完成 默认为0
   * @param userIds 群主过滤。如果不填，表示获取应用可见范围内全部群主的数据（但是不建议这么用，如果可见范围人数超过1000人，为了防止数据包过大，会报错 81017）;用户ID列表。最多100个
   * @return the wx cp user external group chat list
   * @throws WxErrorException the wx error exception
   */
  WxCpUserExternalGroupChatList listGroupChat(String corpId, Integer limit, String cursor, int status, String[] userIds) throws WxErrorException;

  /**
   * <pre>
   * 通过客户群ID，获取详情。包括群名、群成员列表、群成员入群时间、入群方式。（客户群是由具有客户群使用权限的成员创建的外部群）
   * 企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
   * 暂不支持第三方调用。
   * 微信文档：https://work.weixin.qq.com/api/doc/90000/90135/92122
   * </pre>
   *
   * @param corpId   the corp id
   * @param chatId   the chat id
   * @param needName the need name
   * @return group chat
   * @throws WxErrorException the wx error exception
   */
  WxCpUserExternalGroupChatInfo getGroupChat(String corpId, String chatId, Integer needName) throws WxErrorException;

  /**
   * 企业可通过此接口，将已离职成员为群主的群，分配给另一个客服成员。
   *
   * <per>
   * 注意：：
   * <p>
   * 群主离职了的客户群，才可继承
   * 继承给的新群主，必须是配置了客户联系功能的成员
   * 继承给的新群主，必须有设置实名
   * 继承给的新群主，必须有激活企业微信
   * 同一个人的群，限制每天最多分配300个给新群主
   * <p>
   * 权限说明:
   * <p>
   * 企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
   * 第三方应用需拥有“企业客户权限->客户联系->分配离职成员的客户群”权限
   * 对于第三方/自建应用，群主必须在应用的可见范围。
   * </per>
   *
   * @param corpId   the corp id
   * @param chatIds  需要转群主的客户群ID列表。取值范围： 1 ~ 100
   * @param newOwner 新群主ID
   * @return 分配结果 ，主要是分配失败的群列表
   * @throws WxErrorException the wx error exception
   */
  WxCpUserExternalGroupChatTransferResp transferGroupChat(String corpId, String[] chatIds, String newOwner) throws WxErrorException;

  /**
   * <pre>
   * 企业可通过此接口获取成员联系客户的数据，包括发起申请数、新增客户数、聊天数、发送消息数和删除/拉黑成员的客户数等指标。
   * 企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
   * 第三方应用需拥有“企业客户”权限。
   * 第三方/自建应用调用时传入的userid和partyid要在应用的可见范围内;
   * </pre>
   *
   * @param corpId    the corp id
   * @param startTime the start time
   * @param endTime   the end time
   * @param userIds   the user ids
   * @param partyIds  the party ids
   * @return user behavior statistic
   * @throws WxErrorException the wx error exception
   */
  WxCpUserExternalUserBehaviorStatistic getUserBehaviorStatistic(String corpId, Date startTime, Date endTime, String[] userIds, String[] partyIds) throws WxErrorException;

  /**
   * <pre>
   * 获取指定日期全天的统计数据。注意，企业微信仅存储60天的数据。
   * 企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
   * 暂不支持第三方调用。
   * </pre>
   *
   * @param corpId    the corp id
   * @param startTime the start time
   * @param orderBy   the order by
   * @param orderAsc  the order asc
   * @param pageIndex the page index
   * @param pageSize  the page size
   * @param userIds   the user ids
   * @param partyIds  the party ids
   * @return group chat statistic
   * @throws WxErrorException the wx error exception
   */
  WxCpUserExternalGroupChatStatistic getGroupChatStatistic(String corpId, Date startTime, Integer orderBy, Integer orderAsc, Integer pageIndex, Integer pageSize, String[] userIds, String[] partyIds) throws WxErrorException;

  /**
   * 添加企业群发消息任务
   * 企业可通过此接口添加企业群发消息的任务并通知客服人员发送给相关客户或客户群。（注：企业微信终端需升级到2.7.5版本及以上）
   * 注意：调用该接口并不会直接发送消息给客户/客户群，需要相关的客服人员操作以后才会实际发送（客服人员的企业微信需要升级到2.7.5及以上版本）
   * 同一个企业每个自然月内仅可针对一个客户/客户群发送4条消息，超过限制的用户将会被忽略。
   * <p>
   * 请求方式: POST(HTTP)
   * <p>
   * 请求地址:https://qyapi.weixin.qq.com/cgi-bin/externalcontact/add_msg_template?access_token=ACCESS_TOKEN
   * <p>
   * 文档地址：https://work.weixin.qq.com/api/doc/90000/90135/92135
   *
   * @param corpId                      the corp id
   * @param wxCpUserExternalMsgTemplate the wx cp msg template
   * @return the wx cp msg template add result
   * @throws WxErrorException the wx error exception
   */
  WxCpMsgTemplateAddResult addMsgTemplate(String corpId, WxCpUserExternalMsgTemplate wxCpUserExternalMsgTemplate) throws WxErrorException;

  /**
   * 获取群发记录列表
   *
   * @param corpId                                       the corp id
   * @param wxCpUserExternalContactGroupMsgListV2Request the wx cp user external contact group msg list v 2 request
   * @return wx cp user external contact group msg list v 2 result
   * @throws WxErrorException the wx error exception
   */
  WxCpUserExternalContactGroupMsgListV2Result groupMsgListV2(String corpId, WxCpUserExternalContactGroupMsgListV2Request wxCpUserExternalContactGroupMsgListV2Request) throws WxErrorException;

  /**
   * 获取群发成员发送任务列表
   *
   * @param corpId                                     the corp id
   * @param wxCpUserExternalContactGroupMsgTaskRequest the wx cp user external contact group msg task request
   * @return wx cp user external contact group msg task result
   * @throws WxErrorException the wx error exception
   */
  WxCpUserExternalContactGroupMsgTaskResult groupMsgTask(String corpId, WxCpUserExternalContactGroupMsgTaskRequest wxCpUserExternalContactGroupMsgTaskRequest) throws WxErrorException;

  /**
   * 获取企业群发成员执行结果
   *
   * @param corpId                                     the corp id
   * @param wxCpUserExternalContactGroupMsgSendRequest the wx cp user external contact group msg send request
   * @return wx cp user external contact group msg send result
   * @throws WxErrorException the wx error exception
   */
  WxCpUserExternalContactGroupMsgSendResult groupMsgSendResult(String corpId, WxCpUserExternalContactGroupMsgSendRequest wxCpUserExternalContactGroupMsgSendRequest) throws WxErrorException;


  /**
   * 发送新客户欢迎语
   * <pre>
   * 企业微信在向企业推送添加外部联系人事件时，会额外返回一个welcome_code，企业以此为凭据调用接口，即可通过成员向新添加的客户发送个性化的欢迎语。
   * 为了保证用户体验以及避免滥用，企业仅可在收到相关事件后20秒内调用，且只可调用一次。
   * 如果企业已经在管理端为相关成员配置了可用的欢迎语，则推送添加外部联系人事件时不会返回welcome_code。
   * 每次添加新客户时可能有多个企业自建应用/第三方应用收到带有welcome_code的回调事件，但仅有最先调用的可以发送成功。后续调用将返回41051（externaluser has started chatting）错误，请用户根据实际使用需求，合理设置应用可见范围，避免冲突。
   * 请求方式: POST(HTTP)
   *
   * 请求地址:https://qyapi.weixin.qq.com/cgi-bin/externalcontact/send_welcome_msg?access_token=ACCESS_TOKEN
   *
   * 文档地址：https://work.weixin.qq.com/api/doc/90000/90135/92137
   * </pre>
   *
   * @param corpId the corp id
   * @param msg    .
   * @throws WxErrorException .
   */
  void sendWelcomeMsg(String corpId, WxCpWelcomeMsg msg) throws WxErrorException;

  /**
   * <pre>
   * 企业可通过此接口获取企业客户标签详情。
   * </pre>
   *
   * @param corpId the corp id
   * @param tagId  the tag id
   * @return corp tag list
   * @throws WxErrorException the wx error exception
   */
  WxCpUserExternalTagGroupList getCorpTagList(String corpId, String[] tagId) throws WxErrorException;

  /**
   * <pre>
   * 企业可通过此接口获取企业客户标签详情。
   * 若tag_id和group_id均为空，则返回所有标签。
   * 同时传递tag_id和group_id时，忽略tag_id，仅以group_id作为过滤条件。
   * </pre>
   *
   * @param corpId  the corp id
   * @param tagId   the tag id
   * @param groupId the tagGroup id
   * @return corp tag list
   * @throws WxErrorException the wx error exception
   */
  WxCpUserExternalTagGroupList getCorpTagList(String corpId, String[] tagId, String[] groupId) throws WxErrorException;

  /**
   * <pre>
   * 企业可通过此接口向客户标签库中添加新的标签组和标签，每个企业最多可配置3000个企业标签。
   * 暂不支持第三方调用。
   * </pre>
   *
   * @param corpId   the corp id
   * @param tagGroup the tag group
   * @return wx cp user external tag group info
   * @throws WxErrorException the wx error exception
   */
  WxCpUserExternalTagGroupInfo addCorpTag(String corpId, WxCpUserExternalTagGroupInfo tagGroup) throws WxErrorException;

  /**
   * <pre>
   * 企业可通过此接口编辑客户标签/标签组的名称或次序值。
   * 暂不支持第三方调用。
   * </pre>
   *
   * @param corpId the corp id
   * @param id     the id
   * @param name   the name
   * @param order  the order
   * @return wx cp base resp
   * @throws WxErrorException the wx error exception
   */
  WxCpBaseResp editCorpTag(String corpId, String id, String name, Integer order) throws WxErrorException;

  /**
   * <pre>
   * 企业可通过此接口删除客户标签库中的标签，或删除整个标签组。
   * 暂不支持第三方调用。
   * </pre>
   *
   * @param corpId  the corp id
   * @param tagId   the tag id
   * @param groupId the group id
   * @return wx cp base resp
   * @throws WxErrorException the wx error exception
   */
  WxCpBaseResp delCorpTag(String corpId, String[] tagId, String[] groupId) throws WxErrorException;

  /**
   * <pre>
   * 企业可通过此接口为指定成员的客户添加上由企业统一配置的标签。
   * https://work.weixin.qq.com/api/doc/90000/90135/92117
   * </pre>
   *
   * @param corpId         the corp id
   * @param userid         the userid
   * @param externalUserid the external userid
   * @param addTag         the add tag
   * @param removeTag      the remove tag
   * @return wx cp base resp
   * @throws WxErrorException the wx error exception
   */
  WxCpBaseResp markTag(String corpId, String userid, String externalUserid, String[] addTag, String[] removeTag) throws WxErrorException;


}
