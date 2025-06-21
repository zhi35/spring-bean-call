package com.kuajingnet.springbean.call;

import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;
import java.util.List;

public class SpringBeanCallService implements BeanCallService {

    private BeanFactory beanFactory;

    public SpringBeanCallService(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object call(String beanName, String methodName, List<String> args) throws Exception {
        Object bean = beanFactory.getBean(beanName);
        Method callMethod = getBeanMethod(bean, beanName, methodName, args);
        if (callMethod == null) {
            return null;
        }
        Object[] params = new Object[callMethod.getParameterCount()];
        Class<?>[] parameterTypes = callMethod.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i].isAssignableFrom(String.class)) {
                params[i] = args.get(i);
            } else {
                params[i] = JSON.parseObject(args.get(i), parameterTypes[i]);
            }
        }
        return callMethod.invoke(bean, params);
    }

    private Method getBeanMethod(Object bean, String beanName, String methodName, List<String> args) throws Exception {
        if (bean == null) {
            throw new Exception(String.format("未找到对应bean：%s", beanName));
        }
        Method[] methods = bean.getClass().getMethods();
        Method callMethod = null;
        for (Method m : methods) {
            if (m.getName().equals(methodName) && m.getParameterCount() == args.size()) {
                callMethod = m;
                break;
            }
        }

        if (callMethod != null) {
            return callMethod;
        }


        methods = bean.getClass().getDeclaredMethods();
        for (Method m : methods) {
            m.setAccessible(true);
            if (m.getName().equals(methodName) && m.getParameterCount() == args.size()) {
                callMethod = m;
                break;
            }
        }

        if (callMethod != null) {
            return callMethod;
        }

        throw new Exception(String.format("未查找到bean方法(%s)，请检查方法名和参数数量是否对应。", methodName));

    }
}
