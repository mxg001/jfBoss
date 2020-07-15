package cn.eeepay.framework.serviceImpl.sys;

import cn.eeepay.framework.dao.sys.SequenceVipScoreDao;
import cn.eeepay.framework.model.sys.SeqData;
import cn.eeepay.framework.service.sys.RandomNumService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2019/7/3/003.
 * @author  liuks
 * 随机生成
 */
@Service("randomNumService")
public class RandomNumServiceImpl implements RandomNumService {

    @Resource
    private SequenceVipScoreDao sequenceVipScoreDao;

    /**
     * 通过插入新增表生成序列
     * @param start 开头字符
     * @param dataBaseName 数据库名
     * @param length 序列补零数量
     * @return
     */
    @Override
    public String getRandomNumberByData(String start, String dataBaseName, int length) {
        String str=null;
        if("vipScore".equals(dataBaseName)){
            SeqData seq=new SeqData();
            sequenceVipScoreDao.getSequence(seq);
            str=start+String.format("%0"+length+"d",seq.getId());
        }
        return str;
    }
}
