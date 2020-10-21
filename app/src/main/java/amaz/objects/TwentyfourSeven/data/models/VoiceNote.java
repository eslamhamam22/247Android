package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VoiceNote {

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("voicenote")
    @Expose
    private String voiceNote;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVoiceNote() {
        return voiceNote;
    }

    public void setVoiceNote(String voiceNote) {
        this.voiceNote = voiceNote;
    }
}
