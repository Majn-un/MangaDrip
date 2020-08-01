//package com.example.mangadrip;
//import org.jsoup.Connection;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class JsoupCookieContainer {
//    private cookieStore = new HashMap<String, HashMap<String,String>>();
//
//    public static const JsoupCookieContainer Instance = new JsoupCookieContainer();
//
//    private JsoupCookieContainer(){}
//
//    public void update(Connection.Response response)
//    {
//        String domain = response.url.getHost();
//        if (!cookiestore.exists(domain))
//        {
//            cookieStore.add(domain,new HashMap<string,string>());
//        }
//        Map<String,String> cookies = response.cookies;
//        foreach (string cookiename in cookies.getKeys())
//        {
//            cookieStore.get(domain).put(cookiename, cookies.get(cookiename));
//        }
//    }
//
//    public Map<String,String> forRequest(string url)
//    {
//        String domain = new Java.URL(url).getHost();
//        if (cookieStore.exists(domain))
//        {
//            return cookieStore.get(domain);
//        }
//        return new HashMap;
//    }
//
//}
////}
