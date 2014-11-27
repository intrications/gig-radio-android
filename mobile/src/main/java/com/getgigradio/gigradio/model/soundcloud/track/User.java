
package com.getgigradio.gigradio.model.soundcloud.track;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String avatar_url;
    private Number id;
    private String kind;
    private String permalink;
    private String permalink_url;
    private String uri;
    private String username;

    public String getAvatar_url() {
        return avatar_url;
    }

    public Number getId() {
        return id;
    }

    public String getKind() {
        return kind;
    }

    public String getPermalink() {
        return permalink;
    }

    public String getPermalink_url() {
        return permalink_url;
    }

    public String getUri() {
        return uri;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.avatar_url);
        dest.writeSerializable(this.id);
        dest.writeString(this.kind);
        dest.writeString(this.permalink);
        dest.writeString(this.permalink_url);
        dest.writeString(this.uri);
        dest.writeString(this.username);
    }

    public User() {
    }

    private User(Parcel in) {
        this.avatar_url = in.readString();
        this.id = (Number) in.readSerializable();
        this.kind = in.readString();
        this.permalink = in.readString();
        this.permalink_url = in.readString();
        this.uri = in.readString();
        this.username = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
