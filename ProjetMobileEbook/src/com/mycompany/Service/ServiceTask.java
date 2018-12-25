/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Service;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.events.ActionListener;
import com.mycompany.Entite.Task;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sana
 */
public class ServiceTask {

    public void ajoutTask(Task ta) {
        ConnectionRequest con = new ConnectionRequest();
        String Url = "http://41.226.11.243:10004/tasks/" + ta.getNom() + "/" + ta.getEtat();
        con.setUrl(Url);
        con.setPost(true);

        con.addResponseListener((e) -> {
            String str = new String(con.getResponseData());
            System.out.println(str);

        });
        NetworkManager.getInstance().addToQueueAndWait(con);
    }

    public ArrayList<Task> getListTask(String json) {

        ArrayList<Task> listTasks = new ArrayList<>();

        try {
            JSONParser j = new JSONParser();

            Map<String, Object> tasks = j.parseJSON(new CharArrayReader(json.toCharArray()));
           
            List<Map<String, Object>> list = (List<Map<String, Object>>) tasks.get("root");

            for (Map<String, Object> obj : list) {
                Task e = new Task();
                float id = Float.parseFloat(obj.get("id").toString());
                System.out.println(id);
                e.setId((int) id);
                e.setEtat(obj.get("state").toString());
                e.setNom(obj.get("name").toString());
                System.out.println(e);
                listTasks.add(e);
            }

        } catch (IOException ex) {
        }
        return listTasks;

    }
    
    
    ArrayList<Task> listTasks = new ArrayList<>();
    
    public ArrayList<Task> getList2(){       
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl("http://41.226.11.243:10004/tasks/");  
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                ServiceTask ser = new ServiceTask();
                listTasks = ser.getListTask(new String(con.getResponseData()));
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listTasks;
    }

}
