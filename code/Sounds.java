import java.applet.Applet;
import java.applet.AudioClip;

public class Sounds {
    public static final AudioClip theme = Applet.newAudioClip(Sounds.class.getResource("music.wav"));
    public static final AudioClip mode = Applet.newAudioClip(Sounds.class.getResource("mode.wav"));
    public static final AudioClip shoot = Applet.newAudioClip(Sounds.class.getResource("laser_shot.wav"));

}