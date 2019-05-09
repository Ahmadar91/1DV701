/**
 * The enum Content type.
 */
public enum ContentType
{/**
 * Image png content type.
 */
    IMAGE_PNG,
    /**
     * Text html content type.
     */
    TEXT_HTML,
    /**
     * Image jpg content type.
     */
    IMAGE_JPG;

    /**
     * To string string.
     *
     * @param contentType the content type
     * @return the string
     */
    public static String toString(ContentType contentType) {
        switch (contentType) {
            case IMAGE_JPG: return "image/jpg";
            case IMAGE_PNG: return "image/png";
            case TEXT_HTML: return "text/html";
            default: return "";
        }
    }

    /**
     * From extension content type.
     *
     * @param extension the extension
     * @return the content type
     */
    public static ContentType fromExtension(String extension) {
        switch (extension) {
            case "html": return TEXT_HTML;
            case "htm": return TEXT_HTML;
            case "png": return IMAGE_PNG;
            case "jpg": return IMAGE_JPG;
            case "jpeg": return IMAGE_JPG;
            default: return TEXT_HTML;
        }
    }
}
