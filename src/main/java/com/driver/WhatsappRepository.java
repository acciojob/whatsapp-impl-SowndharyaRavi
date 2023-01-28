package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.

    /*private HashMap<String,String>userDB=new HashMap<>();

    private HashMap<int,Group>groupDB=new HashMap<>();*/

    private HashMap<Integer,Message> msgDb=new HashMap<>();
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;


    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public String createUser(String name, String mobile) throws Exception {
        if(userMobile.contains(mobile)){
            throw new Exception("User already exists");
        }
        else{
            userMobile.add(mobile);
            User user=new User(name,mobile);
            return "SUCCESS";
        }
    }

    public Group createGroup(List<User> users){
        if(users.size()>2){
            customGroupCount++;
            Group group=new Group(String.valueOf(customGroupCount), users.size());
            groupUserMap.put(group,users);
            adminMap.put(group,users.get(0));
            return group;
        }
        else{
            Group group=new Group(users.get(1).getName(),users.size());
            groupUserMap.put(group,users);
            adminMap.put(group,users.get(0));
            return group;
        }
    }

    public int createMessage(String content){
        messageId++;
        Message msg=new Message(messageId,content);
        msgDb.put(messageId,msg);
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{
        if(groupUserMap.containsKey(group)){
            throw new Exception(("Group does not exist"));
        }
        boolean isMember=false;
        for(User user:groupUserMap.get(group)){
            if(sender.getMobile().equals(user.getMobile())){
                isMember=true;
                break;
            }
        }
        if(!isMember){
            throw new Exception("You are not allowed to send message");
        }
        senderMap.put(message,sender);
        List<Message>list=groupMessageMap.get(group);
        list.add(message);
        groupMessageMap.put(group,list);
        return list.size();
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception{
        if(!groupUserMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }
        if(adminMap.get(group)!=approver){
            throw new Exception("Approver does not have rights");
        }
        boolean isMember=false;
        for(User users:groupUserMap.get(group)){
            isMember=true;
            break;
        }
        if(!isMember){
            throw new Exception("User is not a participant");
        }
        adminMap.put(group,user);
        return "SUCCESS";
    }
}
