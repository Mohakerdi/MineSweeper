package mysweeper;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundEff {
   public static void playSound(String soundFile) {
    try {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(SoundEff.class.getResource(soundFile));
        AudioFormat format = audioStream.getFormat();
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
        
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        e.printStackTrace();
    }

}
}
