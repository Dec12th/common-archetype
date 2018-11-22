package com.benny.framework.common.framework.lang;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 上下层对象转换器, 只涵盖上下层模型转换<br>
 *
 * @author yin.beibei
 * @date 2018/11/22 14:25
 */
public interface ModelConvertor<TL, TU> {
    /**
     * 将下层模型转换为上层模型
     *
     * @param lowerModel 下层模型
     * @return 上层模型
     */
    TU convertToUpper(TL lowerModel);

    /**
     * 将上层模型转换为下层模型
     *
     * @param upperModel 上层模型
     * @return 下层模型
     */
    TL convertToLower(TU upperModel);

    /**
     * 将上层模型转换为下层模型列表
     *
     * @param upperModels 上层模型列表
     * @return 下层模型列表
     */
    default List<TL> convertToLower(List<TU> upperModels) {
        List<TL> lowerModels = new ArrayList<>();
        if (!CollectionUtils.isEmpty(lowerModels)) {
            for (TU ta : upperModels) {
                lowerModels.add(convertToLower(ta));
            }
        }

        return lowerModels;
    }

    /**
     * 将下层模型转换为上层模型列表
     *
     * @param lowerModels 下层模型列表
     * @return 上层模型列表
     */
    default List<TU> convertToUpper(List<TL> lowerModels) {
        List<TU> upperModels = new ArrayList<>();
        if (!CollectionUtils.isEmpty(lowerModels)) {
            for (TL lowerModel : lowerModels) {
                upperModels.add(convertToUpper(lowerModel));
            }
        }

        return upperModels;
    }
}
