//package com.benny.framework.common.framework.lang.comand;
//
//import com.benny.framework.common.framework.lang.enums.BaseEnum;
//
//import java.util.function.Function;
//import java.util.function.Supplier;
//
///**
// * T: 继承{@link BaseEnum}的枚举类，根据此枚举获取对应的命令
// * K: 命令执行业务主逻辑的参数类型
// * R: 命令执行业务主逻辑的返回参数类型
// *
// * @author yin.beibei
// * @date 2018/11/23 16:05
// */
//public abstract class AbstractComand<T extends BaseEnum,K,R> implements Comand<T> {
//
//    /**
//     * @param k 命令执行业务主逻辑的参数类型
//     * @return R 命令执行业务主逻辑的返回参数类型,execute(k).get();
//     */
//    public  Object execute(Object k) {
//        return function.apply(k);
//    }
//
//    public  Function<K,R> function;
//
//    public void  setExecutor(Executor<K,R> executor) {
//        function = executor;
//    }
//}
