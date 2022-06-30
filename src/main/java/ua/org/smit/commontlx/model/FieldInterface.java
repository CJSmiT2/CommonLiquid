package ua.org.smit.commontlx.model;

import ua.org.smit.commontlx.filesystem.FolderCms;

public interface FieldInterface {

    void writeValueOnDisk(FolderCms folder);

    void readValueFromDisk(FolderCms folder);
}
