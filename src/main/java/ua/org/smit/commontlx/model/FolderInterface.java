package ua.org.smit.commontlx.model;

import ua.org.smit.commontlx.filesystem.FolderCms;

public interface FolderInterface {

    void initFromDisc(FolderCms folder);

    void writeOnDisc(FolderCms folder);
}
