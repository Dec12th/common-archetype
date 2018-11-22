package com.benny.framework.common.framework.lang;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 领域对象转换器, 不可越级转换
 *
 * @author yin.beibei
 * @date 2018/11/22 14:28
 */
public interface DomainConvertor<INFO, DO, PO> {
    /**
     * 将领域对象转换为应用对象
     *
     * @param doObject 领域对象
     * @return 应用对象
     */
    INFO doToInfo(DO doObject);

    /**
     * 将应用对象转换为领域对象
     *
     * @param infoObject 应用对象
     * @return 领域对象
     */
    DO infoToDO(INFO infoObject);

    /**
     * 将持久对象转换为领域对象
     *
     * @param poObject 持久对象
     * @return 领域对象
     */
    DO poToDO(PO poObject);

    /**
     * 将领域对象转换为持久对象
     *
     * @param doObject 领域对象
     * @return 持久对象
     */
    PO doToPO(DO doObject);

    /**
     * 将领域对象转换为应用对象
     *
     * @param doObjects 领域对象
     * @return 应用对象
     */
    default List<INFO> doToInfo(List<DO> doObjects) {
        List<INFO> infoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(doObjects)) {
            for (DO doObject : doObjects) {
                infoList.add(doToInfo(doObject));
            }
        }
        return infoList;
    }

    /**
     * 将应用对象转换为领域对象
     *
     * @param infoObjects 应用对象
     * @return 领域对象
     */
    default List<DO> infoToDO(List<INFO> infoObjects) {
        List<DO> doList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(infoObjects)) {
            for (INFO info : infoObjects) {
                doList.add(infoToDO(info));
            }
        }
        return doList;
    }

    /**
     * 将持久对象转换为领域对象
     *
     * @param poObjects 持久对象
     * @return 领域对象
     */
    default List<DO> poToDO(List<PO> poObjects) {
        List<DO> doList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(poObjects)) {
            for (PO poObject : poObjects) {
                doList.add(poToDO(poObject));
            }
        }
        return doList;
    }

    /**
     * 将领域对象转换为持久对象
     *
     * @param doObjects 领域对象
     * @return 持久对象
     */
    default List<PO> doToPO(List<DO> doObjects) {
        List<PO> poList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(doObjects)) {
            for (DO doObject : doObjects) {
                poList.add(doToPO(doObject));
            }
        }
        return poList;
    }

}
