
package com.getgigradio.gigradio.model.soundcloud.track;

import android.os.Parcel;
import android.os.Parcelable;

import com.getgigradio.gigradio.GigRadioApp;
import com.getgigradio.gigradio.R;

public class Track implements Parcelable {

    private String artwork_url;
    private String attachments_uri;
    private String bpm;
    private Number comment_count;
    private boolean commentable;
    private String created_at;
    private String description;
    private Number download_count;
    private boolean downloadable;
    private Number duration;
    private String embeddable_by;
    private Number favoritings_count;
    private String genre;
    private Number id;
    private String isrc;
    private String key_signature;
    private String kind;
    private String label_id;
    private String label_name;
    private String license;
    private Number original_content_size;
    private String original_format;
    private String permalink;
    private String permalink_url;
    private Number playback_count;
    private String policy;
    private String purchase_title;
    private String purchase_url;
    private String release;
    private String release_day;
    private String release_month;
    private String release_year;
    private String sharing;
    private String state;
    private String stream_url;
    private boolean streamable;
    private String tag_list;
    private String title;
    private String track_type;
    private String uri;
    private User user;
    private Number user_id;
    private String video_url;
    private String waveform_url;

    public String getArtwork_url() {
        return artwork_url;
    }

    public String getAttachments_uri() {
        return attachments_uri;
    }

    public String getBpm() {
        return bpm;
    }

    public Number getComment_count() {
        return comment_count;
    }

    public boolean isCommentable() {
        return commentable;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getDescription() {
        return description;
    }

    public Number getDownload_count() {
        return download_count;
    }

    public boolean isDownloadable() {
        return downloadable;
    }

    public Number getDuration() {
        return duration;
    }

    public String getEmbeddable_by() {
        return embeddable_by;
    }

    public Number getFavoritings_count() {
        return favoritings_count;
    }

    public String getGenre() {
        return genre;
    }

    public Number getId() {
        return id;
    }

    public String getIsrc() {
        return isrc;
    }

    public String getKey_signature() {
        return key_signature;
    }

    public String getKind() {
        return kind;
    }

    public String getLabel_id() {
        return label_id;
    }

    public String getLabel_name() {
        return label_name;
    }

    public String getLicense() {
        return license;
    }

    public Number getOriginal_content_size() {
        return original_content_size;
    }

    public String getOriginal_format() {
        return original_format;
    }

    public String getPermalink() {
        return permalink;
    }

    public String getPermalink_url() {
        return permalink_url;
    }

    public Number getPlayback_count() {
        return playback_count;
    }

    public String getPolicy() {
        return policy;
    }

    public String getPurchase_title() {
        return purchase_title;
    }

    public String getPurchase_url() {
        return purchase_url;
    }

    public String getRelease() {
        return release;
    }

    public String getRelease_day() {
        return release_day;
    }

    public String getRelease_month() {
        return release_month;
    }

    public String getRelease_year() {
        return release_year;
    }

    public String getSharing() {
        return sharing;
    }

    public String getState() {
        return state;
    }

    public String getStream_urlWithApiKey() {
        return stream_url + "?client_id=" + GigRadioApp.getApplication().getString(R.string.soundcloud_api_key);
    }

    public boolean isStreamable() {
        return streamable;
    }

    public String getTag_list() {
        return tag_list;
    }

    public String getTitle() {
        return title;
    }

    public String getTrack_type() {
        return track_type;
    }

    public String getUri() {
        return uri;
    }

    public User getUser() {
        return user;
    }

    public Number getUser_id() {
        return user_id;
    }

    public String getVideo_url() {
        return video_url;
    }

    public String getWaveform_url() {
        return waveform_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.artwork_url);
        dest.writeString(this.attachments_uri);
        dest.writeString(this.bpm);
        dest.writeSerializable(this.comment_count);
        dest.writeByte(commentable ? (byte) 1 : (byte) 0);
        dest.writeString(this.created_at);
        dest.writeString(this.description);
        dest.writeSerializable(this.download_count);
        dest.writeByte(downloadable ? (byte) 1 : (byte) 0);
        dest.writeSerializable(this.duration);
        dest.writeString(this.embeddable_by);
        dest.writeSerializable(this.favoritings_count);
        dest.writeString(this.genre);
        dest.writeSerializable(this.id);
        dest.writeString(this.isrc);
        dest.writeString(this.key_signature);
        dest.writeString(this.kind);
        dest.writeString(this.label_id);
        dest.writeString(this.label_name);
        dest.writeString(this.license);
        dest.writeSerializable(this.original_content_size);
        dest.writeString(this.original_format);
        dest.writeString(this.permalink);
        dest.writeString(this.permalink_url);
        dest.writeSerializable(this.playback_count);
        dest.writeString(this.policy);
        dest.writeString(this.purchase_title);
        dest.writeString(this.purchase_url);
        dest.writeString(this.release);
        dest.writeString(this.release_day);
        dest.writeString(this.release_month);
        dest.writeString(this.release_year);
        dest.writeString(this.sharing);
        dest.writeString(this.state);
        dest.writeString(this.stream_url);
        dest.writeByte(streamable ? (byte) 1 : (byte) 0);
        dest.writeString(this.tag_list);
        dest.writeString(this.title);
        dest.writeString(this.track_type);
        dest.writeString(this.uri);
        dest.writeParcelable(this.user, flags);
        dest.writeSerializable(this.user_id);
        dest.writeString(this.video_url);
        dest.writeString(this.waveform_url);
    }

    public Track() {
    }

    private Track(Parcel in) {
        this.artwork_url = in.readString();
        this.attachments_uri = in.readString();
        this.bpm = in.readString();
        this.comment_count = (Number) in.readSerializable();
        this.commentable = in.readByte() != 0;
        this.created_at = in.readString();
        this.description = in.readString();
        this.download_count = (Number) in.readSerializable();
        this.downloadable = in.readByte() != 0;
        this.duration = (Number) in.readSerializable();
        this.embeddable_by = in.readString();
        this.favoritings_count = (Number) in.readSerializable();
        this.genre = in.readString();
        this.id = (Number) in.readSerializable();
        this.isrc = in.readString();
        this.key_signature = in.readString();
        this.kind = in.readString();
        this.label_id = in.readString();
        this.label_name = in.readString();
        this.license = in.readString();
        this.original_content_size = (Number) in.readSerializable();
        this.original_format = in.readString();
        this.permalink = in.readString();
        this.permalink_url = in.readString();
        this.playback_count = (Number) in.readSerializable();
        this.policy = in.readString();
        this.purchase_title = in.readString();
        this.purchase_url = in.readString();
        this.release = in.readString();
        this.release_day = in.readString();
        this.release_month = in.readString();
        this.release_year = in.readString();
        this.sharing = in.readString();
        this.state = in.readString();
        this.stream_url = in.readString();
        this.streamable = in.readByte() != 0;
        this.tag_list = in.readString();
        this.title = in.readString();
        this.track_type = in.readString();
        this.uri = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.user_id = (Number) in.readSerializable();
        this.video_url = in.readString();
        this.waveform_url = in.readString();
    }

    public static final Parcelable.Creator<Track> CREATOR = new Parcelable.Creator<Track>() {
        public Track createFromParcel(Parcel source) {
            return new Track(source);
        }

        public Track[] newArray(int size) {
            return new Track[size];
        }
    };
}
