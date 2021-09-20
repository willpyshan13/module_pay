package com.fjace.pay.component.websocket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author fjace
 * @version 1.0.0
 * @Description websocket 服务
 * @date 2021-09-11 14:43:00
 */
@Component
@ServerEndpoint("/api/anon/ws/channelUserId/{appId}/{cid}")
public class WsChannelUserIdServer {

    private final static Logger log = LoggerFactory.getLogger(WsChannelUserIdServer.class);

    /**
     * 当前在线客户端 数量
     */
    private static int onlineClientSize = 0;

    /**
     * appId 与 WsPayOrderServer 存储关系, ConcurrentHashMap保证线程安全
     */
    private static Map<String, Set<WsChannelUserIdServer>> wsAppIdMap = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 客户端自定义ID
     */
    private String cid = "";

    /**
     * 支付订单号
     */
    private String appId = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("appId") String appId, @PathParam("cid") String cid) {
        try {
            //设置当前属性
            this.cid = cid;
            this.appId = appId;
            this.session = session;

            Set<WsChannelUserIdServer> wsServerSet = wsAppIdMap.get(appId);
            if (wsServerSet == null) {
                wsServerSet = new CopyOnWriteArraySet<>();
            }
            wsServerSet.add(this);
            wsAppIdMap.put(appId, wsServerSet);

            addOnlineCount(); //在线数加1
            log.info("cid[{}],appId[{}]连接开启监听！当前在线人数为{}", cid, appId, onlineClientSize);

        } catch (Exception e) {
            log.error("ws监听异常cid[{}],appId[{}]", cid, appId, e);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        Set<WsChannelUserIdServer> wsSet = wsAppIdMap.get(this.appId);
        wsSet.remove(this);
        if (wsSet.isEmpty()) {
            wsAppIdMap.remove(this.appId);
        }
        //在线数减1
        subOnlineCount();
        log.info("cid[{}],appId[{}]连接关闭！当前在线人数为{}", cid, appId, onlineClientSize);
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("ws发生错误", error);
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 根据订单ID,推送消息
     * 捕捉所有的异常，避免影响业务。
     *
     * @param appId
     */
    public static void sendMsgByAppAndCid(String appId, String cid, String msg) {
        try {
            log.info("推送ws消息到浏览器, appId={}, cid={}, msg={}", appId, cid, msg);
            Set<WsChannelUserIdServer> wsSet = wsAppIdMap.get(appId);
            if (wsSet == null || wsSet.isEmpty()) {
                log.info("appId[{}] 无ws监听客户端", appId);
                return;
            }

            for (WsChannelUserIdServer item : wsSet) {
                if (!cid.equals(item.cid)) {
                    continue;
                }
                try {
                    item.sendMessage(msg);
                } catch (Exception e) {
                    log.info("推送设备消息时异常，appId={}, cid={}", appId, item.cid, e);
                }
            }
        } catch (Exception e) {
            log.info("推送消息时异常，appId={}", appId, e);
        }
    }

    public static synchronized int getOnlineClientSize() {
        return onlineClientSize;
    }

    public static synchronized void addOnlineCount() {
        onlineClientSize++;
    }

    public static synchronized void subOnlineCount() {
        onlineClientSize--;
    }

}
