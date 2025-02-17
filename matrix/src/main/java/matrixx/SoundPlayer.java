package matrixx;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer {

    public static void playSound(String soundFile) {
        AudioInputStream audioInputStream = null;
        SourceDataLine sourceDataLine = null;
        try {
            // الحصول على الملف الصوتي
            File file = new File(soundFile);

            // التحقق من وجود الملف قبل محاولة تشغيله
            if (file.exists()) {
                // محاولة الحصول على AudioInputStream من الملف الصوتي
                audioInputStream = AudioSystem.getAudioInputStream(file);

                // الحصول على التنسيق الحالي
                AudioFormat format = audioInputStream.getFormat();
                System.out.println("Original Audio Format: " + format);

                // تحويل معدل العينة إلى 44.1 kHz إذا كان مختلفًا
                if (format.getSampleRate() != 44100.0f) {
                    AudioFormat targetFormat = new AudioFormat(
                            AudioFormat.Encoding.PCM_SIGNED,
                            44100.0f, // 44.1 kHz
                            16, // 16 bit
                            format.getChannels(),
                            format.getChannels() * 2,
                            44100.0f, // 44.1 kHz
                            false);      // little-endian
                    audioInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
                    System.out.println("Converted Audio Format: " + targetFormat);
                }

                // الحصول على مصدر بيانات الصوت
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioInputStream.getFormat());
                sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);

                // فتح مصدر البيانات
                sourceDataLine.open(audioInputStream.getFormat());
                sourceDataLine.start();

                // قراءة البيانات الصوتية وكتابتها إلى SourceDataLine
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
                    sourceDataLine.write(buffer, 0, bytesRead);
                }

                // إيقاف الصوت بعد الانتهاء
                sourceDataLine.drain();
                sourceDataLine.stop();
            } else {
                System.out.println("File not found: " + file.getAbsolutePath());
            }
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Unsupported audio format for file: " + soundFile);
        } catch (IOException e) {
            System.out.println("Error reading the audio file: " + soundFile);
        } catch (LineUnavailableException e) {
            System.out.println("Audio line unavailable for file: " + soundFile);
        } catch (IllegalArgumentException e) {
            System.out.println("Illegal argument exception, audio data might be corrupted or invalid.");
        } finally {
            // التأكد من غلق الموارد
            try {
                if (audioInputStream != null) {
                    audioInputStream.close();
                }
                if (sourceDataLine != null) {
                    sourceDataLine.close();
                }
            } catch (IOException e) {
                System.out.println("Error closing resources.");
            }
        }
    }
}
