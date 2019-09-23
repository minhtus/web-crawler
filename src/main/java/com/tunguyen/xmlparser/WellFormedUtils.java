package com.tunguyen.xmlparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WellFormedUtils {
    public static String wellFormedHtml(String src) {
        String body = getBody(src);
        String cleanScript = removeMiscellaneousTags(body);

        XmlSyntaxParser xmlSyntaxParser = new XmlSyntaxParser();
        String result = xmlSyntaxParser.parse(cleanScript);
        return getBody(result);
    }

    private static String getBody(String src) {
        Pattern pattern = Pattern.compile("<body[\\s\\S]+</body>");

        Matcher matcher = pattern.matcher(src);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return src;
    }

    private static String removeMiscellaneousTags(String src) {
        return src.replaceAll("<script[\\s\\S]+</script>", "")
                .replaceAll("<!--.*?-->", "")
                .replaceAll("&.*?;", "");
    }
}
