package com.example.musab.mcit.mRecyclerView;

/**
 * Created by musab on 8/12/2017.
 */

public class BroadcastList {

     String head;
     String desc;
     String date;
     String type;
       int broadcast_id;




    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public int getBroadcast_id() {
        return broadcast_id;
    }

    public BroadcastList(String head, String desc, String date, String type, int broadcast_id) {
        this.head = head;
        this.desc = desc;
        this.date = date;
         this.type = type;
         this.broadcast_id=broadcast_id;

    }
}
