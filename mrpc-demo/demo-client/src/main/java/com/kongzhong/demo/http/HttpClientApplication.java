package com.kongzhong.demo.http;

import com.kongzhong.mrpc.client.RpcClient;
import com.kongzhong.mrpc.demo.service.UserService;

/**
 * Http Hello World
 *
 * @author biezhi
 *         2017/4/19
 */
public class HttpClientApplication {

    public static void main(String[] args) {
        RpcClient rpcClient = new RpcClient();
        rpcClient.setTransport("http");

        UserService userService = rpcClient.getProxyBean(UserService.class);
        String msg = userService.hello("http-client");
        System.out.println(msg);
        rpcClient.stop();
    }
}
