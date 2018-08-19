package com.mizo0203.twitter.account.activity.api.beta.samples.repo;

import com.google.gson.Gson;
import com.mizo0203.twitter.account.activity.api.beta.samples.domain.difine.KeysAndAccessTokens;
import com.sys1yagi.mastodon4j.MastodonClient;
import com.sys1yagi.mastodon4j.api.entity.Status;
import com.sys1yagi.mastodon4j.api.exception.Mastodon4jRequestException;
import com.sys1yagi.mastodon4j.api.method.Statuses;
import okhttp3.GoogleAppEngineOkHttpClient;
import okhttp3.Headers;
import okhttp3.OkHttpClient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GoogleAppEngineMastodonClient {

  private static final Logger LOG = Logger.getLogger(GoogleAppEngineMastodonClient.class.getName());
  private final MastodonClient mMastodonClient;

  public GoogleAppEngineMastodonClient() {

    OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
    mMastodonClient =
        new MastodonClient.Builder(
                KeysAndAccessTokens.MASTODON_INSTANCE_NAME, okHttpClientBuilder, new Gson())
            .build();

    Headers headers =
        new Headers.Builder()
            .set(
                "Authorization",
                String.format("Bearer %s", KeysAndAccessTokens.MASTODON_ACCESS_TOKEN))
            .build();

    try {
      Field field = MastodonClient.class.getDeclaredField("client");
      field.setAccessible(true);
      field.set(
          mMastodonClient,
          new GoogleAppEngineOkHttpClient.Builder(okHttpClientBuilder).headers(headers).build());
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  /**
   * Posting a new status
   *
   * <p>POST /api/v1/statuses
   *
   * <p>注：重複ステータスを防止するため、このエンドポイントはIdempotency-Keyヘッダーを受け取ります。 これは新しいステータスごとに一意の文字列に設定する必要があります。
   * ネットワークエラーが発生した場合、同じIdempotency-Keyで要求を再試行できます。
   * 同じIdempotency-Keyを持つリクエストの数にかかわらず、1つのステータスだけが作成されます。
   *
   * <p>idempotencyとidempotencyの詳細については、https://stripe.com/blog/idempotencyを参照してください。
   *
   * <p>Note: In order to prevent duplicate statuses, this endpoint accepts an Idempotency-Key
   * header, which should be set to a unique string for each new status. In the event of a network
   * error, a request can be retried with the same Idempotency-Key. Only one status will be created
   * regardless of how many requests with the same Idempotency-Key did go through.
   *
   * <p>See https://stripe.com/blog/idempotency for more on idempotency and idempotency keys.
   *
   * <p>https://github.com/tootsuite/documentation/blob/master/Using-the-API/API.md#posting-a-new-status
   *
   * @param status: The text of the status
   * @param inReplyToId (optional): local ID of the status you want to reply to
   * @param mediaIds (optional): array of media IDs to attach to the status (maximum 4)
   * @param sensitive (optional): set this to mark the media of the status as NSFW
   * @param spoilerText (optional): text to be shown as a warning before the actual content
   * @param visibility (optional): either "direct", "private", "unlisted" or "public"
   * @return the new Status
   */
  public Status postStatus(
      @Nonnull String status,
      @Nullable Long inReplyToId,
      @Nullable List<Long> mediaIds,
      @Nullable Boolean sensitive,
      @Nullable String spoilerText,
      @Nullable Status.Visibility visibility) {
    try {
      if (sensitive == null) {
        sensitive = Boolean.FALSE;
      }
      Status ret;
      if (visibility == null) {
        ret =
            new Statuses(mMastodonClient)
                .postStatus(status, inReplyToId, mediaIds, sensitive, spoilerText)
                .execute();
      } else {
        ret =
            new Statuses(mMastodonClient)
                .postStatus(status, inReplyToId, mediaIds, sensitive, spoilerText, visibility)
                .execute();
      }
      LOG.log(Level.INFO, "postStatus ret: " + ret);
      return ret;
    } catch (Mastodon4jRequestException e) {
      LOG.log(Level.SEVERE, "postStatus", e);
      LOG.log(Level.SEVERE, "postStatus ErrorResponse: ", e.getResponse());
      return null;
    }
  }
}
