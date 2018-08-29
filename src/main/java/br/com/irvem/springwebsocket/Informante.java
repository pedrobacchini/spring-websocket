package br.com.irvem.springwebsocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class Informante extends Thread {
    private final Set<WebSocketSession> sessions = new HashSet<WebSocketSession>();

    private boolean executando = false;

    private final DateFormat formatadorData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public void registrarSessao(WebSocketSession session){
        sessions.add(session);
    }

    public void removerSessao(WebSocketSession session){
        sessions.remove(session);
    }

    public void run(){
        System.out.println("run");
        while (executando){
            try {
                String mensagem = formatadorData.format(new Date());
                if(!sessions.isEmpty()){
                    for (WebSocketSession session : sessions){
                        if(session.isOpen()){
                            session.sendMessage(new TextMessage(mensagem));
                        }
                    }
                }
                Thread.sleep(1000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @PostConstruct
    public void init(){
        this.executando = true;
        this.start();
    }

    @PreDestroy
    public void finish(){
        this.executando = false;
    }
}
