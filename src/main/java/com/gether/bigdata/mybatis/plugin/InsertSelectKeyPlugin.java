package com.gether.bigdata.mybatis.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

public class InsertSelectKeyPlugin extends PluginAdapter {
    @Override
    public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        XmlElement selectKey = new XmlElement("selectKey");
        selectKey.addAttribute(new Attribute("keyProperty", "did"));
        selectKey.addAttribute(new Attribute("resultType", "int"));
        selectKey.addAttribute(new Attribute("order", "AFTER"));
        selectKey.addElement(new TextElement("select LAST_INSERT_ID()"));
        element.addElement(selectKey);
        return super.sqlMapInsertElementGenerated(element, introspectedTable);
    }

    ;

    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        // <selectKey keyProperty="did" resultType="int" order="AFTER">
        //
        // select LAST_INSERT_ID()
        //
        // </selectKey>
        XmlElement selectKey = new XmlElement("selectKey");
        selectKey.addAttribute(new Attribute("keyProperty", "did"));
        selectKey.addAttribute(new Attribute("resultType", "int"));
        selectKey.addAttribute(new Attribute("order", "AFTER"));
        selectKey.addElement(new TextElement("select LAST_INSERT_ID()"));
        element.addElement(selectKey);
        return super.sqlMapInsertSelectiveElementGenerated(element, introspectedTable);
    }

    ;

    public boolean validate(List<String> warnings) {
        return true;
    }
}