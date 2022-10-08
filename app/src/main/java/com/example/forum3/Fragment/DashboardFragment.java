package com.example.forum3.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forum3.Activity.AgendaListAct;
import com.example.forum3.Activity.BlogListActivity;
import com.example.forum3.Activity.EventDetailActivity;
import com.example.forum3.Activity.FoodAndDiningActivity;
import com.example.forum3.Activity.HotelInfoActivity;
import com.example.forum3.Activity.MapEventDetailActivity;
import com.example.forum3.Activity.QnAActivity;
import com.example.forum3.Activity.SpeakerDetailsActivity;
import com.example.forum3.Activity.SpeakerListActivity;
import com.example.forum3.Activity.SponsorActivity;
import com.example.forum3.Activity.VideoListActivity;
import com.example.forum3.Adapter.DashboardAdapter;
import com.example.forum3.Adapter.RecentEventAdapter;
import com.example.forum3.Adapter.SpeakerListAdapter;
import com.example.forum3.Adapter.VideoListAdapter;
import com.example.forum3.ApiService.APIServices;
import com.example.forum3.ApiService.ApiCallbackCode;
import com.example.forum3.ApiService.ApiUrl;
import com.example.forum3.ApiService.ApiUtility;
import com.example.forum3.Preference.PreferenceManager;
import com.example.forum3.Preference.Preference_Constant;
import com.example.forum3.R;
import com.google.gson.JsonObject;
//import com.runtime.hforlife.R;
//import com.runtime.hforlife.activity.AgendaListAct;
//import com.runtime.hforlife.activity.BlogListActivity;
//import com.runtime.hforlife.activity.EventDetailActivity;
//import com.runtime.hforlife.activity.MapEventDetailActivity;
//import com.runtime.hforlife.activity.SpeakerDetailsActivity;
//import com.runtime.hforlife.activity.SpeakerListActivity;
//import com.runtime.hforlife.activity.SponsorActivity;
//import com.runtime.hforlife.activity.VideoListActivity;
//import com.runtime.hforlife.adapter.DashboardAdapter;
//import com.runtime.hforlife.adapter.RecentEventAdapter;
//import com.runtime.hforlife.adapter.SpeakerListAdapter;
//import com.runtime.hforlife.adapter.VideoListAdapter;
//import com.runtime.hforlife.apiservice.APIServices;
//import com.runtime.hforlife.apiservice.ApiCallbackCode;
//import com.runtime.hforlife.apiservice.ApiUrl;
//import com.runtime.hforlife.apiservice.ApiUtility;
//import com.runtime.hforlife.preferance.PreferenceManager;
//import com.runtime.hforlife.preferance.Preference_Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Retrofit;


public class DashboardFragment extends Fragment implements ApiCallbackCode {

    private RecyclerView event_rv, dashboard_rv, features_videos_rv, features_speaker_rv;
    private int[] dash_icon={R.drawable.agenda_icon, R.drawable.speaker_icon, R.drawable.blog_icon,
            R.drawable.qna_icon, R.drawable.hotel_icon, R.drawable.dining_icon};
    private String[] dash_name={"Agenda", "Speakers", "Blogs", "Q&A", "Hotel", "Dining"};

    private static final String ARG_PARAM1="param1";
    private static final String ARG_PARAM2="param2";

    private String mParam1;
    private String mParam2;
    private static final String EXTRA_TEXT = "text";
    private JSONArray video_data,speaker_data,Event_data;
    private PreferenceManager preferenceManager;
    private String userregid = "";

    public DashboardFragment () {
        // Required empty public constructor
    }


    public static DashboardFragment newInstance ( String param1, String param2 ) {
        DashboardFragment fragment=new DashboardFragment();
        Bundle args=new Bundle();
        args.putString( ARG_PARAM1, param1 );
        args.putString( ARG_PARAM2, param2 );
        fragment.setArguments( args );
        return fragment;
    }

    public static DashboardFragment createFor(String text) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        if (getArguments() != null) {
            mParam1=getArguments().getString( ARG_PARAM1 );
            mParam2=getArguments().getString( ARG_PARAM2 );
        }
    }


    @Override
    public View onCreateView ( LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState ) {
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.fragment_dashboard,container,false);

        preferenceManager = new PreferenceManager(getActivity());
        userregid = preferenceManager.getPreferenceValues( Preference_Constant.USER_ID);

        event_rv=root.findViewById( R.id.event_rv );
        dashboard_rv=(RecyclerView) root.findViewById( R.id.dashboard_rv );
        features_videos_rv=(RecyclerView) root.findViewById( R.id.features_videos_rv );
        features_speaker_rv=(RecyclerView) root.findViewById( R.id.features_speaker_rv );

        DashboardAdapter dashboardAdapter=new DashboardAdapter( getActivity(), dash_name, dash_icon );
        dashboard_rv.setLayoutManager( new GridLayoutManager( getActivity(), 3 ) );
        dashboard_rv.setAdapter( dashboardAdapter );

        video_data = new JSONArray();
        speaker_data = new JSONArray();
        Event_data = new JSONArray();

        click_event();
        speaker_list_api( "speakers" );
        event_list_api( "events" );
        featured_video_list_api( "videos", "1" );

        return root;

    }

    private void click_event () {

        /*notification_iv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View view ) {
                Intent intent=new Intent( getActivity(), NotificationListActivity.class );
                startActivity( intent );
            }
        } );*/

        dashboard_rv.addOnItemTouchListener( new DashboardAdapter.RecyclerTouchListener( getActivity(), dashboard_rv, new DashboardAdapter.ClickListener() {
            @Override
            public View onClick ( View view, int position ) {
                if (position == 0) {
                    Intent intent=new Intent( getActivity(), AgendaListAct.class );
                    startActivity( intent );
                }
                if (position == 1) {
                    Intent intent=new Intent( getActivity(), SpeakerListActivity.class );
                    startActivity( intent );
                }
                if (position == 2) {
                    Intent intent=new Intent( getActivity(), BlogListActivity.class );
                    startActivity( intent );
                }
                if (position == 3) {
                    Intent intent=new Intent( getActivity(), QnAActivity.class);
                    startActivity( intent );
                }
                if (position == 4) {
                    Intent intent=new Intent( getActivity(), HotelInfoActivity.class);
                    startActivity( intent );
                }
                if (position == 5) {
                    Intent intent=new Intent( getActivity(), FoodAndDiningActivity.class);
                    startActivity( intent );
                }
                return view;
            }

            @Override
            public void onLongClick ( View view, int position ) {

            }
        } ) );

       /* features_videos_rv.addOnItemTouchListener( new VideoListAdapter.RecyclerTouchListener( getActivity(), features_videos_rv, new VideoListAdapter.ClickListener() {
            @Override
            public View onClick ( View view, int position ) {
                try {
                    JSONObject jsonObject = video_data.getJSONObject(position);
                    String video_url = jsonObject.getString("video_url");
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(video_url));
                    startActivity(browserIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return view;
            }

            @Override
            public void onLongClick ( View view, int position ) {

            }
        } ) );

        features_speaker_rv.addOnItemTouchListener(new SpeakerListAdapter.RecyclerTouchListener(getActivity(), features_speaker_rv, new SpeakerListAdapter.ClickListener() {
            @Override
            public View onClick( View view, int position) {
                try {
                    JSONObject jsonObject = speaker_data.getJSONObject(position);
                    String speaker_id = jsonObject.getString("speaker_id");
                    Intent intent = new Intent(getActivity(), SpeakerDetailsActivity.class);
                    intent.putExtra("speaker_id",speaker_id);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return view;
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));*/

        features_speaker_rv.addOnItemTouchListener(new SpeakerListAdapter.RecyclerTouchListener(getActivity(), features_speaker_rv, new SpeakerListAdapter.ClickListener() {
            @Override
            public View onClick( View view, int position) {
                try {
                    JSONObject jsonObject = speaker_data.getJSONObject(position);
                    String speaker_id = jsonObject.getString("speaker_id");
                    Intent intent = new Intent(getActivity(), SpeakerDetailsActivity.class);
                    intent.putExtra("speaker_id",speaker_id);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return view;
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        event_rv.addOnItemTouchListener(new RecentEventAdapter.RecyclerTouchListener(getActivity(), event_rv, new RecentEventAdapter.ClickListener() {
            @Override
            public View onClick( View view, int position) {
                try {
                    JSONObject jsonObject = Event_data.getJSONObject(position);
                    String event_id = jsonObject.getString("event_id");
                    Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                    intent.putExtra("event_id",event_id);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return view;
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void speaker_list_api(String reqAction) {
        ApiUtility api = new ApiUtility(getActivity(), ApiUrl.BASE_URL, "", "Please Wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call < JsonObject > responseCall = apiRequest.speaker_list_api(reqAction);
        api.postRequest(responseCall, this, 1);

    }

    private void featured_video_list_api(String reqAction, String videotypeid) {
        ApiUtility api = new ApiUtility(getActivity(), ApiUrl.BASE_URL, "", "Please Wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call < JsonObject > responseCall = apiRequest.video_list_api(reqAction, videotypeid);
        api.postRequest(responseCall, this, 2);

    }

    private void event_list_api(String reqAction) {
        ApiUtility api = new ApiUtility(getActivity(), ApiUrl.BASE_URL, "", "Please Wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call < JsonObject > responseCall = apiRequest.event_list_api(reqAction);
        api.postRequest(responseCall, this, 3);

    }

    private void save_video_list_api(String reqAction, String userregid, String savedtypeflg, String savedid, String ip_address) {
        ApiUtility api = new ApiUtility(getActivity(), ApiUrl.BASE_URL, "", "Please Wait!", true);
        Retrofit retrofit = api.getRetrofitInstance();
        APIServices apiRequest = retrofit.create(APIServices.class);
        Call < JsonObject > responseCall = apiRequest.saved_data_api(reqAction, userregid, savedtypeflg,savedid,ip_address);
        api.postRequest(responseCall, this, 4);

    }

    public void onResponse ( JSONObject jsonObject, int request_code, int status_code ) {
        if (jsonObject != null) {
            if (request_code == 1) {
                try {
                    speaker_data=jsonObject.getJSONArray( "Content" );
                    SpeakerListAdapter speakerListAdapter=new SpeakerListAdapter( getActivity(), speaker_data );
                    features_speaker_rv.setLayoutManager( new LinearLayoutManager( getActivity(), LinearLayoutManager.HORIZONTAL, false ) );
                    features_speaker_rv.setAdapter( speakerListAdapter );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (request_code == 2) {
                try {
                    video_data = jsonObject.getJSONArray( "Content" );
                    VideoListAdapter videoListAdapter=new VideoListAdapter( getActivity(), video_data, new VideoListAdapter.MyAdapterListener() {
                        @Override
                        public void bannerOnClick ( View v, int position ) {
                            JSONObject jsonObject =null;
                            try {
                                jsonObject = video_data.getJSONObject(position);
                                String video_url = jsonObject.getString("video_url");
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(video_url));
                                startActivity(browserIntent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void saveOnClick ( View v, int position ) {
                            JSONObject jsonObject =null;
                            try {
                                jsonObject = video_data.getJSONObject(position);
                                String video_id = jsonObject.getString("video_id");
                                save_video_list_api("saved",userregid,"1",video_id,"158.58.10.20");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } );
                    features_videos_rv.setLayoutManager( new LinearLayoutManager( getActivity(), LinearLayoutManager.HORIZONTAL, false ) );
                    features_videos_rv.setAdapter( videoListAdapter );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (request_code == 3) {
                try {
                    Event_data=jsonObject.getJSONArray( "Content" );
                    RecentEventAdapter recentEventAdapter=new RecentEventAdapter( getActivity(), Event_data);
                    event_rv.setLayoutManager( new LinearLayoutManager( getActivity(), LinearLayoutManager.HORIZONTAL, false ) );
                    event_rv.setAdapter( recentEventAdapter );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if(request_code == 4){
                try {
                    String msg = jsonObject.getString("msg");
                    Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public void onFailure ( Object jsonObject, String error_msg ) {

        if (!error_msg.equalsIgnoreCase( "" )) {
            Toast.makeText( getActivity(), error_msg, Toast.LENGTH_LONG ).show();
        } else {
            try {
                JSONObject jo=new JSONObject( jsonObject.toString() );
                String response=jo.getString( "Error" );
                Toast.makeText( getActivity(), response, Toast.LENGTH_LONG ).show();
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        }
    }
}



