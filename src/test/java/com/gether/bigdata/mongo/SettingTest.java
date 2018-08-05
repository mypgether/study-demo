package com.gether.bigdata.mongo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.pagesjaunes.json.JSONException;
import com.pagesjaunes.json.service.XmlToJsonService;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.Optional;

public class SettingTest {

  @Test
  public void testProfile() throws Exception {
    String rowProfile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<a>\n" +
        "<b>\n" +
        "<c></c>" +
        "</b>\n" +
        "</a>";
    rowProfile = SettingUtils.profileFliter(rowProfile);
    XmlToJsonService stXmlToJson = new XmlToJsonService(Maps.newHashMap());
    org.jsoup.nodes.Document doc = Jsoup.parse(rowProfile, "", Parser.xmlParser());
    Elements generalElements = Optional.ofNullable(doc)
        .map((ele) -> ele.getElementsByTag("a"))
        .map((ele) -> ele.get(0)).map((ele) -> ele.getElementsByTag("b"))
        .map((ele) -> ele.get(0).children())
        .orElse(null);

    Elements alertsElements = Optional.ofNullable(doc)
        .map((ele) -> ele.getElementsByTag("a"))
        .map((ele) -> ele.get(0)).map((ele) -> ele.getElementsByTag("b"))
        .map((ele) -> ele.get(0).children())
        .orElse(null);
    generalElements.stream().forEach((element -> {
      JSONObject eleJsonObject = toJsonObject(stXmlToJson, element);
      System.out.println(element.nodeName() + "============>" + eleJsonObject.toString());
    }));
    alertsElements.stream().forEach((element -> {
      JSONObject eleJsonObject = toJsonObject(stXmlToJson, element);
      System.out.println(element.nodeName() + "============>" + eleJsonObject.toString());
    }));
  }

  public static final String VALUE_FIELD = "val";

  private JSONObject toJsonObject(XmlToJsonService stXmlToJson, Element element) {
    JSONObject eleJsonObject = new JSONObject();
    element.attributes().asList().stream().forEach((attribute -> {
      eleJsonObject.put(attribute.getKey(), attribute.getValue());
    }));
    if (StringUtils.isNotBlank(element.html()) && element.html().replace("\n", "")
        .matches("<.*>.*</.*>")) {
      try {
        String xmlContent = String
            .format("<%s>%s</%s>", element.nodeName(), element.html(), element.nodeName());
        com.pagesjaunes.json.JSONObject jo = stXmlToJson.toJSONObject(xmlContent);
        com.pagesjaunes.json.JSONObject nodeObj = jo.getJSONObject(element.nodeName());
        for (Object key : nodeObj.keySet()) {
          if (nodeObj.get(String.valueOf(key)) instanceof com.pagesjaunes.json.JSONObject) {
            eleJsonObject
                .put(String.valueOf(key), JSON.parse(nodeObj.get(String.valueOf(key)).toString()));
          } else {
            eleJsonObject.put(String.valueOf(key), nodeObj.get(String.valueOf(key)));
          }
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
      //try {
      //    String xmlContent = element.html();
      //    com.pagesjaunes.json.JSONObject jo = stXmlToJson.toJSONObject(xmlContent);
      //    for (Object key : jo.keySet()) {
      //        //System.out.println(key + "" + jo.get(String.valueOf(key)));
      //    }
      //    //System.out.println(element.nodeName() + "====>" + stXmlToJson.toJSONObject(element.html()).toString());
      //    System.out.println(element.nodeName() + "====>" + jo.toString());
      //} catch (JSONException e) {
      //    e.printStackTrace();
      //}
    } else {
      eleJsonObject.put(VALUE_FIELD, Optional.ofNullable(element.html()).orElse(""));
    }
    return eleJsonObject;
  }


  private String xmlToJson(String xml) {
    org.jsoup.nodes.Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
    Elements generalElements = Optional.ofNullable(doc)
        .map((ele) -> ele.getElementsByTag("a"))
        .map((ele) -> ele.get(0)).map((ele) -> ele.getElementsByTag("g"))
        .map((ele) -> ele.get(0).children())
        .orElse(null);
    return null;
  }
}