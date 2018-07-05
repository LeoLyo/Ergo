package com.washedup.anagnosti.ergo.createEvent;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Date;

public class CESingleton {

    private static CESingleton mInstance = null;
    public ArrayList<CEDay> mCEDays;
    public ArrayList<CERole> mCERoles;
    public ArrayList<CEPerson> mCEPeople;
    public ArrayList<Date> dates;
    public ArrayList<String> mUsedEmails;
    public boolean[] somethingDoneInEveryPart;
    public Uri uriEventImage;
    public long currentNumberOfDays;
    public boolean currentEventDaysChanged;
    public boolean dateStartChanged;
    public boolean dateEndChanged;
    public String locationName;
    public String locationAddress;
    public GeoPoint locationCoordinates;
    public String eventName;
    public String organiserName;
    public String descriptionOfEvent;

    private CESingleton(){
        mCEDays = new ArrayList<>();
        mCERoles= new ArrayList<>();
        mCEPeople= new ArrayList<>();
        mUsedEmails = new ArrayList<>();
        dates = new ArrayList<>();
        somethingDoneInEveryPart = new boolean[5];
        currentNumberOfDays=-7;
        currentEventDaysChanged=false;
        dateStartChanged=false;
        dateEndChanged=false;
        eventName="";
        organiserName="";
        descriptionOfEvent="";
        locationName="";
        locationAddress="";
        locationCoordinates=new GeoPoint(0,0);

    }
    public static CESingleton Instance(){
        if(mInstance==null){
            mInstance=new CESingleton();
        }
        return mInstance;
    }

    public void destroyS(){
        //mInstance=null;
        mCEDays.clear();
        mCERoles.clear();
        mCEPeople.clear();
        mUsedEmails.clear();
        dates.clear();
        somethingDoneInEveryPart=new boolean[0];
        currentNumberOfDays=-7;
        currentEventDaysChanged=false;
        dateStartChanged=false;
        dateEndChanged=false;
        mInstance=null;
        eventName="";
        organiserName="";
        descriptionOfEvent="";
        locationName="";
        locationAddress="";
        locationCoordinates=new GeoPoint(0,0);

    }
}
