package amaz.objects.TwentyfourSeven.listeners;

import java.io.File;

public interface OnRecordVoiceListener {

    void startTimer();
    void saveRecord(File recordFile, int recordDuration, String recordDate);
    void updatePlayingUi(int currentPosition, boolean isRecordFinished);
}
