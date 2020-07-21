package com.himanshu.mynotes.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.himanshu.mynotes.EditProfileActivity;
import com.himanshu.mynotes.R;
import com.himanshu.mynotes.adapter.ProfileItemAdapter;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private TextView ProfileName, ProfileEmail;
    private ImageView ProfileImage;
    private Button EditProfileBtn;
    private RecyclerView recyclerView;
    private CardView ProfileCardView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ProfileName = view.findViewById(R.id.profile_name);
        ProfileEmail = view.findViewById(R.id.profile_email);
        ProfileImage = view.findViewById(R.id.profile_image);
        ProfileCardView = view.findViewById(R.id.profile_image_card_view);

        String[] title = {"Pin","Archive","Delete", "About"};
        int[] image = {R.drawable.pin_icon,
                R.drawable.archive_icon,
                R.drawable.delete_icon,
                R.drawable.ic_about};
        recyclerView = view.findViewById(R.id.profile_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ProfileItemAdapter(getContext(), title, image));

        EditProfileBtn = view.findViewById(R.id.profile_edit);
        EditProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), EditProfileActivity.class);
                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View, String>(ProfileCardView, "card_transition");
                pairs[1] = new Pair<View, String>(ProfileName, "name_transition");
                pairs[2] = new Pair<View, String>(EditProfileBtn, "title_transition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
                startActivity(i, options.toBundle());
            }
        });


        retrieveUserInfo();

        return view;

    }

    private void retrieveUserInfo() {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("userDetails")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ProfileName.setText(snapshot.child("name").getValue().toString());
                    ProfileEmail.setText(snapshot.child("emailId").getValue().toString());
                    Picasso.with(getActivity()).load(snapshot.child("photoUrl").getValue().toString())
                            .placeholder(R.drawable.profilemale).into(ProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}