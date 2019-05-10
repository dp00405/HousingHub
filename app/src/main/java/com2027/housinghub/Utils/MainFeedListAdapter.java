package com2027.housinghub.Utils;
/**
 * This class will help the list of items be displayed on the main feed
 */

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import com2027.housinghub.Models.House;
import com2027.housinghub.Models.User;
import com2027.housinghub.Models.UserAccount;
import com2027.housinghub.R;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 *
 */
public class MainFeedListAdapter extends ArrayAdapter<House> {
    private static final String TAG = "MainFeedListAdapter";

    private LayoutInflater mInflater;
    private int mLayoutResorce;
    private Context mContext;
    private String currentUsername = "";





    public MainFeedListAdapter(@NonNull Context context, int resource, @NonNull List<House> objects) {
        super(context, resource, objects);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutResorce = resource;
        this.mContext = context;



    }

    static class ViewHolder{
        CircleImageView mprofileImage;
        TextView username, title, timeframe, tags;
        SquareImageView image;

        UserAccount settings = new UserAccount();
        User user = new User();
        StringBuilder users;
        House house;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(mLayoutResorce, parent, false);
            holder = new ViewHolder();

            holder.username = (TextView) convertView.findViewById(R.id.username);
            holder.image = (SquareImageView) convertView.findViewById(R.id.post_image);
            holder.title = (TextView) convertView.findViewById(R.id.titleHouse);
            holder.timeframe = (TextView) convertView.findViewById(R.id.timeFrame);
            holder.tags = (TextView) convertView.findViewById(R.id.houseTags);
            holder.mprofileImage = (CircleImageView) convertView.findViewById(R.id.profile_picture);

            holder.house = getItem(position);
            holder.users = new StringBuilder();

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        //get the username
        getCurrentUsername();


        return convertView;
    }

    private void getCurrentUsername(){
        Log.d(TAG, "getCurrentUsername: retrieving user account settings");
        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        //this method woll get current username
    }


    //INCOMPLETE
}
