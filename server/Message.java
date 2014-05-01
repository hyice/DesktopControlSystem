package server;

/**
 * Created by hyice on 4/26/14.
 */
public class Message {

    private String dst;
    private String src;
    private char type;
    private String text = "";

    public Message() {}

    public Message(String dst, String src, char type, String text) {
        this.src = src;
        this.dst = dst;
        this.type = type;
        this.text = text;
    }

    public boolean parse(String msg) {
        boolean ret = false;
        if ( msg.charAt(0) == '$' ) {
            dst = msg.substring(1, 4);
            src = msg.substring(4, 7);
            type = msg.charAt(7);
            text = msg.substring(8);
            ret = true;
        }
        return ret;
    }

    public String generate() {
        StringBuffer sb = new StringBuffer();
        sb.append('$');
        sb.append(dst);
        sb.append(src);
        sb.append(type);
        sb.append(text);
        return sb.toString();
    }

    public String getDst() {
        return dst;
    }

    public String getSrc() {
        return src;
    }

    public char getType() {

        return type;
    }

    public String getText() {
        return text;
    }
}
