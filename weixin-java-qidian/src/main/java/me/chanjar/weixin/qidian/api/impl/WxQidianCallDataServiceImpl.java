package me.chanjar.weixin.qidian.api.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.qidian.api.WxQidianCallDataService;
import me.chanjar.weixin.qidian.api.WxQidianService;
import me.chanjar.weixin.qidian.bean.call.GetSwitchBoardListResponse;

import static me.chanjar.weixin.qidian.enums.WxQidianApiUrl.CallData.GET_SWITCH_BOARD_LIST;

@Slf4j
@RequiredArgsConstructor
public class WxQidianCallDataServiceImpl implements WxQidianCallDataService {
  private final WxQidianService wxQidianService;

  @Override
  public GetSwitchBoardListResponse getSwitchBoardList() throws WxErrorException {
    String result = this.wxQidianService.get(GET_SWITCH_BOARD_LIST, null);
    return GetSwitchBoardListResponse.fromJson(result);
  }

}
