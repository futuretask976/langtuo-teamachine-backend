package com.langtuo.teamachine.biz.service.testor.record;

import com.langtuo.teamachine.dao.helper.SqlSessionFactoryHelper;
import com.langtuo.teamachine.dao.mapper.record.MachineCodeSeqMapper;
import org.apache.ibatis.session.SqlSession;

public class MachineCodeTestor {
    public static void main(String args[]) {
        select();
    }

    public static void select() {
        SqlSession sqlSession = SqlSessionFactoryHelper.getSqlSession();
        MachineCodeSeqMapper mapper = sqlSession.getMapper(MachineCodeSeqMapper.class);

        long next = mapper.getNextSeqValue();
        System.out.println(next);

        sqlSession.commit();
        sqlSession.close();
    }
}
