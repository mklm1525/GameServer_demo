package mina.dao.imp;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import mina.dao.TestDao;
import mina.dao.entity.TestEntity;
import mina.db.HibernateSessionFactory;

public class TestDaoImp implements TestDao {

	@Override
	public boolean saveTest(TestEntity testEntity) {
		return HibernateSessionFactory.saveOrUpdateObject(testEntity);
	}

	@Override
	public TestEntity getTest(long id) {
		if(id < 0){
			return null;
		}
		Session session = HibernateSessionFactory.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from TestEntity as t where t.id=?");
			query.setLong(0, id);
			
			TestEntity testEntity = (TestEntity) query.setMaxResults(1).uniqueResult();
			if(testEntity == null){
				return null;
			}
			
			tx.commit();
			return testEntity;
		}finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TestEntity> getUserTest(long userId) {
		if(userId < 0){
			return null;
		}
		Session session = HibernateSessionFactory.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from TestEntity as t where t.userId=?");
			query.setLong(0, userId);
			List<TestEntity> applyGuildList = (List<TestEntity>)query.list();
			tx.commit();
			return applyGuildList;
		}finally {
			session.close();
		}
	}

}
