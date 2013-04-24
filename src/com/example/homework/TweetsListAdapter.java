package com.example.homework;

import java.util.List;

import twitter4j.Status;
import android.app.Activity;
import android.view.View;

import com.example.homework.ui.BaseListAdapter;

public class TweetsListAdapter extends BaseListAdapter {

	private final Activity activity;

	public TweetsListAdapter(Activity activity, int viewid, List<Status> tweets) {
		super(activity, viewid, tweets);

		this.activity = activity;
	}

	@Override
	protected ViewHolder createHolder(View v, int position) {
		return new TweetViewHolder(activity, v);
	}

	@Override
	protected void bindHolder(ViewHolder h) {
		TweetViewHolder holder = (TweetViewHolder) h;
		holder.bindData((Status) holder.data);
	}
}
