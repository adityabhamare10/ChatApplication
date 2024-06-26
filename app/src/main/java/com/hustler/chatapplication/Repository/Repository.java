package com.hustler.chatapplication.Repository;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hustler.chatapplication.model.ChatGroup;
import com.hustler.chatapplication.model.ChatMessage;
import com.hustler.chatapplication.views.GroupsActivity;

import java.util.ArrayList;
import java.util.List;

public class Repository {

//    This class will be act as a bridge between viewModel and Data Sources


    MutableLiveData<List<ChatGroup>> chatGroupmutableLiveData;

    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference groupReference;

    MutableLiveData<List<ChatMessage>> messagesLiveData;


    public Repository(){
        this.chatGroupmutableLiveData = new MutableLiveData<>();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        messagesLiveData = new MutableLiveData<>();

    }


    public void firebaseAnonymousAuth(Context context){

        FirebaseAuth.getInstance().signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent i =  new Intent(context, GroupsActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }
                    }
                });

    }

    // Getting current User Id
    public String getCurrentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }

    //SignOut Functionality
    public void signOut(){
        FirebaseAuth.getInstance().signOut();
    }

//    Getting Chat Groups available from Firebase realtime DB
    public MutableLiveData<List<ChatGroup>> getChatGroupMutableLiveData(){

        List<ChatGroup> groupsList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupsList.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ChatGroup group = new ChatGroup(dataSnapshot.getKey());
                    groupsList.add(group);
                }
                chatGroupmutableLiveData.postValue(groupsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return chatGroupmutableLiveData;
    }

    public void createNewChatGroup(String groupName){
        reference.child(groupName).setValue(groupName);

    }

    // Getting messages live data
    public MutableLiveData<List<ChatMessage>> getMessagesLiveData(String groupName) {

        groupReference = database.getReference().child(groupName);
        List<ChatMessage> messageList = new ArrayList<>();

        groupReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
                    messageList.add(message);
                }
                messagesLiveData.postValue(messageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return messagesLiveData;
    }



    public void sendMessage(String messageText, String chatGroup){

        DatabaseReference ref = database.getReference(chatGroup);

        if (!messageText.trim().equals("")){
            ChatMessage msg = new ChatMessage(
                    FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    messageText,
                    System.currentTimeMillis()
            );

            String randomKey = ref.push().getKey();

            ref.child(randomKey).setValue(msg);

        }
    }

}
