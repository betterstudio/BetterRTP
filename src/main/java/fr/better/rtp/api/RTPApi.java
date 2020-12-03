package fr.better.rtp.api;

import fr.better.rtp.RTPMain;
import fr.better.rtp.entity.RTP;

import java.util.List;

public interface RTPApi {

    public static RTPApi getInstance(){
        return RTPMain.getInstance();
    }

    List<RTP> getRTPs();
    void addRTP(RTP rtp);
    RTP getRTPbyTag(String tag);

}
