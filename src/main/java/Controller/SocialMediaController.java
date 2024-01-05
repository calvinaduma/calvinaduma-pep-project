package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import Service.SocialMediaService;
import Model.Account;
import Model.Message;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    SocialMediaService socialMediaService;
    public SocialMediaController(){
        socialMediaService = new SocialMediaService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post( "/register", this::createNewUserHandler );
        app.post( "/login", this::userLoginHandler );
        app.post( "/messages", this::createMessageHandler );
        app.get( "/messages/{message_id}", this::getMessageByIDHandler );
        app.get( "/messages", this::getAllMessagesHandler );
        app.get( "/accounts/{account_id}/messages", this::getAllMessagesFromUserByUserIDHandler );
        app.delete( "/messages/{message_id}", this::deleteMessageByIDHandler );
        app.patch( "/messages/{message_id}", this::updateMessageHandler );
        return app;
    }

    /**
     * Handler to create a new user
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void createNewUserHandler( Context ctx ) throws JsonProcessingException {
        ObjectMapper mp = new ObjectMapper();
        Account account = mp.readValue( ctx.body(), Account.class );
        Account createdAccount = socialMediaService.createNewUser( account );
        if( createdAccount == null ) ctx.status( 400 );
        else{
            ctx.json( mp.writeValueAsString(createdAccount) );
        }
    }

    /**
     * Handler to login a user
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void userLoginHandler( Context ctx ) throws JsonProcessingException {
        ObjectMapper mp = new ObjectMapper();
        Account account = mp.readValue( ctx.body(), Account.class );
        Account loggedInAccount = socialMediaService.userLogin( account );
        if( loggedInAccount == null ) ctx.status( 401 );
        else{
            ctx.json( mp.writeValueAsString(loggedInAccount) );
        }
    }

    /**
     * Handler to create a new message
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void createMessageHandler( Context ctx ) throws JsonProcessingException {
        ObjectMapper mp = new ObjectMapper();
        Message message = mp.readValue( ctx.body(), Message.class );
        Message createdMessage = socialMediaService.createMessage( message );
        if( createdMessage == null ) ctx.status( 400 );
        else{
            ctx.json( mp.writeValueAsString(createdMessage) );
        }
    }

    /**
     * Handler to get a message
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void getMessageByIDHandler( Context ctx ) throws JsonProcessingException {
        int messageID = Integer.parseInt( ctx.pathParam( "message_id" ));
        Message returnedMessage = socialMediaService.getMessageByID( messageID );
        if( returnedMessage != null ) ctx.json( returnedMessage );
    }

    /**
     * Handler to get all messages
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void getAllMessagesHandler( Context ctx ) throws JsonProcessingException {
        ctx.json( socialMediaService.getAllMessages() );
    }

    /**
     * Handler to delete a message
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void deleteMessageByIDHandler( Context ctx ) throws JsonProcessingException {
        int messageID = Integer.parseInt( ctx.pathParam( "message_id" ));
        Message deletedMessage = socialMediaService.deleteMessageByID( messageID );
        if( deletedMessage != null ) ctx.json( deletedMessage );
        else ctx.body();
    }

    /**
     * Handler to update a message
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void updateMessageHandler( Context ctx ) throws JsonProcessingException {
        ObjectMapper mp = new ObjectMapper();
        Message message = mp.readValue( ctx.body(), Message.class );
        int message_id = Integer.parseInt( ctx.pathParam( "message_id" ));
        Message currentMessage = socialMediaService.getMessageByID( message_id );
        if( currentMessage != null ){
            message.setMessage_id( message_id );
            message.setPosted_by( currentMessage.getPosted_by() );
            message.setTime_posted_epoch( currentMessage.getTime_posted_epoch() );
        }
        Message updatedMessage = socialMediaService.updateMessageByID( message );
        if( updatedMessage == null ) ctx.status( 400 );
        else{
            ctx.json( mp.writeValueAsString(updatedMessage) );
        }
    }

    /**
     * Handler to get all messages by a user
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void getAllMessagesFromUserByUserIDHandler( Context ctx ) throws JsonProcessingException {
        int userID = Integer.parseInt( ctx.pathParam( "account_id" ));
        if( socialMediaService.getAllMessagesFromUserByUserID( userID ) != null )
            ctx.json( socialMediaService.getAllMessagesFromUserByUserID( userID ) );
    }

}