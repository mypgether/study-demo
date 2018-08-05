package com.gether.bigdata.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by myp on 2017/9/4.
 */
public class MongoServiceImpl implements MongoService, InitializingBean {

  private MongoClient mongoClient;
  private MongoDatabase database;

  @Override
  public void addProfile() {
    MongoCollection<Document> collection = database.getCollection("test");
    collection.count();
  }

  @Override
  public String getProfile() {
    return null;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    mongoClient = new MongoClient("localhost", 27017);
    database = mongoClient.getDatabase("mydb");
  }
}