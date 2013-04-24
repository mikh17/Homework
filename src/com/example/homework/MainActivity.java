package com.example.homework;

import java.util.List;

import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterListener;
import twitter4j.TwitterMethod;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.example.homework.utils.StringUtil;

public class MainActivity extends Activity {

	private Configuration twitterConf;
	private EditText edSearch;
	private Button btnSearch;
	private TextView txtNothingFound;
	private ProgressBar progressBar;
	private ListView lvTweets;
	private TweetsListAdapter tweetsAdapter;
	private List<Status> tweets;
	
	private boolean isSearching;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey("6PkyqVQ97Kq0wdSfL02y3g")
				.setOAuthConsumerSecret("mQr9ztocHoXEsHNrFS5xpslUVO6bQru9krWqaH4Ano")
				.setOAuthAccessToken("53719570-hoEFmsXB500Fi80EqlTBA22RakFB6Pgx8IRMIg9Ha")
				.setOAuthAccessTokenSecret("uuJugniZO9HoG5hrJZqr9MPTrFmZ1vfZWJFblefMI");
		
		twitterConf = cb.build();

		edSearch = (EditText) findViewById(R.id.edSearch);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		txtNothingFound = (TextView) findViewById(R.id.txtNothingFound);
		progressBar = (ProgressBar) findViewById(R.id.progress);
		lvTweets = (ListView) findViewById(R.id.lvTweets);

		btnSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				searchTweets();
			}
		});

		edSearch.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH
						|| actionId == EditorInfo.IME_ACTION_DONE
						|| event.getKeyCode() == KeyEvent.KEYCODE_SEARCH
						|| event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					searchTweets();
					return true;
				}
				return false;
			}
		});
	}

	private void searchTweets() {
		
		if(isSearching)
			return;
		
		String query = edSearch.getText().toString();

		if (StringUtil.isNullOrEmpty(query))
			return;

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edSearch.getWindowToken(), 0);

		showProgress(true);

		TwitterListener listener = new TwitterAdapter() {
			@Override
			public void searched(final QueryResult queryResult) {
				compleated(queryResult, null);			
			}

			@Override
			public void onException(TwitterException e, TwitterMethod method) {
				compleated(null, e);
			}
			
			private void compleated(final QueryResult queryResult, TwitterException e) {
				if(e != null)
					e.printStackTrace();
				
				isSearching = false;
				
				MainActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						tweets = queryResult == null ? null : queryResult.getTweets();
						tweetsAdapter = tweets == null ? null : new TweetsListAdapter(MainActivity.this, R.layout.view_tweet_item, tweets);
						lvTweets.setAdapter(tweetsAdapter);

						showProgress(false);						
					}
				});				
			}
		};		

		AsyncTwitterFactory factory = new AsyncTwitterFactory(twitterConf);
		AsyncTwitter asyncTwitter = factory.getInstance();
		asyncTwitter.addListener(listener);
		asyncTwitter.search(new Query(query));
	}

	private boolean isEmpty() {
		return tweets == null || tweets.size() == 0;
	}

	private void showProgress(boolean isShow) {
		progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
		txtNothingFound.setVisibility(!isShow && isEmpty() ? View.VISIBLE
				: View.GONE);
		lvTweets.setVisibility(!isShow && !isEmpty() ? View.VISIBLE : View.GONE);
		btnSearch.setEnabled(!isShow);
	}
}
