package com.threewater.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author: Yessirskiii
 * @Date: 2022/07/25/21:39
 * @Description: RPC 请求消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class RpcRequestMessage extends Message {

    /**
     * 调用的接口全限定名，服务端根据它找到实现，即远程调用的接口名称
     */
    private String interFaceName;
    /**
     * 调用接口中的方法名称
     */
    private String methodName;
    /**
     * 方法的返回值类型
     */
    private Class<?> returnType;
    /**
     * 方法参数类型数组
     */
    private Class[] parameterTypes;
    /**
     * 方法参数值数组
     */
    private Object[] parameterValue;

    @Override
    public int getMessageType() {
        return RpcRequestMessage;
    }

    public RpcRequestMessage(int sequenceId, String interFaceName, String methodName, Class<?> returnType, Class[] parameterTypes, Object[] parameterValue) {
        super.setSequenceId(sequenceId);
        this.interFaceName = interFaceName;
        this.methodName = methodName;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        this.parameterValue = parameterValue;
    }

}
