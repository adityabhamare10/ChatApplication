package com.hustler.chatapplication.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.hustler.chatapplication.Repository.Repository;
import com.hustler.chatapplication.model.ChatGroup;
import com.hustler.chatapplication.model.ChatMessage;

import java.util.List;

public class MyViewModel extends AndroidViewModel {

    Repository repository;


    public MyViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    //Auth
    public void signUpAnonymousUser(){
        Context c = this.getApplication();
        repository.firebaseAnonymousAuth(c);
    }

    public String getCurrentUserId(){
        return repository.getCurrentUserId();
    }

    public void signOut(){
        repository.signOut();
    }

    //Getting Chat Groups
    public MutableLiveData<List<ChatGroup>> getGroupList(){
        return repository.getChatGroupMutableLiveData();
    }

    public void createNewGroup(String groupName){
        repository.createNewChatGroup(groupName);
    }

    //Getting Messages
    public MutableLiveData<List<ChatMessage>> getMessagesLiveData(String groupName){
        return repository.getMessagesLiveData(groupName);
    }

    public void sendMessage(String msg, String chatGroup){
        repository.sendMessage(msg, chatGroup);
    }

}
