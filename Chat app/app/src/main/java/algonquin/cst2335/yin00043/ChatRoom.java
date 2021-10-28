package algonquin.cst2335.yin00043;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatRoom extends AppCompatActivity{
    RecyclerView chatList;
    MyChatAdapter adt = new MyChatAdapter();
    ArrayList<ChatMessage> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlayout);

        chatList = findViewById(R.id.myrecycler);
        chatList.setAdapter(adt);//how to build the item
        chatList.setLayoutManager(new LinearLayoutManager(this));

        Button ButtonSend = findViewById(R.id.buttonSend);
        ButtonSend.setOnClickListener(clk-> {
            EditText EditTextMessage = findViewById(R.id.editTextMessage);
            String messageInEditText = EditTextMessage.getText().toString();
            Date timeSent = new Date();

            ChatMessage thisMessage = new ChatMessage(messageInEditText, 1, timeSent.toString());
            messages.add(thisMessage);
            adt.notifyItemInserted(messages.size() - 1);//tells the list that there's a new item at the end of the ArrayList, this will call onCreateViewHolder to load a new Layout from the file, and onBindViewHolder() to initialize the text views.
            EditTextMessage.setText("");
        } );

        Button ButtonReceive = findViewById(R.id.buttonReceive);
        ButtonReceive.setOnClickListener(clk-> {
            EditText EditTextMessage = findViewById(R.id.editTextMessage);
            String messageInEditText = EditTextMessage.getText().toString();
            Date timeSent = new Date();

            ChatMessage thisMessage = new ChatMessage(messageInEditText, 2, timeSent.toString());
            messages.add(thisMessage);
            adt.notifyItemInserted(messages.size() - 1);
            EditTextMessage.setText("");
        } );
    }

    private class MyRowViews extends RecyclerView.ViewHolder {//a class that represent a row
        TextView messageText;
        TextView timeText;

        public MyRowViews(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);

            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );//alert dialog
                builder.setTitle("Question:");
                builder.setMessage("Do you want to delete the message: " + messageText.getText());
                builder.setNegativeButton("No",(dialog, click) -> {
                } );
                builder.setPositiveButton("Yes",(dialog, click) -> {
                    ChatMessage removedMessage = messages.get(position);
                    messages.remove(position);
                    adt.notifyItemRemoved(position);
                    Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                            .setAction("UNDO", cl -> {
                                messages.add(position, removedMessage);
                                adt.notifyItemInserted(position);
                            })
                            .show();
                } );
                builder.create().show();
            } );
        }
    }

    private class MyChatAdapter extends RecyclerView.Adapter<MyRowViews> {

        @Override
        public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {//check which kind of layout to use for this row
            LayoutInflater inflater = getLayoutInflater();//load a layout from .xml file
            int layoutID;
            if(viewType == 1)//send
                layoutID = R.layout.sent_message;
            else//receive
                layoutID = R.layout.receive_message;
            View loadedRow = inflater.inflate(layoutID, parent, false);
            MyRowViews initRow = new MyRowViews(loadedRow);//ViewHolder
            return initRow;//return ViewHolder;
        }

        @Override
        public void onBindViewHolder(MyRowViews holder, int position) {//pass the returned object to onCreateViewHolder()
            holder.messageText.setText(messages.get(position).getMessage());
            holder.timeText.setText(messages.get(position).getCurrentDateandTime());
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        @Override
        public int getItemViewType(int position) {//gives you the row that you're currently building, 1 = Send, 2 = Receive
            ChatMessage thisRow = messages.get(position);//get the message that should be at that row
            return thisRow.getSendOrReceive();
        }
    }

    private class ChatMessage {//store all messages
        String message;
        int sendOrReceive;
        String timeSent;
        SimpleDateFormat sdf = new SimpleDateFormat("EE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        public ChatMessage(String message, int sendOrReceive, String timeSent) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;
            this.timeSent = timeSent;
        }

        public String getMessage() {
            return message;
        }

        public int getSendOrReceive() {
            return sendOrReceive;
        }

        public String getCurrentDateandTime() {
            return currentDateandTime;
        }
    }
}