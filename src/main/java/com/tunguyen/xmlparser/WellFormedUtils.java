package com.tunguyen.xmlparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WellFormedUtils {
    /**
     * Receive HTML source, get body tag, remove miscellaneous tags and well-formed HTML into valid XML
     * @param src input string html source
     * @return well-formed DOM
     */
    public static String wellFormedHtml(String src) {
        String body = getBody(src);
        String cleanScript = removeMiscellaneousTags(body);

        XmlSyntaxParser xmlSyntaxParser = new XmlSyntaxParser();
        String result = xmlSyntaxParser.parse(cleanScript);
        return getBody(result);
    }

    /**
     * Get only body tag of html
     * @param src source input
     * @return body content
     */
    private static String getBody(String src) {
        Pattern pattern = Pattern.compile("<body[\\s\\S]+</body>");

        Matcher matcher = pattern.matcher(src);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return src;
    }

    /**
     * Remove <script/> <style/> comments and entity references
     * @param src source string need to remove
     * @return removed output from source
     */
    private static String removeMiscellaneousTags(String src) {
        return src.replaceAll("<script[^>]*>([\\s\\S]*?)</script>", "")
                .replaceAll("<style[^>]*>([\\s\\S]*?)</style>", "")
                .replaceAll("<!--.*?-->", "")
                .replaceAll("&.*?;", "");
    }
}
