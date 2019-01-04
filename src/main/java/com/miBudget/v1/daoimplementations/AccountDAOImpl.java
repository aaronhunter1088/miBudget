package com.miBudget.v1.daoimplementations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.miBudget.v1.entities.Account;
import com.miBudget.v1.entities.Item;
import com.miBudget.v1.entities.ItemAccountObject;
import com.miBudget.v1.entities.User;
import com.miBudget.v1.entities.UserAccountObject;
import com.miBudget.v1.utilities.HibernateUtilities;

public class AccountDAOImpl {

	
	
	public AccountDAOImpl() {
    }
    
    @SuppressWarnings("unchecked")
	public List<String> getAccountIdsFromUser(Item item) {
    	ArrayList<String> accountIds = null;
    	Session session = null;
    	SessionFactory factory = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to execute getAccountIdsFromUser...");
    		factory = HibernateUtilities.getSessionFactory();
    		session = factory.openSession();
    		t = session.beginTransaction();
    		accountIds = (ArrayList<String>) session
    				            .createNativeQuery("SELECT account_id FROM accounts " +
    								               "WHERE item_table_id = " + item.getItemTableId())
    											   .getResultList();
    		t.commit();
    		session.close();
    	} catch (Exception e) {
			System.out.println("Error executing getAccountIdsFromUser...");
			System.out.println(e.getMessage());
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
    public List<String> getAccountIdsFromUser(int userId) {
    	List<String> userAccounts = new ArrayList<>();
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
		try {
			System.out.println("\nAttempting to execute getAccountIdsFromUser query...");
			factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			List<?> userAccountsFromDB = session
					   .createNativeQuery("SELECT account_id FROM users_accounts ")
					   .getResultList();
			System.out.println("Query executed!");
			t.commit();
			session.close();
			if (userAccountsFromDB.size() == 0) {
				System.out.println("This user currently doesn't have any accounts added.");
				return userAccounts;
			} else {
				Iterator<?> iterator = userAccountsFromDB.iterator();
				while (iterator.hasNext()) { 
					String account_id = iterator.next().toString();
					userAccounts.add(account_id);
					System.out.println("account_id: " + account_id);
				}
				System.out.println("user accounts list populated from AccountDAOImpl\n");
				return userAccounts;
			}
		} catch (Exception e) {
			System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			t.rollback();
			session.close();
		} 
		return userAccounts;
    }
    
    public List<String> getAccountIdsFromUser(User user) {
    	List<String> userAccounts = new ArrayList<>();
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
		try {
			System.out.println("\nAttempting to execute getAccountIdsFromUser query...");
			factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			List<?> userAccountsFromDB = session
					   .createNativeQuery("SELECT account_id FROM users_accounts " +
							   			  "WHERE user_id = " + user.getId() )
					   .getResultList();
			System.out.println("Query executed!");
			t.commit();
			session.close();
			if (userAccountsFromDB.size() == 0) {
				System.out.println("This user currently doesn't have any accounts added.");
				return userAccounts;
			} else {
				Iterator<?> iterator = userAccountsFromDB.iterator();
				while (iterator.hasNext()) { 
					String account_id = iterator.next().toString();
					userAccounts.add(account_id);
					System.out.println("account_id: " + account_id);
				}
				System.out.println("accountsIds list populated from AccountDAOImpl\n");
				return userAccounts;
			}
		} catch (Exception e) {
			System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			session.close();
		} 
		return userAccounts;
    }

    public int addAccountObjectToAccountsTableDatabase(com.miBudget.v1.entities.Account account) {
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
			System.out.println("\nAttempting to execute insert account query...");
			factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			
//			com.v1.miBudget.entities.Account myAccount = new com.v1.miBudget.entities.Account(
//					account.getAccountId(),
//					item.getItemTableId(),
//					account.getAvailableBalance() != 0 ? account.getAvailableBalance() : 0.0,
//					account.getCurrentBalance() != 0 ? account.getCurrentBalance() : 0.0,
//					account.getLimit() != 0 ? account.getLimit() : 0.0,
//					account.getCurrencyCode() != null ? account.getCurrencyCode() : "USD",
//					account.getNameOfAccount() != null ? account.getNameOfAccount() : "",
//					account.getOfficialName() != null ? account.getOfficialName() : "",
//					account.getMask(),
//					account.getType(),
//					account.getSubType()
//			);
			System.out.println("Account: " + account);
			
			session.save(account);
			t.commit();
			session.close();
			return 1; // good
		} catch (Exception e) {
			System.out.println("Error connecting to Database");
			System.out.println(e.getMessage());
			e.printStackTrace(System.out);
			t.rollback();
			session.close();
		} 
    	return 0; // bad
    }
    
    public int addAccountObjectToUsers_AccountsTable(com.miBudget.v1.entities.Account account, User user) {
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to execute addAccountToUsers_Table query...");
    		factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			
			
			
			// Using SQL code here because there is no object for users_accounts.
//			int insert = session.createNativeQuery("INSERT INTO users_accounts ('user_id', 'account_id') " +
//									  "VALUES (" + account.getAccountId() + "), (" + user.getId() + ")").executeUpdate();
			session.saveOrUpdate(new UserAccountObject(
					user.getId(), 
					account.getAccountId(),
					account.getItemTableId(),
					account.getAvailableBalance(),
					account.getCurrentBalance(),
					account.getLimit(),
					account.getCurrencyCode(),
					account.getNameOfAccount(),
					account.getOfficialName(),
					account.getMask(),
					account.getType(),
					account.getSubType()
					));
			System.out.println("Inserted account into users_accounts");
			t.commit();
			session.close();
			return 1; // good
    	} catch (Exception e) {
    		e.printStackTrace(System.out);
    		t.rollback();
    		session.close();
    	}
    	return 0; // bad
    }
    
    /**
     * Use this method, getAllAccounts, when you need to get all accounts' info's from one user
     * @param user
     * @param account_ids
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Account> getAllAccounts(ArrayList<String> accountIds) {
    	SessionFactory factory = HibernateUtilities.getSessionFactory();
    	Session session = null;
		Transaction t = null;
    	try {
    		session = factory.openSession();
    	} catch (HibernateException e) {
    		System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
    	}
    	
    	Iterator<String> iter = accountIds.iterator();
    	StringBuilder str = new StringBuilder();
    	str.append("'" + iter.next() + "'");
    	while (iter.hasNext()) {
    		str.append(", '" + iter.next() + "'");
    	}
    	System.out.println("strBuilder: " + str);
    	List<Account> accountsResult = null;
    	
		try {
    		System.out.println("\nAttempting to execute getAllAccounts from user query...");
			t = session.beginTransaction();
			accountsResult = (List<Account>) session
					.createNativeQuery("SELECT * FROM accounts " +
									   "WHERE account_id " +
									   "IN (" +  str + ")")
					.addEntity(Account.class).getResultList();
			t.commit();
			session.close();
			System.out.println("Query executed!");
		} catch (HibernateException e) {
    		System.out.println("Error during retrieval of next account or adding account to accounts list.");
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
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
	public ArrayList<Account> getAllOfItemsAccounts(int itemTableId) {
    	ArrayList<Account> accounts = new ArrayList<>();
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to execute getAllOfItemsAccounts using item_table_id query...");
    		factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			List<Account> userAccountsFromDB = (List<Account>) session
					   .createNativeQuery("SELECT * FROM accounts " +
							   		     "WHERE item_table_id = " + itemTableId)
					   .addEntity(Account.class)
					   .getResultList();
			System.out.println("getAllOfItemsAccounts query executed!");
			t.commit();
			session.close();
			
			for (Object id : userAccountsFromDB) {
				accounts.add((Account) id);
				System.out.println("Returning account: " + ((Account) id));
			}	
    	} catch (HibernateException e) {
    		System.out.println("hibernate error:");
    		System.out.println(e.getMessage());
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
	public ArrayList<UserAccountObject> getAllOfUsersAccounts() {
    	ArrayList<UserAccountObject> accounts = new ArrayList<>();
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to execute getAllOfUsersAccounts query...");
    		factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			List<UserAccountObject> userAccountsFromDB = session
					   .createNativeQuery("SELECT * FROM users_accounts")
					   .addEntity(UserAccountObject.class)
					   .getResultList();
			System.out.println("Query executed!");
			//t.commit();
			session.close();
			
			for (Object id : userAccountsFromDB) {
				accounts.add((UserAccountObject) id);
			}	
    	} catch (Exception e) {
    		System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
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
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to execute getAllAccountIds query...");
    		factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			List<?> userAccountsFromDB = session
					   .createNativeQuery("SELECT account_id FROM users_accounts " +
							   			  "WHERE user_id = " + user.getId() )
					   .getResultList();
			System.out.println("Query executed!");
			t.commit();
			session.close();
			
			for (Object id : userAccountsFromDB) {
				accountIds.add((String) id);
			}	
    	} catch (Exception e) {
    		System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			t.rollback();
			session.close();
    	}
    	return accountIds;
    }
    
    public int addAccountIdToItems_AccountsTable(int itemTableId, String accountId) {
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
			System.out.println("\nAttempting to execute insert accountId to Items_Accounts query...");
			factory = HibernateUtilities.getSessionFactory();
			session = factory.openSession();
			t = session.beginTransaction();
			
			ItemAccountObject obj = new ItemAccountObject(itemTableId, accountId);
			System.out.println("obj: " + obj);
			
			session.save(obj);
			t.commit();
			session.close();
			return 1; // good
		} catch (Exception e) {
			System.out.println("Error connecting to Database");
			System.out.println(e.getMessage());
			e.printStackTrace(System.out);
			t.rollback();
			session.close();
		} 
    	return 0; // bad
    }

    /**
     * TODO: Implement
     * This method will delete one account from the accounts 
     * table for a given Item.
     * @param account
     * @return
     */
    public int deleteAccountFromDatabase(Account account) {
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to execute delete account query...");
    		factory = HibernateUtilities.getSessionFactory();
    		session = factory.openSession();
    		t = session.beginTransaction();
    		// TODO: Implement
    		// t.commit();
    		session.close();
    		return 1;
    	} catch (HibernateException e) {
    		System.out.println("Error performing hibernate action.");
    		System.out.println(e.getMessage());
    		t.rollback();
    		session.close();
    		return 0;
    	} catch (Exception e) {
    		System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			t.rollback();
			session.close();
			return 0; // bad
    	}
    }
    
    /**
     * This method will delete all accounts from the accounts
     * table from a given Item.
     */
    public int deleteAccountsFromDatabase(ArrayList<Account> accounts) {
    	SessionFactory factory = null;
    	Session session = null;
    	Transaction t = null;
    	try {
    		System.out.println("\nAttempting to execute delete accounts query...");
    		factory = HibernateUtilities.getSessionFactory();
    		session = factory.openSession();
    		
//    		StringBuilder allAccountIdsString = new StringBuilder();
//    		allAccountIdsString.append("'");
//    		accounts.forEach(acct -> {
//    			allAccountIdsString.append(acct.getAccountId() + "', ");
//    		});
//    		allAccountIdsString.deleteCharAt(allAccountIdsString.length()-1); // removes last space
//    		allAccountIdsString.deleteCharAt(allAccountIdsString.length()-1); // removes last comma
//    		session.createQuery("DELETE FROM account " + 
//    							"WHERE account_id IN " + allAccountIdsString + "");
    		for(int i = 0; i < accounts.size(); i++) {
    			String delAcct = accounts.get(i).getNameOfAccount();
    			t = session.beginTransaction();
    			session.delete(accounts.get(i));
    			t.commit();
    			System.out.println(delAcct + " was deleted!");
    		}
    		session.close();
    		return 1;
    	} catch (HibernateException e) {
    		System.out.println("Error performing hibernate action.");
    		System.out.println(e.getMessage());
    		t.rollback();
    		session.close();
    		return 0;
    	} catch (Exception e) {
    		System.out.println("Error connecting to DB");
			System.out.println(e.getMessage());
			t.rollback();
			session.close();
			return 0; // bad
    	} 
    }
}
