package com.kuajingnet.springbean.call;

import java.util.List;

public interface BeanCallService {
    Object call(String beanName, String method, List<String> args) throws Exception;
}
