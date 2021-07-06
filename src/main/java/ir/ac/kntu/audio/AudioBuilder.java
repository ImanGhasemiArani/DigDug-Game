package ir.ac.kntu.audio;

import ir.ac.kntu.gamedata.GameData;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioBuilder {

    private static File selectingOption = new File("src/main/resources/audios/1.wav");
    private static File countDownTimer = new File("src/main/resources/audios/4.wav");
    private static File useRandom = new File("src/main/resources/audios/coin.wav");
    private static File winPlayer = new File("src/main/resources/audios/done.wav");
    private static File diePlayer = new File("src/main/resources/audios/die.wav");
    private static File killEnemy = new File("src/main/resources/audios/kill.wav");
    private static File inflatingEnemy = new File("src/main/resources/audios/inflating.wav");
    private static File theme = new File("src/main/resources/audios/theme.wav");
    private static AudioInputStream audioInputStream;
    private static Clip themeAudio;


    public static void playSelectingOptionAudio() {
        playSound(selectingOption);
    }

    public static void playCountDownTimerAudio() {
        playSound(countDownTimer);
    }

    public static void playUseRandomAudio() {
        playSound(useRandom);
    }

    public static void playDiePlayerAudio() {
        playSound(diePlayer);
    }

    public static void playWinPlayerAudio() {
        playSound(winPlayer);
    }

    public static void playKillEnemyAudio() {
        playSound(killEnemy);
    }

    public static void playInflatingEnemyAudio() {
        playSound(inflatingEnemy);
    }

    public static void playThemeAudio() {
        try {
            themeAudio = AudioSystem.getClip();
            themeAudio.open(AudioSystem.getAudioInputStream(theme));
            if (GameData.isPlaySound()) {
//                themeAudio.start();
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ignored) {
        }
    }

    private static void playSound(File soundFile) {
        try {
            Clip audio = AudioSystem.getClip();
            audio.open(AudioSystem.getAudioInputStream(soundFile));
            if (GameData.isPlaySound()) {
//                audio.start();
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ignored) {
        }
    }

    public static void stopSound() {
        themeAudio.stop();
    }
}
