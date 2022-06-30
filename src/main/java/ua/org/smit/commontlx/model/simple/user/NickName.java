package ua.org.smit.commontlx.model.simple.user;

import ua.org.smit.commontlx.model.text.Text;

public class NickName extends Text {

    public static final String NICK_NAME = "nick_name";
    private static final int LENGTH_LIMIT = 20;

    public NickName(String value) {
        super(value);
        valid(value);
    }

    private void valid(String value) {
        if (value.length() > LENGTH_LIMIT) {
            throw new RuntimeException("NickName to long! Value = '" + value + "'");
        }
    }

}
