package com.plaid.client.request.common;

import static com.plaid.client.internal.Util.*;

import com.plaid.request.common.BaseClientRequest;

/**
 * Abstract base class for requests that require an access_token.
 */
public abstract class BaseAccessTokenRequest extends BaseClientRequest {
  private final String accessToken;

  public BaseAccessTokenRequest(String accessToken) {
    notNull(accessToken, "accessToken");
    this.accessToken = accessToken;
  }
}