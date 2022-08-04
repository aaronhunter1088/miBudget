package com.miBudget.controllers;


import com.miBudget.core.Constants;
import com.miBudget.daos.*;
import com.miBudget.entities.Account;
import com.miBudget.entities.Item;
import com.miBudget.entities.User;
import com.miBudget.servlets.Accounts;
import com.plaid.client.PlaidClient;
import com.plaid.client.request.AccountsGetRequest;
import com.plaid.client.request.ItemGetRequest;
import com.plaid.client.request.ItemPublicTokenExchangeRequest;
import com.plaid.client.request.ItemRemoveRequest;
import com.plaid.client.response.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@CrossOrigin(origins = "*")
public class ItemController {
    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);

    private final String client_id = "5ae66fb478f5440010e414ae";
    //private final String secret = "0e580ef72b47a2e4a7723e8abc7df5";
    private final String secretD = "c7d7ddb79d5b92aec57170440f7304";
    private final UserDAO userDAO;
    private final AccountDAO accountDAO;
    private final ItemDAO itemDAO;
    private final BudgetDAO budgetDAO;
    private final CategoryDAO categoryDAO;

    public PlaidClient client() {
        // Use builder to create a client
        PlaidClient client = PlaidClient.newBuilder()
                .clientIdAndSecret(client_id, secretD)
                .publicKey("") // optional. only needed to call endpoints that require a public key
                .developmentBaseUrl() // or equivalent, depending on which environment you're calling into
                .build();
        return client;
    }

    @Autowired
    public ItemController(UserDAO userDAO, AccountDAO accountDAO, ItemDAO itemDAO, BudgetDAO budgetDAO, CategoryDAO categoryDAO) {
        this.userDAO = userDAO;
        this.accountDAO = accountDAO;
        this.itemDAO = itemDAO;
        this.budgetDAO = budgetDAO;
        this.categoryDAO = categoryDAO;
    }

    @RequestMapping(path="/add-item", method=RequestMethod.POST)
    public void addItem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NullPointerException
    {
        LOGGER.info(Constants.start);
        LOGGER.info("ItemController::addItem()");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Item bankToAdd = null;
        if (session == null) {
            LOGGER.info("session is null");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/text");
            response.getWriter().append("FAIL: this is not the same session...");
            return;
        }
        // Public token exchange request and response
        String public_token = request.getParameter("public_token");
        LOGGER.info("public_token: " + public_token);
        ItemPublicTokenExchangeRequest publicTokenExchangeRequest = new ItemPublicTokenExchangeRequest(public_token);
        Response<ItemPublicTokenExchangeResponse> publicTokenExchangeResponse =
                client().service().itemPublicTokenExchange(publicTokenExchangeRequest).execute();
        /**LOGGER.info("publicTokenExchangeResponseBody: " + publicTokenExchangeResponse.body().toString());
         // access token notes: An access_token is used to access product data for an Item
         LOGGER.info("publicTokenExchangeResponseAccessToken: " + publicTokenExchangeResponse.body().getAccessToken());
         LOGGER.info("accessTokenLength: " + publicTokenExchangeResponse.body().getAccessToken().length());
         // item_id notes: An item_id is used to identify an Item in a webhook.
         LOGGER.info("publicTokenExchangeResponseItemId: " + publicTokenExchangeResponse.body().getItemId());
         LOGGER.info("itemIdLength: " + publicTokenExchangeResponse.body().getItemId().length());
         // request_id notes: used to keep track of user's session and what they did (i believe)
         LOGGER.info("publicTokenExchangeResponseRequestId: " + publicTokenExchangeResponse.body().getRequestId()); */

        // Get itemId and accessToken
        String accessToken = "";
        String itemId = "";
        if (publicTokenExchangeResponse.isSuccessful()) {
            accessToken = publicTokenExchangeResponse.body().getAccessToken();
            itemId = publicTokenExchangeResponse.body().getItemId();
        }

        // META DATA - Accounts the user selected
        String accountsRequested = request.getParameter("accounts");
        String institutionId = request.getParameter("institution_id");
        String institutionName = request.getParameter("institution_name");
        String linkSessionId = request.getParameter("link_session_id");

        LOGGER.info("accountsRequested: " + accountsRequested);
        LOGGER.info("institutionId: " + institutionId);
        LOGGER.info("institutionName: " + institutionName);
        LOGGER.info("linkSessionId: " + linkSessionId);

        // Parse accountsRequested; store in accountIdsRequestedList
        JSONParser jsonParser = new JSONParser();
        JSONArray accountsRequestedJsonArray = null;
        try { accountsRequestedJsonArray = (JSONArray) jsonParser.parse(accountsRequested); }
        catch (Exception e) { e.printStackTrace(System.out); }
        LOGGER.info("number of accounts requested: " + accountsRequestedJsonArray.size());
        ArrayList<Account> accountsRequestedList = new ArrayList<>();
        ArrayList<String> accountIdsRequestedList = new ArrayList<>();
        JSONObject jsonObject = null;
        for(int i = 0; i < accountsRequestedJsonArray.size(); i++) {
            try {
                jsonObject = (JSONObject) jsonParser.parse(accountsRequestedJsonArray.get(i).toString());
                accountIdsRequestedList.add(jsonObject.get("id").toString());
            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace(System.out);
            }
        }
        accountIdsRequestedList.forEach(id -> {
            LOGGER.info(id);
        });

        // Make call to AccountsGetResponse to receive additional information about the requested accounts
        AccountsGetRequest acctsGetReq = new AccountsGetRequest(accessToken);
        acctsGetReq.clientId = client_id;
        acctsGetReq.secret = secretD;
        acctsGetReq.withAccountIds(accountIdsRequestedList);
        Response<AccountsGetResponse> acctsGetRes =
                client().service().accountsGet(acctsGetReq).execute(); //.withAccountIds(accountIdsList)
        LOGGER.info("acctsGetRes body: " + acctsGetRes.body().toString());
        LOGGER.info("acctsGetRes code: " + acctsGetRes.code());
        LOGGER.info("acctsGetRes msg: " + acctsGetRes.message());
        LOGGER.info("acctsGetRes raw : " + acctsGetRes.raw());
        LOGGER.info("acctsGetRes err: " + acctsGetRes.errorBody());
        if (acctsGetRes.message().equals("Bad Request") || acctsGetRes.body().getAccounts().size() != accountsRequestedJsonArray.size() ) {
            LOGGER.info("acctsGetRes errString: " + acctsGetRes.errorBody().string());
            System.out.printf("/accounts/get endpoint retrieved %d accounts\n", acctsGetRes.body().getAccounts().size());
            acctsGetRes.body().getAccounts().forEach(acct -> {
                LOGGER.info(acct);
            });
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/text");
            response.getWriter().append("FAIL: " + acctsGetRes.errorBody().toString());
        }
        if (acctsGetRes.isSuccessful()) {
            LOGGER.info("AccountsGetResponse : " + acctsGetRes.code());
            List<com.plaid.client.response.Account> accountsFromRes = acctsGetRes.body().getAccounts();
            accountsFromRes.forEach(acct -> {
                LOGGER.info("acctsGetRes account: " + ((com.plaid.client.response.Account)acct));
            });

            for (String id : accountIdsRequestedList) {
                for (com.plaid.client.response.Account getResA : accountsFromRes) {
                    if (id.equals(getResA.getAccountId()) ) {
                        com.miBudget.entities.Account acctObj = new com.miBudget.entities.Account();
                        //LOGGER.info("Id's matched!");
                        LOGGER.info(id + " == " + getResA.getAccountId());
                        acctObj.setAccountId(getResA.getAccountId());
                        acctObj.setAvailableBalance(getResA.getBalances().getAvailable() != null ? getResA.getBalances().getAvailable() : 0.00);
                        acctObj.setCurrentBalance(getResA.getBalances().getCurrent() != null ? getResA.getBalances().getCurrent() : 0.00);
                        acctObj.set_limit(getResA.getBalances().getLimit() != null ? getResA.getBalances().getLimit() : 0.00);
                        acctObj.setCurrencyCode(getResA.getBalances().getIsoCurrencyCode() != null ? getResA.getBalances().getIsoCurrencyCode() : "Unknown Currency");
                        acctObj.setMask(getResA.getMask());
                        acctObj.setAccountName(getResA.getName());
                        acctObj.setOfficialName(getResA.getOfficialName() != null ? getResA.getOfficialName() : "");
                        acctObj.set_type(getResA.getType());
                        acctObj.setSubType(getResA.getSubtype());
                        acctObj.setItemId(bankToAdd.getId());
                        acctObj.setUserId(user.getId());
                        LOGGER.info("Adding acctObj to accountsList...");
                        accountsRequestedList.add(acctObj);
                    }
                } // end for each getResA
            } // end for each requested id
        } // end if acctsGetRes.isSuccessful()
        else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/text");
            response.getWriter().append("FAIL: " + acctsGetRes.errorBody().toString());
        }

        // Print each account in full
        AtomicInteger an = new AtomicInteger(1);
        LOGGER.info("miBudget accounts, accounts requested after parsing...");
        accountsRequestedList.forEach(account -> {
            LOGGER.info(an.getAndAdd(1) + ") " + account.toString());
        });

        // Check to see if user is attempting to re-add a bank or
        // re-adding accounts
        List<Account> usersCurrentAccountsList = accountDAO.findAccountByUserId(user.getId());
        boolean duplicateBank = false;
        boolean duplicateAcct = false;
        Set<String> institutionIdsList = ((Map<String, List<Account>>)session.getAttribute("institutionIdsAndAccounts")).keySet();
        for(String id : institutionIdsList) {
            if (id.equals(institutionId)) {
                LOGGER.info(institutionId + " has already been added. We cannot add it again. Checking "
                        + "to see if the user is adding accounts back.");
                duplicateBank = true;
                break;
            } else
                LOGGER.info(id + " does not match with " + institutionId);
        }
        int before = accountsRequestedList.size();
        LOGGER.info("accountsRequestedList size before possible removal is " + before);
        ArrayList<com.miBudget.entities.Account> acctsToRemove = new ArrayList<>();
        req:
        for(com.miBudget.entities.Account reqA : accountsRequestedList) {
            for (com.miBudget.entities.Account addedA : usersCurrentAccountsList) {
                if (reqA.getMask().equals(addedA.getMask()) ) {
                    String name = reqA.getAccountName();
                    LOGGER.info(name + " has already been added. We cannot add it again. ");
                    duplicateAcct = true;
                    acctsToRemove.add(reqA);
                    continue req;
                }
            }
        }
        for(com.miBudget.entities.Account a : acctsToRemove) {
            accountsRequestedList.remove(a);
            accountIdsRequestedList.remove(a.getAccountId());
        }
        int after = accountsRequestedList.size();
        LOGGER.info("accountsRequestedList size is now " + after);
        LOGGER.info("accountIdsRequestedList size: " + accountIdsRequestedList.size());

        if (duplicateAcct && after > 0) {
            LOGGER.info("You requested " + before + " but we can only add " + after + ".");
            // Can continue with authenticate
        } else if (duplicateBank && after == 0) {
            LOGGER.info("Trying to add duplicate bank...");
            int numberOfAccounts = user.getAccounts().size();
            request.setAttribute("NoOfAccts", numberOfAccounts);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/text");
            response.getWriter().append("FAIL: Cannot add " + institutionId + ". Bank has already been added. We cannot add it again.");
            LOGGER.info("NOT ALLOWED: " + institutionId + " has already been added. We cannot add it again.");
            return;
        } else if (duplicateBank && duplicateAcct) {
            LOGGER.info("Trying to add duplicate account(s)...");
            int numberOfAccounts = user.getAccounts().size();
            request.setAttribute("NoOfAccts", numberOfAccounts);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/text");
            response.getWriter().append("FAIL: Cannot add " + institutionId + ". Bank or accounts have already been added. We cannot add it again.");
            LOGGER.info("FAIL: Cannot add " + institutionId + " has already been added. We cannot add it again.");
            return;
        }
        // If both the call to exchange the public token for an access token AND
        // If the call to get accounts information is Successful
        if (publicTokenExchangeResponse.isSuccessful() && acctsGetRes.isSuccessful()) {
            LOGGER.info("Responses code: Public Token Exchange: " + publicTokenExchangeResponse.code() + "AccountsGet: " + acctsGetRes.code());
            try {
                if (user.getFirstName() == null) {}
            } catch (NullPointerException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/text");
                response.getWriter().append("FAIL: No user!");
                return;
            }

            // Create bank object
            bankToAdd = new Item(itemId, accessToken, institutionId, institutionName, user.getId().toString());
            LOGGER.info("created bankToAdd: " + bankToAdd);

            //int verify = 0;
            boolean verify = false;
            if (!duplicateBank) {

                /** TODO: change attribute name from CreatedItem a list of items.
                 * Eventually I will need to create a list of items and add that list.
                 */
                session.setAttribute("CreatedItem", bankToAdd);

                // Add bank to items table
                LOGGER.info("Adding item to database");
                verify = addItemToDatabase(bankToAdd);
                if (!verify) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.setContentType("application/text");
                    response.getWriter().append("FAIL: did not add " + bankToAdd + " to database.");
                    return;
                }
                LOGGER.info(bankToAdd + " was added to the database.");
                bankToAdd.setId(itemDAO.findIdByItemId(itemId));
                //bankToAdd.setId(MiBudgetState.getItemDAOImpl().getItemTableIdForItemId(bankToAdd.getItemId())); // until this point, itemTableId is not set
                LOGGER.info("updated bankToAdd: " + bankToAdd);


                // Add created Item to users_items table
                //verify = addItemToUsersItemsInDatabase(getAllUsersItems(user), bankToAdd, user);
                //if (verify == 0)
                //    return "FAIL: did not add UsersItemsObject to UsersItems in database.";
                //else
                //    LOGGER.info("UsersItemsObject was added to UsersItems table in database");

                // Add institutionId to users_institution_ids table
                //verify = addInstitutionIdToUsersInstitutionIdsTableInDatabase(institutionId, user);
                //if (verify == 0)
                //    return "FAIL: did not add " + bankToAdd.getInstitutionId() + " to UsersInstitutions table.";
                //else {
                //    LOGGER.info(bankToAdd.getInstitutionId() + " added to UsersInstitutions table in database");
                //}
            } else {
                bankToAdd.setId(itemDAO.findIdByItemId(itemId)); // until this point, itemTableId is not set
                LOGGER.info("updated bankToAdd: " + bankToAdd); // since bank already exists, get itemTableId and set it for this bankToAdd object
            }
//		    bankToAdd = itemDAOImpl.getItemFromUser(institutionId);
            // Add accounts to users profile
//		    int itemTableId = itemDAOImpl.getItemTableIdForItemId(bankToAdd.getItemId());
            Item finalBankToAdd = bankToAdd;
            accountsRequestedList.forEach(account -> {
                account.setItemId(finalBankToAdd.getId());
            });
            // Need the above lines to set item_table_id for the accounts. they all have the same item
            // But need to set to proper item_table_id that was set.
            verify = addAccountsToAccountsTableDatabase(accountsRequestedList, bankToAdd, user);
            if (!verify) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/text");
                response.getWriter().append("FAIL: did not add accounts to user's profile.");
                return;
            }
            // Add accountsList to requestSession
            //user.setAccountIds( (ArrayList<String>) MiBudgetState.getAccountDAOImpl().getAllAccountsIds(user)); // update accountIds
            usersCurrentAccountsList = accountDAO.findAccountByUserId(user.getId());
            usersCurrentAccountsList.forEach(account -> {
                LOGGER.info(account);
            });

            // Create a Map of itemIds, and list of appropriate accounts
            @SuppressWarnings("unchecked")
            HashMap<String, ArrayList<Account>> institutionIdsAndAccounts =
                    (HashMap<String, ArrayList<com.miBudget.entities.Account>>) session.getAttribute("institutionIdsAndAccounts");
            ArrayList<com.miBudget.entities.Account> newAccountsList = new ArrayList<>();
            newAccountsList = institutionIdsAndAccounts.get(institutionId);
            if (newAccountsList == null) newAccountsList = new ArrayList<com.miBudget.entities.Account>();
            LOGGER.info("institutionIdsAndAccounts");
            for(String insId : institutionIdsAndAccounts.keySet()) {
                LOGGER.info("key: " + insId);
                for (com.miBudget.entities.Account acct : institutionIdsAndAccounts.get(insId)) {
                    LOGGER.info("\t" + acct);
                }
            }
            accountsRequestedList.forEach(account -> {
                LOGGER.info(an.getAndAdd(1) + ") " + account.toString());
            });
            LOGGER.info("Adding " + institutionId + " and the following accounts to institutionIdsAndAccounts");
            List<Long> newAccountIds = new ArrayList<>();
            for (Account account : accountsRequestedList) {
                LOGGER.info("account: " + account);
                newAccountsList.add(account);
                newAccountIds.add(account.getId());
            }
            user.getAccounts().addAll(newAccountsList);

            Object resultOfPutInMap = institutionIdsAndAccounts.put(institutionId, newAccountsList);
            LOGGER.info("resultOfPutInMap: " + resultOfPutInMap);
            //if (resultOfPutInMap != null) LOGGER.info("Some item and its accounts were just overwritten!!");
            //else LOGGER.info("Added the Integer and Accounts pairing to the acctsAndInstitutionIdMap");
            session.setAttribute("institutionIdsAndAccounts", institutionIdsAndAccounts);

            // Accounts list is all accounts in users profile
            session.setAttribute("accountsSize", newAccountIds.size());

            session.setAttribute("user", user);
            String strAccounts = (accountsRequestedList.size() == 1) ? " account!" : " accounts!";
            session.setAttribute("change", "You have successfully loaded " + accountsRequestedList.size() + strAccounts);

            List<Item> items = new ArrayList<>();
            for (String id : institutionIdsList) {
                Item item = itemDAO.findItemByInstitutionId(id);
                LOGGER.info(item);
                items.add(item);
            }
            session.setAttribute("items", items);
            // Populate ErrMapForItems
            HashMap<String, Boolean> errMapForItems = new HashMap<>();
            for (Item item : items) {
                ItemGetRequest getReq = new ItemGetRequest(item.getAccessToken());
                Response<ItemGetResponse> getRes = client().service().itemGet(getReq).execute();
                if (getRes.isSuccessful()) {
                    ItemStatus itemStatus = getRes.body().getItem();
                    ErrorResponse err = itemStatus.getError();
                    if (err != null) {
                        if (err.getErrorType() == ErrorResponse.ErrorType.ITEM_ERROR) {
                            LOGGER.info("There is an Item_Error");
                            LOGGER.info(err.toString());
                            errMapForItems.put(item.getInstitutionId(), true);
                        }
                    } else {
                        LOGGER.info("No error for this " + item);
                        errMapForItems.put(item.getInstitutionId(), false);
                    }
                } else {
                    LOGGER.info("ItemGetResponse failed.");
                    LOGGER.info(getRes.errorBody());
                }
            }
            session.setAttribute("errMapForItems", errMapForItems);
        } // end if publicTokenExchange and acctsGet is successful
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().append("Success: Adding ").append(institutionName);
        response.getWriter().flush();
    }

    protected boolean addItemToDatabase(Item item) {
        try {
            LOGGER.info("Saving bank (item)");
            itemDAO.save(item);
            return true;
        } catch (Exception e) {
            LOGGER.error("There was an exception while saving the bank (item)");
            return false;
        }
    }

    protected boolean addAccountsToAccountsTableDatabase(List<com.miBudget.entities.Account> accountsRequested, Item item, User user) {
        try {
            for (com.miBudget.entities.Account account : accountsRequested) {
                accountDAO.save(account);
            }
            LOGGER.info("Account(s) added to user's profile.");
            return true;
        } catch (Exception e) {
            LOGGER.error("There was an exception saving the account");
            return false;
        }
    }

    @RequestMapping(path="/delete-item", method=RequestMethod.POST)
    public void deleteItem(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        performAction(request, response);
    }

    @RequestMapping(path="/delete-account", method=RequestMethod.POST)
    public void deleteAccount(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        performAction(request, response);
    }

    public void performAction(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        LOGGER.info(Constants.start);
        LOGGER.info("ItemController::performAction");
        String action = request.getParameter("action");
        String institutionId = request.getParameter("institutionId");
        String bankName = request.getParameter("bankName");
        String buttonSelectedText = request.getParameter("buttonSelected");
        LOGGER.info("action: {}", action);
        LOGGER.info("institutionId: {}", institutionId);
        LOGGER.info("bankName: {}", bankName);
        LOGGER.info("buttonSelectedText: {}", buttonSelectedText);
        String deleteResponse = "FAILED: ";
        // Perform the following logic:
        switch (action) {
            case ("delete_bank") : {
                deleteResponse = deleteBank(request, response);
                break;
            }
            case ("delete_account") : {
                deleteResponse = doDeleteAccount(request, response);
                break;
            }

        }
        if (deleteResponse.startsWith("FAIL")) { response.setStatus(HttpServletResponse.SC_BAD_REQUEST);}
        else { response.setStatus(HttpServletResponse.SC_OK);}
        response.setContentType("application/json");
        response.getWriter().append(deleteResponse);
        response.getWriter().flush();
        LOGGER.info("ItemController::performAction");
        LOGGER.info(Constants.end);
    }

    protected String deleteBank(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        LOGGER.info(Constants.start);
        LOGGER.info("ItemController::deleteBank");
        HttpSession session = request.getSession(false);
        String institutionId = request.getParameter("currentId");
        LOGGER.info("Attempting to delete bank: " + institutionId);
        Item item = itemDAO.findItemByInstitutionId(institutionId);
        if (item == null) {
            return "FAIL: error creating the item.";
        }
        User user = (User)session.getAttribute("user");

        //int verify = MiBudgetState.getItemDAOImpl().deleteBankReferencesFromDatabase(item, user);
        //if (verify == 0) {
        //    return "FAIL: did not delete bank references from the database.";
        //}
        List<Account> banksAccounts = accountDAO.findAccountsByItemId(item.getItemId());
        boolean verify = deleteAccounts(banksAccounts, item.getBankName());
        if (!verify) {
            return "FAIL: did not delete the accounts";
        }
        verify = deleteItem(item);
        if (!verify) { return "FAIL: did not delete the item"; }
        else { LOGGER.info("The item was successfully deleted."); }
        Response<ItemRemoveResponse> itemRemoveRes =  client().service()
                .itemRemove(new ItemRemoveRequest(item.getAccessToken()))
                .execute();
        // The Item has been removed and the access token is now invalid
        boolean isRemoved = false;
        if (itemRemoveRes.isSuccessful()) {
            isRemoved = itemRemoveRes.body().getRemoved();
        } else {
            LOGGER.info(itemRemoveRes.errorBody().string());
            return "FAIL: item's access token was not invalidated. Request failed.";
        }
        LOGGER.info(item.getAccessToken() + " was invalidated?: " + isRemoved);
        if (isRemoved) {
            //ArrayList<UsersItemsObject> usersItemsList = itemDAOImpl.getAllUserItems((User)session.getAttribute("user"));
            @SuppressWarnings("unchecked")
            HashMap<Integer, ArrayList<Account>> institutionIdsAndAccounts = (HashMap<Integer, ArrayList<Account>>)
                    session.getAttribute("institutionIdsAndAccounts");
            institutionIdsAndAccounts.remove(item.getId());
            // update session values
            updateUser(user, banksAccounts, item);
            int numberOfAccounts = user.getAccounts().size() - banksAccounts.size();
            //ArrayList<String> institutionIdsList = (ArrayList<String>) MiBudgetState.getMiBudgetDAOImpl().getAllInstitutionIdsFromUser(user);
            //session.setAttribute("institutionIdsList", institutionIdsList);
            //session.setAttribute("institutionIdsAndAccounts", institutionIdsAndAccounts);
            //session.setAttribute("institutionIdsListSize", institutionIdsList.size());
            session.setAttribute("accountsSize", numberOfAccounts);
        } else {
            return "FAIL: access token was not invalidated for " + item.getInstitutionId();
        }
        LOGGER.info("ItemController::deleteBank");
        LOGGER.info(Constants.end);
        // Need to call /item/delete to invalidate access-token for Item
        return "deleteBank: SUCCESS";
    }

    protected String doDeleteAccount(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        LOGGER.info(Constants.start);
        LOGGER.info("ItemController::doDeleteAccount");
        String accountId = request.getParameter("accountId");
        String item_Id = request.getParameter("item_id");
        Account account = accountDAO.findByAccountId(accountId);
        LOGGER.info("Attempting to delete: " + account);
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("user");
        Item item = itemDAO.findById(Long.valueOf(item_Id)).orElse(null);
        if (item == null) {
            return "FAIL: Item came back null";
        }
        List<Account> accountsForItem = accountDAO.findAccountsByItemId(item.getItemId());
        LOGGER.info("# of accounts: {} for {}" + accountsForItem.size(), item.getBankName());
        accountsForItem.remove(account);
        user.getAccounts().remove(account);
        LOGGER.info("ItemController::doDeleteAccount");
        LOGGER.info(Constants.end);
        return "SUCCESS: Account deleted";
    }

    protected boolean deleteAccounts(List<Account> bankAccounts, String bankName)
    {
        try {
            accountDAO.deleteAll(bankAccounts);
            return true;
        } catch (Exception e) {
            LOGGER.error("There was an issue deleting associated accounts for {}", bankName);
            return false;
        }
    }

    protected boolean deleteItem(Item item)
    {
        try {
            itemDAO.delete(item);
            return true;
        } catch (Exception e) {
            LOGGER.error("There was an issue deleting the item: {}", item);
            return false;
        }
    }

    protected void updateUser(User user, List<Account> bankAccountsDeleted, Item itemDeleted)
    { user.getAccounts().removeAll(bankAccountsDeleted); }
}