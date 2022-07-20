package com.miBudget.daoimplementations;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.miBudget.entities.Account;
import com.miBudget.entities.Item;
import com.miBudget.entities.ItemAccountObject;
import com.miBudget.entities.User;
import com.miBudget.entities.UserAccountObject;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountDAOImpl {

	private static Logger LOGGER = LogManager.getLogger(AccountDAOImpl.class);
	private SessionFactory factory;

	@Autowired
	public AccountDAOImpl(SessionFactory factory) {
		this.factory = factory;
    }
    
	// TODO: think of a better name and incorporate user.
	/**
	 * This method returns all the accountIds from a specific item for a specific user.
	 * @param item
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public List<String> getAccountIdsFromUser(Item item) {
    	ArrayList<String> accountIds = null;
    	Session session = null;

    	Transaction t = null;
    	try {
    		LOGGER.info("Attempting to execute getAccountIdsFromUser...");

    		session = factory.openSession();
    		t = session.beginTransaction();
    		accountIds = (ArrayList<String>) session
    				            .createNativeQuery("SELECT account_id FROM accounts " +
    								               "WHERE item__id = " + item.getId())
    											   .getResultList();
    		LOGGER.info("Query executed!");
    		t.commit();
    		session.close();
    	} catch (Exception e) {
    		LOGGER.error("Error executing getAccountIdsFromUser...");
    		LOGGER.error(e.getMessage());
			t.rollback();
			session.close();
		}
    	return accountIds;
    }
    
    /**
     * This method will return a List<String> object that
     * has all the accountIds for a specific user.
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<String> getAccountIdsFromUser(int userId) {
    	List<String> userAccounts = new ArrayList<>();

    	Session session = null;
    	Transaction t = null;
		try {
			LOGGER.info("Attempting to execute getAccountIdsFromUser query...");

			session = factory.openSession();
			t = session.beginTransaction();
			List<?> userAccountsFromDB = session
					   .createNativeQuery("SELECT account_id FROM user_accounts " +
					   						 "WHERE user_id = \'" + userId + "\'")
					   .getResultList();
			LOGGER.info("Query executed!");
			t.commit();
			session.close();
			if (userAccountsFromDB.size() == 0) {
				System.out.println("This user currently doesn't have any accounts added.");
				return userAccounts;
			} else {
				for (String accountId : (List<String>) userAccountsFromDB) {
					userAccounts.add(accountId);
					LOGGER.info("accountId: " + accountId);
				}
				LOGGER.info("user accounts list populated from AccountDAOImpl");
				return userAccounts;
			}
		} catch (Exception e) {
			LOGGER.error("Error connecting to DB");
			LOGGER.error(e.getMessage());
			t.rollback();
			session.close();
		} 
		return userAccounts;
    }
    
    @SuppressWarnings("unchecked")
	public ArrayList<String> getAccountIdsFromUser(User user) {
    	ArrayList<String> usersAccounts = new ArrayList<>();

    	Session session = null;
    	Transaction t = null;
		try {
			LOGGER.info("Attempting to execute getAccountIdsForUser + " + user.getFirstName() + " " + user.getLastName() + "...");

			session = factory.openSession();
			t = session.beginTransaction();
			usersAccounts = (ArrayList<String>) session
					   .createNativeQuery("SELECT account_id FROM user_accounts " +
							   			  "WHERE user_id = " + user.getId() )
					   .getResultList();
			LOGGER.info("Query executed!");
			t.commit();
			session.close();
			if (usersAccounts.size() == 0) {
				LOGGER.info(user.getFirstName() + " " + user.getLastName() + " currently doesn't have any accounts added.");
				return usersAccounts; // TODO: maybe return an empty list...
			} else {
				for (String acctId : usersAccounts) {
					LOGGER.info("accountId: " + acctId);
				}
				LOGGER.info("accountsIds list populated from AccountDAOImpl\n");
				return usersAccounts;
			}
		} catch (Exception e) {
			LOGGER.error("Error connecting to DB: " + e.getMessage());
			session.close();
		} 
		return usersAccounts;
    }

    public int addAccountObjectToAccountsTableDatabase(Account account) {

    	Session session = null;
    	Transaction t = null;
    	try {
    		LOGGER.info("Attempting to execute insert account query...");
    		LOGGER.info(account);

			session = factory.openSession();
			t = session.beginTransaction();
			session.save(account);
			LOGGER.info("Query executed!");
			t.commit();
			session.close();
			return 1; // good
		} catch (Exception e) {
			LOGGER.error("Error saving the AccountObject to the AccountsTable");
			t.rollback();
			session.close();
		} 
    	return 0; // bad
    }
    
    public int addAccountObjectToUsersAccountsTable(Account account, User user) {

    	Session session = null;
    	Transaction t = null;
    	try {
    		LOGGER.info("Attempting to execute addAccountToUsersAccounts Table query...");

			session = factory.openSession();
			t = session.beginTransaction();
			session.saveOrUpdate(new UserAccountObject(
					user.getId(),
					account.getAccountId(),
					account.getItemId(),
					account.getAvailableBalance(),
					account.getCurrentBalance(),
					account.get_limit(),
					account.getCurrencyCode(),
					account.getAccountName(),
					account.getOfficialName(),
					account.getMask(),
					account.get_type(),
					account.getSubType()
					));
			LOGGER.info("Inserted account into UsersAccounts.");
			t.commit();
			session.close();
			return 1; // good
    	} catch (Exception e) {
    		LOGGER.error("There was an error adding the AccountObject to the UsersAccountsTable!");
    		t.rollback();
    		session.close();
    	}
    	return 0; // bad
    }
    
    /**
     * Use this method, getAllAccounts, when you need to get all accounts' info's from one user
     * @param accountIds
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Account> getAllAccounts(List<String> accountIds) {
    	Session session = null;
		Transaction t = null;
		ArrayList<Account> accountsResult = new ArrayList<>();
		StringBuilder str = new StringBuilder();
    	if (accountIds.size() != 0) {
			str.append("'" + accountIds.get(0) + "'");
    		for (int i = 1; i < accountIds.size(); i++)
    			str.append(", '" + accountIds.get(i) + "'");
    	} else {
    		LOGGER.info("Query not attempted. No accounts!");
    		return accountsResult;
    	}
    	LOGGER.info("strBuilder: " + str);
    	try {
    		LOGGER.info("Attempting to execute getAllAccounts from user query...");
    		session = factory.openSession();
			t = session.beginTransaction();
			accountsResult = (ArrayList<Account>) session
					.createNativeQuery("SELECT * FROM accounts " +
									   "WHERE account_id " +
									   "IN (" +  str + ")")
					.addEntity(Account.class).getResultList();
			session.getTransaction().commit();
			session.close();
			LOGGER.info("Query executed!");
		} catch (HibernateException e) {
			LOGGER.error("Error during retrieval of next account or adding account to accounts list.");
			LOGGER.error(e.getMessage());
			t.rollback();
			session.close();
    	} 
		ArrayList<Account> accounts = new ArrayList<>();
		for (int i = 0; i < accountsResult.size(); i++) {
			//System.out.println("acc: " + accountsResult.get(i));
			accounts.add(accountsResult.get(i));
		}
		
		return accounts;
		
		/**
		 * Hibernate: 
			    SELECT
			        * 
			    FROM
			        accounts 
			    WHERE
			        account_id = 'D7zZvPBJn9uyk5WA3gr1CWgLJJRaDbfvwQ8rB'
			account received: [[Ljava.lang.Object;@4fba8eec]
			
		 * Solution: See above implementation
		 */
    }
    
    @SuppressWarnings("unchecked")
	public ArrayList<Account> getAllAccountsForItem(Long itemTableId) {
    	ArrayList<Account> accounts = new ArrayList<>();

    	Session session = null;
    	Transaction t = null;
    	try {
    		LOGGER.info("Attempting to getAllAccountsForItem using itemTableId query...");

			session = factory.openSession();
			t = session.beginTransaction();
			List<Account> userAccountsFromDB = (List<Account>) session
					   .createNativeQuery("SELECT * FROM accounts " +
							   		     "WHERE item__id = " + itemTableId)
					   .addEntity(Account.class)
					   .getResultList();
			LOGGER.info("getAllAccountsForItem query executed!");
			t.commit();
			session.close();
			
			for (Object o : userAccountsFromDB) {
				accounts.add((Account) o);
				System.out.println("Returning account: " + ((Account) o));
			}	
    	} catch (HibernateException e) {
    		LOGGER.info("hibernate error: " + e.getMessage());
    	}	
//    	} catch (Exception e) {
//    		System.out.println("Error connecting to DB");
//			System.out.println(e.getStackTrace());
//			t.rollback();
//			session.close();
//    	}
    	return accounts;	
    }
    
    @SuppressWarnings("unchecked")
    public ArrayList<UserAccountObject> getAllUserAccountObjectsFromUserAndItemTableId(User user, long itemTableId) {
    	ArrayList<UserAccountObject> accounts = new ArrayList<>();

    	Session session = null;
    	Transaction t = null;
    	try {
    		LOGGER.info("Attempting to execute getAllUsersAccounts for " + user.getFirstName() + " " + user.getLastName());

			session = factory.openSession();
			t = session.beginTransaction();
			List<UserAccountObject> userAccountsFromDB = null;
			if (itemTableId == 0) {
				userAccountsFromDB = (List<UserAccountObject>) session
						.createNativeQuery("SELECT * FROM user_accounts " +
						   		   		   "WHERE user_id = " + user.getId())
						.addEntity(UserAccountObject.class).getResultList();
			} else {
				userAccountsFromDB = (List<UserAccountObject>) session
						.createNativeQuery("SELECT * FROM user_accounts " +
								   		   "WHERE userId = " + user.getId() + " " +
								   		   "AND itemId = " + itemTableId)
						.addEntity(UserAccountObject.class).getResultList();
			}
			LOGGER.info("Query executed!");
			for (Object id : userAccountsFromDB) {
				accounts.add((UserAccountObject) id);
			}
			LOGGER.info("Returning " + accounts.size() + " accounts for {}", itemTableId != 0 ? itemTableId : user.getFirstName());
			session.close();	
    	} catch (Exception e) {
    		LOGGER.info("Error connecting to DB: " + e.getMessage());
			t.rollback();
			session.close();
    	}
    	return accounts;
    	
    }
    
    @SuppressWarnings("unchecked")
	public ArrayList<UserAccountObject> getAllOfUsersAccounts() {
    	ArrayList<UserAccountObject> accounts = new ArrayList<>();

    	Session session = null;
    	Transaction t = null;
    	try {
    		LOGGER.info("Attempting to execute getAllOfUsersAccounts query...");

			session = factory.openSession();
			t = session.beginTransaction();
			List<UserAccountObject> userAccountsFromDB = session
					   .createNativeQuery("SELECT * FROM user_accounts")
					   .addEntity(UserAccountObject.class)
					   .getResultList();
			LOGGER.info("Query executed!");
			session.close();
			for (Object id : userAccountsFromDB) {
				accounts.add((UserAccountObject) id);
			}	
    	} catch (Exception e) {
    		LOGGER.info("Error connecting to DB: " + e.getMessage());
			t.rollback();
			session.close();
    	}
    	return accounts;
    }
    
    /**
     * This method returns all the account_ids stored in the users_accounts table.
     * @param user
     * @return
     */
    public List<String> getAllAccountsIds(User user) {
    	List<String> accountIds = new ArrayList<>();

    	Session session = null;
    	Transaction t = null;
    	try {
    		LOGGER.info("Attempting to execute getAllAccountIds query...");

			session = factory.openSession();
			t = session.beginTransaction();
			List<?> userAccountsFromDB = session
					   .createNativeQuery("SELECT account_id FROM user_accounts " +
							   			  "WHERE user_id = " + user.getId() )
					   .getResultList();
			LOGGER.info("Query executed!");
			t.commit();
			session.close();
			
			for (Object id : userAccountsFromDB) {
				accountIds.add((String) id);
			}	
    	} catch (Exception e) {
    		LOGGER.info("Error connecting to DB: " + e.getMessage());
			t.rollback();
			session.close();
    	}
    	return accountIds;
    }
    
    public int addAccountIdToItemsAccountsTable(long itemTableId, String accountId) {

    	Session session = null;
    	Transaction t = null;
    	try {
    		ItemAccountObject iaObj = new ItemAccountObject(itemTableId, accountId);
    		LOGGER.info("Attempting to insert: " + iaObj + " into the ItemsAccounts table...");

			session = factory.openSession();
			t = session.beginTransaction();
			session.save(iaObj);
			t.commit();
			session.close();
			return 1; // good
		} catch (Exception e) {
			LOGGER.info("Error connecting to Database: " + e.getMessage());
			e.printStackTrace(System.out);
			t.rollback();
			session.close();
		} 
    	return 0; // bad
    }
    
    
    public boolean deleteAccountFromDatabase(Account account) {

    	Session session = null;
    	Transaction t = null;
    	
    	try {
    		LOGGER.info("Attempting to execute delete account from database query...");

    		session = factory.openSession();
    		t = session.beginTransaction();
    		session.delete(account);
    		session.getTransaction().commit();
    		String name = account.getOfficialName() != null ? account.getOfficialName() : account.getAccountName();
    		LOGGER.info(name + " was deleted!");
    		session.close();
    		return true;
    	} catch (HibernateException e) {
    		LOGGER.info("Error performing hibernate action: " + e.getMessage());
    		t.rollback();
    		session.close();
    		return false;
    	} catch (Exception e) {
    		LOGGER.info("Error connecting to DB: " + e.getMessage());
			t.rollback();
			session.close();
			return false; // bad
    	} 
    }
    
    /**
     * This method will delete all accounts from the accounts
     * table from a given Item.
     */
    public int deleteAccountsFromDatabase(ArrayList<Account> accounts) {

    	Session session = null;
    	Transaction t = null;
    	try {
    		LOGGER.info("Attempting to execute delete accounts query...");

    		session = factory.openSession();
    		
    		//StringBuilder allAccountIdsString = new StringBuilder();
    		//allAccountIdsString.append("'");
    		//accounts.forEach(acct -> {
    		//	allAccountIdsString.append(acct.getAccountId() + "', ");
    		//});
    		//allAccountIdsString.deleteCharAt(allAccountIdsString.length()-1); // removes last space
    		//allAccountIdsString.deleteCharAt(allAccountIdsString.length()-1); // removes last comma
    		//session.createQuery("DELETE FROM account " + 
    		//					"WHERE account_id IN (" + allAccountIdsString + ")");
    		for(int i = 0; i < accounts.size(); i++) {
    			String delAcct = accounts.get(i).getAccountName();
    			t = session.beginTransaction();
    			session.delete(accounts.get(i));
    			t.commit();
    			LOGGER.info(delAcct + " was deleted!");
    		}
    		session.close();
    		return 1;
    	} catch (HibernateException e) {
    		LOGGER.error("Error performing hibernate action: " + e.getMessage());
    		t.rollback();
    		session.close();
    		return 0;
    	} catch (Exception e) {
    		LOGGER.info("Error connecting to DB: " + e.getMessage());
			t.rollback();
			session.close();
			return 0; // bad
    	} 
    }
    
    
    
    public boolean deleteFromItemsAccounts(Item item, String accountId) {

    	Session session = null;
    	Transaction t = null;
    	try {
    		LOGGER.info("Attempting to delete ItemsAccountsObject from items_accounts table...");

    		session = factory.openSession();
	    	t = session.beginTransaction();
			// Items_accounts table
			// delete from items_accounts where item_table_id = 963;
			session.createQuery("DELETE FROM ItemAccountObject " + 
			    				"WHERE itemId = \'" + item.getId() + "\'" +
			    				"AND accountId = \'" + accountId + "\'").executeUpdate();
			LOGGER.info("... account was deleted from items_accounts table...");
			t.commit();
			session.close();
			return true;
			
		} catch (HibernateException e) {
			LOGGER.error("Error performing hibernate action: " + e.getMessage());
			StackTraceElement[] err = e.getStackTrace();
			for(int i=0; i<err.length; i++) { System.out.println(err[i]); }
			t.rollback();
			session.close();
			return false;
		} catch (Exception e) {
			LOGGER.info("Error connecting to DB: " + e.getMessage());
			t.rollback();
			session.close();
			return false; // bad
		}
    }
    
    
    public boolean deleteAccountFromUsersAccounts(String accountId, Item item, User user) {

    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to execute delete account from users_accounts table...");

    		session = factory.openSession();
    		t = session.beginTransaction();
    		// Users_institutions_ids table
    		// delete from users_institution_ids where user_id = 20 and institution_id = 'ins_3';
    		session.createQuery("DELETE FROM UserAccountObject " +
								   "WHERE accountId = \'" + accountId + "\' " +
					  			   "AND item__id = " + item.getId()).executeUpdate();

    		System.out.println("... account was deleted from users_accounts table...");
    		t.commit();
    		session.close();
    		return true;
    	} catch (HibernateException e) {
    		System.out.println("Error performing hibernate action.");
    		System.out.println(e.getMessage());
    		t.rollback();
    		session.close();
    		return false;
    	} catch (Exception e) {
    		System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			t.rollback();
			session.close();
			return false; // bad
    	}
    }
}
