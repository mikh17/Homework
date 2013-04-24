package com.example.homework;

import org.droidparts.net.image.ImageFetcher;

import twitter4j.Status;
import twitter4j.User;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.homework.ui.BaseListAdapter.ViewHolder;

public class TweetViewHolder extends ViewHolder {

	private TextView txtName;
	private TextView txtStatus;
	private ImageView imAvatar;

	private ImageFetcher imageFetcher;

	public TweetViewHolder(Context context, View view) {

		imageFetcher = new ImageFetcher(context);
		txtName = (TextView) view.findViewById(R.id.txtName);
		txtStatus = (TextView) view.findViewById(R.id.txtStatus);
		imAvatar = (ImageView) view.findViewById(R.id.imAvatar);
	}

	protected long getItemID() {

		if (data == null)
			return 0;

		if (data instanceof Status)
			return ((Status) data).getId();

		return 0;
	}

	public void bindData(Status item) {

		if (item == null)
			return;

		imAvatar.setImageBitmap(null);

		User user = item.getUser();

		if (user != null) {
			txtName.setText(user.getName());
			imageFetcher.attachImage(imAvatar, user.getProfileImageURL());
		}

		txtStatus.setText(item.getText());
	}
}
