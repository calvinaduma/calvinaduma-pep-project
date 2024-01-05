package Service;

import DAO.SocialMediaDAO;
import Model.Account;
import Model.Message;

import java.util.List;

/**
 * Contains business logic between web layer (controller) and persistence layer (DAO).
 * 
 * Tasks include tasks not done through web, SQL programming tasks like validation/checks,
 * conducting additional security checks, or logging API actions in log file.
 * 
 */
public class SocialMediaService {
    SocialMediaDAO socialMediaDAO;

    public SocialMediaService(){
        socialMediaDAO = new SocialMediaDAO();
    }

    /**
     * @param socialMediaDAO
     */
    public SocialMediaService( SocialMediaDAO socialMediaDAO ){
        this.socialMediaDAO = socialMediaDAO;
    }

    /**
     * @param account object representing account to create
     * @return SUCCESS: the created account
     *         FAIL: null
     */
    public Account createNewUser( Account account ){
        if( account.getUsername() == "" || 
        account.getPassword().length() < 4 || 
        socialMediaDAO.userLogin( account ) != null )
            return null;
        return socialMediaDAO.createNewUser( account );
    }

    /**
     * @param account object representing account to login into
     * @return SUCCESS: the logged in account
     *         FAIL: null
     */
    public Account userLogin( Account account ){
        return socialMediaDAO.userLogin( account );
    }

    /**
     * @param message object representing message
     * @return SUCCESS: the created message
     *         FAIL: null
     */
    public Message createMessage( Message message ){
        if( message.getMessage_text().length() == 0 ||
        message.getMessage_text() == null ||
        message.getMessage_text().length() > 255 ||
        !socialMediaDAO.getUserByID( message.getPosted_by() ))
            return null;
        return socialMediaDAO.createMessage( message );
    }

    /**
     * @param id the ID of message to be retrieved
     * @return the message from message_id = ID
     */
    public Message getMessageByID( int id ){
        return socialMediaDAO.getMessageByID( id );
    }

    /**
     * @return all messages in database
     */
    public List<Message> getAllMessages(){
        return socialMediaDAO.getAllMessages();
    }

    /**
     * @param id the ID of the message to be deleted
     * @return the deleted message from message_id = ID if exists,
     *         null otherwise
     */
    public Message deleteMessageByID( int id ){
        return socialMediaDAO.deleteMessageByID( id );
    }

    /**
     * @param id the ID of the message to be updated.
     * @param message the object representing the new message
     * @return SUCCESS: the updated message from message_id = ID.
     *         FAIL: null.
     */
    public Message updateMessageByID( Message message ){
        if( message.getMessage_text().length() == 0 ||
        message.getMessage_text() == null ||
        message.getMessage_text().length() > 255 ||
        socialMediaDAO.getMessageByID( message.getMessage_id() ) == null )
            return null;
        return socialMediaDAO.updateMessage( message );
    }

    /**
     * @param id the ID of the account to retrieve messages from.
     * @return all messages from the account_id = ID.
     */
    public List<Message> getAllMessagesFromUserByUserID( int id ){
        return socialMediaDAO.getAllMessagesFromUserByUserID( id );
    }
}
