package com.thanhtran.redstring.main.components;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.thanhtran.redstring.R;
import com.thanhtran.redstring.newstory.activities.AddLoveMsgActivity;
import com.thanhtran.redstring.main.adapter.StoryAdapter;
import com.thanhtran.redstring.app.AppController;
import com.thanhtran.redstring.main.models.StoryItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ThanhTran on 9/28/2015.
 */
public class StoryFragment extends Fragment {
    private static final String TAG = StoryFragment.class.getSimpleName();
    private StoryAdapter listAdapter;
    private List<StoryItem> storyItems;
    private String URL_FEED = "http://api.androidhive.info/feed/feed.json";

    public StoryFragment() {
        System.out.println("init StoryFragment..........");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View timeLineView = inflater.inflate(R.layout.main_fragment_story, container, false);
        ListView listView = (ListView) timeLineView.findViewById(R.id.news_feed);
        storyItems = new ArrayList<>();
        listAdapter = new StoryAdapter(this.getActivity(), storyItems);
        listView.setAdapter(listAdapter);
        initNewsFeed();
        FloatingActionButton fab = (FloatingActionButton) timeLineView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent messagingActivity = new Intent(getContext(), AddLoveMsgActivity.class);
                startActivity(messagingActivity);
            }
        });
        return timeLineView;
    }

    private void initNewsFeed() {
        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(URL_FEED);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                    URL_FEED, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        parseJsonFeed(response);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }
    }

    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                StoryItem item = new StoryItem();
                item.setId(feedObj.getInt("id"));
                item.setName(feedObj.getString("name"));

                // Image might be null sometimes
                String image = feedObj.isNull("image") ? null : feedObj
                        .getString("image");
                item.setImge(image);
                item.setStatus(feedObj.getString("status"));
                item.setProfilePic(feedObj.getString("profilePic"));
                item.setTimeStamp(feedObj.getString("timeStamp"));

                // url might be null sometimes
                String feedUrl = feedObj.isNull("url") ? null : feedObj
                        .getString("url");
                item.setUrl(feedUrl);

                storyItems.add(item);
            }

            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
