package com.gether.bigdata.util;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

public class XMLUtils {

    private static final Logger logger = LoggerFactory
            .getLogger(XMLUtils.class);

    /**
     * 判断xml中是否包含path节点
     */
    public static boolean isContainNode(String profile, String path) {
        try {
            Document doc = DocumentHelper.parseText(profile);
            List list = doc.selectNodes(path);
            if (null != list && list.size() > 0) {
                return true;
            }
        } catch (Exception e) {
            logger.error("isContainNode error ,", e);
        }
        return false;
    }

    /**
     * 仅仅支持单节点的解析
     * 从< nodeName >nodevalue< /nodeName>当中获取nodevalue
     */
    public static String getNodeValue(String element, String nodeName) {
        try {
            Document doc = DocumentHelper.parseText(element);
            Element e = (Element) doc.selectSingleNode(nodeName);
            return e.getText();
        } catch (Exception e) {
            logger.error("getNodeValue error ,", e);
        }
        return null;
    }

    /**
     * 支持单,多节点的解析
     * 从< nodeName >nodevalue< /nodeName>当中根据xpath获取nodevalue
     */
    public static String getMultiNodeValue(String element, String xpath) {
        StringBuilder sb = new StringBuilder();
        try {
            Document doc = DocumentHelper.parseText(element);
            Element e = (Element) doc.selectSingleNode(xpath);
            Iterator iterator = e.nodeIterator();
            while (iterator.hasNext()) {
                Object next = iterator.next();
                if (next instanceof Text) {
                    sb.append(((Text) next).asXML());
                } else if (next instanceof Element) {
                    sb.append(((Element) next).asXML());
                }
            }
            return sb.toString();
        } catch (Exception e) {
            logger.error("getNodeValue error ,", e);
        }
        return null;
    }

    /**
     * 添加节点到profile中，返回修改后的profile
     */
    public static String addNodeToXML(String path, String value, String profile) {
        String pathArr[] = path.split("/");
        String settingNode = pathArr[1];
        StringBuffer sb = new StringBuffer();
        sb.append(xmlToStandard(pathArr[2], true, true));
        sb.append(value);
        sb.append(xmlToStandard(pathArr[2], false, true));
        settingNode = xmlToStandard(settingNode, false);
        sb.append(settingNode);
        profile = profile.replace(settingNode, sb.toString());
        return profile;
    }

    /**
     * 添加节点到profile中，返回修改后的profile
     */
    public static String addSingleNodeToXML(String path, String value, String profile) {
        try {
            Document doc = DocumentHelper.parseText(profile);
            int lastIndex = path.lastIndexOf("/");
            Element e = (Element) doc.selectSingleNode(path.substring(0, lastIndex));

            Element addNode = e.addElement(path.substring(lastIndex + 1, path.length()));
            addNode.setText(value);
            return doc.asXML();
        } catch (Exception e) {
            logger.error("XMLUtils.setSingleNodeValue error ,", e);
        }
        return profile;
    }

    /**
     * 修改support属性的节点(如果不存在这个节点会添加操作,但是允许一层)
     */
    public static String modifySupportAttr(String path, String supportValue,
                                           String profile) {
        try {
            String support = "support";
            if (!"1".equals(supportValue)) {
                supportValue = "0";
            }
            Document doc = DocumentHelper.parseText(profile);
            Element element = (Element) doc.selectSingleNode(path);
            // 如果不存在这个节点会添加操作,但是允许一层
            if (null == element) {
                int lastIndexSep = path.lastIndexOf("/");
                Element parent = (Element) doc.selectSingleNode(path.substring(
                        0, lastIndexSep));
                element = parent.addElement(path.substring(lastIndexSep + 1,
                        path.length()));
            }
            Attribute attribute = element.attribute(support);
            if (null == attribute) {
                element.addAttribute(support, supportValue);
            } else {
                attribute.setText(supportValue);
            }
            return doc.asXML();
        } catch (Exception e) {
            logger.error("isContainNode error ,", e);
        }
        return profile;
    }

    /**
     * true返回< settingname >，false返回< / settingname >
     */
    public static String xmlToStandard(String settingName, boolean nameValid) {
        StringBuffer sb = new StringBuffer();
        if (nameValid) {
            sb.append("<");
            sb.append(settingName);
            sb.append(">");
            return sb.toString();
        } else {
            sb.append("</");
            sb.append(settingName);
            sb.append(">");
            return sb.toString();
        }
    }

    /**
     * support true表示添加support属性 true返回< settingname >，false返回< / settingname >
     */
    private static String xmlToStandard(String settingName, boolean nameValid,
                                        boolean support) {
        StringBuffer sb = new StringBuffer();
        if (nameValid) {
            sb.append("<");
            sb.append(settingName);
            if (support) {
                sb.append(" support=\"1\"");
            } else {
                sb.append(" support=\"0\"");
            }
            sb.append(">");
            return sb.toString();
        } else {
            sb.append("</");
            sb.append(settingName);
            sb.append(">");
            return sb.toString();
        }
    }

    /**
     * xml转义字符串
     *
     * @param str
     * @param isInAttribute true表示转义节点里面的内容,false表示转义内容
     * @return
     */
    public static String getEscapedStr(String str, boolean isInAttribute) {
        String result = new String();
        if (!isInAttribute) {//case title
            int start = str.indexOf('>');
            int end = str.lastIndexOf('<');
            result = str.substring(0, start + 1) +
                    StringEscapeUtils.escapeXml(str.substring(start + 1, end))
                    + str.substring(end);
        } else {
            String[] strary = str.split("\"");
            for (int i = 0; i < strary.length; i++) {
                if (strary[i].equals(" name="))
                    strary[i + 1] = StringEscapeUtils.escapeXml(strary[i + 1]);
            }
            result += strary[0];
            for (int i = 1; i < strary.length; i++) {
                result += "\"" + strary[i];
            }
        }
        return result;
    }

    /**
     * 获取v字符串中，v1、v2之间的字符串
     *
     * @param v
     * @param v1
     * @param v2
     * @return
     */
    public static String getBetweenStr(String v, String v1, String v2) {
        String r = "";
        try {
            if (v.indexOf(v1) != -1 && v.indexOf(v2) != -1) {
                String tmp = v.substring(v.indexOf(v1) + v1.length());
                r = tmp.substring(0, tmp.indexOf(v2));
            }
        } catch (Exception e) {
            return "";
        }
        return r.trim();
    }

    /**
     * 仅仅支持单节点的设置，修改节点的内容
     * 从< nodeName >nodevalue< /nodeName>当中修改nodevalue
     */
    public static String setNodeValue(String profile, String nodeName, String nodeValueAll) {
        try {
            String sourceValue = getMultiNodeValue(profile, nodeName);
            String pathArr[] = nodeName.split("/");
            String searchStr = ">" + sourceValue + "</" + pathArr[pathArr.length - 1] + ">";
            String replaceStr = ">" + nodeValueAll + "</" + pathArr[pathArr.length - 1] + ">";
            return StringUtils.replace(profile, searchStr, replaceStr);
        } catch (Exception e) {
            logger.error("XMLUtils.setNodeValue error ,", e);
        }
        return profile;
    }

    /**
     * 仅仅支持单节点的设置，修改节点的内容
     * 从< nodeName >nodevalue< /nodeName>当中修改nodevalue
     */
    public static String setSingleNodeValue(String profile, String nodeName, String nodeValueAll) {
        try {
            Document doc = DocumentHelper.parseText(profile);
            Element e = (Element) doc.selectSingleNode(nodeName);
            if (null != e) {
                e.setText(nodeValueAll);
            }
            return doc.asXML();
        } catch (Exception e) {
            logger.error("XMLUtils.setSingleNodeValue error ,", e);
        }
        return profile;
    }
}