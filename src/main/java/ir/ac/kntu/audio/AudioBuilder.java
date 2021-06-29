package ir.ac.kntu.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioBuilder {

    private static File selectingOption = new File("src/main/resources/audios/1.wav");
    private static File countDownTimer = new File("src/main/resources/audios/4.wav");
    private static AudioInputStream audioInputStream;
    private static Clip audio;


    public static void playSelectingOptionAudio() {
        playSound(selectingOption);
    }

    public static void playCountDownTimerAudio() {
        playSound(countDownTimer);
    }

    private static void playSound(File soundFile) {
        try {
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            audio = AudioSystem.getClip();
            audio.open(audioInputStream);
//            audio.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ignored) {
        }
    }
}
