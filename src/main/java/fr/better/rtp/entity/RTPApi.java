package fr.better.rtp.entity;

import fr.better.rtp.RTPMain;

import java.util.List;

public interface RTPApi {

    public static RTPApi getInstance(){
        return RTPMain.getInstance();
    }

    List<RTP> getRTPs();
    void addRTP(RTP rtp);
    RTP getRTPbyTag(String tag);

}
