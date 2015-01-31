package mina.dao;

import java.util.List;

import mina.dao.entity.TestEntity;

public interface TestDao {
	
	public boolean saveTest(TestEntity testEntity);
	
	public TestEntity getTest(long id);
	
	public List<TestEntity> getUserTest(long userId);

}
