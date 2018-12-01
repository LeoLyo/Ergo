package com.washedup.anagnosti.ergo.eventPerspective;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Person implements Parcelable {

    private String address;
    private String email;
    private String firstName;
    private String lastName;
    private String nickname;
    private String phoneNumber;
    private String profileImageUrl;
    private String superior;
    private String role;
    private Boolean invitation_accepted;
    private ArrayList<String> subordinates;
    private ArrayList<Obligation> obligations;
    private String status; // "free", "declined", "working", "pending_obligation","on_break";
    private int busy_obligation_count;
    private int breaks_today;
    private int breaks_whole;
    private String breakRequestId;
    private String privileges;

    public Person() {

    }

    public Person(String address, String email, String firstName, String lastName, String nickname, String phoneNumber, String profileImageUrl, String superior, String role, Boolean invitation_accepted, ArrayList<String> subordinates) {
        this.address = address;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
        this.superior = superior;
        this.role = role;
        this.invitation_accepted = invitation_accepted;
        this.subordinates = subordinates;
        this.obligations = new ArrayList<>();
        this.status = "free";
        this.busy_obligation_count = 0;
        this.breaks_today = 0;
        this.breaks_whole = 0;
        this.breakRequestId = "no_request_yet";
        privileges = "basic";
    }

    protected Person(Parcel in) {
        address = in.readString();
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        nickname = in.readString();
        phoneNumber = in.readString();
        profileImageUrl = in.readString();
        superior = in.readString();
        role = in.readString();
        byte tmpInvitation_accepted = in.readByte();
        invitation_accepted = tmpInvitation_accepted == 0 ? null : tmpInvitation_accepted == 1;
        subordinates = in.createStringArrayList();
        obligations = in.createTypedArrayList(Obligation.CREATOR);
        status = in.readString();
        busy_obligation_count = in.readInt();
        breaks_today = in.readInt();
        breaks_whole = in.readInt();
        breakRequestId = in.readString();
        privileges = in.readString();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };


    public String getPrivileges() {
        return privileges;
    }

    public void setPrivileges(String privileges) {
        this.privileges = privileges;
    }

    public String getBreakRequestId() {
        return breakRequestId;
    }

    public void setBreakRequestId(String breakRequestId) {
        this.breakRequestId = breakRequestId;
    }

    public int getBreaks_today() {
        return breaks_today;
    }

    public void setBreaks_today(int breaks_today) {
        this.breaks_today = breaks_today;
    }

    public int getBreaks_whole() {
        return breaks_whole;
    }

    public void setBreaks_whole(int breaks_whole) {
        this.breaks_whole = breaks_whole;
    }

    public int getBusy_obligation_count() {
        return busy_obligation_count;
    }

    public void setBusy_obligation_count(int busy_obligation_count) {
        this.busy_obligation_count = busy_obligation_count;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getSuperior() {
        return superior;
    }

    public void setSuperior(String superior) {
        this.superior = superior;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getInvitation_accepted() {
        return invitation_accepted;
    }

    public void setInvitation_accepted(Boolean invitation_accepted) {
        this.invitation_accepted = invitation_accepted;
    }

    public ArrayList<String> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(ArrayList<String> subordinates) {
        this.subordinates = subordinates;
    }

    public ArrayList<Obligation> getObligations() {
        return obligations;
    }

    public void setObligations(ArrayList<Obligation> obligations) {
        this.obligations = obligations;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Person{" +
                "address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", superior='" + superior + '\'' +
                ", role='" + role + '\'' +
                ", invitation_accepted=" + invitation_accepted +
                ", subordinates=" + subordinates +
                ", obligations=" + obligations +
                ", status='" + status + '\'' +
                ", busy_obligation_count=" + busy_obligation_count +
                ", breaks_today=" + breaks_today +
                ", breaks_whole=" + breaks_whole +
                ", breakRequestId='" + breakRequestId + '\'' +
                ", privileges='" + privileges + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(address);
        parcel.writeString(email);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(nickname);
        parcel.writeString(phoneNumber);
        parcel.writeString(profileImageUrl);
        parcel.writeString(superior);
        parcel.writeString(role);
        parcel.writeByte((byte) (invitation_accepted == null ? 0 : invitation_accepted ? 1 : 2));
        parcel.writeStringList(subordinates);
        parcel.writeTypedList(obligations);
        parcel.writeString(status);
        parcel.writeInt(busy_obligation_count);
        parcel.writeInt(breaks_today);
        parcel.writeInt(breaks_whole);
        parcel.writeString(breakRequestId);
        parcel.writeString(privileges);
    }
}
