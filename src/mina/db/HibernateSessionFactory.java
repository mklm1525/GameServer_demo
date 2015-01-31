package mina.db;

import mina.dao.entity.TestEntity;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateSessionFactory {
	// private static Log log =
	// LogFactory.getLog(HibernateSessionFactory.class);

	// Path of configuration file
	private static String CONFIG_FILE_LOCATION = "/hibernate.cfg.xml";
	private static String configFile = CONFIG_FILE_LOCATION;

	// Use ThreadLocal to control Session object
	private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
	private static Configuration configuration;
	private static org.hibernate.SessionFactory sessionFactory;
	
	static {
		try {
			configuration = new Configuration();
			configuration.configure(configFile);
//			String url = DbItem.class.getProtectionDomain().getCodeSource().getLocation().getFile();
//			int index = url.indexOf("bin");
//			if(index > 0){
//				url = url.substring(0, url.indexOf("bin")) + "bin/hibernate.cfg.xml";
//			}
//			index = url.indexOf("x3server.jar");
//			if(index > 0){
//				url = url.substring(0, url.indexOf("x3server.jar")) + "x3server.jar/hibernate.cfg.xml";
//			}
//			
//			File file = new File(url);
			ServiceRegistryBuilder serviceRegistryBuilder = new ServiceRegistryBuilder();
			ServiceRegistry serviceRegistry = serviceRegistryBuilder.applySettings(configuration.getProperties()).buildServiceRegistry();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
//			DBLogger.info("Load Hibernate Config Success!");
		} catch (HibernateException e) {
//			DBLogger.error("Load Hibernate Config Failed!",e);
			e.printStackTrace();
		}
	}

	/**
	 * Abstraction: Obtain Session
	 */
	public static Session getSession() throws HibernateException {
		Session session = threadLocal.get();

		// Rebulid Session object if there is no session in ThreadLocal
		if (session == null || !session.isOpen()) {
			if (sessionFactory == null) {
				rebuildSessionFactory();
			}
			// Obtain Session object
			session = (sessionFactory != null) ? sessionFactory.openSession() : null;
			threadLocal.set(session);
		}
		return session;
	}

	/**
	 * Abstract: Build SessionFactory object
	 */
	public static void rebuildSessionFactory() {
		try {
			// Initial application using configuration file
			configuration = new Configuration();
			configuration.configure(configFile);
			// Create SessionFactory object according to the configuration
			// Data model can be created in MySQL automatically after execute
			// this method
			ServiceRegistryBuilder serviceRegistryBuilder = new ServiceRegistryBuilder();
			ServiceRegistry serviceRegistry = serviceRegistryBuilder.applySettings(configuration.getProperties()).buildServiceRegistry();
			sessionFactory = configuration.configure().buildSessionFactory(serviceRegistry);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Abstraction: Close Session object
	 */
	public static void closeSession() throws HibernateException {
		Session session = (Session) threadLocal.get();
		threadLocal.set(null);
		if (session != null) {
			session.close();
		}
	}
	
	public static void loadHibernate() {
	}

	public static boolean saveOrUpdateObject(Object object) {
//		DBLogger.debug("HibernateUtil saveOrUpdateObject:" + obj.getClass());
		Session session = HibernateSessionFactory.getSession();
		Transaction transaction = null; 
		try {
			transaction = session.beginTransaction();
			session.saveOrUpdate(object);
			transaction.commit();
			return true;
		}
		catch(Exception e){
//			DBLogger.error("saveOrUpdateObject() error", e);
			transaction.rollback();
			return false;
		} finally {
			session.close();
		}
	}

}