package com.cristidospra.bookadvisor

import android.util.Log
import com.cristidospra.bookadvisor.Models.Conversation
import com.cristidospra.bookadvisor.Models.FirebaseMessage
import com.cristidospra.bookadvisor.Models.Message
import com.cristidospra.bookadvisor.Models.User
import com.cristidospra.bookadvisor.Networking.LoginApiManager
import com.cristidospra.bookadvisor.Utils.Utils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object FirebaseManager {

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    fun signIn(email: String, password: String) {

        if (firebaseAuth.currentUser != null) {
            firebaseAuth.signOut()
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {

                CurrentUser.instance.firebasUID = it.user.uid

            }.addOnFailureListener {
                Log.e("FirebaseSignIn", it.toString())
            }

    }

    fun register(email: String, password: String, firstName: String, lastName: String, onSuccess: () -> Unit) {

        if (firebaseAuth.currentUser != null) {
            firebaseAuth.signOut()
        }

        val task = firebaseAuth.createUserWithEmailAndPassword(email, password)
        task.addOnCompleteListener(object : OnCompleteListener<AuthResult> {

            override fun onComplete(task: Task<AuthResult>) {

                if(!task.isSuccessful) {

                    Log.w("FirebaseRegistration", task.exception?.message)
                } else {

                    print("Registration successful")
                    LoginApiManager.register(email, password, getUID(), firstName, lastName) {

                        CurrentUser.instance.authToken = it.token

                        onSuccess()
                    }
                }
            }

        })
    }

    fun getUID() : String {

        return firebaseAuth.uid.toString()
    }



    fun addMessage(sender: User, receiver: User, message: Message) {

        val chatRef = firebaseDatabase.reference.child("user_chats").child(sender.firebasUID).child(receiver.firebasUID)

        chatRef.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                Log.w("Firebase", "Failed to read value.", error.toException())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                print("")

                var chatUID = ""
                if (dataSnapshot.hasChildren()) {
                    chatUID = (dataSnapshot.value as HashMap<String, String>).values.first()

                    val newRef = firebaseDatabase.reference.child("chat_messages").child(chatUID).push()
                    newRef.setValue(FirebaseMessage(message))
                    firebaseDatabase.reference.child("chats").child(chatUID).child("last_message").setValue(newRef.key)
                }
                else {

                    chatUID = addUserChats(sender, receiver)
                    val newRef = firebaseDatabase.reference.child("chat_messages").child(chatUID).push()
                    newRef.setValue(FirebaseMessage(message))

                    addChat(chatUID, newRef.key.toString())
                }

                for (child in dataSnapshot.children) {

                    if (child.key == sender.firebasUID) {

                    }
                }
            }

        })

    }

    private fun addChat(chatUID: String, messageUID: String) {

        firebaseDatabase.reference.child("chats").child(chatUID).child("last_message").setValue(messageUID)
    }

    private fun addUserChats(sender: User, receiver: User) : String {

        val chatRef = firebaseDatabase.reference.child("chats").push()
        val chatUID = chatRef.key

        firebaseDatabase.reference.child("user_chats").child(sender.firebasUID).child(receiver.firebasUID).child(chatUID.toString()).setValue(chatUID)
        firebaseDatabase.reference.child("user_chats").child(receiver.firebasUID).child(sender.firebasUID).child(chatUID.toString()).setValue(chatUID)

        return chatUID.toString()
    }

    private fun addUserChats(senderUID: String, receiverUID: String) : String {

        val chatRef = firebaseDatabase.reference.child("chats").push()
        val chatUID = chatRef.key

        firebaseDatabase.reference.child("user_chats").child(senderUID).child(receiverUID).child(chatUID.toString()).setValue(chatUID)
        firebaseDatabase.reference.child("user_chats").child(receiverUID).child(senderUID).child(chatUID.toString()).setValue(chatUID)

        return chatUID.toString()
    }

    fun getConversations(onSuccess: (Conversation) -> Unit) {

        val convoRef = firebaseDatabase.reference.child("user_chats").child(getUID())

        convoRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                print("")

                for (child in dataSnapshot.children) {

                    val chatUID = (child.value as HashMap<String, String>).values.first()

                    val userUID = child.key.toString()

                    getLastMessage(CurrentUser.instance.firebasUID, userUID, chatUID) { msg ->

                        print("")
                        onSuccess(Conversation(chatUID, User(firebasUID = userUID), msg))
                    }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }

    fun getLastMessage(user1UID: String, user2UID: String, chatUID: String, onSuccess: (Message?) -> Unit) {

        val chatRef = firebaseDatabase.reference.child("chats").child(chatUID)

        chatRef.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.value == null) {
                    onSuccess(null)
                }
                else {

                    val messageUID = (dataSnapshot.value as HashMap<String, String>).values.first()

                    getMessage(chatUID, messageUID) {

                        var sender: String
                        var receiver: String
                        if (it.senderUID == user1UID) {
                            sender = user1UID
                            receiver = user2UID
                        } else {
                            sender = user2UID
                            receiver = user1UID
                        }

                        onSuccess(
                            Message(
                                User(firebasUID = sender),
                                User(firebasUID = receiver),
                                Utils.stringToDate(it.timeStamp),
                                it.content
                            )
                        )
                    }
                }
            }

        })
    }


    fun getUserChat(user1UID: String, user2UID: String, onSuccess: (String) -> Unit) {

        val chatRef = firebaseDatabase.reference.child("user_chats").child(user1UID).child(user2UID)

        chatRef.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.value == null) {
                    onSuccess(addUserChats(user1UID, user2UID))
                }
                else {
                    onSuccess((dataSnapshot.value as HashMap<String, String>).values.first())
                }
            }

        })
    }


    fun getMessage(chatUID: String, messageUID: String, onSuccess: (FirebaseMessage) -> Unit) {

        val chatRef = firebaseDatabase.reference.child("chat_messages").child(chatUID).child(messageUID)

        chatRef.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val firebaseMessage = FirebaseMessage((dataSnapshot.value as HashMap<String, String>))

                onSuccess(firebaseMessage)
            }

        })
    }

    fun getNewMessage(chatUID: String, user: User, onSuccess: (ArrayList<Message>) -> Unit) {

        val ref = firebaseDatabase.reference.child("chat_messages")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.value != null) {

                    val chatMap: HashMap<String, HashMap<String, HashMap<String, String>>> = (dataSnapshot.value as HashMap<String, HashMap<String, HashMap<String, String>>>)
                    val messageMap: HashMap<String, HashMap<String, String>> = chatMap[chatUID] ?: return
                    val messages: ArrayList<Message> = ArrayList()

                    for (messageUID in messageMap.keys) {

                        val firebaseMessage = FirebaseMessage(messageMap[messageUID])

                        var sender: User
                        var receiver: User

                        if (firebaseMessage.senderUID == CurrentUser.instance.firebasUID) {
                            sender = CurrentUser.instance
                            receiver = user

                        } else {
                            sender = user
                            receiver = CurrentUser.instance
                        }

                        messages.add(Message(sender, receiver, Utils.accurateStringToDate(firebaseMessage.timeStamp), firebaseMessage.content))

                    }

                    onSuccess(messages)
                }

            }

        })
    }
}
