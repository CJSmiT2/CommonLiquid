package ua.org.smit.commontlx.model.filed.id.album;

import ua.org.smit.commontlx.model.field.id.Id;

public class AlbumId extends Id {

    public static final String ALBUM_ID = "album_id";

    public AlbumId(int value) {
        super(value);
    }

    public AlbumId(Id value) {
        super(value.getValue());
    }

    public AlbumId(String albumId) {
        super(Integer.valueOf(albumId));
    }

}
