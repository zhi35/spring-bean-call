package com.kuajingnet.springbean.call;

import com.alibaba.fastjson2.JSON;
import fi.iki.elonen.NanoHTTPD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * 暴露 http 接口调用 spring bean 方法(包含私有方法)
 */
public class BeanHttpServer extends NanoHTTPD {
    private static final Logger log = LoggerFactory.getLogger(BeanHttpServer.class);
    public static final String APPLICATION_JSON = "application/json; charset=UTF-8";
    private BeanCallService beanCallService;

    private BeanHttpServer(int port, BeanCallService beanCallService) throws IOException {
        super(port);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, true);
        this.beanCallService = beanCallService;
    }

    public static final BeanHttpServer build(BeanFactory beanFactory) throws IOException {
        return build(9009, beanFactory);
    }

    public static final BeanHttpServer build(int port, BeanFactory beanFactory) throws IOException {
        BeanCallService beanCallService = new SpringBeanCallService(beanFactory);
        return build(port, beanCallService);
    }

    public static final BeanHttpServer build(int port, BeanCallService beanCallService) throws IOException {
        return new BeanHttpServer(port, beanCallService);
    }

    @Override
    public Response serve(String uri, Method method, Map<String, String> headers, Map<String, String> parms, Map<String, String> files) {
        if (method != Method.POST) {
            return newFixedLengthResponse(Response.Status.METHOD_NOT_ALLOWED, NanoHTTPD.MIME_PLAINTEXT, "只支持POST请求");
        }
        String[] splitUri = uri.split("/");
        if (splitUri.length < 3) {
            return newFixedLengthResponse("不支持参数");
        }
        String beanName = splitUri[1];
        String methodName = splitUri[2];
        String postData = files.get("postData");
        Response.Status status = Response.Status.OK;
        String respStr;
        try {
            List<String> args = JSON.parseArray(postData, String.class);
            Object resp = beanCallService.call(beanName, methodName, args);
            respStr = JSON.toJSONString(resp);
        } catch (InvocationTargetException ite) {
            status = Response.Status.INTERNAL_ERROR;
            respStr = ite.getTargetException().getMessage();
            log.error("uri {} postData {}", uri, postData, ite);
        } catch (Exception e) {
            status = Response.Status.INTERNAL_ERROR;
            respStr = e.getMessage();
            log.error("uri {} postData {}", uri, postData, e);
        }
        if (status == Response.Status.OK) {
            newFixedLengthResponse(Response.Status.OK, APPLICATION_JSON, respStr);
        }
        return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT, respStr);
    }

}
