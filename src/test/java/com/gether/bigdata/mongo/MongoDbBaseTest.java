package com.gether.bigdata.mongo;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.pagesjaunes.json.JSONException;
import com.pagesjaunes.json.service.XmlToJsonService;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by myp on 2017/9/5.
 */
public class MongoDbBaseTest {

  private MongoClient mongoClient;
  private MongoDatabase database;

  //public static final int PORT = 32768;
  public static final String HOST = "localhost";
  public static final int PORT = 27017;

  @Before
  public void before() {
    CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    //MongoCredential credential = MongoCredential.createCredential(user, database, password);
    mongoClient = new MongoClient(new ServerAddress(HOST, PORT),
        MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
    database = mongoClient.getDatabase("mydb_compare");
  }

  @Test
  public void mongoCreate() {
    // tables
    MongoCollection<Document> collection = database.getCollection("test");

    System.out.println(collection.count());

    List<Document> documentList = Lists.newArrayList();
    for (int i = 0; i < 4; i++) {
      Document document = new Document("name", "user").append("type", "database")
          .append("count", 1)
          .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
          .append("info", new Document("x", 203).append("y", 102)).append("_id", i);
      documentList.add(document);
    }
    collection.insertMany(documentList);
  }

  @Test
  public void delete() {
    MongoCollection<Document> collection = database.getCollection("test");
    DeleteResult res = collection.deleteOne(eq("_id", 1));
    System.out.println(res.getDeletedCount());

    res = collection.deleteMany(eq("type", "database"));
    System.out.println(res.getDeletedCount());
  }


  @Test
  public void insertObj() {
    MongoCollection<Document> collection = database.getCollection("profile", Document.class);

    String deviceId = "2123451";
    ProfilePojo pojo = new ProfilePojo();
    pojo.setId(ObjectId.get());
    pojo.setDeviceId(deviceId);
    pojo.setCreateTime(new Date());
    pojo.setModifyTime(new Date());

    Map<String, Map<String, String>> map = Maps.newHashMap();
    Map<String, String> objMap = Maps.newHashMap();
    objMap.put("support", "1");
    map.put("a", objMap);

    objMap = Maps.newHashMap();
    objMap.put("support", "1");
    objMap.put("value", "这是名称😄");
    map.put("title", objMap);

    Document document = new Document("deviceId", pojo.getDeviceId())
        .append("createTime", pojo.getCreateTime())
        .append("modifyTime", pojo.getModifyTime());
    Document secondDoc = new Document();
    for (String key : map.keySet()) {
      secondDoc.clear();
      for (String secondKey : map.get(key).keySet()) {
        secondDoc.append(secondKey, map.get(key).get(secondKey));
      }
      document.append(key, secondDoc);
    }
    collection.insertOne(document);
  }

  @Test
  public void getObj() {
    MongoCollection<Document> collection = database.getCollection("profile", Document.class);
    FindIterable<Document> res = collection.find(eq("deviceId", "deviceId")).limit(1);
    Document document = res.first();
    ProfilePojo profilePojo = new ProfilePojo();
    Map<String, Map<String, Object>> profileMap = Maps.newHashMap();
    Map<String, Object> secondMap = null;
    for (String key : document.keySet()) {
      if (StringUtils.equalsIgnoreCase("_id", key)) {
      } else if (StringUtils.equalsIgnoreCase("deviceId", key)) {
        profilePojo.setDeviceId(document.getString(key));
      } else if (StringUtils.equalsIgnoreCase("createTime", key)) {
        profilePojo.setCreateTime(document.getDate(key));
      } else if (StringUtils.equalsIgnoreCase("modifyTime", key)) {
        profilePojo.setModifyTime(document.getDate(key));
      } else {
        secondMap = Maps.newHashMap();
        Document secondDocument = document.get(key, Document.class);
        for (String secondKey : secondDocument.keySet()) {
          secondMap.put(secondKey, secondDocument.getString(secondKey));
        }
        profileMap.put(key, secondMap);
      }
      profilePojo.setProfile(profileMap);
      System.out.println("key:" + key + "\t\tvalue:" + document.get(key));
    }

    System.out.println("result:" + JSON.toJSONString(profilePojo));
  }

  @Test
  public void updateObj() {
    MongoCollection<ProfilePojo> collection = database.getCollection("profile", ProfilePojo.class);
    String deviceId = "d1";
    //Map<String, Map<String, String>> map = Maps.newHashMap();
    //Map<String, String> objMap = Maps.newHashMap();
    //objMap.put("support", "1");
    //map.put("title", objMap);
    //BasicDBObject setValue = new BasicDBObject();
    //for (String key : map.keySet()) {
    //    for (String secondKey : map.get(key).keySet()) {
    //        setValue.put(String.format("%s.%s", key, secondKey), map.get(key).get(secondKey));
    //    }
    //}
    //BasicDBObject upsertValue = new BasicDBObject("$set", setValue);
    //collection.updateOne(eq("deviceId", deviceId),
    //        upsertValue,
    //        new UpdateOptions().upsert(true));

    BasicDBObject setValue = new BasicDBObject();
    setValue.put("a.b", "On");

    BasicDBObject query = new BasicDBObject();
    query.put("deviceId", deviceId);

    BasicDBObject upsertValue = new BasicDBObject("$set", setValue);
    UpdateResult res = collection.updateMany(query, upsertValue);
    System.out.println(res.toString());
  }

  @Test
  public void testSettingXml2Json() throws Exception {
    String deviceId = "d1";
    rowProfile = SettingUtils.profileFliter(rowProfile);
    XmlToJsonService stXmlToJson = new XmlToJsonService(Maps.newHashMap());
    org.jsoup.nodes.Document doc = Jsoup.parse(rowProfile, "", Parser.xmlParser());
    Elements generalElements = Optional.ofNullable(doc)
        .map((ele) -> ele.getElementsByTag("profile"))
        .map((ele) -> ele.get(0)).map((ele) -> ele.getElementsByTag("general"))
        .map((ele) -> ele.get(0).children())
        .orElse(null);
    final JSONObject eleJsonObject = new JSONObject();
    generalElements.stream().forEach((element -> {
      eleJsonObject.put(element.nodeName(), toJsonObject(stXmlToJson, element));
      System.out.println(element.nodeName() + "============>" + eleJsonObject.toString());
    }));
    System.out.println(eleJsonObject.toString());

    MongoCollection<ProfilePojo> collection = database.getCollection("profile", ProfilePojo.class);
    ProfilePojo pojo = new ProfilePojo();
    pojo.setDeviceId(deviceId);
    pojo.setCreateTime(new Date());
    pojo.setModifyTime(new Date());

    BasicDBObject setValue = new BasicDBObject();
    for (String key : eleJsonObject.keySet()) {
      if (StringUtils.equalsIgnoreCase("deviceId", key)) {
        continue;
      }
      JSONObject secondJO = eleJsonObject.getJSONObject(key);
      for (String secondKey : secondJO.keySet()) {
        setValue.put(String.format("profile.%s.%s", key, secondKey), secondJO.get(secondKey));
      }
    }
    BasicDBObject upsertValue = new BasicDBObject("$set", setValue);
    BasicDBObject query = new BasicDBObject();
    query.put("deviceId", pojo.getDeviceId());
    collection.updateOne(query, upsertValue, new UpdateOptions().upsert(true));
  }

  @Test
  public void testSettingJson2Xml() throws Exception {
    String deviceId = "d1";
    org.jsoup.nodes.Document doc = Jsoup.parse(rowProfile, "", Parser.xmlParser());
    MongoCollection<Document> collection = database.getCollection("profile", Document.class);
    FindIterable<Document> result = collection.find(eq("deviceId", deviceId));
    Document document = result.first();

    ProfilePojo profilePojo = new ProfilePojo();
    Map<String, Map<String, Object>> profileMap = Maps.newHashMap();
    Map<String, Object> secondMap = null;
    for (String key : document.keySet()) {
      if (StringUtils.equalsIgnoreCase("_id", key)) {
      } else if (StringUtils.equalsIgnoreCase("deviceId", key)) {
        profilePojo.setDeviceId(document.getString(key));
      } else if (StringUtils.equalsIgnoreCase("createTime", key)) {
        profilePojo.setCreateTime(document.getDate(key));
      } else if (StringUtils.equalsIgnoreCase("modifyTime", key)) {
        profilePojo.setModifyTime(document.getDate(key));
      } else {
        secondMap = Maps.newHashMap();
        Document secondDocument = document.get(key, Document.class);
        for (String secondKey : secondDocument.keySet()) {
          Object secondValue = secondDocument.get(secondKey);
          if (secondValue instanceof String) {
            secondMap.put(secondKey, secondDocument.getString(secondKey));
          } else {
            Object json = JSON.parse(JSON.toJSONString(secondValue));
            secondMap.put(secondKey, json);
          }
        }
        profileMap.put(key, secondMap);
      }
      profilePojo.setProfile(profileMap);
      System.out.println("key:" + key + "\t\tvalue:" + document.get(key));
    }
    System.out.println(JSON.toJSONString(profilePojo));
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


  String rowProfile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<profile>\n" +
      "<general>\n" +
      "<b>123</b>" +
      "</profile>";
}