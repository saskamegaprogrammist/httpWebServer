package server;

public class ExtensionResolver {
    private static final String CSS_EXT = "css";
    private static final String HTML_EXT = "html";
    private static final String JS_EXT = "js";
    private static final String JPG_EXT = "jpg";
    private static final String JPEG_EXT = "jpeg";
    private static final String PNG_EXT = "png";
    private static final String GIF_EXT = "gif";
    private static final String SWF_EXT = "swf";


    private static final String HTML_CT = "text/html";
    private static final String CSS_CT = "text/css";
    private static final String JS_CT = "application/javascript";
    private static final String JPG_CT = "image/jpeg";
    private static final String PNG_CT = "image/png";
    private static final String GIF_CT = "image/gif";
    private static final String SWF_CT = "application/x-shockwave-flash";
    private static final String DEFAULT_CT = "text/plain";

    public String getCT(String ext) {
        switch (ext) {
            case CSS_EXT :
                return CSS_CT;
            case HTML_EXT :
                return HTML_CT;
            case JS_EXT :
                return JS_CT;
            case JPEG_EXT :
                return JPG_CT;
            case JPG_EXT :
                return JPG_CT;
            case GIF_EXT :
                return GIF_CT;
            case SWF_EXT :
                return SWF_CT;
            default :
                return DEFAULT_CT;
        }
    }

}
