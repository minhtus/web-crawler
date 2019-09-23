package com.tunguyen.xmlparser;

import java.util.Arrays;
import java.util.List;

class SyntaxState {
    // states
    static final String CONTENT = "content";
    static final String OPEN_BRACKET = "openBracket";
    static final String OPEN_TAG_NAME = "openTagName";
    static final String TAG_INNER = "tagInner";
    static final String ATTR_NAME = "attributeName";
    static final String EQUAL_WAIT = "equalWait";
    static final String EQUAL = "equal";
    static final String ATTR_VALUE_NQ = "nonQuotedAttributeValue";
    static final String ATTR_VALUE_Q = "QuotedAttributeValue";
    static final String EMPTY_SLASH = "emptyTagSlash";
    static final String CLOSE_BRACKET = "closeBracket";
    static final String CLOSE_TAG_SLASH = "closeTagSlash";
    static final String CLOSE_TAG_NAME = "closeTagName";
    static final String WAIT_END_TAG_CLOSE = "waitEndTagClose";

    // xml characters
    static final char LT = '<';
    static final char SLASH = '/';
    static final char GT = '>';
    static final char EQ = '=';
    static final char D_QUOT = '"';
    static final char S_QUOT = '\'';

    // special characters
    private static final char UNDERSCORE = '_';
    private static final char COLON = ':';
    private static final char HYPHEN = '-';
    private static final char PERIOD = '.';

    static final String DEFAULT_ATTRIBUTE_VALUE = "true";

    private static boolean isStartChar(char c) {
        return Character.isLetter(c) || UNDERSCORE == c || COLON == c;
    }

    private static boolean isNamedChar(char c) {
        return Character.isLetterOrDigit(c) || UNDERSCORE == c || HYPHEN == c || PERIOD == c;
    }

    static boolean isStartTagChars(char c) {
        return isStartChar(c);
    }

    static boolean isStartAttrChars(char c) {
        return isStartChar(c);
    }

    static boolean isTagChars(char c) {
        return isNamedChar(c);
    }

    static boolean isAttrChars(char c) {
        return isNamedChar(c);
    }

    static boolean isSpace(char c) {
        return Character.isSpaceChar(c);
    }

    static final List<String> INLINE_TAGS = Arrays
            .asList("area", "base", "br", "col", "command", "embed", "hr", "img", "input", "keygen", "link", "meta", "param", "source", "track", "wbr");
}
