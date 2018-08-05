package com.gether.bigdata.mongo;

public class SettingUtils {

  public static String profileFliter(String profile) throws Exception {
    if (profile.contains("<phoneid>")) {
      String profileDate = profile;
      String phoneid = "<phoneid>";
      int sum = 0;
      int index = 0;
      while (true) {
        index = profileDate.indexOf(phoneid, index + 1);
        if (index > 0) {
          sum++;
        } else {
          break;
        }
      }
      if (sum > 0) {
        for (int i = 0; i < sum; i++) {
          if (profile.contains("<phoneid>")) {
            String temp = profile.substring(
                profile.indexOf("<phoneid>"),
                profile.indexOf("</phoneid>"));
            profile = profile.replaceAll(temp, "<phoneid>");
          }
          if (profile.contains("<timestamp>")) {
            String temp = profile.substring(
                profile.indexOf("<timestamp>"),
                profile.indexOf("</timestamp>"));
            profile = profile.replaceAll(temp, "<timestamp>");
          }
          profile = profile.replace(
              "<phoneid></phoneid><timestamp></timestamp>", "");
        }
      }
    }
    return profile;
  }
}