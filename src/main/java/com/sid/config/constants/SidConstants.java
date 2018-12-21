package com.sid.config.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SidConstants {

    @Value("${sid.portal.link}")
    public String myPortalLink;

    public String getMyPortalLink() {
        return myPortalLink;
    }

    public void setMyPortalLink(String myPortalLink) {
        this.myPortalLink = myPortalLink;
    }
}
