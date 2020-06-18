package com.it.ssm.dao;

import com.it.ssm.domain.Member;
import org.apache.ibatis.annotations.Select;


public interface IMemberDao {
    @Select("select * from member where id=#{id}")
    public Member findById(String id) throws Exception;
}
