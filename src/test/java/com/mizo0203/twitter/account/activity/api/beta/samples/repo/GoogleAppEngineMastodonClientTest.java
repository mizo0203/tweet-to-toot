package com.mizo0203.twitter.account.activity.api.beta.samples.repo;

import com.sys1yagi.mastodon4j.api.entity.Status;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GoogleAppEngineMastodonClientTest {

  private GoogleAppEngineMastodonClient mTarget;

  @Before
  public void setUp() throws Exception {
    mTarget = new GoogleAppEngineMastodonClient();
  }

  @After
  public void tearDown() throws Exception {}

  @Test
  public void postStatus() {
    mTarget.postStatus("Test", null, null, null, null, Status.Visibility.Direct);
  }
}
