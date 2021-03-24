import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class SoundRecorder {
    private static String RECORD = "o Record";
    private static String STOP = "[ ] Stop";
    private TargetDataLine line;

    public SoundRecorder(File wavFile) {
        SwingUtilities.invokeLater(() -> {
            JDialog dialog = new JDialog();
            JButton button = new JButton(RECORD);
            JLabel label = new JLabel("Saving recording to: " + wavFile);
            button.addActionListener(e -> {
                if(button.getText().equals(RECORD)) {
                    button.setText(STOP);
                    new Thread(() -> {
                        try {
                            record(wavFile);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }).start();
                } else {
                    button.setText(RECORD);
                    line.stop();
                    line.close();
                }
            });

            JPanel panel = new JPanel(new FlowLayout());
            panel.add(button);
            panel.add(label);
            dialog.setContentPane(panel);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        });
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
        new SoundRecorder(new File(System.getProperty("user.home") + "/Desktop/test.wav"));
    }
}