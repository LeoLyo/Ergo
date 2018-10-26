package com.washedup.anagnosti.ergo.createEvent;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.washedup.anagnosti.ergo.R;
import com.washedup.anagnosti.ergo.authentication.YHomeActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class CreateEventActivity extends AppCompatActivity implements SliderInfoFragment.OnFragmentInteractionListener, SliderLinksFragment.OnFragmentInteractionListener, SliderDaysFragment.OnFragmentInteractionListener, SliderRolesFragment.OnFragmentInteractionListener, SliderPeopleFragment.OnFragmentInteractionListener {

    private static final String TAG = "CreateEventActivity";

    private LinearLayout create_event_linlay;

    private TextView[] mDots;

    private Button create_event_prevb, create_event_nextb;
    private int mCurrentPage;

    private CESingleton singleton = CESingleton.Instance();
    private FirebaseFirestore db;


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
                String compBtn = create_event_nextb.getText().toString();
                //Toast.makeText(CreateEventActivity.this, "SingletonCheckDays: " + singleton.mCEDays.size(), Toast.LENGTH_SHORT).show();
                for(int w=0;w<singleton.mCEDays.size();w++){
                    //Toast.makeText(CreateEventActivity.this, "Every day check: " + singleton.mCEDays.get(w).toString(), Toast.LENGTH_SHORT).show();
                }
                String allz = "";
                for(int z=0;z<singleton.somethingDoneInEveryPart.length;z++){
                    allz+=singleton.somethingDoneInEveryPart[z] + ", ";
                }
                //Toast.makeText(CreateEventActivity.this, "[JEB]: " + allz, Toast.LENGTH_SHORT).show();
                if (compBtn.matches("Finish")) {
                   //Toast.makeText(CreateEventActivity.this, "FINISHED", Toast.LENGTH_SHORT).show();
                    boolean everythingOk=true;
                    String notFilledParts="";
                    for(int i=0;i<singleton.somethingDoneInEveryPart.length;i++){
                        if(!singleton.somethingDoneInEveryPart[i]){
                            everythingOk=false;
                            notFilledParts=notFilledParts+(i+1)+", ";
                        }

                    }
                    if(everythingOk){
                        final Dialog popUpDialog = new Dialog(CreateEventActivity.this);
                        popUpDialog.setContentView(R.layout.pop_up_dialog_create_event);
                        TextView eventName = popUpDialog.findViewById(R.id.create_event_pop_up_dialog_event_name);
                        Button createButton = popUpDialog.findViewById(R.id.create_event_pop_up_dialog_button);
                        CardView cardView = popUpDialog.findViewById(R.id.create_event_pop_up_dialog_card_view);
                        ImageView imageView = popUpDialog.findViewById(R.id.create_event_pop_up_dialog_image_view);
                        final ProgressBar progressBar = popUpDialog.findViewById(R.id.create_event_pop_up_dialog_pb);
                        if(singleton.uriEventImage!=null){
                            try {

                                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), singleton.uriEventImage);

                                Bitmap drawableBitmap = bm.copy(Bitmap.Config.ARGB_8888, true);

                                Bitmap background = Bitmap.createBitmap(200 , 200,Bitmap.Config.ARGB_8888);

                                float originalWidth = drawableBitmap.getWidth();
                                float originalHeight = drawableBitmap.getHeight();

                                Canvas canvas = new Canvas(background);

                                float scale = 200 / originalWidth;

                                float xTranslation = 0.0f;
                                float yTranslation = (200 - originalHeight * scale) / 2.0f;

                                Matrix transformation = new Matrix();
                                transformation.postTranslate(xTranslation,yTranslation);
                                transformation.preScale(scale,scale);

                                Paint paint = new Paint();
                                paint.setFilterBitmap(true);

                                canvas.drawBitmap(drawableBitmap, transformation, paint);
                                canvas.setBitmap(drawableBitmap);

                                Bitmap darkened = darkenBitMap(drawableBitmap);
                                RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(getResources(),darkened);
                                dr.setCornerRadius(16);
                                imageView.setBackground(dr);
                                cardView.setCardElevation(0);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            /*File f = new File(getRealPathFromURI(singleton.uriEventImage));
                            if(f.exists()) {
                                Drawable d = Drawable.createFromPath(f.getAbsolutePath());
                                innerLayout.setBackground(d);
                            }*/
                        }
                        eventName.setText(singleton.eventName);
                        createButton.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View view) {
                                progressBar.setVisibility(View.VISIBLE);
                                uploadAndCreateEvent(popUpDialog,progressBar);
                            }
                        });
                        Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        popUpDialog.show();
                    }else{
                        notFilledParts="Parts "+notFilledParts;
                        notFilledParts=notFilledParts.substring(0,notFilledParts.length()-2);
                        notFilledParts+=" have not been filled. Please refer to the mentioned parts and complete the inputs.";
                        Toast.makeText(CreateEventActivity.this, notFilledParts, Toast.LENGTH_LONG).show();
                    }
                }

                create_event_vp.setCurrentItem(mCurrentPage + 1);
            }
        });
        create_event_prevb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_event_vp.setCurrentItem(mCurrentPage - 1);
            }
        });
    }



    private Bitmap darkenBitMap(Bitmap bm) {

        Canvas canvas = new Canvas(bm);
        Paint p = new Paint(Color.RED);
        //ColorFilter filter = new LightingColorFilter(0xFFFFFFFF , 0x00222222); // lighten
        ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000);    // darken
        p.setColorFilter(filter);
        canvas.drawBitmap(bm, new Matrix(), p);

        return bm;
    }

    //StackOverflow code
    /*
    * ZAJEBAVA DEO SA PRIKAZIVANJEM SLIKE U POZADINI KAD SE POTVRDJUJE KREIRANJE EVENT-A
    *
    *
    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }

    }*/

    public void uploadAndCreateEvent(final Dialog popUpDialog, final ProgressBar progressBar){
        db=FirebaseFirestore.getInstance();
        final Map<String, Object> event = new HashMap<>();
        event.put("event_name", singleton.eventName);
        event.put("organiser_name",singleton.organiserName);
        event.put("description_of_event",singleton.descriptionOfEvent);
        event.put("location_coordinates",singleton.locationCoordinates);
        event.put("location_address",singleton.locationAddress);
        event.put("location_name",singleton.locationName);
        event.put("emails_of_people", singleton.mUsedEmails);

        db.collection("events").add(event).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(final DocumentReference documentReference) {
                Log.d(TAG,"Event successfully added! "+documentReference.getId());

                final String eventId = documentReference.getId();
                final String timeOfUpload = String.valueOf(System.currentTimeMillis());

                //Adding event picture to storage

                String path = "events/"+eventId+"/event_images/current_event_image/"+singleton.eventName+timeOfUpload+".jpg";
                String path1 = "events/"+singleton.eventName+timeOfUpload+".jpg";
                String path2 = "events/testing1.jpg";
                Toast.makeText(CreateEventActivity.this, "PATH : " + path, Toast.LENGTH_SHORT).show();
                Toast.makeText(CreateEventActivity.this, "PATH 1: " + path1, Toast.LENGTH_SHORT).show();
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference eventImageRef = storageRef.child(path2);
                Toast.makeText(CreateEventActivity.this, "AFSFA: " + singleton.uriEventImage, Toast.LENGTH_SHORT).show();
                if(singleton.uriEventImage!=null){
                    eventImageRef.putFile(singleton.uriEventImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String eventImageName = singleton.eventName + timeOfUpload;
                            DocumentReference thisEvent = db.collection("events").document(eventId);
                            thisEvent.update("current_event_image",eventImageName).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Event picture successfully updated!");
                                    Toast.makeText(CreateEventActivity.this, "Event image added!", Toast.LENGTH_SHORT).show();



                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating event picture", e);
                                    Toast.makeText(CreateEventActivity.this, "Error adding event image :(", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(CreateEventActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //Adding days to event

                for(int i=0;i<singleton.mCEDays.size();i++){
                    Map<String,Object> day = new HashMap<>();
                    day.put("date",singleton.mCEDays.get(i).getDate());
                    day.put("time_start",singleton.mCEDays.get(i).getTimeStart());
                    day.put("time_end",singleton.mCEDays.get(i).getTimeEnd());
                    db.collection("events").document(eventId).collection("days").add(day).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(CreateEventActivity.this, "Days successfully added", Toast.LENGTH_SHORT).show();
                            Log.d(TAG,"Day successfully added into event! "+documentReference.getId());

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateEventActivity.this, "Error adding days", Toast.LENGTH_SHORT).show();

                            Log.w(TAG,"Error adding day.",e);

                        }
                    });
                }


                //Adding roles to event

                for(int j=0;j<singleton.mCERoles.size();j++){
                    Map<String,Object> role = new HashMap<>();
                    role.put("title",singleton.mCERoles.get(j).getName());
                    role.put("description",singleton.mCERoles.get(j).getDescription());
                    ArrayList<String> subs = new ArrayList<>();
                    if(!singleton.mCERoles.get(j).getSubordinates().isEmpty()){
                        for(int h=0;h<singleton.mCERoles.get(j).getSubordinates().size();h++){
                            subs.add(singleton.mCERoles.get(j).getSubordinates().get(h).getName());
                        }
                    }else{
                        subs.add("None");
                    }
                    role.put("subordinates",subs);
                    db.collection("events").document(eventId).collection("roles").add(role).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG,"Role successfully added into event! "+documentReference.getId());
                            Toast.makeText(CreateEventActivity.this, "Roles successfully added", Toast.LENGTH_SHORT).show();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG,"Error adding role.",e);
                            Toast.makeText(CreateEventActivity.this, "Error adding roles", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                //Adding people to event

                for(int z=0;z<singleton.mCEPeople.size();z++){
                    Map<String,Object> person = new HashMap<>();
                    person.put("boss",singleton.mCEPeople.get(z).getParentOfIndividual().getEmail());
                    // person.put("email",singleton.mCEPeople.get(z).getEmail());
                    person.put("role",singleton.mCEPeople.get(z).getRoleOfIndividual().getName());
                    person.put("invitation_accepted",false);
                    db.collection("events").document(eventId).collection("people").document(singleton.mCEPeople.get(z).getEmail()).set(person)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(CreateEventActivity.this, "People successfully added", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG,"Person successfully added into event! "+documentReference.getId());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateEventActivity.this, "Error adding people", Toast.LENGTH_SHORT).show();
                        }
                    });

                   /* db.collection("events").document(eventId).collection("people").add(person).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG,"Person successfully added into event! "+documentReference.getId());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });*/
                }

                //Toast.makeText(CreateEventActivity.this, "Successfully added the event! :D", Toast.LENGTH_SHORT).show();
                Toast.makeText(CreateEventActivity.this, "Congratulations! Event has been successfully added!", Toast.LENGTH_SHORT).show();
                popUpDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(CreateEventActivity.this, YHomeActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG,"Error adding event.",e);
                //Toast.makeText(CreateEventActivity.this, "Error: something went wrong with adding the event. :(", Toast.LENGTH_SHORT).show();
            }
        });

    }


    //NOT TESTED YET
    /*public Drawable getDimmedDrawable(Drawable drawable) {
        Resources resources = this.getResources();
        StateListDrawable stateListDrawable = new StateListDrawable();
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{
                drawable,
                new ColorDrawable(resources.getColor(R.color.translucent_black))
        });
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, layerDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_focused}, layerDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, layerDrawable);
        stateListDrawable.addState(new int[]{}, drawable);
        return stateListDrawable;
    }*/


    public void addDotsIndicator(int position) {
        mDots = new TextView[5];
        create_event_linlay.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorAccent));

            create_event_linlay.addView(mDots[i]);
        }
        if (mDots.length > 0) {
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

            mCurrentPage = position;

            if (position == 0) {
                create_event_nextb.setEnabled(true);
                create_event_prevb.setEnabled(false);
                create_event_prevb.setVisibility(View.INVISIBLE);

                create_event_nextb.setText(R.string.create_event_nextb);
                create_event_prevb.setText("");

            } else if (position == mDots.length - 1) {
                create_event_nextb.setEnabled(true);
                create_event_prevb.setEnabled(true);
                create_event_prevb.setVisibility(View.VISIBLE);

                create_event_nextb.setText(R.string.create_event_finishb);
                create_event_prevb.setText(R.string.create_event_prevb);

            } else {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        singleton.destroyS();
    }

    public int getmCurrentPage() {
        return mCurrentPage;
    }
}
