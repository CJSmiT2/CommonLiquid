package ua.org.smit.commontlx.filesystem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderCms extends File {

    public FolderCms(String path) {
        super(path);
        this.mkdirs();
        valid(this);
    }

    public FolderCms(File file) {
        super(file.getAbsolutePath());
        this.mkdirs();
        valid(this);
    }

    public static List<FolderCms> convert(List<File> folders) {
        List<FolderCms> result = new ArrayList<>();
        for (File folder : folders) {
            result.add(new FolderCms(folder));
        }
        return result;
    }

    public long getTotalFilesLength() {
        long length = 0;

        for (File file : getFilesRecursive()) {
            if (file.isFile()) {
                length += file.getTotalSpace();
            }
        }

        return length;
    }

    public List<File> getFiles() {
        List<File> filesList = new ArrayList<>();
        File[] listOfFiles = this.listFiles();

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                filesList.add(listOfFile);
            }
        }

        return filesList;
    }

    public boolean isFileExist(String name) {
        for (File file : getFiles()) {
            if (file.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public File getByname(String name) {
        for (File file : getFiles()) {
            if (file.getName().equals(name)) {
                return file;
            }
        }
        throw new RuntimeException("Cant find file by name '" + name + "' in folder '" + super.getAbsolutePath() + "'");
    }

    public List<FileCms> getFilesTlx() {
        return FileCms.convert(getFiles());
    }

    public List<FileCms> getFilesRecursiveTlx() {
        return FileCms.convert(getFilesRecursive());
    }

    public List<File> getFilesRecursive() {
        List<File> result = new ArrayList<>();
        File[] listOfFiles = this.listFiles();

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                result.add(listOfFile);
            } else {
                result.addAll(getFilesRecursive());
            }
        }

        return result;
    }

    public List<File> getFolders() {
        ArrayList<File> foldersList = new ArrayList();
        File[] listOfFiles = this.listFiles();

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isDirectory()) {
                foldersList.add(listOfFile);
            }
        }

        return foldersList;
    }

    public List<FolderCms> getFoldersCms() {
        return FolderCms.convert(getFolders());
    }

    public void deleteSelfRecursive() {
        for (File childFile : this.listFiles()) {
            if (childFile.isDirectory()) {
                deleteRecursive(childFile);
            } else {
                if (!childFile.delete()) {
                    throw new RuntimeException("Cant delete '" + childFile + "'!");
                }
            }
        }
        this.delete();
    }

    private void deleteRecursive(File file) {
        for (File childFile : file.listFiles()) {
            if (childFile.isDirectory()) {
                deleteRecursive(childFile);
            } else {
                if (!childFile.delete()) {
                    throw new RuntimeException("Cant delete '" + childFile + "'!");
                }
            }
        }
        file.delete();
    }

    private void valid(FolderCms aThis) {
        if (aThis.exists()) {
            if (!aThis.isDirectory()) {
                throw new RuntimeException("Is not a folder! " + aThis);
            }
        }
    }

    public boolean isEmpty() {
        return (getFiles().size() + getFolders().size()) == 0;
    }

    public void put(FileCms file) {
        File dest = new File(this + File.separator + file.getName());
        file.moveTo(dest);
    }

    public void copyIn(List<FileCms> files) {
        for (FileCms file : files) {
            FileCms dst = new FileCms(this + File.separator + file.getName());
            file.copyTo(dst);
        }
    }
}
