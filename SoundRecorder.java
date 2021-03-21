import javax.sound.sampled.*;
import java.io.*;

public class SoundRecorder {
    private TargetDataLine line;

    public SoundRecorder(File wavFile, long duration) throws LineUnavailableException, IOException{
        new Thread(() -> {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException ignore) {}
            line.stop();
            line.close();
            System.out.println("Finished");
        }).start();

        record(wavFile);
    }

    private void record(File wavFile) throws LineUnavailableException, IOException {
        AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();

        // remove the old file if exists
        wavFile.delete();
        System.out.println("Start capturing...");
        AudioInputStream ais = new AudioInputStream(line);

        System.out.println("Start recording...");
        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, wavFile);
    }

    public static void main(String[] args) throws LineUnavailableException, IOException{
        new SoundRecorder(new File(System.getProperty("user.home") + "/Desktop/test.wav"), 5000);
    }
}