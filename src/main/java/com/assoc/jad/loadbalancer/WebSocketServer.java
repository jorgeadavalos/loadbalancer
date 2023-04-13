package com.assoc.jad.loadbalancer;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.assoc.jad.loadbalancer.tools.JsonToHashMap;

@ServerEndpoint("/websocketserver")
public class WebSocketServer {

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("Open Connection ...");
	}

	@OnClose
	public void onClose() {
		System.out.println("Close Connection ...");
	}

	@OnMessage
	public void onMessage(String jsonMsg) {
		JSONObject jsonObj;
		try {
			jsonObj = (JSONObject) new JSONParser().parse(jsonMsg);
			String servicesKey		= (String)jsonObj.get("instance");		//key1
			String viewId		= (String)jsonObj.get("viewId");		//key3
			StringBuilder url = new StringBuilder((String)jsonObj.get("schema"));
			url.append("://").append((String)jsonObj.get("serverName")).append(':').append((String)jsonObj.get("instancePort"));

			JsonToHashMap jsonToHashMap = new JsonToHashMap(servicesKey, url.toString(), viewId);
			jsonToHashMap.bldListFromJson(jsonObj);

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Message from the client: " + jsonMsg);
	}

	@OnError
	public void onError(Throwable e) {
		e.printStackTrace();
	}

}
