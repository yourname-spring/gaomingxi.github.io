package com.rrs.intergration.cronjobs.performable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.rrs.core.util.CodeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.rrs.intergration.firstPurchase.dao.RrsFirstPurchaseDao;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

/**
 * 定时汇算客户冻结状态for jsh
 * 客户编码(不带admin,只要售达方编码)-客户名称-门店标识 产品协议 僵尸客户 窜货闸口 工程验收 虚假工程的冻结状态
 *
 * @author ZY
 * <p>
 * 2019年7月31日，性能优化 by Gaomx
 */
public class RrsAutoCustomerMoreSixMonthCustomerFreezedPerformable extends AbstractJobPerformable {

    private static final Logger LOG = Logger.getLogger(RrsAutoCustomerMoreSixMonthCustomerFreezedPerformable.class);

    @Resource(name = "rrsFirstPurchaseDao")
    private RrsFirstPurchaseDao rrsFirstPurchaseDao;

    @Override
    public PerformResult perform(CronJobModel arg0) {
        // 超6个月未交易僵尸客户数据统计并插入接口库样表汇算黑名单表
        try {
            LOG.info("RrsAutoCustomerFreezedStatusForJshPerformable===moreSixMonthCustomerFreezed=====start=======");
            moreSixMonthCustomerFreezed();
            LOG.info("RrsAutoCustomerFreezedStatusForJshPerformable===moreSixMonthCustomerFreezed=====end=======");
        } catch (Exception e) {
            LOG.error("RrsAutoCustomerFreezedStatusForJshPerformable====moreSixMonthCustomerFreezed====Exception：{}", e);
            return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
        }

        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }

    /**
     * 超6个月未交易僵尸客户标识汇算增量更新
     */
    public void moreSixMonthCustomerFreezed() {
        // 超6个月未交易僵尸客户数据
        Map<String, String> resultMap = this.rrsFirstPurchaseDao.getMoreSixMonthCustomerList(180);
        if (resultMap == null || resultMap.size() < 1) {
            return;
        }
        // 删除已有数据
        List<String> zombieCustomerList = rrsFirstPurchaseDao.listDjDontOrderCustomerByStatusAndFlag("1", "2",null);
        if (CollectionUtils.isNotEmpty(zombieCustomerList)) {
            for (String s : zombieCustomerList) {
                resultMap.remove(s);
            }
        }
        // 是否需要处理数据
        if (resultMap.size() < 1) {
            return;
        }
        // MAP转list
        List<String> todoCustomerList = new ArrayList<>();
        for (Map.Entry<String, String> m : resultMap.entrySet()) {
            todoCustomerList.add(m.getKey());
        }
        // 事务、性能优化
        int size = 900;
        int totalSize = todoCustomerList.size();
        int current = 0;
        while (true) {
            int starSize = current * size;
            int endSize = ((starSize + 1) * size) > totalSize ? totalSize : ((current + 1) * size);
            List<String> tmpList = todoCustomerList.subList(starSize, endSize);
            saveDjDontOrderCustomerList(tmpList);
            if (endSize == totalSize) {
                break;
            }
            current++;
        }
    }

    /**
     * 保存数据，PS：数据量够小 所以先分组了
     *
     * @param customerList 客户编码列表
     */
    private void saveDjDontOrderCustomerList(List<String> customerList) {
        try {
            // 1 查询需要更新的数据
            List<String> updateList = rrsFirstPurchaseDao.listDjDontOrderCustomerByStatusAndFlag("0", "2", customerList);
            // 2 update list
            if (CollectionUtils.isNotEmpty(updateList)) {
                this.rrsFirstPurchaseDao.updateDjDontOrderCustomerList("1", "2", updateList);
            }
            // 3 计算出需要插入的数据
            List<String> insertList = replaceUpdateCustomer(customerList, updateList);
            // 4 inset list

            if (CollectionUtils.isNotEmpty(insertList)) {
                this.rrsFirstPurchaseDao.insertDjDontOrderCustomerList("1", "2", insertList);
            }
        } catch (Exception e) {
            LOG.error("RrsAutoCustomerFreezedStatusForJshPerformable=moreSixMonthCustomerFreezed=Exception：{}", e);
        }
    }

    /**
     * 去除需要更新的客户
     *
     * @param customerList 所有客户
     * @param updateList   需要更新的客户
     * @return 需要插入的客户
     */
    private List<String> replaceUpdateCustomer(List<String> customerList, List<String> updateList) {
        Map<String, String> todoAllMap = new HashMap<>(CodeUtil.HASHMAP_INITIAL_CAPACITY_SIZE);
        for (String customer : customerList) {
            todoAllMap.put(customer, customer);
        }
        for (int i = 0; i < updateList.size(); i++) {
            String customer = todoAllMap.get(updateList.get(i));
            if (StringUtils.isBlank(customer)) {
                continue;
            }
            customerList.set(i, null);
        }
        for (int i = customerList.size() - 1; i >= 0; i--) {
            if (StringUtils.isBlank(customerList.get(i))) {
                customerList.remove(i);
            }
        }
        return customerList;
    }

}
