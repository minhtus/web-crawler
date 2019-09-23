package com.tunguyen.xmlparser;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

import static com.tunguyen.xmlparser.SyntaxState.*;

class XmlSyntaxParser {
    private Character quote;

    String parse(final String src) {
        char[] reader = src.toCharArray();
        StringBuilder htmlWriter = new StringBuilder();

        boolean isOpenTag = false;
        boolean isEmptyTag = false;
        boolean isCloseTag = false;
        StringBuilder openTag = new StringBuilder();
        StringBuilder closeTag = new StringBuilder();
        StringBuilder attrName = new StringBuilder();
        StringBuilder attrValue = new StringBuilder();
        Map<String, String> attributes = new LinkedHashMap<>();

        StringBuilder content = new StringBuilder();
        Stack<String> stack = new Stack<>();

        String state = CONTENT;

        char c;
        for (int i = 0; i < reader.length; i++) {
            c = reader[i];
            switch (state) {
                case CONTENT:
                    if (LT == c) {
                        state = OPEN_BRACKET;
                        htmlWriter.append(content.toString().trim().replace("&", "&amp;"));
                    } else {
                        content.append(c);
                    }
                    break;
                case OPEN_BRACKET:
                    if (isStartTagChars(c)) {
                        state = OPEN_TAG_NAME;

                        isOpenTag = true;
                        isCloseTag = false;
                        isEmptyTag = false;

                        openTag.setLength(0);
                        openTag.append(c);
                    } else if (SLASH == c) {
                        state = CLOSE_TAG_SLASH;

                        isOpenTag = false;
                        isCloseTag = true;
                        isEmptyTag = false;
                    }
                    break;
                case OPEN_TAG_NAME:
                    if (isTagChars(c)) {
                        openTag.append(c);
                    } else if (isSpace(c)) {
                        state = TAG_INNER;

                        attributes.clear();
                    } else if (GT == c) {
                        state = CLOSE_BRACKET;
                    } else if (SLASH == c) {
                        state = EMPTY_SLASH;
                    }
                    break;
                case TAG_INNER:
                    if (isSpace(c)) {
                        // loop ignore space do nothing
                    } else if (isStartAttrChars(c)) {
                        state = ATTR_NAME;

                        attrName.setLength(0);
                        attrName.append(c);
                    } else if (GT == c) {
                        state = CLOSE_BRACKET;
                    } else if (SLASH == c) {
                        state = EMPTY_SLASH;
                    }
                    break;
                case ATTR_NAME:
                    if (isAttrChars(c)) {
                        attrName.append(c);
                    } else if (EQ == c) {
                        state = EQUAL;
                    } else if (isSpace(c)) {
                        state = EQUAL_WAIT;
                    } else {
                        if (SLASH == c) {
                            attributes.put(attrName.toString(), DEFAULT_ATTRIBUTE_VALUE);
                            state = EMPTY_SLASH;
                        } else if (GT == c) {
                            attributes.put(attrName.toString(), DEFAULT_ATTRIBUTE_VALUE);
                            state = CLOSE_BRACKET;
                        }
                    }
                    break;
                case EQUAL_WAIT:
                    if (isSpace(c)) {
                        // loop through space do nothing
                    } else if (EQ == c) {
                        state = EQUAL;
                    } else {
                        if (isStartAttrChars(c)) {
                            attributes.put(attrName.toString(), DEFAULT_ATTRIBUTE_VALUE);
                            state = ATTR_NAME;

                            attrName.setLength(0);
                            attrName.append(c);
                        }
                    }
                    break;
                case EQUAL:
                    if (isSpace(c)) {
                        // loop through space do nothing
                    } else if (D_QUOT == c || S_QUOT == c) {
                        quote = c;
                        state = ATTR_VALUE_Q;

                        attrValue.setLength(0);
                    } else if (!isSpace(c) && GT != c) {
                        state = ATTR_VALUE_NQ;

                        attrValue.setLength(0);
                        attrValue.append(c);
                    }
                    break;
                case ATTR_VALUE_Q:
                    if (quote != c) {
                        attrValue.append(c);
                    } else {
                        state = TAG_INNER;
                        attributes.put(attrName.toString(), attrValue.toString());
                    }
                    break;
                case ATTR_VALUE_NQ:
                    if(!isSpace(c) && GT != c) {
                        attrValue.append(c);
                    } else if (isSpace(c)) {
                        state = TAG_INNER;
                        attributes.put(attrName.toString(), attrValue.toString());
                    } else if (GT == c) {
                        state = CLOSE_BRACKET;
                        attributes.put(attrName.toString(), attrValue.toString());
                    }
                    break;
                case EMPTY_SLASH:
                    if (GT == c) {
                        state = CLOSE_BRACKET;
                        isEmptyTag = true;
                    }
                    break;
                case CLOSE_TAG_SLASH:
                    if (isStartTagChars(c)) {
                        state = CLOSE_TAG_NAME;

                        closeTag.setLength(0);
                        closeTag.append(c);
                    }
                    break;
                case CLOSE_TAG_NAME:
                    if (isTagChars(c)) {
                        closeTag.append(c);
                    } else if (isSpace(c)) {
                        state = WAIT_END_TAG_CLOSE;
                    } else if (GT == c) {
                        state = CLOSE_BRACKET;
                    }
                    break;
                case WAIT_END_TAG_CLOSE:
                    if (isSpace(c)) {
                        // loop through do nothing
                    } else if (c == GT) {
                        state = CLOSE_BRACKET;
                    }
                    break;
                case CLOSE_BRACKET:
                    if (isOpenTag) {
                        String openTagName = openTag.toString().toLowerCase();

                        if (INLINE_TAGS.contains(openTagName)) {
                            isEmptyTag = true;
                        }
                        htmlWriter.append(LT)
                                .append(openTagName)
                                .append(writeAttributes(attributes))
                                .append(isEmptyTag ? "/" : "")
                                .append(GT);

                        attributes.clear();

                        // Push open tag
                        if (!isEmptyTag) {
                            stack.push(openTagName);
                        }

                    } else if (isCloseTag) {
                        String closeTagName = closeTag.toString().toLowerCase();
                        if (!stack.isEmpty() && stack.contains(closeTagName)) {
                            while (!stack.isEmpty() && !stack.peek().equals(closeTagName)) {
                                htmlWriter.append(LT)
                                        .append(SLASH)
                                        .append(stack.pop())
                                        .append(GT);
                            }
                            if (!stack.isEmpty() && stack.peek().equals(closeTagName)) {
                                htmlWriter.append(LT)
                                        .append(SLASH)
                                        .append(stack.pop())
                                        .append(GT);
                            }
                        }
                    }
                    if (LT == c) {
                        state = OPEN_BRACKET;
                    } else {
                        state = CONTENT;

                        content.setLength(0);
                        content.append(c);
                    }
                    break;
            }
        }
        if (CONTENT.equals(state)) {
            htmlWriter.append(content.toString().trim().replace("&", "&amp;"));
        }

        // pop all tag in stack if there is
        while (!stack.isEmpty()) {
            htmlWriter.append(LT)
                    .append(SLASH)
                    .append(stack.pop())
                    .append(GT);
        }
        return htmlWriter.toString();

    }

    private String writeAttributes(final Map<String, String> attributes) {
        if (attributes.isEmpty()) return "";
        final StringBuilder builder = new StringBuilder();
        attributes.entrySet()
                .forEach(entry -> {
                    String value = entry.getValue()
                            .replace("&", "&amp;")
                            .replace("\"", "&quot;")
                            .replace("'", "&apos;")
                            .replace("<", "&lt;")
                            .replace(">", "&qt;");

                    builder.append(entry.getKey())
                            .append("=")
                            .append("\"")
                            .append(value)
                            .append("\"")
                            .append(" ");
                });
        String result = builder.toString().trim();
        if (!"".equals(result)) {
            result = " " + result;
        }
        return result;
    }
}
