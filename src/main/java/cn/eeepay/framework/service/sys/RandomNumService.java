package cn.eeepay.framework.service.sys;

/**
 * Created by Administrator on 2019/7/3/003.
 * @author liuks
 * 随机生成序列
 */
public interface RandomNumService {

    /**
     * 通过插入新增表生成序列
     * @param start 开头字符
     * @param dataBaseName 数据库名
     * @param length 序列补零数量
     * @return
     */
    String getRandomNumberByData(String start,String dataBaseName,int length);
}
