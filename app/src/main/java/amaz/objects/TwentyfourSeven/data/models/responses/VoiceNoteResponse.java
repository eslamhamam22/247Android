package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import amaz.objects.TwentyfourSeven.data.models.VoiceNote;

public class VoiceNoteResponse extends BaseResponse{

    @SerializedName("data")
    @Expose
    private VoiceNote voiceNote;

    public VoiceNote getVoiceNote() {
        return voiceNote;
    }

    public void setVoiceNote(VoiceNote voiceNoteData) {
        this.voiceNote = voiceNoteData;
    }
}
