package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUsersMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUsersMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public String createUser(String name, String mobile) throws Exception {
        if(userMobile.contains(mobile))
        {
            throw new Exception("User already exists");
        }
        User user=new User(name,mobile);
        userMobile.add(mobile);
        return "SUCCESS";
    }
    public Group createGroup(List<User> users){
        if(users.size()>2){
            customGroupCount++;
            Group group = new Group("Group "+customGroupCount, users.size());
            groupUsersMap.put(group, users);
            adminMap.put(group, users.get(0));
            groupMessageMap.put(group, new ArrayList<Message>());
            return group;
        }
        Group group = new Group(users.get(1).getName(), users.size());
        groupUsersMap.put(group, users);
        adminMap.put(group, users.get(0));
        groupMessageMap.put(group, new ArrayList<Message>());
        return group;

    }

    public int createMessage(String content){
        messageId++;
        Message message=new Message(messageId,content);

        return messageId;
    }
    public int sendMessage(Message message, User sender, Group group) throws Exception{

        if(!groupUsersMap.containsKey(group))
        {
            throw new Exception("Group does not exist");
        }
        if(!groupUsersMap.get(group).contains(sender))
        {
            throw new Exception("You are not allowed to send message");
        }
        senderMap.put(message, sender);
        List<Message>ml=groupMessageMap.get(group);
        ml.add(message);
        groupMessageMap.put(group,ml);
        return ml.size();

    }
    public String changeAdmin(User approver, User user, Group group) throws Exception{

        if(!groupUsersMap.containsKey(group))
        {
            throw new Exception("Group does not exist");
        }
        if(!approver.equals(adminMap.get(group)))
        {
            throw new Exception("Approver does not have rights");
        }
        if(!groupUsersMap.get(group).contains(user))
        {
            throw new Exception("User is not a participant");
        }
        adminMap.put(group,user);

        return "SUCCESS";
    }

}