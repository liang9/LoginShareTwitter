package br.com.carinatiemiyoshida.twitterdemonumberone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.net.MalformedURLException;
import java.net.URL;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {
    private static final String TWITTER_KEY = "oJmi7pCPyQpGAW6CLoOY0tuq9";
    private static final String TWITTER_SECRET = "04OPG0nU83uuT0P9ex8IX5Clm1GFq5v9bBWy3ChKS1W7SZFVC4";
    private static  String LAG = "-----------MainActivity";
    private TwitterLoginButton loginButton;
    private ImageView imageView;
    private TextView txtDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        imageView = (ImageView) findViewById(R.id.imageView);
        txtDetails = (TextView) findViewById(R.id.txtDetails);
        Log.e(LAG,"第一步");
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.e(LAG,"第二步");
                TwitterSession session = result.data;
                final String userName = session.getUserName();
                //String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Call<User> user = Twitter.getApiClient(session).getAccountService().verifyCredentials(true, false);
                user.enqueue(new Callback<User>() {
                    @Override
                    public void success(Result<User> result) {
                        Log.e(LAG,"第3步");
                        User userInfo = result.data;
                        String email = userInfo.email;
                        String description = userInfo.description;
                        String location = userInfo.location;
                        userInfo.getId();
                        int friendsCount = userInfo.friendsCount;
                        int favouritesCount = userInfo.favouritesCount;
                        int followersCount = userInfo.followersCount;

                      /*  String profileImageUrl = userInfo
                                .profileImageUrl
                                .replace("normal", "");*/
//                        Log.e(LAG,"------------     "+profileImageUrl);
                        Picasso.with(getApplicationContext())
                                .load(userInfo.profileImageUrl)
                                .into(imageView);
                        Log.e(LAG,"------------     "+userInfo.profileImageUrl);
                        StringBuilder sb = new StringBuilder();
                        sb.append("User Name: "+ userName);
                        sb.append("\n");
                        sb.append("Email: "+ email);
                        sb.append("\n");
                        sb.append("Description: "+ description);
                        sb.append("\n");
                        sb.append("Location: "+ location);
                        sb.append("\n");
                        sb.append("FriendsCount: "+ friendsCount);
                        sb.append("\n");
                        sb.append("FavouritesCount: "+ favouritesCount);
                        sb.append("\n");
                        sb.append("FollowersCount: "+ followersCount);
                        txtDetails.setText(sb.toString());
                    }
                    @Override
                    public void failure(TwitterException exception) {
                        Log.e(LAG,"第4步");
                    }
                });
                loginButton.setVisibility(View.INVISIBLE);
                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
            @Override
            public void failure(TwitterException exception) {
                Log.e(LAG,"第5步");
                Log.e("TwitterKit", "Login with Twitter failure", exception);
                Log.e("BLA: ", exception.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(LAG,"第6步");
        loginButton.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 分享到twitter
     * 若未安装twitter客户端，则会跳转到浏览器
     *
     * @param view
     */
    public void shareToTwitter(View view) {
        //这里分享一个链接，更多分享配置参考官方介绍：https://dev.twitter.com/twitterkit/android/compose-tweets
        try {
            TweetComposer.Builder builder = new TweetComposer.Builder(MainActivity.this)
                    .url(new URL("https://www.google.com/"));
            builder.show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
