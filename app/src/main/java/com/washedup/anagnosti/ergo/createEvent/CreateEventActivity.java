package com.washedup.anagnosti.ergo.createEvent;

import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.washedup.anagnosti.ergo.R;

import java.util.ArrayList;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity implements SliderInfoFragment.OnFragmentInteractionListener, SliderLinksFragment.OnFragmentInteractionListener, SliderDaysFragment.OnFragmentInteractionListener, SliderRolesFragment.OnFragmentInteractionListener, SliderPeopleFragment.OnFragmentInteractionListener {


    private LinearLayout create_event_linlay;

    private TextView[] mDots;

    private Button create_event_prevb, create_event_nextb;
    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        final ViewPager create_event_vp = findViewById(R.id.create_event_vp);
        create_event_linlay = findViewById(R.id.create_event_linlay1);

        create_event_prevb = findViewById(R.id.create_event_prevb);
        create_event_nextb = findViewById(R.id.create_event_nextb);


        List<String> pages = new ArrayList<>();
        pages.add("Page 1");
        pages.add("Page 2");
        pages.add("Page 3");
        pages.add("Page 4");
        pages.add("Page 5");

        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), pages.size());
        create_event_vp.setAdapter(adapter);

        /*sliderAdapter = new SliderAdapter(this);

        create_event_vp.setAdapter(sliderAdapter);*/

        addDotsIndicator(0);

        create_event_vp.addOnPageChangeListener(viewListener);

        create_event_nextb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String compBtn=create_event_nextb.getText().toString();
                if(compBtn.matches("Finish")){
                    Toast.makeText(CreateEventActivity.this, "FINISHED", Toast.LENGTH_SHORT).show();
                }

                create_event_vp.setCurrentItem(mCurrentPage+1);
            }
        });
        create_event_prevb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_event_vp.setCurrentItem(mCurrentPage-1);
            }
        });
    }

    public void addDotsIndicator(int position){
        mDots = new TextView[5];
        create_event_linlay.removeAllViews();

        for(int i=0;i< mDots.length;i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorAccent));

            create_event_linlay.addView(mDots[i]);
        }
        if(mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.dirtyWhite));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);

            mCurrentPage=position;

            if(position==0){
                create_event_nextb.setEnabled(true);
                create_event_prevb.setEnabled(false);
                create_event_prevb.setVisibility(View.INVISIBLE);

                create_event_nextb.setText(R.string.create_event_nextb);
                create_event_prevb.setText("");

            } else if(position==mDots.length-1){
                create_event_nextb.setEnabled(true);
                create_event_prevb.setEnabled(true);
                create_event_prevb.setVisibility(View.VISIBLE);

                create_event_nextb.setText(R.string.create_event_finishb);
                create_event_prevb.setText(R.string.create_event_prevb);

            }else{
                create_event_nextb.setEnabled(true);
                create_event_prevb.setEnabled(true);
                create_event_prevb.setVisibility(View.VISIBLE);

                create_event_nextb.setText(R.string.create_event_nextb);
                create_event_prevb.setText(R.string.create_event_prevb);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public int getmCurrentPage() {
        return mCurrentPage;
    }
}
