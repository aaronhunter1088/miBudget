package com.miBudget.core;

import com.miBudget.daoimplementations.AccountDAOImpl;
import com.miBudget.daoimplementations.ItemDAOImpl;
import com.miBudget.daoimplementations.MiBudgetDAOImpl;
import com.miBudget.utilities.HibernateUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;

public class MiBudgetState {
    private static Logger LOGGER = LogManager.getLogger(MiBudgetState.class);

    public static HibernateUtilities hibernate;
    public static SessionFactory sessionFactory;
    public static MiBudgetDAOImpl miBudgetDAOImpl;
    public static ItemDAOImpl itemDAOImpl;
    public static AccountDAOImpl accountDAOImpl;
    private static boolean initialized = false;

    public static void initialize() {
        if (!isInitialized()) {
            LOGGER.info("Initializing miBudget...");
            hibernate = new HibernateUtilities();
            sessionFactory = hibernate.getSessionFactory();
            miBudgetDAOImpl = new MiBudgetDAOImpl(sessionFactory);
            itemDAOImpl = new ItemDAOImpl(sessionFactory);
            accountDAOImpl = new AccountDAOImpl(sessionFactory);
            MiBudgetState.initialized = true;
            LOGGER.info("Initialized miBudget...");
        }
        else { LOGGER.info("Initialize MiBudget"); }
    }

    public static HibernateUtilities getHibernate() { return hibernate; }
    public static SessionFactory getSessionFactory() { return sessionFactory; }
    public static MiBudgetDAOImpl getMiBudgetDAOImpl() { return miBudgetDAOImpl; }
    public static ItemDAOImpl getItemDAOImpl() { return itemDAOImpl; }
    public static AccountDAOImpl getAccountDAOImpl() { return accountDAOImpl; }
    public static boolean isInitialized() { return MiBudgetState.initialized; }
}
