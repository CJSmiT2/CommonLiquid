package ua.org.smit.commontlx.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

public class FileCms extends File {

    private static final String[] VIDEO_EXTENSIONS = {"mp4", "avi"};
    private static final String[] AUDIO_EXTENSIONS = {"mp3", "aac", "ac3", "ogg"};
    private static final String[] IMAGE_EXTENSIONS = {"jpeg", "jpg", "png", "avif"};
    private static final String[] TXT_EXTENSIONS = {"txt", "xml"};

    private FileType type;

    public static List<FileCms> convert(List<File> files) {
        List<FileCms> filesTlx = new ArrayList<>();
        for (File file : files) {
            filesTlx.add(new FileCms(file));
        }
        return filesTlx;
    }

    public FileCms(String path) {
        super(path);
//        defineType();
        valid(this);
    }

    public FileCms(File file) {
        super(file.getAbsolutePath());
//        defineType();
        valid(this);
    }

    public FileType getType() {
        return type;
    }

    public String getExtension() {
        String name = this.getName();
        if (name.lastIndexOf(".") != -1 && name.lastIndexOf(".") != 0) {
            return name.substring(name.lastIndexOf(".") + 1);
        } else {
            throw new RuntimeException("Extension not found in file name! " + name);
        }
    }

    public void rename(String nameTo) {
        File dest = new File(this.getParent() + File.separator + nameTo);
        moveTo(dest);
    }

    public String getNameWithoutExtension() {
        return this.getName().replaceFirst("[.][^.]+$", "");
    }

    public byte[] getBytes() throws FileNotFoundException, IOException {
        InputStream inputStream = new FileInputStream(this);
        return IOUtils.toByteArray(inputStream);
    }

    public void defineType() {
        String extension = this.getExtension().toLowerCase();

        for (int i = 0; i < VIDEO_EXTENSIONS.length; i++) {
            if (VIDEO_EXTENSIONS[i].equals(extension)) {
                this.type = FileType.VIDEO;
                return;
            }
        }

        for (int i = 0; i < AUDIO_EXTENSIONS.length; i++) {
            if (AUDIO_EXTENSIONS[i].equals(extension)) {
                this.type = FileType.AUDIO;
                return;
            }
        }

        for (int i = 0; i < IMAGE_EXTENSIONS.length; i++) {
            if (IMAGE_EXTENSIONS[i].equals(extension)) {
                this.type = FileType.IMAGE;
                return;
            }
        }

        for (int i = 0; i < TXT_EXTENSIONS.length; i++) {
            if (TXT_EXTENSIONS[i].equals(extension)) {
                this.type = FileType.TXT;
                return;
            }
        }
    }

    public void moveTo(File dest) {
        try {
            File src = this.getAbsoluteFile();
            OutputStream outStream = null;
            try ( InputStream inStream = new FileInputStream(src)) {
                outStream = new FileOutputStream(dest);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inStream.read(buffer)) > 0) {
                    outStream.write(buffer, 0, length);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FileCms.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FileCms.class.getName()).log(Level.SEVERE, null, ex);
            }
            outStream.close();
            src.delete();
        } catch (IOException ex) {
            Logger.getLogger(FileCms.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void copyTo(FileCms dest) {
        if (dest.exists()) {
            throw new RuntimeException("Cant copy file '" + dest + "' dest already exist!");
        }

        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {

            sourceChannel = new FileInputStream(this).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                sourceChannel.close();
                destChannel.close();
            } catch (IOException ex) {
            }
        }
    }

    private void valid(FileCms aThis) {
        if (aThis.exists()) {
            if (!aThis.isFile()) {
                throw new RuntimeException("Is not a file! " + aThis);
            }
        }
    }

    public boolean isTxt() {
        return this.type == FileType.TXT;
    }

    public boolean isVideo() {
        return type == FileType.VIDEO;
    }

    public boolean isImage() {
        return type == FileType.IMAGE;
    }

    public byte[] getInBytes() throws FileNotFoundException, IOException {
        return IOUtils.toByteArray(new FileInputStream(this));
    }

}
