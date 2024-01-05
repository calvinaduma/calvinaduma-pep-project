package DAO;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SocialMediaDAO {
    /**
     * @param account account to be added to database
     * @return account added to database
     */
    public Account createNewUser( Account account ){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO account (username,password) VALUES (?,?);";
            PreparedStatement ps = connection.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
            ps.setString( 1, account.getUsername() );
            ps.setString( 2, account.getPassword() );
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if( rs.next() ){
                int account_id = rs.getInt( "account_id" );
                String username = account.getUsername();
                String password = account.getPassword();
                return new Account( account_id, username, password );
            }
        } catch( SQLException e ){
            System.out.println( e.getMessage() );
        }
        return null;
    }

    /**
     * @param account account to log into
     * @return account logged into
     */
    public Account userLogin( Account account ){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM account WHERE username=? AND password=?";
            PreparedStatement ps = connection.prepareStatement( sql );
            ps.setString( 1, account.getUsername() );
            ps.setString( 2, account.getPassword() );
            ResultSet rs = ps.executeQuery();
            if( rs.next() ){
                int account_id = rs.getInt( "account_id" );
                String username = rs.getString( "username" );
                String password = rs.getString( "password" );
                return new Account( account_id, username, password );
            }
        } catch( SQLException e ){
            System.out.println( e.getMessage() );
        }
        return null;
    }

    /**
     * @param username id of account to be retrieved from database
     * @return True if account found
     *         False if account not found
     */
    public boolean getUserByID( int id ){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM account WHERE account_id=?";
            PreparedStatement ps = connection.prepareStatement( sql );
            ps.setInt( 1, id );
            ResultSet rs = ps.executeQuery();
            if( rs.next() ) return true;
        } catch( SQLException e ){
            System.out.println( e.getMessage() );
        }
        return false;
    }

    /**
     * @param message message to be added to database
     * @return message added to database
     */
    public Message createMessage( Message message ){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO message(posted_by,message_text,time_posted_epoch) VALUES (?,?,?)";
            PreparedStatement ps = connection.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
            ps.setInt( 1, message.getPosted_by() );
            ps.setString( 2, message.getMessage_text() );
            ps.setLong( 3, message.getTime_posted_epoch() );
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if( rs.next() ){
                int message_id = rs.getInt( "message_id" );
                int posted_by = message.getPosted_by();
                String message_text = message.getMessage_text();
                long time_posted_epoch = message.getTime_posted_epoch();
                return new Message( message_id, posted_by, message_text, time_posted_epoch );
            }
        } catch( SQLException e ){
            System.out.println( e.getMessage() );
        }
        return null;
    }

    /**
     * @return all messages
     */
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message";
            PreparedStatement ps = connection.prepareStatement( sql );
            ResultSet rs = ps.executeQuery();
            while( rs.next() ){
                int message_id = rs.getInt( "message_id" );
                int posted_by = rs.getInt( "posted_by" );
                String message_text = rs.getString( "message_text" );
                long time_posted_epoch = rs.getLong( "time_posted_epoch" );
                messages.add( new Message( message_id, posted_by, message_text, time_posted_epoch ) ); 
            }
        } catch( SQLException e ){
            System.out.println( e.getMessage() );
        }
        return messages;
    }

    /**
     * @param id id of message to be retrieved from database
     * @return retrieved message from database
     */
    public Message getMessageByID( int id ){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM message WHERE message_id=?";
            PreparedStatement ps = connection.prepareStatement( sql );
            ps.setInt( 1, id );
            ResultSet rs = ps.executeQuery();
            if( rs.next() ){
                int message_id = rs.getInt( "message_id" );
                int posted_by = rs.getInt( "posted_by" );
                String message_text = rs.getString( "message_text" );
                long time_posted_epoch = rs.getLong( "time_posted_epoch" );
                return new Message( message_id, posted_by, message_text, time_posted_epoch );
            }
        } catch( SQLException e ){
            System.out.println( e.getMessage() );
        }
        return null;
    }

    /**
     * @param id id of message to be deleted from database
     * @return deleted message from database
     */
    public Message deleteMessageByID( int id ){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM message WHERE message_id=?";
            PreparedStatement ps = connection.prepareStatement( sql );
            ps.setInt( 1, id );
            ResultSet rs = ps.executeQuery();
            Message message;
            if( rs.next() ){
                int message_id = rs.getInt( "message_id" );
                int posted_by = rs.getInt( "posted_by" );
                String message_text = rs.getString( "message_text" );
                long time_posted_epoch = rs.getLong( "time_posted_epoch" );
                message = new Message( message_id, posted_by,message_text,time_posted_epoch );
           
                sql = "DELETE FROM message WHERE message_id=?";
                ps = connection.prepareStatement( sql );
                ps.setInt( 1, id );
                ps.executeUpdate();
                return message;
            }
        } catch( SQLException e ){
            System.out.println( e.getMessage() );
        }
        return null;
    }

    /**
     * @param message message to be updated in database
     * @return updated message in database
     */
    public Message updateMessage( Message message ){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "UPDATE message SET message_text=? WHERE message_id=?";
            PreparedStatement ps = connection.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
            ps.setString( 1, message.getMessage_text() );
            ps.setInt( 2, message.getMessage_id() );
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if( rs.next() ){
                int message_id = rs.getInt( "message_id" );
                int posted_by = message.getPosted_by();
                String message_text = message.getMessage_text();
                long time_posted_epoch = message.getTime_posted_epoch();
                return new Message( message_id, posted_by, message_text, time_posted_epoch );
            }
        } catch( SQLException e ){
            System.out.println( e.getMessage() );
        }
        return null;
    }

    /**
     * @param id id of account to retrieve all messages from in database
     * @return retrieved messages of account from database
     */
    public List<Message> getAllMessagesFromUserByUserID( int id ){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message INNER JOIN account WHERE message.posted_by=? AND account.account_id=?";
            PreparedStatement ps = connection.prepareStatement( sql );
            ps.setInt( 1, id );
            ps.setInt( 2, id );
            ResultSet rs = ps.executeQuery();
            while( rs.next() ){
                int message_id = rs.getInt( "message_id" );
                int posted_by = rs.getInt( "posted_by" );
                String message_text = rs.getString( "message_text" );
                long time_posted_epoch = rs.getLong( "time_posted_epoch" );
                messages.add( new Message( message_id, posted_by, message_text, time_posted_epoch ) ); 
            }
        } catch( SQLException e ){
            System.out.println( e.getMessage() );
        }
        return messages;
    }
}