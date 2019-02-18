package com.sand.client.controller;

import com.sand.client.config.ClientHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("nc")
public class NettyController {
    @Autowired
    private ClientHandler clientHandler;

    @RequestMapping("nettyTest")
    public String nettyTest() {
        String req = "netty demo";
        Object o = clientHandler.sendRequest(req, 50);
        String result = o == null ? "" : o.toString();
        return result;
    }
}
