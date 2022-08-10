package org.itkk.udf.core.utils;

import org.itkk.udf.core.constant.IdWorkerConstant;
import org.itkk.udf.core.domain.id.Id;

/**
 * IdUtil
 */
public class IdUtil {

    /**
     * 私有化构造函数
     */
    private IdUtil() {

    }

    /**
     * 反解析ID
     *
     * @param id id
     * @return id信息
     */
    public static Id reverse(long id) {
        Id reverseId = new Id();
        reverseId.setSequence((id) & ~(-1L << IdWorkerConstant.SEQUENCE_BITS));
        reverseId.setDwId((id >> (IdWorkerConstant.WORKER_ID_SHIFT)) & ~(-1L << (IdWorkerConstant.DATACENTER_ID_BITS + IdWorkerConstant.WORKER_ID_BITS)));
        reverseId.setWorkerId((id >> IdWorkerConstant.WORKER_ID_SHIFT) & ~(-1L << IdWorkerConstant.WORKER_ID_BITS));
        reverseId.setDatacenterId((id >> IdWorkerConstant.DATACENTER_ID_SHIFT) & ~(-1L << IdWorkerConstant.DATACENTER_ID_BITS));
        reverseId.setTimestamp((id >> IdWorkerConstant.TIMESTAMP_LEFT_SHIFT) + IdWorkerConstant.TWEPOCH);
        return reverseId;
    }

}
